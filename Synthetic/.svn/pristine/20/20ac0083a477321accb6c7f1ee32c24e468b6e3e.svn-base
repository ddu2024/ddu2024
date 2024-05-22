package org.nbme.dwbi.synthetic.parser;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.nbme.dwbi.synthetic.model.Container;
import org.nbme.dwbi.synthetic.model.ContainerType;
import org.nbme.dwbi.synthetic.model.Field;
import org.nbme.dwbi.synthetic.model.FieldType;
import org.nbme.dwbi.synthetic.model.Metadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

public class JsonParser {
	private Metadata md;
	private HashMap<String, ArrayList<String>> values = new HashMap<String, ArrayList<String>>();
	private HashMap<String, Integer> arrayElementMaxCounts = new HashMap<String, Integer>();
	private HashMap<String, Integer> arrayElementMinCounts = new HashMap<String, Integer>();
	private ColumnAnalysis colAnal = new ColumnAnalysis();

	private static final Logger logger = LoggerFactory.getLogger(JsonParser.class);

	public JsonParser() {
		this(new Metadata());
	}

	public JsonParser(Metadata md) {
		super();
		this.md = md;
	}
	
	public static void main(String[] args) throws Exception {
		JsonParser p = new JsonParser();
		p.parseJson(new File("C:\\Users\\MMenga\\Desktop\\SyntheticData\\JsonInput\\RPTI_PROFILE_EXAMINEE_DETAIL_2023-02-05 18_30_40.497.json"));
	}

	public void parseJson(File file) throws Exception {
		String jsonContent = FileUtils.readFileToString(file);

		String name = file.getName().replaceAll("^(.*?)\\..*","$1");
		
		Container container = null;
		boolean foundExistingContainer = false;
		
		for(Container containerX : md.getObjects()) {
			if(containerX.getContainerName().equals(name)) {
				container = containerX;
				foundExistingContainer = true;
				break;
			}
		}
		if(container == null) {
			container = new Container();
			container.setContainerName(name);
			container.setFields(new ArrayList<Field>());
			container.setContainerType(ContainerType.JSON);
			md.getObjects().add(container);
		}

		JSONObject obj = new JSONObject(jsonContent);
		
		iterateObj(obj, container, null, name);
		
		iterateContainer(container, name);
		
//		iterateObj(obj, "");
		Yaml yaml = YmlParser.getYamlWithOptions();
//		logger.info(yaml.dump(md));
	}
	
	private void iterateContainer(Object obj, String trackingName) throws Exception {
		if (obj instanceof Container) {
			for (Field field : ((Container) obj).getFields()) {
				iterateContainer(field, trackingName + "_" + field.getName());
			}
		} else if (obj instanceof Field) {
			Field field = (Field) obj;
			if(FieldType.ARRAY == field.getFieldType()) {
				field.setMaxSubFields(arrayElementMaxCounts.get(trackingName));
				field.setMinSubFields(arrayElementMinCounts.get(trackingName));
			}

			if (field.getSubFields().size() > 0) {
				for (Field subField : field.getSubFields()) {
					iterateContainer(subField, trackingName + "_" + subField.getName());
				}
			} else {
				colAnal.analyzeColumn(field, 0, values.get(trackingName));
			}
		}
	}

	static void iterateObj(Object obj, String indent) throws JSONException {
		if (obj instanceof JSONObject) {
			Iterator<String> keys = ((JSONObject) obj).keys();
			logger.info(indent + "{");
			while (keys.hasNext()) {
				String key = keys.next();
				Object newObj = ((JSONObject) obj).get(key);
				logger.info(indent + key);
				iterateObj(newObj, indent + "    ");
			}
			logger.info(indent + "}");
		} else if (obj instanceof JSONArray) {
			logger.info(indent + "[");
			for (int i = 0; i < ((JSONArray) obj).length(); i++) {
				iterateObj(((JSONArray) obj).getJSONObject(i), indent);
				if(i < ((JSONArray) obj).length() -1) {
				logger.info(",");
				}
			}
			logger.info(indent + "]");
		} else {
			String val = (String) obj; 
			ColumnAnalysis colAnal = new ColumnAnalysis();
			logger.info(indent + colAnal.resolveVal(val));
		}
	}

	private void iterateObj(Object obj, Container container, Field field, String trackingName) throws JSONException {
		if (obj instanceof JSONObject) {
			Iterator<String> keys = ((JSONObject) obj).keys();
		//	logger.info(indent + "{");
			while (keys.hasNext()) {
				Field fieldObj = new Field();
				String key = keys.next();
				fieldObj.setName(key);
				if(container != null) {
					container.getFields().add(fieldObj);
					container = null;
				}
				Object newObj = ((JSONObject) obj).get(key);
//				logger.info(indent + key);
				iterateObj(newObj, container, fieldObj, trackingName + "_" + key);
				if(field != null) {
					if(field.getFieldType() == null) {
						field.setFieldType(FieldType.SUBSET);
					}
					field.getSubFields().add(fieldObj);
				}
			}
			//logger.info(indent + "}");
		} else if (obj instanceof JSONArray) {
			field.setFieldType(FieldType.ARRAY);
			int length = ((JSONArray) obj).length();
			Integer maxCount = arrayElementMaxCounts.get(trackingName);
			if(maxCount == null || length > maxCount) {
				arrayElementMaxCounts.put(trackingName, length);
			}
			Integer minCount = arrayElementMinCounts.get(trackingName);
			if(minCount == null || length < minCount) {
				arrayElementMinCounts.put(trackingName, length);
			}

//			logger.info(indent + "[");
			for (int i = 0; i < length; i++) {
				iterateObj(((JSONArray) obj).getJSONObject(i), container, i == 0 ? field : null, trackingName);// + "_array");

//				if(i < ((JSONArray) obj).length() -1) {
//				logger.info(",");
//				}
			}
//			logger.info(indent + "]");
		} else {
			String val = (String) obj;
			if(field != null) {
				field.setFieldType(colAnal.resolveVal(val));				
			} else {
			}
			ArrayList<String> vals = values.get(trackingName);
			if(vals == null) {
				vals = new ArrayList<String>();
				values.put(trackingName, vals);
			}
			vals.add(val);
		}
	}
}
