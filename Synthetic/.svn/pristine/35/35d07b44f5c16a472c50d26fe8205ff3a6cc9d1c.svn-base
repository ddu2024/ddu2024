package org.nbme.dwbi.synthetic.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.nbme.dwbi.synthetic.model.Container;
import org.nbme.dwbi.synthetic.model.ContainerType;
import org.nbme.dwbi.synthetic.model.Field;
import org.nbme.dwbi.synthetic.model.Metadata;

public class XLSXParser {
	private Metadata md;
	private ArrayList<String> header = new ArrayList<String>();
	private ArrayList<ArrayList<String>> array = new ArrayList<ArrayList<String>>();
	private ColumnAnalysis colAnal = new ColumnAnalysis();
	  
	public XLSXParser() {
		this(new Metadata());
	}

	public XLSXParser(Metadata md) {
		super();
		this.md = md;
	}
	
	public void parseXLSX(File file) throws Exception {
		String name = file.getName().replaceAll("^(.*?)\\..*","$1");

		Container container = null;
		boolean foundExistingContainer = false;
		
		for(Container containerX : md.getObjects()) {
			if(containerX.getContainerName().equalsIgnoreCase(name)) {
				container = containerX;
				foundExistingContainer = true;
				break;
			}
		}
		if(container == null) {
			container = new Container();
			container.setContainerName(name);
			container.setFields(new ArrayList<Field>());
			container.setContainerType(ContainerType.CSV);
			md.getObjects().add(container);
		}
		
		DataFormatter dataFormatter = new DataFormatter();
		try (InputStream inp = new FileInputStream(file); Workbook wb = WorkbookFactory.create(inp)) {
			Sheet sheet = wb.getSheetAt(0);

			for (int i = 0; i < sheet.getLastRowNum(); i++) {
				Row row = sheet.getRow(i);
				
                if (i == 0) {
                	for(int j = 0; j < row.getLastCellNum(); j++) {
	                   header.add(dataFormatter.formatCellValue(row.getCell(j)));
	                   array.add(new ArrayList<String>());
                	}
                } else {
                	for(int j = 0; j < row.getLastCellNum(); j++) {
	                   array.get(j).add(dataFormatter.formatCellValue(row.getCell(j)));
                	}
                }
			}
		}

        if(header.size() != array.size()) {
        	throw new Exception("Header size does not match column count from data");
        }
        
		for (int i = 0; i < array.size(); i++) {
			Field field = null;
			if (foundExistingContainer) {
				for (Field fieldX : container.getFields()) {
					if (fieldX.getName().equalsIgnoreCase(header.get(i))) {
						field = fieldX;
						break;
					}
				}
				if (field == null) {
					throw new Exception("Field '" + header.get(i) + "' not found in the existing container '" + container.getContainerName() + "' (probaby from DDL parsing)");
				}
			} else {
				field = new Field();
				field.setName(header.get(i));
				container.getFields().add(field);
			}
			colAnal.analyzeColumn(field, i, array.get(i));
		}
	}
}
