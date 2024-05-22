package org.nbme.dwbi.synthetic.engine;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.nbme.dwbi.synthetic.constants.Defaults;
import org.nbme.dwbi.synthetic.generator.Generator;
import org.nbme.dwbi.synthetic.generator.GeneratorFactory;
import org.nbme.dwbi.synthetic.generator.SubfieldGenerator;
import org.nbme.dwbi.synthetic.model.Container;
import org.nbme.dwbi.synthetic.model.ContainerType;
import org.nbme.dwbi.synthetic.model.Field;
import org.nbme.dwbi.synthetic.model.Metadata;
import org.nbme.dwbi.synthetic.parser.YmlParser;
import org.nbme.dwbi.synthetic.util.ParquetUtil;
import org.nbme.dwbi.synthetic.util.TreeNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.yaml.snakeyaml.Yaml;

public class SyntheticEngine {
	private File metadataFile;
	private File parameterFile;
	private File outputDir;
	private Metadata md;
	private Properties properties;
	private HashSet<TreeNode> containerNodes;
	private HashMap<String, ArrayList<String>> sourceData = new HashMap<String, ArrayList<String>>();
	private HashSet<String> referencedFields = new HashSet<String>();
	private String outputFileSuffix = "";
	private int randomSeed = (int)System.currentTimeMillis();
	private ContainerType forcedContainerType = null;
	private boolean jsonOuterBracket = true;
	private Pattern jsonSubPattern = Pattern.compile(Defaults.JSON_SUB_PATTERN);
	private static final Logger logger = LoggerFactory.getLogger(SyntheticEngine.class);
	
	
	public SyntheticEngine(String metadataFile, String parameterFile, String outputDir) throws FileNotFoundException {
		this.metadataFile = new File(metadataFile);
		this.parameterFile = new File(parameterFile);
		
		if(!this.metadataFile.exists() || !this.parameterFile.exists()) {
			throw new FileNotFoundException("Metadata or parameter file does not exist: " + this.metadataFile.getAbsolutePath() + " or " + this.parameterFile.getAbsolutePath());
		}
		
		this.outputDir = new File(outputDir);
		if(!this.outputDir.exists()) {
			this.outputDir.mkdir();
		}
	}
	
	public void makeData() throws Exception {
		readInput();
		createData();
	}

	private void createData() throws Exception {
		randomSeed = getProperty("randomSeed") != null ? Integer.parseInt(getProperty("randomSeed")) : (int)System.currentTimeMillis();
		forcedContainerType = getProperty("containerType") != null ? ContainerType.valueOf(getProperty("containerType")) : null;
		jsonOuterBracket = getProperty("json.outer.brackets") != null ? new Boolean(getProperty("json.outer.brackets")) : true;
		HashSet<String> dupChecker = new HashSet<String>();

		containerNodes = new HashSet<TreeNode>();		

		for(Container container : md.getObjects()) {
			TreeNode contNode = new TreeNode(container.getContainerName());
			containerNodes.add(contNode);
		}
		
		//make dependency tree
		for(Container container : md.getObjects()) {
			TreeNode contNode = searchNodes(container.getContainerName());
			for(Field field : container.getFields()) {
				if(!dupChecker.add(container.getContainerName() + "." + field.getName())) {
					throw new Exception("Duplicate name: " + container.getContainerName() + "." + field.getName());
				}
				TreeNode parentNode = null;
				if(field.getReference() != null) {
					referencedFields.add(field.getReference());
					if ((parentNode = searchNodes(field.getReference().split("\\.")[0])) != null) {
					parentNode.addChild(contNode);
					}
				}
			}	
		}
		
		//walk tree to make sample data	
		LinkedList<TreeNode> orderedNodes = findNodeTraversal();
		
		for (TreeNode node : orderedNodes) {
			for (Container container : md.getObjects()) {
				if (container.getContainerName().equals(node.getName())) {
					generateBaseContainerData(container);
				}
			}
		}
		logger.info("data created");
	}
	
