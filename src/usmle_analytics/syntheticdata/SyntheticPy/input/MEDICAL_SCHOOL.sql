--school_id is PK
create table CBIO_V1.MEDICAL_SCHOOL
(
  school_id         VARCHAR2(6) not null, --|NUMERICID
  name              VARCHAR2(100) not null, --|SCHOOL_NAME
  state_code        VARCHAR2(2), --|STATE_CODE
  country_id        VARCHAR2(3), --|NUMERICID
  short_name        VARCHAR2(25) not null, --|SCHOOL_SHORT
  foreign_name      VARCHAR2(80), --|FOREIGN_NAME 
  create_dtime      DATE, --|TIMESTAMP
  last_update_dtime DATE  --|TIMESTAMP
);
-- Add comments to the table 
comment on table CBIO_V1.MEDICAL_SCHOOL
  is 'Medical School look-up';
-- Add comments to the columns 
comment on column CBIO_V1.MEDICAL_SCHOOL.school_id
  is 'Medical school id';
comment on column CBIO_V1.MEDICAL_SCHOOL.name
  is 'Medical school name';
comment on column CBIO_V1.MEDICAL_SCHOOL.state_code
  is 'State code';
comment on column CBIO_V1.MEDICAL_SCHOOL.country_id
  is 'Home country of the medical school';
comment on column CBIO_V1.MEDICAL_SCHOOL.short_name
  is 'Medical school name in short form';
comment on column CBIO_V1.MEDICAL_SCHOOL.foreign_name
  is 'Medical school name in foreign spelling';