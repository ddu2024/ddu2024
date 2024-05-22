#########################################################################
'''
Author: Vigneshwar Thilagar

Description: This is a template job for processing data from landing zone to raw zone
Applied Features:
1. database and table names are parameterized.
2. dynamical datatype casting based on the rawzone schema.
3. boomi_summary audit details are extracted from file name.


Change log:
    21-Aug-2023 : Added feature to process failed data and loaded into seperate errro catalog table for future referance and debugging.
    28-Aug-2023 : Added feature to handle schema change (if the source file in S3 have lesser column that datacatalog table)
                    Schema_matcher method will comaparethe schema across dyf and catalog table


'''
#########################################################################
import sys
import os
from awsglue.transforms import *
from awsglue.utils import getResolvedOptions
from pyspark.context import SparkContext
from awsglue.context import GlueContext
from awsglue.job import Job
from pyspark.sql.functions import *
from pyspark.sql.types import StringType, DateType, IntegerType, TimestampType
import pyspark.sql.functions
from awsglue.dynamicframe import DynamicFrame
from pyspark.sql.functions import when
from pyspark.sql.functions import concat_ws, col, lit
from datetime import datetime
import logging
import traceback
import json
import boto3


log = logging.getLogger(__name__)

# @params: ["JOB_NAME", "lz_db_name", "lz_table_name", "rz_db_name", "rz_table_name",  "TempDir"]
args = getResolvedOptions(sys.argv, ["JOB_NAME", "lz_db_name", "lz_table_name", "rz_db_name", "rz_table_name","lz_error_db", "lz_error_table","lz_error_s3_path", "TempDir"])

sc = SparkContext()
glueContext = GlueContext(sc)
spark = glueContext.spark_session
spark.conf.set("spark.sql.legacy.parquet.datetimeRebaseModeInWrite", "CORRECTED")
job = Job(glueContext)
job.init(args["JOB_NAME"], args)
ETL_DATE = datetime.utcnow()


# HELPER FUNCTIONS
def cast_column_datatype(df, col_list, datatype):
    """
    Cast the columns datatypes and added as new column with suffix ("tempCastedColumn")
 
    Args:
        df (dataframe): Dataframe to apply casting.
        col_list (list): List of columns to convert.
        datatype ("string"): Datatype convert the data in list of columns
 
    Returns:
        dataframe : Casted dataframe
    """
    try:
        fl = list(map( lambda col : "cast({0} as {1}) as {2}tempCastedColumn".format(col, datatype, col), col_list))
        cast_query = ", ".join(fl)
        print(cast_query)
        castedDf = df.selectExpr("*", *fl)
        return castedDf
    except Exception as e:
        print(traceback.format_exc())
        

def update_column_names(df):
    """
    modify the casted column names to original column names and drop the older columns
 
    Args:
        df (dataframe): Dataframe to apply.
 
    Returns:
        dataframe : Casted dataframe 
    """
    try:
        _col_list = []
        drop_column_list = []
        for field in df.schema.fields:
            field_name = field.name
            if field_name.endswith("tempCastedColumn"):
                rz_col_name = field_name.replace("tempCastedColumn", "")
                _col_list.append("{0} as {1}".format(field_name, rz_col_name ))
                drop_column_list.append(field_name)
        ucDf = df.selectExpr("*", *_col_list)
        ucDf = ucDf.drop(*drop_column_list)
        return ucDf
    except Exception as e:
        print(traceback.print_exc())
    
    