	private void generateBaseContainerData(Container container) throws Exception {
		//if passed a random seed, results can be repeatable.  BUT we want each table to start anew, so we derive a new seed for each table
		GeneratorFactory.setSeed(randomSeed + container.getContainerName().hashCode());
		
		logger.info("Generating base data for '" + container.getContainerName() + "'");
		
		LinkedList<Generator> generators = new LinkedList<Generator>();
		LinkedList<String> sources = new LinkedList<String>();
		LinkedList<String> references = new LinkedList<String>();
		
		String[] header = new String[container.getFields().size()];
		int j = 0;
		for(Field field : container.getFields()) {
			StringBuilder sb = new StringBuilder();
			sb.append("Field '" + field.getName() + "'");
			header[j++] = field.getName();
			if(referencedFields.contains(container.getContainerName() + "." + field.getName())) {
				sources.add(container.getContainerName() + "." + field.getName());
				sourceData.put(container.getContainerName() + "." + field.getName(), new ArrayList<String>());
				sb.append(" referenced by other fields");
			} else {
				sources.add("");
			}
			if(field.getReference() != null) {
				references.add(field.getReference());
				sb.append(" references '" + field.getReference() + "'");
			} else {
				references.add("");
			}

			Generator gen = GeneratorFactory.getGenerator(field);
			sb.append(" generated by " + gen.getClass().getSimpleName());
			if(gen instanceof SubfieldGenerator) {
				((SubfieldGenerator)gen).init(field, container.getContainerName() + "." + field.getName(), referencedFields, sources, references, sourceData, field.getMinSubFields(), field.getMaxSubFields());
			}
			generators.add(gen);
			GeneratorFactory.incrementSeed(1);
			logger.info(sb.toString());
		}
		
		String tempNumRows;
		int numRowsTable = (tempNumRows = getProperty("(?i)" + container.getContainerName() + "\\.rowCount")) != null ? Integer.parseInt(tempNumRows): -1;
		int numRows = numRowsTable != -1 ? numRowsTable : Integer.parseInt(getProperty("numBaseRows"));
		String[][] rows = new String[numRows][];
		for (int i = 0; i < numRows; i++) {
			Iterator<String> sourceIter = sources.iterator();
			Iterator<String> referenceIter = references.iterator();
			String[] row = new String[generators.size()];
			int k = 0;
			for (Generator generator : generators) {
				String source = sourceIter.next();
				String reference = referenceIter.next();
				String val= null;
				try {
					val = !"".equals(reference) ? sourceData.get(reference).get(i % sourceData.get(reference).size()): generator.generate();
				} catch (Exception e) {
					throw new Exception("Could not find source data for reference: " + reference +", k: " + k);
				}
				row[k++] = val;
				if(!"".equals(source)) {
					ArrayList<String> vals = sourceData.get(source);
					vals.add(val);
				}
			}
			rows[i] = row;
		}
		Data data = new Data();
		data.setHeader(header);
		data.setRows(rows);
		generateFormattedContainerData(data, forcedContainerType != null ? forcedContainerType : container.getContainerType(), container.getContainerName());
		logger.info("");
	}
	

	private void generateFormattedContainerData(Data data, ContainerType containerType, String name) throws Exception {
		File file = new File(outputDir, name + "_synth" + outputFileSuffix + "." + containerType.toString().toLowerCase());
		logger.info("Formatting as " + containerType + " to file " + file.getAbsolutePath());
		switch(containerType) {
			case CSV:
				generateCSVContainerData(data, file);
				break;
			case JSON:
				generateJSONContainerData(data, file);
				break;
			case YML:
				generateYMLContainerData(data, file);
				break;
			case XML:
				generateXMLContainerData(data, file);
				break;
			case PARQUET:
				generateParquetContainerData(data, file);
				break;
			default:
				throw new Exception("Invalid container type: " + containerType);
		}
	}
	
	private void generateCSVContainerData(Data data, File file) throws Exception {
	    CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
	        .setHeader(data.getHeader())
	        .build();
	    
	    try (FileWriter writer = new FileWriter(file); CSVPrinter printer = new CSVPrinter(writer, csvFormat)) {
	        for(Object[] row : data.getRows()) {
	        	printer.printRecord(row);
	        }
	    }
	}

