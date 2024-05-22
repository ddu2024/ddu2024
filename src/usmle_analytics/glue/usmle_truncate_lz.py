#########################################################################
'''

Author: Brian Kong
Description: Truncate for usmle lz
Helper Functions:
'''
#########################################################################
import sys
import os
# import traceback
from awsglue.transforms import *
from awsglue.utils import getResolvedOptions
from pyspark.context import SparkContext
from awsglue.context import GlueContext
from awsglue.job import Job
from awsglue.dynamicframe import DynamicFrame
import pyspark.sql.functions as ps_sql_funcs
from pyspark.sql.functions import col, lit
import pyspark.sql as ps_sql
import pyspark.sql.types as ps_types
import json
import boto3
import datetime
import time
import pandas as pd
from nbme_util import qc_framework
import logging

log = logging.getLogger(__name__)
###########################################################################
## @params: [JOB_NAME]
args = getResolvedOptions(sys.argv, ['JOB_NAME'])
###########################################################################
sc = SparkContext()
glueContext = GlueContext(sc)
spark = glueContext.spark_session
job = Job(glueContext)
job.init(args['JOB_NAME'], args)
###########################################################################
# VARIABLES
###########################################################################

def purge_cvrt_lz_boomi_summary(DEBUG=False):
    try: 
        if DEBUG: print("Attempting to purge cvrt.lz_boomi_summary")
        glueContext.purge_table("cvrt", 
                                 "lz_boomi_summary", 
                                {"retentionPeriod": 0})
        print("Successfully purged cvrt.lz_boomi_summary")
    except Exception as e:
        print("Failed to purge cvrt.lz_boomi_summary")
        print("Exception: ", e)
        raise error

#########################

def purge_cvrt_exam_event():
    try: 
        print("Attempting to purge cvrt.lz_exam_event")
        glueContext.purge_table("cvrt", 
                                 "exam_event", 
                                {"retentionPeriod": 0})
        print("Successfully purged cvrt.lz_exam_event")
    except Exception as e:
        print("Failed to purge cvrt.lz_exam_event")
        print("Exception: ", e)
        raise
    
#########################################################################
# JOB EXECUTION
exit_code = 0


try:
    purge_taps_lz_examinee()
except Exception as e:
    log.exception(e)
    exit_code = 1

try:
    purge_taps_lz_boomi_summary()
except Exception as e:
    log.exception(e)
    exit_code = 1

job.commit()
if exit_code == 1:
    raise Exception("Failed with Errors")
os._exit(0)