
create table CBIO_V1.CANDIDATE_CURRENT_BIOG
(
  usmle_id                  VARCHAR2(8) not null, --|INCID_1000
  last_name                 VARCHAR2(40) not null, --|LAST_NAME
  rest_of_name              VARCHAR2(40),--|OPTIONAL_NAME
  first_name                VARCHAR2(30),--|FIRST_NAME
  middle_name               VARCHAR2(30),--|OPTIONAL_NAME
  generation_suffix         VARCHAR2(4),  --|GENSUFFIX
  gender                    VARCHAR2(1) default '?' not null, --|GENDER
  birth_year                VARCHAR2(4) default '----',--|YYYY
  birth_month               VARCHAR2(2) default '--',--|MM
  birth_day                 VARCHAR2(2) default '--',--|DD
  deceased_year             VARCHAR2(4) default '----', --|DECEASED_YYYY
  deceased_month            VARCHAR2(2) default '--',--|DECEASED_MM
  deceased_day              VARCHAR2(2) default '--',--|DECEASED_DD
  deceased_flag             VARCHAR2(1) default 'N' not null, --|IS_DECEASED
  school_id                 NUMERIC(6), --|NUMERICID
  ssn                       VARCHAR2(9), --|SSN
  synchronized_flag         VARCHAR2(1) default 'N' not null, --|CHOICE_Y,N
  alert_note_available_flag VARCHAR2(1) default 'N' not null, --|CHOICE_Y,N
  graduation_year           VARCHAR2(4) default '----', --|YYYY
  graduation_month          VARCHAR2(2) default '--', --|MM
  graduation_verified_flag  VARCHAR2(1) default 'N' not null, --|CHOICE_Y,N
  internet_address          VARCHAR2(100), --|URL
  ssn_last4                 VARCHAR2(4) --|SSN4
);
-- Add comments to the table 
comment on table CBIO_V1.CANDIDATE_CURRENT_BIOG
  is 'The current USMLE Biographic view of this USMLE candidate.';
-- Add comments to the columns 
comment on column CBIO_V1.CANDIDATE_CURRENT_BIOG.usmle_id
  is 'USMLE identification number';
comment on column CBIO_V1.CANDIDATE_CURRENT_BIOG.last_name
  is 'The self-reported last name (or surname) of a candidate.';
comment on column CBIO_V1.CANDIDATE_CURRENT_BIOG.rest_of_name
  is 'All but last name (SURNAME) separated by spaces.';
comment on column CBIO_V1.CANDIDATE_CURRENT_BIOG.first_name
  is 'First name';
comment on column CBIO_V1.CANDIDATE_CURRENT_BIOG.middle_name
  is 'Middle name';
comment on column CBIO_V1.CANDIDATE_CURRENT_BIOG.generation_suffix
  is 'The generation suffix for this USMLE candidate, e.g. JR, VII, SR.';
comment on column CBIO_V1.CANDIDATE_CURRENT_BIOG.gender
  is 'Gender of applicant.';
comment on column CBIO_V1.CANDIDATE_CURRENT_BIOG.birth_year
  is 'Birth Year';
comment on column CBIO_V1.CANDIDATE_CURRENT_BIOG.birth_month
  is 'Birth Month';
comment on column CBIO_V1.CANDIDATE_CURRENT_BIOG.birth_day
  is 'Birth Day';
comment on column CBIO_V1.CANDIDATE_CURRENT_BIOG.deceased_year
  is 'Deceased Year';
comment on column CBIO_V1.CANDIDATE_CURRENT_BIOG.deceased_month
  is 'Deceased Month';
comment on column CBIO_V1.CANDIDATE_CURRENT_BIOG.deceased_day
  is 'Deceased Day';
comment on column CBIO_V1.CANDIDATE_CURRENT_BIOG.deceased_flag
  is 'Flag indicating deceased status.';
comment on column CBIO_V1.CANDIDATE_CURRENT_BIOG.school_id
  is 'Most current medical school enrolled';
comment on column CBIO_V1.CANDIDATE_CURRENT_BIOG.ssn
  is 'U.S. social security number';
comment on column CBIO_V1.CANDIDATE_CURRENT_BIOG.synchronized_flag
  is 'A flag to indicate if this record need to be resynchronized with other CIBIS info';
comment on column CBIO_V1.CANDIDATE_CURRENT_BIOG.alert_note_available_flag
  is 'Alert note is available from either NBME, FSMB or ECFMG';
comment on column CBIO_V1.CANDIDATE_CURRENT_BIOG.graduation_year
  is 'Actual/expected medical school graduation year';
comment on column CBIO_V1.CANDIDATE_CURRENT_BIOG.graduation_month
  is 'Actual/expected medical school graduation month';
comment on column CBIO_V1.CANDIDATE_CURRENT_BIOG.graduation_verified_flag
  is 'Medical school graduation is verified: Y=Yes, N=No';
