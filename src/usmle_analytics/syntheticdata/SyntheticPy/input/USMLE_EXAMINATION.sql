--exam is PK
create table CBIO_V1.USMLE_EXAMINATION
(
  exam            VARCHAR2(7) not null, --|EXAM
  description     VARCHAR2(40) not null, --|EXAM_DESC
  usmle_exam_type VARCHAR2(1) not null, --|EXAM_TYPE
  exam_alias      VARCHAR2(10) --|EXAM
);
-- Add comments to the table 
comment on table CBIO_V1.USMLE_EXAMINATION
  is 'Exams offered by USMLE';
-- Add comments to the columns 
comment on column CBIO_V1.USMLE_EXAMINATION.exam
  is 'USMLE exam acronym';
comment on column CBIO_V1.USMLE_EXAMINATION.description
  is 'Exam description';
comment on column CBIO_V1.USMLE_EXAMINATION.usmle_exam_type
  is 'USMLE Exam Type: A=Step 1, B=Step 2, C=Step 3';
comment on column CBIO_V1.USMLE_EXAMINATION.exam_alias
  is 'Exam alias, used in reports';