def extract_conversion_columns():
    """
    extract list of columns with specified datatypes
    Note: Currently this feature is supported for int and timestamp types
 
    Args:
        NA
 
    Returns:
        schema(list), date_columns(list), int_columns(list) , timestamp_columns(list), bigint_cols(list)
    """
    try:
        date_cols = []
        int_cols = []
        timestamp_cols = []
        bigint_cols = []
        audit_columns = ['etl_date', 'boomi_etl_date']
        client = boto3.client('glue')
        response = client.get_table_versions(
        DatabaseName=args["rz_db_name"],
        TableName=args["rz_table_name"]
            )
        glue_catalog_meta = response.get("TableVersions")[0].get("Table").get("StorageDescriptor").get("Columns")
        print(glue_catalog_meta)
        for meta in glue_catalog_meta:
            if meta.get("Type").lower() == "int":
                int_cols.append(meta.get("Name"))
            elif meta.get("Type").lower() == "bigint":
                bigint_cols.append(meta.get("Name"))
            elif "date" in meta.get("Type").lower() and meta.get("Name").lower() not in audit_columns:
                date_cols.append(meta.get("Name"))
            elif "timestamp" in meta.get("Type").lower() and meta.get("Name").lower() not in audit_columns:
                timestamp_cols.append(meta.get("Name"))
        return   glue_catalog_meta, date_cols, int_cols, timestamp_cols, bigint_cols  
    except Exception as e:
        print("Failed to read schema from rawzone")
        print("Exception: ", e)
        raise   
    
    
def process_error(df):
    try:
        errDf = df.errorsAsDynamicFrame()
        errDf = errDf.toDF().selectExpr("error")
        errorDyf = DynamicFrame.fromDF(errDf, glueContext, "error Df")
        # errDf.show(10, False)
        
        sinkNode = glueContext.getSink(
                    path=args["lz_error_s3_path"],
                    connection_type="s3",
                    updateBehavior="UPDATE_IN_DATABASE",
                    partitionKeys=[],
                    compression="snappy",
                    enableUpdateCatalog=True,
                    transformation_ctx="erros incident update",
                )
        sinkNode.setCatalogInfo(
            catalogDatabase=args["lz_error_db"], catalogTableName=args["lz_error_table"]
        )
        sinkNode.setFormat("glueparquet")
        sinkNode.writeFrame(errorDyf)
        return True
    except Exception as e:
        print("Failed to write error data")
        print("Exception: ", e)
        return False
        raise   
    
    
def schema_matcher(df):
    dyfColList = []
    dyfSchema = df.schema.json()
    dyf_col_list = json.loads(dyfSchema).get("fields", [])
    for col_meta in dyf_col_list:
        col_name= col_meta.get("name").lower()
        dyfColList.append(col_name)
    client = boto3.client('glue')
    response = client.get_table_versions(
        DatabaseName=args["lz_db_name"],
        TableName=args["lz_table_name"]
                )
    glue_catalog_meta = response.get("TableVersions")[0].get("Table").get("StorageDescriptor").get("Columns")
    for meta in glue_catalog_meta:
        meta_col_name = meta.get("Name").lower()
        if meta_col_name not in dyfColList:
            df = df.withColumn(meta_col_name, lit(""))
    return df
        
        
