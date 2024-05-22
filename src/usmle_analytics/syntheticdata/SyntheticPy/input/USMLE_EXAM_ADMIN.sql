--exam_admin is PK
create table CBIO_V1.USMLE_EXAM_ADMIN
(
  exam_admin  VARCHAR2(12) not null, --|ADMIN
  description VARCHAR2(70) not null, --|EXAM_DESC
  start_date  DATE, --|TIMESTAMP
  end_date    DATE, --|TIMESTAMP
  exam        VARCHAR2(7) not null --|EXAM
);
-- Add comments to the table 
comment on table CBIO_V1.USMLE_EXAM_ADMIN
  is 'USMLE Paper and Pencil Exam Administration';
-- Add comments to the columns 
comment on column CBIO_V1.USMLE_EXAM_ADMIN.exam_admin
  is 'USMLE exam admin code';
comment on column CBIO_V1.USMLE_EXAM_ADMIN.description
  is 'Exam admin description';
comment on column CBIO_V1.USMLE_EXAM_ADMIN.start_date
  is 'Exam admin start date';
comment on column CBIO_V1.USMLE_EXAM_ADMIN.end_date
  is 'Exam admin end date';
comment on column CBIO_V1.USMLE_EXAM_ADMIN.exam
  is 'USMLE Exam acronym';