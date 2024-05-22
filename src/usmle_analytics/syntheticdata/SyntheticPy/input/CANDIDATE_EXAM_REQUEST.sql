create table CBIO_V1.CANDIDATE_EXAM_REQUEST
(
  usmle_id                     VARCHAR2(8) not null, --|INCID_2000
  exam                         VARCHAR2(7) not null, --|EXAM
  application_id               NUMBER(2) not null, --|NUMERICID
  agency_code                  VARCHAR2(1) not null, --|EXAM_TYPE
  last_name                    VARCHAR2(40) not null, --|LAST_NAME
  rest_of_name                 VARCHAR2(40), --|OPTIONAL_NAME
  first_name                   VARCHAR2(30), --|FIRST_NAME
  middle_name                  VARCHAR2(30), --|OPTIONAL_NAME
  generation_suffix            VARCHAR2(4), --|GENSUFFIX
  report_name                  VARCHAR2(68), --|REPORT
  gender                       VARCHAR2(1) default '?' not null, --|GENDER
  birth_year                   VARCHAR2(4), --|YYYY
  birth_month                  VARCHAR2(2), --|MM
  birth_day                    VARCHAR2(2), --|DD
  ssn                          VARCHAR2(9), --|SSN
  address1                     VARCHAR2(40), --|ADDRESS
  address2                     VARCHAR2(40), --|STREET_ADDRESS
  address3                     VARCHAR2(40), --|BUILDING_ADDRESS
  address4                     VARCHAR2(40), --|SECONDRY_ADDRESS
  city                         VARCHAR2(25),  --|CITY
  state                        VARCHAR2(2), --|STATE
  zip                          VARCHAR2(10), --|ZIPCODE
  country_id                   VARCHAR2(3), --|NUMERICID
  ethnics                      VARCHAR2(2), --|ETHNICS
  citizenship_country_id       VARCHAR2(3), --|NUMERICID
  school_id                    VARCHAR2(6), --|NUMERICID
  school_start_date            DATE, --|DATE
  school_end_date              DATE, --|DATE
  medical_specialty_id         NUMBER(2), --|NUMERICID
  hospital_id                  VARCHAR2(4), --|NUMERICID
  residency_type               VARCHAR2(2), --|CHOICE_C1,PR
  residency_start_date         DATE, --|DATE
  residency_end_date           DATE, --|DATE
  english_native_flag          VARCHAR2(1) default '?' not null, --|CHOICE_Y,N
  dominate_hand                VARCHAR2(1) default '?' not null, --|CHOICE_L,R
  exam_admin                   VARCHAR2(12), --|ADMIN
  exam_request_type            VARCHAR2(1) default 'C' not null, --|CHOICE_C,N
  registration_status          VARCHAR2(1) not null, --|CHOICE_Y,N
  registration_status_date     DATE, --|DATE
  proctor_attention_flag       VARCHAR2(1) default 'N' not null, --|CHOICE_Y,N
  sponsor_authority_id         VARCHAR2(3), --|NUMERICID
  accommodation_request_flag   VARCHAR2(1) default 'N' not null, --|CHOICE_Y,N
  accommodation_req_recd_flag  VARCHAR2(1) default '?' not null, --|CHOICE_Y,N
  accommodation_process_status VARCHAR2(1) default 'P' not null, --|CHOICE_Y,N
  test_accommodation_flag      VARCHAR2(1) default 'N' not null, --|CHOICE_Y,N
  special_administration       VARCHAR2(2), --|EXAM_TYPE
  score_annotation_flag        VARCHAR2(1) default 'N' not null, --|CHOICE_Y,N
  assigned_center_id           VARCHAR2(6), --|NUMERICID
  assigned_facility_id         VARCHAR2(2), --|NUMERICID
  original_usmle_id            VARCHAR2(8) not null, --|NUMERICID
  provider_id                  VARCHAR2(2), --|NUMERICID
  region_id                    VARCHAR2(2), --|NUMERICID
  eligibility_start_date       DATE, --|DATE
  eligibility_end_date         DATE, --|DATE
  score_report_date            DATE, --|DATE
  initial_create_date          DATE, --|DATE
  last_update_date             DATE, --|DATE
  request_record_id            NUMBER(16) not null, --|NUMERICID
  daytime_phone                VARCHAR2(30), --|PHONE
  internet_address             VARCHAR2(100), --|URL
  load_scores_flag             VARCHAR2(1) not null, --|CHOICE_Y,N
  withhold_school_type         VARCHAR2(1) default 'N' not null, --|CHOICE_A,B
  registration_id              VARCHAR2(10), --|NUMERICID
  requested_location_id        VARCHAR2(6), --|NUMERICID
  requested_exam_month         VARCHAR2(6), --|MM
  last_elig_update_date        DATE, --|DATE
  invoice_date                 DATE, --|DATE
  invoice_exam_fee             NUMBER(9,2), --|DECIMAL_9,2
  invoice_region_fee           NUMBER(9,2), --|DECIMAL_9,2
  ssn_last4                    VARCHAR2(4) --|SSN4
);
-- Add comments to the table 
comment on table CBIO_V1.CANDIDATE_EXAM_REQUEST
  is 'Information about an USMLE candidate''s request to attempt an USMLE';
