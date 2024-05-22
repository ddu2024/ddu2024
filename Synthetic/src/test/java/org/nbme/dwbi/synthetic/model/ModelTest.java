package org.nbme.dwbi.synthetic.model;

import java.util.ArrayList;
import java.util.Date;

import org.junit.Test;
import org.nbme.dwbi.synthetic.parser.YmlParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

public class ModelTest {
	private static final Logger logger = LoggerFactory.getLogger(ModelTest.class);
	
	@Test
	public void testModel() throws Exception {
		Metadata md = new Metadata();
		md.setName("Test_Model_MD");
		md.setVersion(1);
		md.setDate(new Date());		

		Container container = new Container();
		container.setContainerType(ContainerType.CSV);
		container.setContainerName("Test_Container");

		ArrayList<Container> containers = new ArrayList<Container>();
		containers.add(container);
		
		md.setObjects(containers);
		
		Field examinee = new Field();
		examinee.setFieldType(FieldType.STR);
		examinee.setName("ExamineeId");
		
		Field name = new Field();
		name.setName("name");
		name.setFieldType(FieldType.STR);
		
		ArrayList<Field> fields = new ArrayList<Field>();
		
		container.setFields(fields);		

		Field freq = new Field();
		freq.setFieldType(FieldType.FREQ);
		freq.setName("AssortedData");
		fields.add(freq);
		fields.add(examinee);
		fields.add(name);
		
		Frequency frequency = new Frequency();
		
		Field val1 = new Field();
		val1.setFieldType(FieldType.STR);
		frequency.addCount(112);
		
		Field val2 = new Field();
		val2.setFieldType(FieldType.DOUBLE);
		frequency.addCount(31);
		frequency.addField(val2);
		frequency.addField(val1);
		
		freq.setFrequency(frequency);
		
		Field subField = new Field();
		subField.setName("subField_test");
		fields.add(subField);		

		Field val3 = new Field();
		val3.setName("Name_test");
		val3.setFieldType(FieldType.FULL_NAME);
		
		Field val4 = new Field();
		val4.setFieldType(FieldType.INT);
		val4.setName("counter");
		subField.getSubFields().add(val3);
		subField.getSubFields().add(val4);
		
		Yaml yaml = YmlParser.getYamlWithOptions();
		logger.info(yaml.dump(md));
	}
}
