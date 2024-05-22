package org.nbme.dwbi.synthetic.parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.nbme.dwbi.synthetic.constants.Defaults;
import org.nbme.dwbi.synthetic.model.Container;
import org.nbme.dwbi.synthetic.model.Field;
import org.nbme.dwbi.synthetic.model.Metadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

public class Parser {
	private File input;
	private File outputDir;
	private static final Logger logger = LoggerFactory.getLogger(Parser.class);


	public Parser(String inputIn, String outputDirIn) throws Exception {
		super();
		this.input = new File(inputIn);
		this.outputDir = new File(outputDirIn);
		
		if(!input.exists() ) {
			throw new FileNotFoundException("Input file does not exist: " + inputIn);
		}
		if(!this.outputDir.exists()) {
			this.outputDir.mkdir();
		}
	}
	
	public void parse() throws Exception {
		ArrayList<File> files = new ArrayList<File>();
		if(input.isDirectory()) {
			//process DDL (.sql) first
			ArrayList<File> fileList = new ArrayList<File>(Arrays.asList(input.listFiles()));
			Iterator<File> itr = fileList.iterator();
			while(itr.hasNext()) {
				File file = itr.next();
				String extension = file.getName().replaceAll(".*?\\.([^\\.]+?)", "$1").toLowerCase();
				if("sql".equals(extension)) {
					files.add(file);
					itr.remove();
				} else if (!extension.matches(Defaults.ALLOWED_PARSE_EXT)) {
					itr.remove();
				}
			}
			files.addAll(fileList);			
		} else {
			String extension = input.getName().replaceAll(".*?\\.([^\\.]+?)", "$1").toLowerCase();
			if (extension.matches(Defaults.ALLOWED_PARSE_EXT)) {
				files.add(input);
			}
		}
		
		if(files.size() == 0) {
			logger.info("No valid files found to parse. Extension must match regex: " + Defaults.ALLOWED_PARSE_EXT);
			return;
		}
		
		Metadata md = new Metadata();
		for(File file : files) {
			String extension = file.getName().replaceAll(".*?\\.([^\\.]+?)", "$1").toLowerCase();
			logger.info(">>>>> Parsing " + file.getName());
	
			switch(extension) {
				case "sql":
					String content = FileUtils.readFileToString(file);
					SQLParser sqlParser = new SQLParser(md);
					sqlParser.parseSqlStatements(content);
					break;
				case "csv":
					CSVParser csvParser = new CSVParser(md);
					csvParser.parseCSV(file);
					break;
				case "xlsx":
					XLSXParser xlsxParser = new XLSXParser(md);
					xlsxParser.parseXLSX(file);
					break;
				case "json":
					JsonParser jsonParser = new JsonParser(md);
					jsonParser.parseJson(file);
					break;
				case "xml":
					//TODO -- if ever requested
					break;
			}
			logger.info(">>>>> Finished Parsing " + file.getName() + System.lineSeparator());
		}

        cleanMD(md);
        Yaml yaml = YmlParser.getYamlWithOptions();
		String res = yaml.dump(md);
		res = res.replaceAll("(?m)^( *- [^ \\d])", "\r\n$1");
		
		File outfile = new File(outputDir, input.getName() + ".metadata.yml");
		logger.info("Writing parse results to: " + outfile.getAbsolutePath());
		FileWriter writer = new FileWriter(outfile);
		writer.write(res);
		writer.close();
		
	}
	
	public void cleanMD(Metadata md) {
		for(Container container : md.getObjects()){
			for(Field field : container.getFields()) {
				cleanField(field);
			}
		}
	}
	
	private void cleanField(Field field) {
		if(StringUtils.isNotBlank(field.getReference())) {
			field.clearOptions();
		}
		for(Field subField : field.getSubFields()) {
			cleanField(subField);
		}
	}
}
