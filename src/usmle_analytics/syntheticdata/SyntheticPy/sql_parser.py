
import re
from datetime import datetime 
from simple_ddl_parser import DDLParser


class SqlParser:
    def __init__(self):
        pass

    def clean_up_ddl(self, ddl_script:str) -> str:
        return  re.sub(r'\s+default\s+[^,]+,', ',', ddl_script)

    def get_column_info(self, ddl_script:str) -> None:
        ddl_script = self.clean_up_ddl(ddl_script)
        parsed_ddl = DDLParser(ddl_script).run()
        parsed_columns = parsed_ddl[0]["columns"]
        column_info=[]
        for c in parsed_columns:
            column_info.append((c['name'], c['type'], c['size']))
        return column_info

    def add_column_comments(self, column_info, ddl_script:str) -> None:
        pattern = r'(\w+)\s+(\s*default\s+\'?(\w+)\'?)?.*?--\|([,\w]+)'
        # pattern = r'(\w+)\s+(\w+\s+\w+\s+\w+)?\s+([A-Z]+\([\d,]+\))?\s*--\|(.+?)\n'

        column_matches = re.findall(pattern, ddl_script, re.MULTILINE | re.IGNORECASE)
        column_info_with_comment = []
        col_comments = {}
        for match in column_matches:
            column_name,_,_,comment=match
            col_comments[column_name]=comment

        for col in column_info:
            column_name, data_type, field_length = col
            comment = col_comments[column_name] if column_name in col_comments else "NA"
            column_info_with_comment.append((column_name, data_type, field_length, comment))

        return column_info_with_comment

    def parseSqlStatements(self, ddl_script:str) -> None:
        
        table_name_pattern = re.compile(r'^create\s+table\s+(\w+\.\w+)', re.MULTILINE | re.IGNORECASE)
        
        table_name_match = table_name_pattern.findall(ddl_script)
        table_name = ""
        if table_name_match:
            table_name = table_name_match[0]        
        print(f"Columns and Data Types for table {table_name}:")
        print("--------------------------------------------")
        column_info=self.get_column_info( ddl_script) 
        column_info=self.add_column_comments(column_info, ddl_script)       
        # print(column_info)
        # exit()
        table_name = "CBIS_" + table_name.split(".")[1] + "_" + datetime.strftime(datetime.today(),"%Y-%m-%d %H:%M:%S.000")
        return table_name, column_info



