#########################################################################
'''
Author: Vigneshwar Thilagar
Description: This job is for testing some usecases in Tipsw QC
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
from pyspark.sql.functions import lit, col
import boto3
import time
from nbme_util_tips_0825 import qc_framework
import logging

log = logging.getLogger(__name__)

# @params: [JOB_NAME]
args = getResolvedOptions(sys.argv, ['JOB_NAME', 'DEFAULT_EMAIL_ADDR', 'qc_email_groups', 'group_dl', 'environment'])

sc = SparkContext()
glueContext = GlueContext(sc)
spark = glueContext.spark_session
job = Job(glueContext)
job.init(args['JOB_NAME'], args)

#########################################################################
# VARIABLES
qc_obj = qc_framework.QC(spark, glueContext, args["TempDir"])


#########################################################################
# HELPER FUNCTIONS

#########################################################################
# ETL FUNCTIONS

def send_qc_mail(batch_id, failed_tests, sub_prefix, to_addr):
    from nbme_util_tips_0825 import email_util
    sub_prefix = 'aws ' + args['environment'] + ': ' + sub_prefix
    try:
        print(batch_id, failed_tests, sub_prefix, to_addr)
        drop_mail_cols = ("alert_flag", "mail_it", "mail_pada", "mail_its", "mail_tds", "mail_bu_only_on_second_instance")
        failed_tests = failed_tests.drop(*drop_mail_cols)
        email_obj = email_util.Email(to_addr, sub_prefix + ' TIPS INCIDENT Test results')
        failed_tests.show()
        email_content = email_obj.create_email_content_from_df(failed_tests,
                                                               'Please find the TIPS INCIDENT QC failed test results for batch:%s' % batch_id)
        print(email_content)
        email_obj.send_email(email_content)
    except Exception as e:
        print("Failed to send email.")
        print("Exception: ", e)
        raise

#########################

def qc(group_name):
    tc = qc_obj.get_testcases_by_group_name(group_name)
    batch_id = qc_obj.execute_testcases(tc)
    result_df = qc_obj.get_batch_results(batch_id)
    result_df = result_df.orderBy(col("testcase_id"))

    failed_tests = result_df.filter(col("test_result") != 'success')
    print("Failed test count:%s" % failed_tests.count())
    result_df.show()
    failed_tests.show()
    
    try:
        qc_email_groups = args['qc_email_groups'].split(',')
        group_dl = eval(args['group_dl'])
        default_email = args['DEFAULT_EMAIL_ADDR'].split(',')
        for group in qc_email_groups:
            if group == 'IT':
                group_condition = "mail_" + str(group).lower() + "==1"
            else:
                group_condition = "mail_" + str(group).lower() + "==1 and mail_bu_only_on_second_instance <> 1 "
            fail_alert_df = failed_tests.filter(f"{group_condition} and alert_flag == 1")
            if fail_alert_df.count() > 0:
                send_qc_mail(batch_id, fail_alert_df, sub_prefix='Alert! ' + str(group).lower(),
                                 to_addr=group_dl.get(group, default_email))

            fail_error_df = failed_tests.filter(f"{group_condition} and alert_flag != 1")
            if fail_error_df.count() > 0:
                send_qc_mail(batch_id, fail_error_df, sub_prefix='ERROR! ' + str(group).lower(),
                                 to_addr=group_dl.get(group, default_email))
                                

    except Exception as e:
        print("Failed to send email.")
        print("Exception: ", e)
        # print(traceback.format_exc())
        raise


#########################################################################
# JOB EXECUTION
exit_code = 0

try:
    qc('count_check_usmle')
except Exception as e:
    log.exception(e)
    exit_code = 1


job.commit()
if exit_code == 1:
    raise Exception("Failed with Errors")
os._exit(0)