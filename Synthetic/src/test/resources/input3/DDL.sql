-- Start of DDL Script for Table RPTI_V1.table_1
-- Generated 5/3/2023 11:34:44 AM from RPTI_V1@WBDV

CREATE TABLE table_1
    (profile_examinee_id NUMBER NOT NULL,
    report_id NUMBER NOT NULL,
    sort_order NUMBER(9,0) NOT NULL,
    examinee_id VARCHAR2(30),
    last_name VARCHAR2(40),
    first_name VARCHAR2(100),
    middle_name VARCHAR2(30),
    test_date DATE NOT NULL,
    total_test_score NUMBER,
    total_raw_score NUMBER,
    total_percent_correct_score NUMBER,
    total_sem NUMBER,
    primary_category_description VARCHAR2(100),
    taps_examinee_id VARCHAR2(30),
    original_exam_date DATE,
    report_period_start_date DATE,
    report_period_end_date DATE,
    exam_instance VARCHAR2(11),
    school_cohort VARCHAR2(100),
    score_category_id VARCHAR2(3),
    histogram_set_id NUMBER,
    recent_mean NUMBER(3,0),
    recent_sd NUMBER(3,0),
    profile_char_size VARCHAR2(1),
    pass_probability NUMBER(3,0))
  SEGMENT CREATION IMMEDIATE
  TABLESPACE  rpti_v1_table
  NOPARALLEL
  LOGGING
  MONITORING
/


-- Indexes for table_1

CREATE INDEX table_1_dx02 ON table_1
  (
    histogram_set_id  ASC
  )
  TABLESPACE  rpti_v1_index
NOPARALLEL
LOGGING
/

CREATE INDEX table_1_dx01 ON table_1
  (
    report_id  ASC
  )
  TABLESPACE  rpti_v1_index
NOPARALLEL
LOGGING
/



-- Constraints for table_1



ALTER TABLE table_1
ADD CONSTRAINT table_1_pk PRIMARY KEY (profile_examinee_id)
USING INDEX
  TABLESPACE  rpti_v1_index
/


-- Triggers for table_1

CREATE OR REPLACE TRIGGER table_1_b_i01
 BEFORE
  INSERT
 ON table_1
REFERENCING NEW AS NEW OLD AS OLD
 FOR EACH ROW
BEGIN
  if :new.profile_examinee_id is null then
     select rpti_seq_profile_examinee_id.nextval
       into :new.profile_examinee_id
       from dual;
  end if;
END;
/


-- Comments for table_1

COMMENT ON COLUMN table_1.examinee_id IS 'candidate id in TAPS or FBPI'
/
COMMENT ON COLUMN table_1.taps_examinee_id IS 'examinee id in TAPS or FBPI'
/

-- End of DDL Script for Table RPTI_V1.table_1

-- Foreign Key
ALTER TABLE table_1
ADD CONSTRAINT table_1_fk01 FOREIGN KEY (report_id)
REFERENCES table_2 (report_id)
/
ALTER TABLE table_1
ADD CONSTRAINT table_1_fk02 FOREIGN KEY (histogram_set_id)
REFERENCES table_3 (histogram_set_id)
/
-- End of DDL script for Foreign Key(s)
-- Start of DDL Script for Table RPTI_V1.table_2
-- Generated 5/3/2023 2:59:25 PM from RPTI_V1@WBDV

