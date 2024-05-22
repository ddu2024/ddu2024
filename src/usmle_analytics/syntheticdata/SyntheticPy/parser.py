
from sql_parser import SqlParser


class Parser:
    def __init__(self) -> None:
        pass

    def parse(self,input_file: str):       
        extnsn = input_file.split(".")[-1]        
        if extnsn.lower() == 'sql':
            content = open(input_file, 'r').read()           
            sqlParser: SqlParser = SqlParser()
            return sqlParser.parseSqlStatements(content)
        else:
            pass
    
    def get_field_type(schmea_map: tuple):
        for column_name, data_type, length, comment in schmea_map:
            pass

            

    



            


            
        