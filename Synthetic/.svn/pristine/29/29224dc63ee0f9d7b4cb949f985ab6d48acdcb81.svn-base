package org.nbme.dwbi.synthetic.model;

import java.util.HashSet;

public enum FieldType {
STR, CHAR, SENTENCE,
BOOLEAN,
FULL_NAME, FIRST_NAME, LAST_NAME, MIDDLE_NAME,
SCHOOL,
ADDRESS, STREET, CITY, COUNTRY, ZIP,
PHONE, SSN, EMAIL, URL, FILE,
REGEX,
INT, INC_INT, DOUBLE,
B_DATE, DATE,
ARRAY, SUBSET, FREQ, STATIC, NULL, EMPTY;

	private static HashSet<FieldType> numerics = new HashSet<FieldType>();
	private static HashSet<FieldType> strings = new HashSet<FieldType>();
	private static HashSet<FieldType> dates = new HashSet<FieldType>();
	
	static {
		numerics.add(INT);
		numerics.add(DOUBLE);
		numerics.add(INC_INT);

		strings.add(STR);
		strings.add(ADDRESS);
		strings.add(STREET);
		strings.add(CITY);
		strings.add(COUNTRY);
		strings.add(ZIP);
		strings.add(FULL_NAME);
		strings.add(FIRST_NAME);
		strings.add(LAST_NAME);
		strings.add(MIDDLE_NAME);
		strings.add(CHAR);
		strings.add(FILE);
		strings.add(EMAIL);
		strings.add(URL);
		strings.add(REGEX);
		strings.add(SCHOOL);
		strings.add(SENTENCE);
		
		dates.add(B_DATE);
		dates.add(DATE);
	}

	public static boolean isNumeric(FieldType typeIn) {
		return numerics.contains(typeIn);
	}

	public static boolean isString(FieldType typeIn) {
		return strings.contains(typeIn);
	}

	public static boolean isDate(FieldType typeIn) {
		return dates.contains(typeIn);
	}
}
