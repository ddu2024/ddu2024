create table CEXM_V1.CANDIDATE_EXAM_OUTCOME
(
  usmle_id                  VARCHAR2(8) not null, --|INCID_1000
  exam                      VARCHAR2(7) not null, --|EXAM
  application_id            NUMBER(2) not null, --|NUMERICID
  eligibility_send_date     DATE, --|TIMESTAMP
  permit_send_date          DATE, --|TIMESTAMP
  outcome_receive_date      DATE, --|TIMESTAMP
  score_available_date      DATE, --|TIMESTAMP
  eligibility_start_date    DATE, --|TIMESTAMP
  eligibility_end_date      DATE, --|TIMESTAMP
  attended_center_id        VARCHAR2(6), --|NUMERICID
  permit_scheduling_id      VARCHAR2(9), --|NUMERICID
  scheduled_provider_id     VARCHAR2(2), --|NUMERICID
  scheduled_location_id     VARCHAR2(6), --|NUMERICID
  scheduled_start_date      DATE, --|TIMESTAMP
  actual_provider_id        VARCHAR2(2), --|NUMERICID
  actual_location_id        VARCHAR2(6), --|NUMERICID
  actual_start_date         DATE, --|TIMESTAMP
  actual_end_date           DATE, --|TIMESTAMP
  elapsed_days              NUMBER(2), --|NUMERICID
  elapsed_hours             NUMBER(5,2), --|DECIMAL_5,2
  photo_image_location      VARCHAR2(50), --|FILE_LOC
  exam_completion_status    VARCHAR2(1) default '?' not null, --|CHOICE_C,N
  score_status              VARCHAR2(1) default '?' not null, --|EXAM_TYPE
  indeterminate_flag        VARCHAR2(1) default '?' not null, --|CHOICE_Y,N
  irregularity_status       VARCHAR2(1) default 'N' not null, --|CHOICE_Y,N
  three_digit_score         NUMBER(3), --|NUMERICID
  two_digit_score           NUMBER(3,1), --|DECIMAL_3,1
  pass_fail                 VARCHAR2(1) default '?' not null, --|CHOICE_P,F
  examination_repeat_flag   VARCHAR2(1) default '?' not null, --|CHOICE_Y,N
  counted_attempt_flag      VARCHAR2(1) default '?' not null, --|CHOICE_Y,N
  scoring_task_id           NUMBER(8), --|NUMERICID
  exam_request_id           NUMBER(12), --|NUMERICID
  last_update_date          DATE, --|TIMESTAMP
  covert_push_date          DATE, --|TIMESTAMP
  covert_pull_date          DATE, --|TIMESTAMP
  processing_complete_flag  VARCHAR2(1) default 'N' not null, --|CHOICE_Y,N
  permanent_score_hold_flag VARCHAR2(1), --|CHOICE_P,F
  addendum_send_date        DATE, --|TIMESTAMP
  appointment_status        VARCHAR2(1), --|CHOICE_Y,N
  last_elig_send_date       DATE, --|TIMESTAMP
  ice_pass_fail             VARCHAR2(1), --|CHOICE_P,F
  cis_pass_fail             VARCHAR2(1), --|CHOICE_P,F
  sep_pass_fail             VARCHAR2(1), --|CHOICE_P,F
  practice_exam_request_id  NUMBER(12), --|NUMERICID
  practice_permit_date      DATE, --|TIMESTAMP
  score_report_document_id  VARCHAR2(100), --|NUMERICID
  first_session_sks_id      VARCHAR2(16) --|NUMERICID
);
-- Add comments to the table 
comment on table CEXM_V1.CANDIDATE_EXAM_OUTCOME
  is 'Outcome (score, pass/fail, completion status) of each request of exam';
-- Add comments to the columns 
comment on column CEXM_V1.CANDIDATE_EXAM_OUTCOME.usmle_id
  is 'USMLE identification number';
comment on column CEXM_V1.CANDIDATE_EXAM_OUTCOME.exam
  is 'USMLE exam acronym';
comment on column CEXM_V1.CANDIDATE_EXAM_OUTCOME.application_id
  is 'A unique id for each incident that the candidate applys for an USMLE exam';
comment on column CEXM_V1.CANDIDATE_EXAM_OUTCOME.eligibility_send_date
  is 'Date that exam eligibility is sent to the exam provider';
comment on column CEXM_V1.CANDIDATE_EXAM_OUTCOME.permit_send_date
  is 'Date that scheduling permit is sent to the candidate';
comment on column CEXM_V1.CANDIDATE_EXAM_OUTCOME.outcome_receive_date
  is 'Date that exam outcome is received from the exam provider';
comment on column CEXM_V1.CANDIDATE_EXAM_OUTCOME.score_available_date
  is 'Date score is available for report';