CREATE TABLE table_2
    (report_id NUMBER NOT NULL,
    source_id NUMBER NOT NULL,
    report_type_id VARCHAR2(20) NOT NULL,
    sort_order NUMBER(9,0),
    active_flag VARCHAR2(1) DEFAULT 'Y' NOT NULL,
    destination_document_path VARCHAR2(250) NOT NULL,
    template_document VARCHAR2(100),
    school_id VARCHAR2(30) NOT NULL,
    org_party_id VARCHAR2(5),
    school_name VARCHAR2(100) NOT NULL,
    exam_name VARCHAR2(100) NOT NULL,
    scaling_mean NUMBER,
    scaling_sd NUMBER,
    sem_bar_width_factor NUMBER,
    sd_graph_width_factor NUMBER,
    physical_chart_width NUMBER,
    post_date DATE,
    graph_left_position NUMBER,
    graph_right_position NUMBER,
    physical_unit_factor NUMBER,
    create_date DATE,
    location_only_flag VARCHAR2(1) DEFAULT 'N' NOT NULL,
    physical_band_left_position NUMBER,
    physical_band_right_position NUMBER,
    left_title VARCHAR2(100),
    right_title VARCHAR2(100),
    band_title VARCHAR2(100),
    report_status VARCHAR2(30),
    band_left_position NUMBER,
    band_right_position NUMBER,
    marker_line_position NUMBER,
    physical_marker_line_position NUMBER,
    marker_line_title VARCHAR2(100),
    report_catalog VARCHAR2(100),
    total_examinee_volume NUMBER,
    profile_required_flag VARCHAR2(1),
    longitude_required_flag VARCHAR2(1),
    graph_low_title VARCHAR2(100),
    graph_high_title VARCHAR2(100),
    graph_title VARCHAR2(100),
    graph_low_position NUMBER,
    graph_high_position NUMBER,
    graph_inter_range NUMBER,
    exam VARCHAR2(7),
    requester_internet_address VARCHAR2(100),
    sig_sem NUMBER,
    sig_proficiency NUMBER,
    profile_all_report_id NUMBER,
    preliminary_flag VARCHAR2(1),
    score_type VARCHAR2(1),
    modular_order_flag VARCHAR2(1),
    report_config_id NUMBER)
  SEGMENT CREATION IMMEDIATE
  TABLESPACE  rpti_v1_table
  NOPARALLEL
  LOGGING
  MONITORING
/


-- Indexes for table_2

CREATE INDEX table_2_dx02 ON table_2
  (
    report_type_id  ASC
  )
  TABLESPACE  rpti_v1_index
NOPARALLEL
LOGGING
/

CREATE INDEX table_2_dx05 ON table_2
  (
    report_type_id  ASC,
    report_status  ASC,
    active_flag  ASC,
    source_id  ASC
  )
  TABLESPACE  rpti_v1_index
NOPARALLEL
LOGGING
/

CREATE INDEX table_2_dx04 ON table_2
  (
    profile_all_report_id  ASC
  )
  TABLESPACE  rpti_v1_index
NOPARALLEL
LOGGING
/

CREATE INDEX table_2_dx01 ON table_2
  (
    report_status  ASC
  )
  TABLESPACE  rpti_v1_index
NOPARALLEL
LOGGING
/

CREATE INDEX table_2_dx03 ON table_2
  (
    source_id  ASC
  )
  TABLESPACE  rpti_v1_index
NOPARALLEL
LOGGING
/

CREATE INDEX table_2_dx06 ON table_2
  (
    report_config_id  ASC
  )
  TABLESPACE  rpti_v1_index
NOPARALLEL
LOGGING
/



-- Constraints for table_2

ALTER TABLE table_2
ADD CONSTRAINT table_2_ck01 CHECK (Active_flag in ('Y','N'))
/

ALTER TABLE table_2
ADD CONSTRAINT table_2_ck02 CHECK (Location_only_flag in ('Y','N'))
/




ALTER TABLE table_2
ADD CONSTRAINT table_2_pk PRIMARY KEY (report_id)
USING INDEX
  TABLESPACE  rpti_v1_index
/


-- Triggers for table_2

CREATE OR REPLACE TRIGGER table_2_b_i01
 BEFORE
  INSERT
 ON table_2
REFERENCING NEW AS NEW OLD AS OLD
 FOR EACH ROW
BEGIN
  if :new.report_id is null then
     select rpti_seq_report_id.nextval
       into :new.report_id
       from dual;
  end if;
  :new.create_date := sysdate;
END;
/


-- End of DDL Script for Table RPTI_V1.table_2

