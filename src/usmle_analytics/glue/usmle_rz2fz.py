#########################################################################
'''
Author: Vigneshwar
Description: This job reads data from  rawzone datacatlog which is parameterised and load into the refined zone using custom SQL statement stored in S3

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
import boto3
import json

log = logging.getLogger(__name__)
## @params: [JOB_NAME ,rz_db_name, rz_table_name, fz_db_name, fz_table_name]
args = getResolvedOptions(sys.argv, ["JOB_NAME", "rz_db_name", "rz_table_name", "fz_db_name", "fz_table_name","s3_bucket", "prefix", "sql_file_name" ])


sc = SparkContext()
glueContext = GlueContext(sc)
spark = glueContext.spark_session
job = Job(glueContext)
job.init(args["JOB_NAME"], args)


# queryString = "select incident_id, incident_date, email, etl_date, boomi_etl_date from sourceDynamicFrame"

#HELPER functions
def temp_view_creator(glueContext, db_name, tablename):
    try:
        dyf = glueContext.create_dynamic_frame.from_catalog(database=db_name, table_name=tablename)
        dyf.toDF().createOrReplaceTempView(tablename)
        return True
    except Exception as e:
        print("Issue in creating tempview")
        print(traceback.print_exc())
        return False


def s3_query_fetch():
    try:
        s3 = boto3.client('s3')
        result = s3.list_objects(Bucket = args["s3_bucket"], Prefix=args["prefix"] +  args["sql_file_name"])
        for o in result.get('Contents'):
            data = s3.get_object(Bucket=args["s3_bucket"], Key=o.get('Key'))
            contents = data['Body'].read()
        return contents.decode("utf-8")
    except Exception as e:
        print("Error occured at query fetch from s3")
        print(traceback.print_exc())
        

def rz2fz():
    try:
        queryString = s3_query_fetch()
        queryDf = spark.sql(queryString)
        dyf = DynamicFrame.fromDF(queryDf, glueContext, "query_dynamicframe")
        print("dyf.count() : ", dyf.count())
        print("Type : dyf.count(): ", dyf.count())
    except Exception as e:
        print("Failed to read raw zone table")
        print(traceback.print_exc())
        print("Exception: ", e)
        raise

    
    if dyf.count() >0:
        #write to refined zone
        try:
            final = glueContext.write_dynamic_frame.from_catalog(
                frame=dyf,
                database=args["fz_db_name"],
                table_name=args["fz_table_name"],
                transformation_ctx="write to refined zone")
            print("SUCCESS: data load completed in refined zone ")
        
        except Exception as e:
            print(traceback.print_exc())
            print("Failed to write to refined zone")
            print("Exception: ", e)
            raise
        
        
# Job Excecution
exit_code = 0

try:
    rz2fz()
except Exception as e:
    log.exception(e)
    exit_code = 1


job.commit()
if exit_code == 1:
    raise Exception("Failed with Errors")
os._exit(0) 