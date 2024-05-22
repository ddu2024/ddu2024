#########################################################################
'''
Author: Vigneshwar Thilagar

Description: This is a template job for purging the tables based on the table name sent as job parameter

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

# @params: ["JOB_NAME", "db_name", "table_name"]
args = getResolvedOptions(sys.argv, ["JOB_NAME", "db_name", "table_name"])

sc = SparkContext()
glueContext = GlueContext(sc)
spark = glueContext.spark_session
job = Job(glueContext)
job.init(args["JOB_NAME"], args)
ETL_DATE = datetime.utcnow()


def purge_lz_table(db_name, table_name):
    try: 
        print("Attempting to {0}.{1}".format(db_name, table_name))
        glueContext.purge_table(db_name, 
                                 table_name, 
                                {"retentionPeriod": 0})
        print("Successfully purged {0}.{1}".format(db_name, table_name))
    except Exception as e:
        print("Failed to purge {0}.{1}".format(db_name, table_name))
        print("Exception: ", traceback.print_exc())
        raise
    
 # Job Excecution
exit_code = 0

try:
    purge_lz_table(args["db_name"], args["table_name"])
except Exception as e:
    log.exception(e)
    exit_code = 1

job.commit()
if exit_code == 1:
    raise Exception("Failed with Errors")
os._exit(0)