-- Foreign Key
ALTER TABLE table_2
ADD CONSTRAINT table_2_fk02 FOREIGN KEY (report_status)
REFERENCES table_4 (report_status)
/
ALTER TABLE table_2
ADD CONSTRAINT table_2_fk01 FOREIGN KEY (report_type_id)
REFERENCES table_6 (report_type_id)
/
ALTER TABLE table_2
ADD CONSTRAINT table_2_fk03 FOREIGN KEY (report_config_id)
REFERENCES table_5 (report_config_id)
/
-- End of DDL script for Foreign Key(s)
-- Start of DDL Script for Table RPTI_V1.table_3
-- Generated 5/3/2023 3:01:28 PM from RPTI_V1@WBDV

CREATE TABLE table_3
    (histogram_set_id NUMBER NOT NULL,
    exam VARCHAR2(20) NOT NULL,
    remarks VARCHAR2(1000),
    effective_start_date DATE,
    create_date DATE,
    last_updated_date DATE,
    mean_score NUMBER(3,0),
    std NUMBER(3,0),
    see NUMBER(3,0),
    mps NUMBER(3,0),
    comparison_group_time_period VARCHAR2(50) NOT NULL,
    mps_plus NUMBER(3,0),
    score_range NUMBER(3,0),
    active_flag VARCHAR2(1) DEFAULT 'N',
    histogram_flag VARCHAR2(1) DEFAULT 'Y',
    hist_bucket_score_num NUMBER(4,0),
    hist_noshow_score_num NUMBER(4,0))
  SEGMENT CREATION IMMEDIATE
  TABLESPACE  rpti_v1_table
  STORAGE   (
    INITIAL     131072
    NEXT        131072
    PCTINCREASE 0
    MINEXTENTS  1
    MAXEXTENTS  2147483645
  )
  NOCACHE
  MONITORING
  NOPARALLEL
  LOGGING
/


-- Constraints for table_3

ALTER TABLE table_3
ADD CONSTRAINT table_3_pk PRIMARY KEY (histogram_set_id)
USING INDEX
  TABLESPACE  rpti_v1_index
/


-- Triggers for table_3

CREATE OR REPLACE TRIGGER table_3_b_iu01
 BEFORE
  INSERT OR UPDATE
 ON table_3
REFERENCING NEW AS NEW OLD AS OLD
 FOR EACH ROW
begin
  IF INSERTING
  THEN
    IF :new.create_date IS NULL THEN
       :new.create_date := sysdate;
    END IF;
  ELSE
    :new.last_updated_date := sysdate;
  END IF;
end;
/


-- Comments for table_3

COMMENT ON COLUMN table_3.hist_bucket_score_num IS 'Number of scores in histogram bucket that is showing on report histogram chart, related to Hist_noshow_score_num '
/
COMMENT ON COLUMN table_3.hist_noshow_score_num IS 'Number of lower scores (0 ~ this number) that is not showing in report histogram chart, related to hist_bucket_score_num column'
/
COMMENT ON COLUMN table_3.mps_plus IS 'low_pass range'
/

-- End of DDL Script for Table RPTI_V1.table_3

-- Start of DDL Script for Table RPTI_V1.table_4
-- Generated 5/3/2023 3:02:20 PM from RPTI_V1@WBDV

CREATE TABLE table_4
    (report_status VARCHAR2(30) NOT NULL,
    report_status_description VARCHAR2(100))
  SEGMENT CREATION IMMEDIATE
  TABLESPACE  rpti_v1_table
  NOPARALLEL
  LOGGING
  MONITORING
/


-- Constraints for table_4

ALTER TABLE table_4
ADD CONSTRAINT table_4_pk PRIMARY KEY (report_status)
USING INDEX
  TABLESPACE  rpti_v1_index
/


-- End of DDL Script for Table RPTI_V1.table_4

-- Start of DDL Script for Table RPTI_V1.table_5
-- Generated 5/3/2023 3:02:54 PM from RPTI_V1@WBDV