def lz2rz():
    """
    This function reads data from landing zone and write into rawzone with type casting.
    Also the summary details will be updated in redshift table.
 
    Args:
        NA
 
    Returns:
        NA
    """
    try:
        dyf = glueContext.create_dynamic_frame.from_catalog(database=args["lz_db_name"],
                                                        table_name=args["lz_table_name"],
                                                        transformation_ctx="read landing zone")
        print("Starting error dynamicFrame process: ", process_error(dyf))
        
    except Exception as e:
        print("Failed to read landing zone data")
        print("Exception: ", e)
        raise
    
    if dyf.count() >0:
        try:
            @udf(returnType=StringType()) 
            def file_name_extractor(_col, postion = 0): 
                extracted_column = _col.split("/")[-1].strip(".json")
                return extracted_column
            rz_schema ,date_cols, int_cols, timestamp_cols, bigint_cols = extract_conversion_columns()
            print("rz_schema", rz_schema)
            print("date columns", date_cols)
            print("int columns", int_cols)
            print("timestamp_cols", timestamp_cols)
            print("bigint_cols", bigint_cols)
            df = dyf.toDF()
            df = schema_matcher(df)
            df_enhanced = cast_column_datatype(df, timestamp_cols, "timestamp")
            df_enhanced = cast_column_datatype(df_enhanced, date_cols, "date")
            df_enhanced = cast_column_datatype(df_enhanced, int_cols, "int")
            df_enhanced = cast_column_datatype(df_enhanced, bigint_cols, "bigint")
            df_enhanced = df_enhanced.drop(*date_cols)
            df_enhanced = df_enhanced.drop(*int_cols)
            df_enhanced = df_enhanced.drop(*timestamp_cols)
            df_enhanced = df_enhanced.drop(*bigint_cols)
            df_enhanced = update_column_names(df_enhanced)
            
            df_enhanced = df_enhanced.withColumn("ETL_DATE", lit(ETL_DATE).cast(TimestampType()))
            df_enhanced = df_enhanced.withColumn("filename",input_file_name())
            df_enhanced = df_enhanced.withColumn("extracted_file_name", file_name_extractor(col("filename")))
            #extracted boomi summary detail from source file name as fixed length
            df_enhanced =df_enhanced.selectExpr("substr(extracted_file_name, LENGTH(extracted_file_name) - INSTR(REVERSE(extracted_file_name), '_') + 2) as total_records", \
                  "substr(extracted_file_name,1, LENGTH(extracted_file_name) - INSTR(REVERSE(extracted_file_name), '_')) as obj_and_etl_dt", \
                  "*")
            df_enhanced = df_enhanced.selectExpr("substr(obj_and_etl_dt,1, LENGTH(obj_and_etl_dt) - INSTR(REVERSE(obj_and_etl_dt), '_') ) as object_name", \
                    "substr(obj_and_etl_dt,LENGTH(obj_and_etl_dt) - INSTR(REVERSE(obj_and_etl_dt), '_') +2 ) as etl_date_summary", \
                     "*")
            df_enhanced = df_enhanced.withColumn("etl_date_summary_upd", to_timestamp(col("etl_date_summary"), \
                                                                                      'yyyy-dd-MM HH_mm_ss.SSS'))
            #DF for rawzone datacatalog
            rawZoneDf= df_enhanced.drop("filename", "etl_date_summary", "extracted_file_name", "object_name", "total_records","obj_and_etl_dt")
            rawZoneDf = rawZoneDf.withColumnRenamed("etl_date_summary_upd", "BOOMI_ETL_DATE")
            final_dyf = DynamicFrame.fromDF(rawZoneDf, glueContext, "final")
            #.resolveChoice(specs=[('INCIDENT_ID', 'cast:int')])
            final_dyf.printSchema()

            #DF for redshfift boomi_summary audit table
            boomiSummaryDf = df_enhanced.selectExpr("object_name", "etl_date_summary_upd as etl_date", "cast(total_records as int)").distinct()
            boomiSummaryDf.show(100, False)
            boomiSummaryDyf = DynamicFrame.fromDF(boomiSummaryDf, glueContext, "dashboard_final")
            
        except Exception as e:
            print("Failed to convert datatypes")
            print("Exception: ", e)
            raise
        
        #write to raw zone
        try:
            final = glueContext.write_dynamic_frame.from_catalog(
                frame=final_dyf,
                database=args["rz_db_name"],
                table_name=args["rz_table_name"],
                transformation_ctx="write to raw zone")
            print("SUCCESS: data loaded into rawzone")
        
        except Exception as e:
            print("Failed to write lz to the Raw Zone.")
            print("Exception: ", traceback.print_exc())
            raise
        
        #write to redshift dashboard table
        try:
            tgtsz_boomi_summary = glueContext.write_dynamic_frame.from_catalog(
                frame=boomiSummaryDyf,
                database="usmle_analytics",
                table_name="usmle_analytics_scrap_dashboard_v1_boomi_summary",
                redshift_tmp_dir=args["TempDir"],
                transformation_ctx="boomi_summary",
            )
            print("successful write to redshift boomi_summary.")
                                
        except Exception as e:
            print("Failed write to boomi_summary.")
            print("Error !!!! ", traceback.format_exc())
            print("Exception: ", e)
    else:
        print("no record available in landing zone")
    

 # Job Excecution
exit_code = 0

try:
    lz2rz()
except Exception as e:
    log.exception(e)
    exit_code = 1

job.commit()
if exit_code == 1:
    raise Exception("Failed with Errors")
os._exit(0)