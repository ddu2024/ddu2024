
from faker import Faker
from faker.providers import python
import csv
import uuid
import random
import time
import json

class Generator:

    def __init__(self):
        self.fake = Faker()
        self.fake.add_provider(python)
        Faker.seed()

    def generate_synthetic_rows(self,  output_file:str, table_name: str, schema_map, synthetic_rows: int, output_format:str="csv",) -> None:
        synthetic_records = []
        i = 0
        for _ in range(synthetic_rows):
            i = i + 1
            record = self.generate(schema_map, i)                         
            synthetic_records.append(record)

        if "csv" == output_format.lower():
            self.write_as_csv(output_file, synthetic_records, schema_map)
        elif "json" == output_format.lower():
            self.write_as_json(output_file, synthetic_records)

    def write_as_csv(self, output_file, synthetic_records, schmea_map):     
        with open(output_file, "w") as csvfile:
            csv_writer = csv.DictWriter(csvfile, fieldnames=[col_name for col_name, _, _, _ in schmea_map])
            csv_writer.writeheader()
            for record in synthetic_records:
                csv_writer.writerow(record)


    def write_as_json(self, filename, records, chunk_size=10000):
        with open(filename, "w") as f:
            for i in range(0, len(records), chunk_size):
                chunk = records[i:i + chunk_size]
                for record in chunk:
                    f.write(json.dumps(record))
                    f.write("\n")


    def generate(self, schema_map, incid:int):
        string_types = ["VARCHAR", "VARCHAR2", "CHAR", "STRING"]
        numeric_dtypes = ["NUMERIC", "INT", "INTEGER", "DECIMAL"]
        boolean_dtypes = ["BOOL", "BOOLEAN", "TINYINT"]
        date_dtypes = ["DATE", "DATE2"]
        timestamp_dtypes = ["TIMESTAMP"]

        gender = self.generate_gender()
        deceased, deceased_year, deceased_month, deceased_day = self.generate_deceased()
        ssn = self.generate_ssn()
        state=self.generate_state()     
        ssn4 = ssn[-4:]
        exam = self.generate_exam()
        record = {}
        for column_name, data_type, field_length, comment in schema_map:            
            field_type = comment.strip() if "NA" != comment else data_type.strip()
            print(column_name, data_type, field_type, field_length, comment)
            print("------------------------")
            synthetic_value = None 
   
    
            if "INCID" in field_type :
                start_idx = int(field_type.split("_")[1])
                synthetic_value = start_idx+incid

            elif field_type == "ADMIN":
                synthetic_value = self.generate_admin(field_length)
            elif field_type == "EXAM":
                synthetic_value = exam
            elif field_type == "EXAM_TYPE":
                synthetic_value = self.generate_exam_type()
            elif field_type == "EXAM_DESC":
                synthetic_value = self.generate_exam_desc(field_length,exam)
            elif field_type == "STATE_CODE":
                synthetic_value = self.generate_state_code(field_length, state)
            elif field_type == "SCHOOL_NAME":
                synthetic_value = self.generate_school_name(field_length, state)
            elif field_type == "SCHOOL_SHORT":
                synthetic_value = self.generate_school_short(field_length)
            elif field_type == "FOREIGN_NAME":
                synthetic_value = self.generate_foreign_name(field_length)    
            elif field_type == "NAME":
                synthetic_value = self.generate_name(field_length)
            elif field_type == "OPTIONAL_NAME":
                synthetic_value = random.choice([self.generate_first_name(field_length,gender), None])
            elif field_type == "FIRST_NAME":
                synthetic_value = self.generate_first_name(field_length,gender)
            elif field_type == "LAST_NAME":
                synthetic_value = self.generate_last_name(field_length)
            elif field_type == "GENDER":
                synthetic_value = gender
            elif field_type == "GENSUFFIX":
                synthetic_value = self.generate_generation_suffix()
            elif field_type == "YYYY":
                synthetic_value = self.generate_year_YYYY()
            elif field_type == "DD":
                synthetic_value = self.generate_day_DD()
            elif field_type == "MM":
                synthetic_value = self.generate_month_MM()
            elif field_type == "EMAIL":
                synthetic_value = self.generate_email(field_length)
            elif field_type == "URL":
                synthetic_value = self.generate_url(field_length)
            elif field_type == "ID":
                synthetic_value = self.generate_id(field_length)
            elif field_type == "SSN":
                synthetic_value = ssn
            elif field_type == "SSN4":
                synthetic_value = ssn4
            elif field_type == "NUMERICID":
                synthetic_value = self.generate_numeric_id(field_length)
            elif field_type == "IS_DECEASED":
                synthetic_value = deceased
            elif field_type == "DECEASED_YYYY":
                synthetic_value = deceased_year
            elif field_type == "DECEASED_MM":
                synthetic_value = deceased_month
            elif field_type == "DECEASED_DD":
                synthetic_value = deceased_day
            elif field_type == "EXAMNAME":
                synthetic_value = self.generate_examname(field_length)
            elif field_type == "UNIVERSITY":
                synthetic_value = self.generate_university(state, field_length)
            elif field_type == "ADDRESS":
                synthetic_value = self.generate_address(field_length, type="FL", optional=False)
            elif field_type == "STREET_ADDRESS":
                synthetic_value = self.generate_address(field_length, type="ST", optional=True)
            elif field_type == "SECONDRY_ADDRESS":
                synthetic_value = self.generate_address(field_length, type="S2", optional=True)
            elif field_type == "BUILDING_ADDRESS":
                synthetic_value = self.generate_address(field_length, type="BL" , optional=True)
            elif field_type == "OPTIONAL_ADDRESS":
                synthetic_value = self.generate_address(field_length, type="FL",  optional=True)
            elif field_type == "CITY":
                synthetic_value=self.generate_city()
            elif field_type == "STATE":
                synthetic_value=state
            elif field_type == "ZIPCODE":
                synthetic_value=self.generate_zipcode()
            elif field_type == "PHONE":
                synthetic_value=self.generate_phone()
            elif field_type == "FILE_LOC":
                synthetic_value = self.generate_file_loc(field_length)
            elif field_type == "ETHNICS":
                synthetic_value = self.generate_ethnics()
            elif "DECIMAL" in field_type:
                field_values = field_type.split("_")[1]
                synthetic_value=self.generate_decimal(field_values)
            elif "CHOICE" in field_type:
                field_values = field_type.split("_")[1]
                synthetic_value = self.generate_custom_choice(field_values)
            elif field_type in string_types:
                field_length = 5 if field_length < 5 else field_length
                synthetic_value = self.fake.text(max_nb_chars=field_length)[:field_length]
            elif field_type in numeric_dtypes:            
                synthetic_value = self.fake.random_number(digits=field_length)
            elif field_type in boolean_dtypes:
                synthetic_value = str(self.fake.boolean())
            elif field_type in date_dtypes:
                synthetic_value = str(self.fake.date_between(start_date='-40y', end_date='-20y'))
            elif field_type in timestamp_dtypes:
                synthetic_value = str(self.fake.date_time_between(start_date='-30y', end_date='now'))
            else:
                synthetic_value = None
            
            record[column_name]=synthetic_value
        return record

    def generate_deceased(self):
        deceased = self.generate_custom_choice("Y,N")
        deceased_year = None
        deceased_month = None
        deceased_day = None
        if deceased == "Y":
            deceased_year = self.generate_year_YYYY()
            deceased_month = self.generate_month_MM()
            deceased_day = self.generate_day_DD()
        return deceased, deceased_year, deceased_month, deceased_day

    def generate_decimal(self, field_values):
        v = field_values.split(",")
        return str(round(random.uniform(1, 10*int(v[0])), int(v[1])))

    def generate_id(self, field_length):
        return str(uuid.uuid4()).replace("-","")[:field_length]
    
    def generate_numeric_id(self, field_length):
        return f"{random.randint(1,10*field_length)}"
    
    def generate_file_loc(self,field_length):
        dummmy_path = self.fake.file_path()
        while True:
            if len(dummmy_path) <= field_length:
                return dummmy_path
    
    def generate_name(self, field_length,gender):
        return self.fake.name_female()[:field_length] if gender == "F" else self.fake.name_male()[:field_length]

    def generate_first_name(self, field_length,gender):
        return self.fake.first_name_female()[:field_length] if gender == "F" else self.fake.first_name_male()[:field_length]

    def generate_city(self):
        return self.fake.city()
    
    def generate_zipcode(self):
        return self.fake.zipcode()

    def generate_state(self):
        return self.fake.state()
    
    def generate_phone(self):
        return self.fake.phone_number()
    
    def generate_address(self, field_length, type="FL", optional=False):
        fake_address = self.fake.address()[:field_length] 
        if type == "ST":
            fake_address = self.fake.street_address()[:field_length]
        elif type == "S2":
            fake_address = self.fake.secondary_address()[:field_length]
        elif type == "BL":
            fake_address = self.fake.building_number()[:field_length]
        if optional :
            return random.choice([fake_address, None])
        else:
            return fake_address

    def generate_last_name(self, field_length):
        return self.fake.last_name()[:field_length]

    def generate_gender(self):
        return random.choice(["F", "M"])
    
    def generate_admin(self, field_length):
        int_part = str(random.randint(1000, 9999))
        exam_part = random.choice(["STEP 1", "STEP 2","STEP 3","PART 1","PART 2","PART 3"])
        admin = f"{exam_part} - {int_part}"
        return admin
    
    def generate_exam(self):        
        return random.choice(["STEP 1", "STEP 2","STEP 3","PART 1","PART 2","PART 3"])
    
    def generate_exam_type(self):
        return random.choice(["A", "B" , "C", "D"])
    
    def generate_month_MMM(self, field_length=3):
        months = ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"]
        return random.choice(months)[:field_length]
    
    def generate_exam_desc(self, exam, field_length):
        # june 1994 USMLE Step 1 Examination
        month = self.generate_month_MMM().lower()
        year = self.generate_year_YYYY()        
        desc = f"{month} {year} USMLE {exam} Examination"
        return desc
    
    def generate_state_code(self, field_length, state):
        return state[:2]
    
    def generate_school_name(self, field_length, state):
        type = random.choice(["School of Medicine","College of Osteopathic Med", "College of Medicine" , "Demo Domestic"])
        return f"{state} {type}"
    
    def generate_ethnics(self):
        return random.choice(["AA", "AS", "CA", "HI", "NA", "PA", "ME", "EU", "LA"])
    
    def generate_school_short(self , state):
        return f"{state} School"

    def generate_foreign_name(self,field_length):
        return random.choice(["INSTITUTE OF MEDICAL", None])
 
    def generate_generation_suffix(self):
        return random.choice(["JR.", "SR.", "II", "III", "VII"])
    
    def generate_examname(self, field_length):
        dummy_vals = ["ABC", "LMN", "XYZ"]
        return random.choice(dummy_vals)
    
    def generate_university(self, state, field_length):
        dummy_univ_name = f"{state} State University"
        return random.choice(dummy_univ_name)

    def generate_year_YYYY(self):
        return str(random.randint(1900, 2020))

    def generate_month_MM(self):
        return str(random.randint(1, 12)).zfill(2)

    def generate_day_DD(self):
        return str(random.randint(1, 28)).zfill(2)
    
    def generate_ssn(self, field_length=9):
        return str(random.randint(100000000, 999999999))

    def generate_url(self, field_length):
        return self.fake.url()[:field_length]
    
    def generate_email(self, field_length):
        return self.fake.email()[:field_length]

    def generate_custom_choice(self, field_values):
        return random.choice([f.strip() for f in field_values.split(",")])



    