CREATE TABLE table_5
    (report_config_id NUMBER NOT NULL,
    report_config_name VARCHAR2(100) NOT NULL,
    report_type VARCHAR2(25) NOT NULL,
    remarks VARCHAR2(1000),
    effective_start_date DATE NOT NULL,
    effective_end_date DATE,
    create_date DATE,
    created_by VARCHAR2(50),
    last_updated_date DATE,
    last_updated_by VARCHAR2(50),
    active_flag VARCHAR2(1) DEFAULT 'N' NOT NULL,
    exam VARCHAR2(8))
  SEGMENT CREATION IMMEDIATE
  TABLESPACE  rpti_v1_table
  NOPARALLEL
  LOGGING
  MONITORING
/


-- Indexes for table_5

CREATE INDEX table_5_idx01 ON table_5
  (
    report_type  ASC
  )
  TABLESPACE  rpti_v1_index
NOPARALLEL
LOGGING
/



-- Constraints for table_5

ALTER TABLE table_5
ADD CONSTRAINT table_5_ck01 CHECK (Active_flag in ('Y','N'))
/

ALTER TABLE table_5
ADD CONSTRAINT table_5_pk PRIMARY KEY (report_config_id)
USING INDEX
  TABLESPACE  rpti_v1_index
/

ALTER TABLE table_5
ADD CONSTRAINT table_5_uk UNIQUE (report_config_name)
USING INDEX
  TABLESPACE  rpti_v1_index
/


-- Triggers for table_5

CREATE OR REPLACE TRIGGER table_5_b_i01
 BEFORE
  INSERT
 ON table_5
REFERENCING NEW AS NEW OLD AS OLD
 FOR EACH ROW
BEGIN
    IF :NEW.report_config_id IS NULL THEN
       :NEW.report_config_id := seq_report_config_id.nextval;
    END IF;

    IF :NEW.create_date IS NULL THEN
       :NEW.create_date := SYSDATE;
    END IF;

    IF :NEW.created_by IS NULL THEN
       IF UPPER(user) = 'RPTI_V1' THEN
          :NEW.created_by := 'DBA';
       ELSE
          :NEW.created_by := user;
       END IF;
    END IF;
END;
/

CREATE OR REPLACE TRIGGER table_5_b_u01
 BEFORE
  UPDATE
 ON table_5
REFERENCING NEW AS NEW OLD AS OLD
 FOR EACH ROW
BEGIN
    IF :NEW.last_updated_date IS NULL THEN
       :NEW.last_updated_date := SYSDATE;
    END IF;

    IF :NEW.last_updated_by IS NULL THEN
       IF UPPER(user) = 'RPTI_V1' THEN
          :NEW.last_updated_by := 'DBA';
       ELSE
          :NEW.last_updated_by := user;
       END IF;
    END IF;
END;
/


-- End of DDL Script for Table RPTI_V1.table_5

-- Start of DDL Script for Table FBPI_V1.table_6
-- Generated 5/3/2023 3:03:08 PM from FBPI_V1@WBDV

CREATE TABLE table_6
    (report_type_id VARCHAR2(20) NOT NULL,
    description VARCHAR2(100),
    minimum_items NUMBER(3,0),
    minimum_examinees NUMBER(3,0))
  SEGMENT CREATION IMMEDIATE
  TABLESPACE  fbpi_v1_table
  NOPARALLEL
  LOGGING
  MONITORING
/


-- Constraints for table_6

ALTER TABLE table_6
ADD CONSTRAINT table_6_pk PRIMARY KEY (report_type_id)
USING INDEX
  TABLESPACE  fbpi_v1_index
/


-- End of DDL Script for Table FBPI_V1.table_6

-- Start of DDL Script for Table RPTI_V1.table_7
-- Generated 09-Jun-2023 11:25:49 from RPTI_V1@WBDV