	private void generateJSONContainerData(Data data, File file) throws Exception {
		JSONArray ar = new JSONArray();
		for (int i = 0; i < data.getRows().length; i++) {
			JSONObject jo = new JSONObject();
			for (int j = 0; j < data.getHeader().length; j++) {
				String dataX = (String)data.getRows()[i][j];
				Matcher m = jsonSubPattern.matcher(dataX);
				if(m.matches()) {
					iterateGenerateJSONContainerData(m.group(2), jo, data.getHeader()[j], "X".equals(m.group(1)));
				} else {
					jo.put(data.getHeader()[j], data.getRows()[i][j]);
				}
			}
			ar.put(jo);
		}
		String out = jsonOuterBracket ? ar.toString(2) : ar.toString(2).replaceAll(Defaults.JSON_BRACKET_PATTERN, "");
			
		try(FileWriter writer = new FileWriter(file)){
			writer.write(out);
		}
	}

	private void generateParquetContainerData(Data data, File file) throws Exception {
		ParquetUtil pu = new ParquetUtil();

		for (String head : data.getHeader()) {
			pu.addSchemaField(head, "string");
		}
		pu.generateSchema();

		for (Object[] row : data.getRows()) {
			pu.newRecord();
			for (int i = 0; i < row.length; i++) {
				pu.addRecordField(data.getHeader()[i], row[i]);
			}
		}

		pu.generateParquetFile(file);
	}
	
	private void iterateGenerateJSONContainerData(String csvLikeData, JSONObject joIn, String headerIn, boolean isArray) throws Exception {
		int lineCount = 0;
		ArrayList<String> header = new ArrayList<String>();
		
		JSONArray ar = new JSONArray();
		JSONObject jo = new JSONObject();
		
        try (Reader reader = new InputStreamReader(IOUtils.toInputStream(csvLikeData))) {
    		Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(reader);
    		Iterator<CSVRecord> itr = records.iterator();
            while (itr.hasNext()) {
            	CSVRecord record = itr.next();
                lineCount++;
                if (lineCount == 1) {
                	for(int i = 0; i < record.size(); i++) {
	                   header.add(record.get(i));
                	}
                } else {
        			jo = new JSONObject();
                	for(int i = 0; i < record.size(); i++) {
    					String dataX = record.get(i);
    					Matcher m = jsonSubPattern.matcher(dataX);
    					if(m.matches()) {
    						iterateGenerateJSONContainerData(m.group(2), jo, header.get(i), "X".equals(m.group(1)));
    					} else {
        					jo.put(header.get(i), record.get(i));
    					}
                	}
                	ar.put(jo);
                }
            }
        } 
		
		joIn.put(headerIn, isArray ? ar : jo);
	}
	
	private void generateYMLContainerData(Data data, File file) throws Exception {
        Yaml yaml = YmlParser.getYamlWithOptions();
		
		LinkedHashMap<String, ArrayList<LinkedHashMap<String, String>>> map = new LinkedHashMap<String, ArrayList<LinkedHashMap<String, String>>>();
		ArrayList<LinkedHashMap<String, String>> ar = new ArrayList<LinkedHashMap<String, String>>();
		for (int i = 0; i < data.getRows().length; i++) {
			LinkedHashMap<String, String> jo = new LinkedHashMap<String, String>();
			for (int j = 0; j < data.getHeader().length; j++) {
				jo.put(data.getHeader()[j], data.getRows()[i][j]);
			}
			ar.add(jo);
		}
		map.put("data", ar);
		String res = yaml.dump(ar);
		
		try(FileWriter writer = new FileWriter(file)){
			writer.write(res);
		}
	}
	
	private void generateXMLContainerData(Data data, File file) throws Exception {
		//https://mkyong.com/java/how-to-create-xml-file-in-java-dom/
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

		Document doc = docBuilder.newDocument();
		Element rootElement = doc.createElement("records");
		doc.appendChild(rootElement);

		for (int i = 0; i < data.getRows().length; i++) {
			Element record = doc.createElement("record");
			rootElement.appendChild(record);
			for (int j = 0; j < data.getHeader().length; j++) {
				Element name = doc.createElement(data.getHeader()[j].replaceAll("[^A-Za-z]", "_"));
				name.setTextContent((String)data.getRows()[i][j]);
				record.appendChild(name);
			}
		}

		try(FileWriter writer = new FileWriter(file)){
			writeXml(doc, writer);
		}
	}