-- Add comments to the columns 
comment on column CBIO_V1.CANDIDATE_EXAM_REQUEST.usmle_id
  is 'USMLE identification number';
comment on column CBIO_V1.CANDIDATE_EXAM_REQUEST.exam
  is 'USMLE exam acronym';
comment on column CBIO_V1.CANDIDATE_EXAM_REQUEST.application_id
  is 'A unique id for each incident that the candidate applys for an USMLE exam';
comment on column CBIO_V1.CANDIDATE_EXAM_REQUEST.agency_code
  is 'USMLE participating agency code';
comment on column CBIO_V1.CANDIDATE_EXAM_REQUEST.last_name
  is 'Last name (surname)';
comment on column CBIO_V1.CANDIDATE_EXAM_REQUEST.rest_of_name
  is 'The rest of the candidate''s full name without the surname (last name)';
comment on column CBIO_V1.CANDIDATE_EXAM_REQUEST.first_name
  is 'First name, if identifiable from rest of name';
comment on column CBIO_V1.CANDIDATE_EXAM_REQUEST.middle_name
  is 'Middle name, if identifiable from rest of name';
comment on column CBIO_V1.CANDIDATE_EXAM_REQUEST.generation_suffix
  is 'Generation suffix, e.g. JR, SR, III, VIII, etc.';
comment on column CBIO_V1.CANDIDATE_EXAM_REQUEST.report_name
  is 'Full name to be used on reports (<last name>, <rest of name>)';
comment on column CBIO_V1.CANDIDATE_EXAM_REQUEST.gender
  is 'Gender';
comment on column CBIO_V1.CANDIDATE_EXAM_REQUEST.birth_year
  is 'Self reported birth year';
comment on column CBIO_V1.CANDIDATE_EXAM_REQUEST.birth_month
  is 'Self reported birth month';
comment on column CBIO_V1.CANDIDATE_EXAM_REQUEST.birth_day
  is 'Self reported birth day';
comment on column CBIO_V1.CANDIDATE_EXAM_REQUEST.ssn
  is 'Self-reported (as in the application form) U.S social security number';
comment on column CBIO_V1.CANDIDATE_EXAM_REQUEST.address1
  is 'Address line 1';
comment on column CBIO_V1.CANDIDATE_EXAM_REQUEST.address2
  is 'Address line 2';
comment on column CBIO_V1.CANDIDATE_EXAM_REQUEST.address3
  is 'Address line 3';
comment on column CBIO_V1.CANDIDATE_EXAM_REQUEST.address4
  is 'Address line 4';
comment on column CBIO_V1.CANDIDATE_EXAM_REQUEST.city
  is 'City';
comment on column CBIO_V1.CANDIDATE_EXAM_REQUEST.state
  is 'State/Province code';
comment on column CBIO_V1.CANDIDATE_EXAM_REQUEST.zip
  is 'Zip code';
comment on column CBIO_V1.CANDIDATE_EXAM_REQUEST.country_id
  is 'Address country code';
comment on column CBIO_V1.CANDIDATE_EXAM_REQUEST.ethnics
  is 'Ethnicity code';
comment on column CBIO_V1.CANDIDATE_EXAM_REQUEST.citizenship_country_id
  is 'Country of citizenship upon entering the medical school';
comment on column CBIO_V1.CANDIDATE_EXAM_REQUEST.school_id
  is 'Medical school the candidate enrolled (or last enrolled) upon requesting the exam';
comment on column CBIO_V1.CANDIDATE_EXAM_REQUEST.school_start_date
  is 'Self reported starting date of medical school enrollment';
comment on column CBIO_V1.CANDIDATE_EXAM_REQUEST.school_end_date
  is 'Self reported ending date of medical school enrollment';
comment on column CBIO_V1.CANDIDATE_EXAM_REQUEST.medical_specialty_id
  is 'Medical specialty code';
comment on column CBIO_V1.CANDIDATE_EXAM_REQUEST.hospital_id
  is 'Hospital of residency';