CREATE TABLE table_7
    (profile_category_id NUMBER NOT NULL,
    profile_examinee_id NUMBER NOT NULL,
    customer_sort_order NUMBER(9,0),
    category_name VARCHAR2(100) NOT NULL,
    category_description VARCHAR2(100) NOT NULL,
    scaled_score NUMBER,
    raw_score NUMBER,
    percent_correct_score NUMBER,
    sem NUMBER,
    report_sort_order NUMBER(9,0),
    physical_bar_left_position NUMBER,
    physical_bar_right_position NUMBER,
    left_overflow_indicator VARCHAR2(1),
    right_overflow_indicator VARCHAR2(1),
    sem_bar_width_factor NUMBER,
    header_description VARCHAR2(100),
    bar_left_position NUMBER,
    bar_right_position NUMBER,
    physical_score NUMBER,
    score_category_id VARCHAR2(3),
    std_deviation NUMBER,
    sd_bar_width_factor NUMBER,
    plot_low NUMBER,
    plot_high NUMBER,
    plot_median NUMBER,
    plot_q1 NUMBER,
    plot_q3 NUMBER,
    category_img_path VARCHAR2(200),
    group_img_path VARCHAR2(200),
    physical_plot_q1_position NUMBER,
    physical_plot_q3_position NUMBER,
    physical_plot_low_position NUMBER,
    physical_plot_high_position NUMBER,
    physical_plot_median_position NUMBER,
    test_score1 NUMBER,
    item_percent_min NUMBER,
    item_percent_max NUMBER,
    score_type VARCHAR2(10),
    test_score2 NUMBER,
    comparison_mean NUMBER)
  SEGMENT CREATION IMMEDIATE
  TABLESPACE  rpti_v1_table
  STORAGE   (
    INITIAL     131072
    NEXT        131072
    PCTINCREASE 0
    MINEXTENTS  1
    MAXEXTENTS  2147483645
  )
  NOCACHE
  MONITORING
  NOPARALLEL
  LOGGING
/


-- Indexes for table_7

CREATE INDEX table_7_dx01 ON table_7
  (
    profile_examinee_id  ASC
  )
  TABLESPACE  rpti_v1_index
  STORAGE   (
    INITIAL     131072
    NEXT        131072
    PCTINCREASE 0
    MINEXTENTS  1
    MAXEXTENTS  2147483645
  )
NOPARALLEL
LOGGING
/

CREATE INDEX table_7_dx02 ON table_7
  (
    score_category_id  ASC
  )
  TABLESPACE  rpti_v1_index
  STORAGE   (
    INITIAL     131072
    NEXT        131072
    PCTINCREASE 0
    MINEXTENTS  1
    MAXEXTENTS  2147483645
  )
NOPARALLEL
LOGGING
/



-- Constraints for table_7



ALTER TABLE table_7
ADD CONSTRAINT table_7_pk PRIMARY KEY (profile_category_id)
USING INDEX
  TABLESPACE  rpti_v1_index
  STORAGE   (
    INITIAL     131072
    NEXT        131072
    PCTINCREASE 0
    MINEXTENTS  1
    MAXEXTENTS  2147483645
  )
/


-- Triggers for table_7

CREATE OR REPLACE TRIGGER table_7_b_i01
 BEFORE
  INSERT
 ON table_7
REFERENCING NEW AS NEW OLD AS OLD
 FOR EACH ROW
BEGIN
  if :new.profile_category_id is null then
     select rpti_seq_profile_category_id.nextval
       into :new.profile_category_id
       from dual;
  end if;
END;
/


-- Comments for table_7

COMMENT ON COLUMN table_7.score_type IS 'Comma separated string'
/

-- End of DDL Script for Table RPTI_V1.table_7

-- Foreign Key
ALTER TABLE table_7
ADD CONSTRAINT table_7_fk02 FOREIGN KEY (score_category_id)
REFERENCES table_8 (score_category_id)
/
ALTER TABLE table_7
ADD CONSTRAINT table_7_fk01 FOREIGN KEY (profile_examinee_id)
REFERENCES table_1 (profile_examinee_id)
/
-- End of DDL script for Foreign Key(s)

-- Start of DDL Script for Table MSSI_V1.table_8
-- Generated 09-Jun-2023 11:32:23 from MSSI_V1@WBDV