	private void writeXml(Document doc, Writer writer) throws TransformerException {
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");

		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(writer);

		transformer.transform(source, result);
	}
	
	private TreeNode searchNodes(String name) {
		for(TreeNode node : containerNodes) {
			if(name.equals(node.getName())){
				return node;
			}
		}
		return null;
	}
	
	private List<TreeNode> findSingleNodes() {
		LinkedList<TreeNode> retVal = new LinkedList<TreeNode>();
		for(TreeNode node : containerNodes) {
			if(node.getParentNodes().size() == 0 && node.getChildNodes().size() == 0){
				retVal.add(node);
			}
		}
		return retVal;
	}
	
	private List<TreeNode> findRootParents() {
		LinkedList<TreeNode> retVal = new LinkedList<TreeNode>();
		for(TreeNode node : containerNodes) {
			if(node.getParentNodes().size() == 0 && node.getChildNodes().size() != 0){
				retVal.add(node);
			}
		}
		return retVal;
	}
	
	private LinkedList<TreeNode> findNodeTraversal() throws Exception {
		LinkedList<TreeNode> retVal = new LinkedList<TreeNode>();
		List<TreeNode> singles = findSingleNodes();
		List<TreeNode> roots = findRootParents();
		retVal.addAll(singles);
		retVal.addAll(roots);
		containerNodes.removeAll(retVal);
		
		StringBuilder sb = new StringBuilder();
		sb.append("Object construction order:" + System.lineSeparator());
		if(singles.size() > 0) {
			sb.append(">>Singles:" + System.lineSeparator());
			for(TreeNode node : singles) {
				sb.append(node.getName() + System.lineSeparator());
			}
		}
		if(roots.size() > 0) {
			sb.append(">>Roots:" + System.lineSeparator());
			for(TreeNode node : roots) {
				sb.append(node.getName() + System.lineSeparator());
			}
		}
		sb.append(">>Remaining:" + System.lineSeparator());
		
		while(containerNodes.size() > 0) {
			boolean didOne = false;
			Iterator<TreeNode> nodeItr = containerNodes.iterator();
			
			while(nodeItr.hasNext()) {
				TreeNode node = nodeItr.next();
				if(retVal.containsAll(node.getParentNodes())) {
					retVal.add(node);
					sb.append(node.getName() + System.lineSeparator());
					nodeItr.remove();
					didOne = true;
				}
			}
			
			if(!didOne) {
				sb.append(">>Problems:" + checkCircularDependencies());
				throw new Exception("Could not find next container to construct.  Possile circular reference?" + System.lineSeparator() + sb.toString());
			}
		}

		logger.info(sb.toString() + System.lineSeparator());
		return retVal;
	}
	
	private String checkCircularDependencies() {
		HashSet<String> rel = new HashSet<String>();
		StringBuilder sb = new StringBuilder();
		for(TreeNode node : containerNodes) {
//			logger.info("Container: " + node.getName());
			if(node.getParentNodes().size() != 0){
//				logger.info("Parents:");
			}
			if(node.getChildNodes().size() != 0){
//				logger.info("--Children:");
				for(TreeNode child : node.getChildNodes()) {
//					logger.info("    " + child.getName());
					if(rel.contains(child.getName() + " - " + node.getName())) {
						sb.append(System.lineSeparator() + child.getName() + " - " + node.getName());
					}
					rel.add(node.getName() + " - " + child.getName());
				}
			}
		}
		return sb.length() > 0 ? "There may be circular dependencies between the following:" + sb : "No circular dependency found";
	}

	private void readInput() throws Exception {
		Yaml yaml = new Yaml();
		try (FileReader reader = new FileReader(metadataFile)) {
			md = yaml.loadAs(reader, Metadata.class);
		}

		properties = new Properties();
		try (FileReader reader = new FileReader(parameterFile)) {
			properties.load(reader);
		}
	}
	
	private String getProperty(String name) {
		for(Object key : properties.keySet()) {
			String keyString = (String)key;
			if(keyString.matches(name)) {
				return properties.getProperty(keyString);
			}
		}
		return null;
	}	
}
