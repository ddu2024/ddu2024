package org.nbme.dwbi.synthetic.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.nbme.dwbi.synthetic.model.Container;
import org.nbme.dwbi.synthetic.model.ContainerType;
import org.nbme.dwbi.synthetic.model.Field;
import org.nbme.dwbi.synthetic.model.Metadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CSVParser {
	private Metadata md;
	private ArrayList<String> header = new ArrayList<String>();
	private ArrayList<ArrayList<String>> array = new ArrayList<ArrayList<String>>();
	private ColumnAnalysis colAnal = new ColumnAnalysis();
	
	private static final String UTF8_BOM = "\uFEFF";
	private static final Logger logger = LoggerFactory.getLogger(CSVParser.class);
	  
	public CSVParser() {
		this(new Metadata());
	}

	public CSVParser(Metadata md) {
		super();
		this.md = md;
	}

	public void parseCSV(File file) throws Exception {
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
		
        boolean hasBOM = detectBOM(file);
		
		int lineCount = 0;
        try (Reader reader = new InputStreamReader(new FileInputStream(file), "UTF8")) { //must use UTF8 or BOM remover fails
        	if(hasBOM) {
            	logger.info(name + " has BOM");
        		char[] cbuf = new char[1];
        		reader.read(cbuf);
        	}
    		Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(reader);
    		Iterator<CSVRecord> itr = records.iterator();
            while (itr.hasNext()) {
            	CSVRecord record = itr.next();
                lineCount++;
                if (lineCount == 1) {
                	for(int i = 0; i < record.size(); i++) {
	                   header.add(record.get(i));
	                   array.add(new ArrayList<String>());
                	}
                } else {
                	for(int i = 0; i < record.size(); i++) {
	                   array.get(i).add(record.get(i));
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
	
	public static boolean detectBOM(File file) {
		try (FileInputStream fis = new FileInputStream(file); BufferedReader r = new BufferedReader(new InputStreamReader(fis, "UTF8"));) {
			String line = r.readLine();
			if (line != null && line.startsWith(UTF8_BOM)) {
				return true;
			}
		} catch (Exception e) {
//			LOGGER.error("Error while parsing CSV (detectBOM): {}", e.getMessage());
		}
		return false;
	}
	

}