CREATE TABLE table_8
    (score_category_id VARCHAR2(3) NOT NULL,
    short_title VARCHAR2(40) NOT NULL,
    long_title VARCHAR2(70) NOT NULL,
    report_short_name VARCHAR2(7))
  SEGMENT CREATION IMMEDIATE
  TABLESPACE  mssi_v1_table
  STORAGE   (
    INITIAL     131072
    NEXT        131072
    PCTINCREASE 0
    MINEXTENTS  1
    MAXEXTENTS  2147483645
  )
  NOCACHE
  MONITORING
  NOPARALLEL
  LOGGING
/


-- Constraints for table_8

ALTER TABLE table_8
ADD CONSTRAINT table_8_pk PRIMARY KEY (score_category_id)
USING INDEX
  TABLESPACE  mssi_v1_index
  STORAGE   (
    INITIAL     131072
    NEXT        131072
    PCTINCREASE 0
    MINEXTENTS  1
    MAXEXTENTS  2147483645
  )
/


-- End of DDL Script for Table MSSI_V1.table_8

-- Start of DDL Script for Table RPTI_V1.table_9
-- Generated 09-Jun-2023 11:36:16 from RPTI_V1@WBDV

CREATE TABLE table_9
    (item_detail_id NUMBER NOT NULL,
    item_subset_detail_id NUMBER NOT NULL,
    report_id NUMBER NOT NULL,
    item_stem_object_id VARCHAR2(20) NOT NULL,
    medley_id VARCHAR2(20) NOT NULL,
    reference_name VARCHAR2(40),
    item_description VARCHAR2(200),
    step1_outline VARCHAR2(200),
    organ_system VARCHAR2(200),
    vignette VARCHAR2(200),
    local_difficulty NUMBER,
    step1_difficulty NUMBER,
    difficulty_difference NUMBER,
    local_corrected_biserial NUMBER,
    sort_order NUMBER(9,0),
    national_difficulty NUMBER,
    exam_instance VARCHAR2(11),
    stat_source VARCHAR2(40),
    keyword VARCHAR2(400),
    description VARCHAR2(400),
    test_score NUMBER(1,0),
    item_phrase1 VARCHAR2(200),
    item_phrase2 VARCHAR2(200),
    item_phrase3 VARCHAR2(200),
    item_id NUMBER(5,0),
    item_score_categories VARCHAR2(500))
  SEGMENT CREATION IMMEDIATE
  TABLESPACE  rpti_v1_table
  STORAGE   (
    INITIAL     131072
    NEXT        131072
    PCTINCREASE 0
    MINEXTENTS  1
    MAXEXTENTS  2147483645
  )
  NOCACHE
  MONITORING
  NOPARALLEL
  LOGGING
/


-- Indexes for table_9

CREATE INDEX table_9_dx01 ON table_9
  (
    item_subset_detail_id  ASC
  )
  TABLESPACE  rpti_v1_index
  STORAGE   (
    INITIAL     131072
    NEXT        131072
    PCTINCREASE 0
    MINEXTENTS  1
    MAXEXTENTS  2147483645
  )
NOPARALLEL
LOGGING
/

CREATE INDEX table_9_dx02 ON table_9
  (
    item_stem_object_id  ASC,
    medley_id  ASC
  )
  TABLESPACE  rpti_v1_index
  STORAGE   (
    INITIAL     131072
    NEXT        131072
    PCTINCREASE 0
    MINEXTENTS  1
    MAXEXTENTS  2147483645
  )
NOPARALLEL
LOGGING
/

CREATE INDEX table_9_dx03 ON table_9
  (
    report_id  ASC
  )
  TABLESPACE  rpti_v1_index
  STORAGE   (
    INITIAL     131072
    NEXT        131072
    PCTINCREASE 0
    MINEXTENTS  1
    MAXEXTENTS  2147483645
  )
NOPARALLEL
LOGGING
/

CREATE INDEX table_9_dx04 ON table_9
  (
    exam_instance  ASC
  )
  TABLESPACE  rpti_v1_index
  STORAGE   (
    INITIAL     131072
    NEXT        131072
    PCTINCREASE 0
    MINEXTENTS  1
    MAXEXTENTS  2147483645
  )
NOPARALLEL
LOGGING
/



-- Constraints for table_9

