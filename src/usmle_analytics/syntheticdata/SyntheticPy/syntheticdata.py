import os
from parser import Parser
from generator import Generator
import argparse

'''
To Run
python3 syntheticdata.py -i CANDIDATE_EXAM_REQUEST.sql -f json -r 1000
python3 syntheticdata.py -i MEDICAL_SCHOOL.sql -f json -r 1000
python3 syntheticdata.py -i USMLE_EXAM_ADMIN.sql -f json -r 1000
python3 syntheticdata.py -i USMLE_EXAMINATION.sql -f json -r 1000
python3 syntheticdata.py -i CANDIDATE_CURRENT_BIOG.sql -f json -r 1000
python3 syntheticdata.py -i CANDIDATE_EXAM_OUTCOME.sql -f json -r 1000

'''

def main(args):
    file_path = os.path.dirname(os.path.abspath(__file__))

    output_format = str(args.output_format).lower()
    input_file_name = args.input_file_name
    synthetic_rows = args.synthetic_rows

    p = Parser()
    input_file = f"{file_path}/input/{input_file_name}"
    table_name, schema_map = p.parse(input_file)

    g = Generator()
    output_file = f"{file_path}/output/{table_name}_{synthetic_rows}.{output_format}"
    g.generate_synthetic_rows(output_file, table_name, schema_map, synthetic_rows=synthetic_rows, output_format=output_format)

def argument_parser():
    parser = argparse.ArgumentParser(description="Inputs for synthetic data generator")
    parser.add_argument('--input_file_name', '-i', type=str, help="Input sql file name")
    parser.add_argument('--output_format', '-f', type=str, choices=["csv", "json"], help="Output file format")
    parser.add_argument('--synthetic_rows', '-r', type=int, help="Sample synthetic rows to generate")
    return parser.parse_args()

if __name__ == "__main__":
    args = argument_parser()
    main(args)