comment on column CBIO_V1.CANDIDATE_EXAM_REQUEST.residency_type
  is 'Residency type';
comment on column CBIO_V1.CANDIDATE_EXAM_REQUEST.residency_start_date
  is 'Self reported start date of last residency program';
comment on column CBIO_V1.CANDIDATE_EXAM_REQUEST.residency_end_date
  is 'Self reported end date of last residency program';
comment on column CBIO_V1.CANDIDATE_EXAM_REQUEST.english_native_flag
  is 'Native language is English';
comment on column CBIO_V1.CANDIDATE_EXAM_REQUEST.dominate_hand
  is 'Dominate hand (right or left handed)';
comment on column CBIO_V1.CANDIDATE_EXAM_REQUEST.exam_admin
  is 'USMLE Exam Admin';
comment on column CBIO_V1.CANDIDATE_EXAM_REQUEST.exam_request_type
  is 'Exam Request Type: P=Paper and Pencil, C=Computer Based';
comment on column CBIO_V1.CANDIDATE_EXAM_REQUEST.registration_status
  is 'Registration status code';
comment on column CBIO_V1.CANDIDATE_EXAM_REQUEST.registration_status_date
  is 'Timestamp of last time registration status was changed';
comment on column CBIO_V1.CANDIDATE_EXAM_REQUEST.proctor_attention_flag
  is 'A USMLE flag to indicate special proctoring is required for this attempt';
comment on column CBIO_V1.CANDIDATE_EXAM_REQUEST.sponsor_authority_id
  is 'Licensing authority (state board) that sponsored this request of examination (for Step 3 only)';
comment on column CBIO_V1.CANDIDATE_EXAM_REQUEST.accommodation_request_flag
  is 'Whether test accommodation is needed (as provided by application form)';
comment on column CBIO_V1.CANDIDATE_EXAM_REQUEST.accommodation_req_recd_flag
  is 'Whether test accommodation document is received by OTA';
comment on column CBIO_V1.CANDIDATE_EXAM_REQUEST.accommodation_process_status
  is 'Test accommodation process status: P=Pending, H=Hold, R=Release';
comment on column CBIO_V1.CANDIDATE_EXAM_REQUEST.test_accommodation_flag
  is 'Test accommodation is needed for this request of exam';
comment on column CBIO_V1.CANDIDATE_EXAM_REQUEST.special_administration
  is 'Special administration code';
comment on column CBIO_V1.CANDIDATE_EXAM_REQUEST.score_annotation_flag
  is 'Due to the type of test accommodation provided, this score report needs to be annotated';
comment on column CBIO_V1.CANDIDATE_EXAM_REQUEST.assigned_center_id
  is 'Assigned test center id (for historical paper and pencil exams only)';
comment on column CBIO_V1.CANDIDATE_EXAM_REQUEST.assigned_facility_id
  is 'Assigned facility in a test center';
comment on column CBIO_V1.CANDIDATE_EXAM_REQUEST.original_usmle_id
  is 'Original USMLE ID of this record before any consolidation';
comment on column CBIO_V1.CANDIDATE_EXAM_REQUEST.provider_id
  is 'Exam provider as requested by the candidate';
comment on column CBIO_V1.CANDIDATE_EXAM_REQUEST.region_id
  is 'Exam provider region id selected by the candidate';
comment on column CBIO_V1.CANDIDATE_EXAM_REQUEST.eligibility_start_date
  is 'The first day the candidate is eligibility to take the exam';
comment on column CBIO_V1.CANDIDATE_EXAM_REQUEST.eligibility_end_date
  is 'The last day the candidate is eligible to take the exam';
comment on column CBIO_V1.CANDIDATE_EXAM_REQUEST.score_report_date
  is 'Date score report was sent to the candidate';
comment on column CBIO_V1.CANDIDATE_EXAM_REQUEST.initial_create_date
  is 'Date this record was created for entry into CIBIS';
comment on column CBIO_V1.CANDIDATE_EXAM_REQUEST.last_update_date
  is 'Date this record was updated/entered';
comment on column CBIO_V1.CANDIDATE_EXAM_REQUEST.request_record_id
  is 'Unique record id assigned to this exam request';
comment on column CBIO_V1.CANDIDATE_EXAM_REQUEST.daytime_phone
  is 'Daytime phone# provided on the application form';
comment on column CBIO_V1.CANDIDATE_EXAM_REQUEST.internet_address
  is 'E-mail address provided on the application form';
comment on column CBIO_V1.CANDIDATE_EXAM_REQUEST.withhold_school_type
  is '''N''=Normal (release data), ''S''=Withhold data (do not release data)';