ALTER TABLE table_9
ADD CONSTRAINT table_9_pk PRIMARY KEY (item_detail_id)
USING INDEX
  TABLESPACE  rpti_v1_index
  STORAGE   (
    INITIAL     131072
    NEXT        131072
    PCTINCREASE 0
    MINEXTENTS  1
    MAXEXTENTS  2147483645
  )
/



-- Triggers for table_9

CREATE OR REPLACE TRIGGER table_9_b_i01
 BEFORE
  INSERT
 ON table_9
REFERENCING NEW AS NEW OLD AS OLD
 FOR EACH ROW
BEGIN
  if :new.item_detail_id is null then
     select rpti_seq_item_detail_id.nextval
       into :new.item_detail_id
       from dual;
  end if;
END;
/


-- End of DDL Script for Table RPTI_V1.table_9

-- Foreign Key
ALTER TABLE table_9
ADD CONSTRAINT table_9_fk01 FOREIGN KEY (item_subset_detail_id)
REFERENCES table_10 (item_subset_detail_id)
/
-- End of DDL script for Foreign Key(s)

-- Start of DDL Script for Table RPTI_V1.table_10
-- Generated 09-Jun-2023 11:36:52 from RPTI_V1@WBDV

CREATE TABLE table_10
    (item_subset_detail_id NUMBER NOT NULL,
    report_id NUMBER NOT NULL,
    subset_description VARCHAR2(100) NOT NULL,
    item_count NUMBER(9,0),
    avg_local_difficulty NUMBER,
    avg_step1_difficulty NUMBER,
    avg_difficulty_difference NUMBER,
    avg_local_corrected_biserial NUMBER,
    sort_order NUMBER(9,0),
    avg_national_difficulty NUMBER,
    score_category_id VARCHAR2(3),
    header_description VARCHAR2(100),
    test_proportion VARCHAR2(10),
    profile_examinee_id NUMBER)
  SEGMENT CREATION IMMEDIATE
  TABLESPACE  rpti_v1_table
  STORAGE   (
    INITIAL     131072
    NEXT        131072
    PCTINCREASE 0
    MINEXTENTS  1
    MAXEXTENTS  2147483645
  )
  NOCACHE
  MONITORING
  NOPARALLEL
  LOGGING
/


-- Indexes for table_10

CREATE INDEX table_10_dx01 ON table_10
  (
    report_id  ASC
  )
  TABLESPACE  rpti_v1_index
  STORAGE   (
    INITIAL     131072
    NEXT        131072
    PCTINCREASE 0
    MINEXTENTS  1
    MAXEXTENTS  2147483645
  )
NOPARALLEL
LOGGING
/

CREATE INDEX table_10_dx02 ON table_10
  (
    profile_examinee_id  ASC
  )
  TABLESPACE  rpti_v1_index
  STORAGE   (
    INITIAL     131072
    NEXT        131072
    PCTINCREASE 0
    MINEXTENTS  1
    MAXEXTENTS  2147483645
  )
NOPARALLEL
LOGGING
/



-- Constraints for table_10

ALTER TABLE table_10
ADD CONSTRAINT table_10_pk PRIMARY KEY (item_subset_detail_id)
USING INDEX
  TABLESPACE  rpti_v1_index
  STORAGE   (
    INITIAL     131072
    NEXT        131072
    PCTINCREASE 0
    MINEXTENTS  1
    MAXEXTENTS  2147483645
  )
/



-- Triggers for table_10

CREATE OR REPLACE TRIGGER table_10_b_i01
 BEFORE
  INSERT
 ON table_10
REFERENCING NEW AS NEW OLD AS OLD
 FOR EACH ROW
BEGIN
  if :new.item_subset_detail_id is null then
     select rpti_seq_item_subset_detail_id.nextval
       into :new.item_subset_detail_id
       from dual;
  end if;
END;
/


-- End of DDL Script for Table RPTI_V1.table_10

-- Foreign Key
ALTER TABLE table_10
ADD CONSTRAINT table_10_fk02 FOREIGN KEY (profile_examinee_id)
REFERENCES table_1 (profile_examinee_id)
/
-- End of DDL script for Foreign Key(s)