comment on column CEXM_V1.CANDIDATE_EXAM_OUTCOME.eligibility_start_date
  is 'The first day that the candidate is eligible to take the exam';
comment on column CEXM_V1.CANDIDATE_EXAM_OUTCOME.eligibility_end_date
  is 'The last day the candidate is eligible to take the exam';
comment on column CEXM_V1.CANDIDATE_EXAM_OUTCOME.attended_center_id
  is 'For paper and pencil exam: actual attended test center id';
comment on column CEXM_V1.CANDIDATE_EXAM_OUTCOME.permit_scheduling_id
  is 'Scheduling id as printed on the scheduling permit';
comment on column CEXM_V1.CANDIDATE_EXAM_OUTCOME.scheduled_provider_id
  is 'The provider where the exam is scheduled to be taken at';
comment on column CEXM_V1.CANDIDATE_EXAM_OUTCOME.scheduled_location_id
  is 'Scheduled exam provider''s location';
comment on column CEXM_V1.CANDIDATE_EXAM_OUTCOME.scheduled_start_date
  is 'Scheduled exam start date';
comment on column CEXM_V1.CANDIDATE_EXAM_OUTCOME.actual_provider_id
  is 'The provider where the exam is actually taken';
comment on column CEXM_V1.CANDIDATE_EXAM_OUTCOME.actual_location_id
  is 'Exam provider location where the exam was attempted';
comment on column CEXM_V1.CANDIDATE_EXAM_OUTCOME.actual_start_date
  is 'The exam attempt start date as it applies to this applicant';
comment on column CEXM_V1.CANDIDATE_EXAM_OUTCOME.actual_end_date
  is 'The exam attempt end date as it applies to this applicant';
comment on column CEXM_V1.CANDIDATE_EXAM_OUTCOME.elapsed_days
  is 'Total number of days the exam has elapsed';
comment on column CEXM_V1.CANDIDATE_EXAM_OUTCOME.elapsed_hours
  is 'Total number of hours the exam has elapsed';
comment on column CEXM_V1.CANDIDATE_EXAM_OUTCOME.photo_image_location
  is 'The file/directory location of the photo image of the examinee taken during the exam';
comment on column CEXM_V1.CANDIDATE_EXAM_OUTCOME.exam_completion_status
  is 'Exam completion status code';
comment on column CEXM_V1.CANDIDATE_EXAM_OUTCOME.score_status
  is 'Score status code';
comment on column CEXM_V1.CANDIDATE_EXAM_OUTCOME.indeterminate_flag
  is 'Indeterminate flat: Y=Yes, N=No, ?=N/A';
comment on column CEXM_V1.CANDIDATE_EXAM_OUTCOME.irregularity_status
  is 'Irregularity status, ?=Unknown, R=Irregular, P=Irregular with proactive notification, N=No';
comment on column CEXM_V1.CANDIDATE_EXAM_OUTCOME.three_digit_score
  is 'Three-digit score for this exam attempt';
comment on column CEXM_V1.CANDIDATE_EXAM_OUTCOME.two_digit_score
  is 'Two-digit score for this exam attempt';
comment on column CEXM_V1.CANDIDATE_EXAM_OUTCOME.pass_fail
  is 'Pass/fail decision for this exam attempt';
comment on column CEXM_V1.CANDIDATE_EXAM_OUTCOME.examination_repeat_flag
  is 'The flag is set (to ''Y'') if this is a repeated attempt for the exam';
comment on column CEXM_V1.CANDIDATE_EXAM_OUTCOME.counted_attempt_flag
  is 'The flag will be set (to ''Y''), if this attempt is counted.';
comment on column CEXM_V1.CANDIDATE_EXAM_OUTCOME.scoring_task_id
  is 'The TAPS scoring task that this exam attempt was scored and processed';
comment on column CEXM_V1.CANDIDATE_EXAM_OUTCOME.exam_request_id
  is 'The examinee id assigned by TAPS during scoring and processing';
comment on column CEXM_V1.CANDIDATE_EXAM_OUTCOME.last_update_date
  is 'Date record was last updated/entered';
comment on column CEXM_V1.CANDIDATE_EXAM_OUTCOME.covert_push_date
  is 'The last time this record is pushed to Covert';
comment on column CEXM_V1.CANDIDATE_EXAM_OUTCOME.covert_pull_date
  is 'Last time this record is pull to Covert';
comment on column CEXM_V1.CANDIDATE_EXAM_OUTCOME.permanent_score_hold_flag
  is 'The flag will be set (to ''Y''), if the score is held permanently.';
comment on column CEXM_V1.CANDIDATE_EXAM_OUTCOME.addendum_send_date
  is 'The date on which addendum was sent.';