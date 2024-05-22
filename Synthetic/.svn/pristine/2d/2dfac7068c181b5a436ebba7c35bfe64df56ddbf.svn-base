package org.nbme.dwbi.synthetic.generator;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import org.junit.Test;
import org.nbme.dwbi.synthetic.model.Field;
import org.nbme.dwbi.synthetic.model.FieldType;
import org.nbme.dwbi.synthetic.model.Frequency;

public class GeneratorTest {
	private EnumMap<FieldType, String> map = new EnumMap<FieldType, String>(FieldType.class);

	@Test
	public void testFactory() throws Exception {
		map.put(FieldType.STR, "StringGenerator");
		map.put(FieldType.CHAR, "StringGenerator");
		map.put(FieldType.SENTENCE, "SentenceGenerator");
		map.put(FieldType.BOOLEAN, "BooleanGenerator");
		map.put(FieldType.FULL_NAME, "NameGenerator");
		map.put(FieldType.FIRST_NAME, "NameGenerator");
		map.put(FieldType.LAST_NAME, "NameGenerator");
		map.put(FieldType.MIDDLE_NAME, "NameGenerator");
		map.put(FieldType.SCHOOL, "SchoolGenerator");
		map.put(FieldType.ADDRESS, "AddressGenerator");
		map.put(FieldType.STREET, "AddressGenerator");
		map.put(FieldType.CITY, "AddressGenerator");
		map.put(FieldType.COUNTRY, "AddressGenerator");
		map.put(FieldType.ZIP, "AddressGenerator");
		map.put(FieldType.PHONE, "PhoneGenerator");
		map.put(FieldType.SSN, "SSNGenerator");
		map.put(FieldType.EMAIL, "EmailGenerator");
		map.put(FieldType.URL, "UrlGenerator");
		map.put(FieldType.FILE, "FileGenerator");
		map.put(FieldType.REGEX, "RegexGenerator");
		map.put(FieldType.INT, "IntGenerator");
		map.put(FieldType.INC_INT, "IncIntGenerator");
		map.put(FieldType.DOUBLE, "DoubleGenerator");
		map.put(FieldType.B_DATE, "DateGenerator");
		map.put(FieldType.DATE, "DateGenerator");
		map.put(FieldType.ARRAY, "SubfieldGenerator");
		map.put(FieldType.SUBSET, "SubfieldGenerator");
		map.put(FieldType.FREQ, "FreqGenerator");
		map.put(FieldType.STATIC, "StaticGenerator");
		map.put(FieldType.NULL, "StaticGenerator");
		map.put(FieldType.EMPTY, "StaticGenerator");

		Field field = new Field();

		Frequency frequency = new Frequency();
		field.setFrequency(frequency);
		Field freqField = new Field();
		freqField.setFieldType(FieldType.INT);
		field.getFrequency().addField(freqField);
		field.getFrequency().addCount(1);		

		field.getSubFields().add(freqField);
		
		for (FieldType fieldType : map.keySet()) {
			field.setFieldType(fieldType);

			Generator gen = GeneratorFactory.getGenerator(field);
			
			if(gen instanceof SubfieldGenerator) {
				((SubfieldGenerator)gen).init(field, "", new HashSet<String>(), new LinkedList<String>(), new LinkedList<String>(), new HashMap<String, ArrayList<String>>(), 1, 1);
			}
			
			assertTrue(map.get(fieldType).equals(gen.getClass().getSimpleName()));
			gen.generate();
		}
	}
}
