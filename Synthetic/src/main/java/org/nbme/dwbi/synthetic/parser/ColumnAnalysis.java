package org.nbme.dwbi.synthetic.parser;

import java.math.BigInteger;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;

import org.apache.commons.lang3.StringUtils;
import org.nbme.dwbi.synthetic.constants.Defaults;
import org.nbme.dwbi.synthetic.generator.Generator;
import org.nbme.dwbi.synthetic.generator.GeneratorFactory;
import org.nbme.dwbi.synthetic.model.Field;
import org.nbme.dwbi.synthetic.model.FieldType;
import org.nbme.dwbi.synthetic.model.Frequency;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ColumnAnalysis {
	private EnumMap<FieldType, Integer> counts = new EnumMap<FieldType, Integer>(FieldType.class);
	private HashMap<String, Integer> dfCounts;
	
	private Double lowestNum = null;
	private Double highestNum = null;
	private Integer minLen = null;
	private Integer maxLen = null;
	private EnumMap<FieldType, HashSet<String>> values = new EnumMap<FieldType, HashSet<String>>(FieldType.class);
	private String dateFormat = null;
	private Long minDate = null;
	private Long maxDate = null;
	private String dfFinal = "MM/dd/yyyy";
	private static final Logger logger = LoggerFactory.getLogger(ColumnAnalysis.class);
	
//	private Double ACCEPTANCE_RATIO = 75.0;
	
	private FieldType incrementMap(FieldType type) {
		Integer inty = counts.get(type);
		if(inty == null) {
			inty = 0;
		}
		inty++;
		counts.put(type, inty);
		return type;
	}
	
	private void incrementDFMap(String df) {
		Integer inty = dfCounts.get(df);
		if(inty == null) {
			inty = 0;
		}
		inty++;
		dfCounts.put(df, inty);
	}
	
	public void analyzeColumn(Field field, int fieldOrder, ArrayList<String> col) throws Exception {
		if(field.getReference() != null) {
			logger.info("'" + field.getName() + "' had a reference, so skipping type analysis");
			 return;
		}
		counts = new EnumMap<FieldType, Integer>(FieldType.class);
		dfCounts = new HashMap<String, Integer>();
		
		lowestNum = null;
		highestNum = null;
		minLen = null;
		maxLen = null;
		values = new EnumMap<FieldType, HashSet<String>>(FieldType.class);
		dateFormat = null;
		minDate = null;
		maxDate = null;
		dfFinal = "MM/dd/yyyy";
		
		for(String val : col) {
			FieldType tempType = resolveVal(val);
			
			HashSet<String> set = values.get(tempType);
			if(set == null) {
				set = new HashSet<String>();
				values.put(tempType, set);
			}
			set.add(val);
		}		
		
		//determine ratios and find max, if passs preset level
		FieldType maxType = null;
		int maxCount = 0;
		
		for(FieldType type : counts.keySet()) {
			int count = counts.get(type);
			if(count > maxCount) {
				maxType = type;
				maxCount = count;
			}
		}		

		int dfCount = 0;
		for(String df : dfCounts.keySet()) {
			int count = dfCounts.get(df);
			if(count > dfCount) {
				dfFinal = df;
				dfCount = count;
			}
		}
		
		FieldType possibleType = null;
		if(counts.keySet().size() > 1){
			//make frequency object
			field.clearOptions();
			Frequency frequency = new Frequency();
			field.setFrequency(frequency);
			StringBuilder sb = new StringBuilder();
			
			for(FieldType type : counts.keySet()) {
				int count = counts.get(type);
				
				FieldType newType = getFieldType(field, type);
				
				frequency.addCount(count);
				
				Field freqSubField = new Field();
				freqSubField.setFieldType(newType);
				freqSubField.setName("FREQUENCY: " + field.getName());
				
				setOptions(freqSubField);
				HashSet<String> valuesSet = values.get(type);
				checkConstantValue(valuesSet, freqSubField);
				freqSubField.setName(null);
				sb.append((sb.length() > 0 ? ", " : "") + newType + ":" + count);
				frequency.addField(freqSubField);
			}
			field.setFieldType(FieldType.FREQ);
			logger.info("Field '" + field.getName() + "' was determined to have a frequency: [" + sb.toString() + "]");
			return;
		}		

		possibleType = maxType;

		field.setFieldType(getFieldType(field, possibleType));
		logger.info("Field '" + field.getName() + "' set to type: " + field.getFieldType() + " NORMAL");

		setOptions(field);
		HashSet<String> valuesSet = new HashSet<String>();
		for(FieldType type : values.keySet()) {
			valuesSet.addAll(values.get(type));
		}
		checkConstantValue(valuesSet, field);
	}
	
	public FieldType resolveVal(String val) {
		FieldType tempType = FieldType.STR;
		int len = val.length();
		if (len > 0) {
			if (minLen == null || len < minLen) {
				minLen = len;
			}
			if (maxLen == null || len > maxLen) {
				maxLen = len;
			}
		}
		if(StringUtils.isAllBlank(val)) {
			tempType = incrementMap(FieldType.EMPTY);
		} else if(val.matches("(?i).*(?:SCHOOL|UNIVERSITY|UNIV\\.|COLLEGE).*")) { //school
			tempType = incrementMap(FieldType.SCHOOL);
		} else if(val.matches("(?i)^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,}$")) { //email
			tempType = incrementMap(FieldType.EMAIL);
		} else if(val.matches("^-*\\d+")) { //integer
			tempType = incrementMap(FieldType.INT);
			int intVal = new BigInteger(val).intValue();
			if(lowestNum == null || intVal < lowestNum) {
				lowestNum = Double.parseDouble(val);
			}
			if(highestNum == null || intVal > highestNum) {
				highestNum = Double.parseDouble(val);
			}

		} else if(val.matches("^([+-]?(?:\\d+\\.?|\\d*\\.\\d+))(?:[Ee][+-]?\\d+)?$")) {//double
			tempType = incrementMap(FieldType.DOUBLE);
			double doubleVal = Double.parseDouble(val);
			if(lowestNum == null || doubleVal < lowestNum) {
				lowestNum = Double.parseDouble(val);
			}
			if(highestNum == null || doubleVal > highestNum) {
				highestNum = Double.parseDouble(val);
			}
			
		} else if((dateFormat = checkForDate(val)) != null) { //date
			incrementDFMap(dateFormat);
			tempType = incrementMap(FieldType.DATE);
			long date = -1;
			try {
				date = new SimpleDateFormat(dateFormat).parse(val).getTime();
			} catch (ParseException e) {
				logger.error("Date Check issue", e);
			}
			if(date == -1) {
				return tempType;
			}
			if(minDate == null || date < minDate) {
				minDate = date;
			}
			if(maxDate == null || date > maxDate) {
				maxDate = date;
			}
		} else if(val.matches("(?i)^(?:yes|no|y|n|true|false|on|off)$")){
			tempType = incrementMap(FieldType.BOOLEAN);
		} else if(val.length() == 1) { //single char
			tempType = incrementMap(FieldType.CHAR);
		} else if(val.matches("^(?:[a-zA-Z]:|(?:\\\\|/))(?:(?:\\\\|/){1}|(((?:\\\\|/){1})[^\\\\/][^/:*?<>\"|]*)+)$")) { //file path
			tempType = incrementMap(FieldType.FILE);
		} else if(val.matches(".*?\\d{1,5}(?:\\s[\\w-.,]*){1,6},\\s[A-Z]{2}\\s\\d{5}(?:-\\d{4}|)\\b")) { //address
			tempType = incrementMap(FieldType.ADDRESS);
		} else if(val.matches("\\d{3}-\\d{2}-\\d{4}")) { //ssn
			tempType = incrementMap(FieldType.SSN);
		} else if(val.matches("(?i)\\b((?:https?:(?:/{1,3}|[a-z0-9%])|[a-z0-9.\\-]+[.](?:com|net|org|edu|gov|mil|aero|asia|biz|cat|coop|info|int|jobs|mobi|museum|name|post|pro|tel|travel|xxx|ac|ad|ae|af|ag|ai|al|am|an|ao|aq|ar|as|at|au|aw|ax|az|ba|bb|bd|be|bf|bg|bh|bi|bj|bm|bn|bo|br|bs|bt|bv|bw|by|bz|ca|cc|cd|cf|cg|ch|ci|ck|cl|cm|cn|co|cr|cs|cu|cv|cx|cy|cz|dd|de|dj|dk|dm|do|dz|ec|ee|eg|eh|er|es|et|eu|fi|fj|fk|fm|fo|fr|ga|gb|gd|ge|gf|gg|gh|gi|gl|gm|gn|gp|gq|gr|gs|gt|gu|gw|gy|hk|hm|hn|hr|ht|hu|id|ie|il|im|in|io|iq|ir|is|it|je|jm|jo|jp|ke|kg|kh|ki|km|kn|kp|kr|kw|ky|kz|la|lb|lc|li|lk|lr|ls|lt|lu|lv|ly|ma|mc|md|me|mg|mh|mk|ml|mm|mn|mo|mp|mq|mr|ms|mt|mu|mv|mw|mx|my|mz|na|nc|ne|nf|ng|ni|nl|no|np|nr|nu|nz|om|pa|pe|pf|pg|ph|pk|pl|pm|pn|pr|ps|pt|pw|py|qa|re|ro|rs|ru|rw|sa|sb|sc|sd|se|sg|sh|si|sj|Ja|sk|sl|sm|sn|so|sr|ss|st|su|sv|sx|sy|sz|tc|td|tf|tg|th|tj|tk|tl|tm|tn|to|tp|tr|tt|tv|tw|tz|ua|ug|uk|us|uy|uz|va|vc|ve|vg|vi|vn|vu|wf|ws|ye|yt|yu|za|zm|zw)/)(?:[^\\s()<>{}\\[\\]]+|\\([^\\s()]*?\\([^\\s()]+\\)[^\\s()]*?\\)|\\([^\\s]+?\\))+(?:\\([^\\s()]*?\\([^\\s()]+\\)[^\\s()]*?\\)|\\([^\\s]+?\\)|[^\\s`!()\\[\\]{};:'\".,<>?«»“”‘’])|(?:(?<!@)[a-z0-9]+(?:[.\\-][a-z0-9]+)*[.](?:com|net|org|edu|gov|mil|aero|asia|biz|cat|coop|info|int|jobs|mobi|museum|name|post|pro|tel|travel|xxx|ac|ad|ae|af|ag|ai|al|am|an|ao|aq|ar|as|at|au|aw|ax|az|ba|bb|bd|be|bf|bg|bh|bi|bj|bm|bn|bo|br|bs|bt|bv|bw|by|bz|ca|cc|cd|cf|cg|ch|ci|ck|cl|cm|cn|co|cr|cs|cu|cv|cx|cy|cz|dd|de|dj|dk|dm|do|dz|ec|ee|eg|eh|er|es|et|eu|fi|fj|fk|fm|fo|fr|ga|gb|gd|ge|gf|gg|gh|gi|gl|gm|gn|gp|gq|gr|gs|gt|gu|gw|gy|hk|hm|hn|hr|ht|hu|id|ie|il|im|in|io|iq|ir|is|it|je|jm|jo|jp|ke|kg|kh|ki|km|kn|kp|kr|kw|ky|kz|la|lb|lc|li|lk|lr|ls|lt|lu|lv|ly|ma|mc|md|me|mg|mh|mk|ml|mm|mn|mo|mp|mq|mr|ms|mt|mu|mv|mw|mx|my|mz|na|nc|ne|nf|ng|ni|nl|no|np|nr|nu|nz|om|pa|pe|pf|pg|ph|pk|pl|pm|pn|pr|ps|pt|pw|py|qa|re|ro|rs|ru|rw|sa|sb|sc|sd|se|sg|sh|si|sj|Ja|sk|sl|sm|sn|so|sr|ss|st|su|sv|sx|sy|sz|tc|td|tf|tg|th|tj|tk|tl|tm|tn|to|tp|tr|tt|tv|tw|tz|ua|ug|uk|us|uy|uz|va|vc|ve|vg|vi|vn|vu|wf|ws|ye|yt|yu|za|zm|zw)\\b/?(?!@)))")) {
			tempType = incrementMap(FieldType.URL);
		} else if(val.matches("(?i)^\\s*?(\\w{3,}\\s){3,}.*$")){
			tempType = incrementMap(FieldType.SENTENCE);
		} else {
			//assume string/ generate pattern and start statistics //length
			tempType = incrementMap(FieldType.STR);
		}
		return tempType;
	}
	
	private FieldType getFieldType(Field field, FieldType possibleType) {
		if(FieldType.EMPTY == possibleType) {
			return FieldType.EMPTY;
		}
		FieldType typeFromName = null;
		FieldType finalType = null;
		if (field.getName().matches("(?i)(?:DATE.*|.*?_(?:DATE|DT|DTIME))")) {
			typeFromName = FieldType.DATE;
		} else if (field.getName().matches("(?i).*?birth.*")) {
			typeFromName = FieldType.B_DATE;
		} else if (field.getName().matches("(?i).*?_ID")) {
			if(FieldType.INT.equals(possibleType)) {
//				if(fieldOrder == 0) {
					finalType = FieldType.INC_INT;
//				} else {
//					finalType = FieldType.INT;
//				}
			} else {
				
			}
		} else if (field.getName().matches("(?i).*?phone.*")) {
			typeFromName = FieldType.PHONE;
		} else if (field.getName().matches("(?i).*?email.*")) {
			typeFromName = FieldType.EMAIL;
		} else if (field.getName().matches("(?i).*?first.*?name.*")) {
			typeFromName = FieldType.FIRST_NAME;
		} else if (field.getName().matches("(?i).*?last.*?name.*")) {
			typeFromName = FieldType.LAST_NAME;
		} else if (field.getName().matches("(?i).*?middle.*?name.*")) {
			typeFromName = FieldType.MIDDLE_NAME;
		} else if (field.getName().matches("(?i).*?address.*")) {
			typeFromName = FieldType.ADDRESS;
		} else if (field.getName().matches("(?i).*?street.*")) {
			typeFromName = FieldType.STREET;
		} else if (field.getName().matches("(?i).*?city.*")) {
			typeFromName = FieldType.CITY;
		} else if (field.getName().matches("(?i).*?country.*")) {
			typeFromName = FieldType.COUNTRY;
		} else if (field.getName().matches("(?i).*?zip(:?.*?code|)$")) {
			typeFromName = FieldType.ZIP;
		} else if (field.getName().matches("(?i).*?(_PATH|FILE).*$")) {
			typeFromName = FieldType.FILE;
		} else if (field.getName().matches("(?i).*?SCHOOL.*$")) {
			typeFromName = FieldType.SCHOOL;
		} else if (field.getName().matches("(?i)(?:GENDER|SEX)$")) {
			HashSet<String> vals = values.get(possibleType);
			if (vals != null && vals.size() > 0) {
				values.put(FieldType.BOOLEAN, vals);
				typeFromName = FieldType.BOOLEAN;
			}
		}
		
		if(finalType == null ) {
			if(typeFromName != null) {
				logger.info("Field Type determined from field name: '" + field.getName() + "' - " + typeFromName);
			}
			finalType = typeFromName == null ? possibleType : typeFromName;
		}
		
		FieldType currentType = field.getFieldType();
		
		if(finalType == null) {
			if(currentType == null) {
				return FieldType.STR;
			} else {
				return currentType;
			}
		}
		return finalType;
	}
	
	private void setOptions(Field field) {
		StringBuilder sb = new StringBuilder();
		sb.append("Options set for '" + field.getName() + "': ");
		int sbLen = sb.length();
		if(FieldType.STR == field.getFieldType() || FieldType.SENTENCE == field.getFieldType()) {
			field.setMinLen(minLen);
			field.setMaxLen(maxLen);
			sb.append("min=" + minLen + ", max " + maxLen);
		} else if (FieldType.DATE == field.getFieldType()) {
			field.setMinDate(minDate);
			field.setMaxDate(maxDate);
			field.setDateFormat(dfFinal);
			sb.append("min=" + minDate + ", max=" + maxDate + ", df='" + dfFinal + "'");
		} else if (FieldType.B_DATE == field.getFieldType()) {
			field.setMaxAge(Period.between(LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(minDate)), LocalDate.now()).getYears());
			field.setMinAge(Period.between(LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(maxDate)), LocalDate.now()).getYears());
			field.setDateFormat(dfFinal);
			sb.append("min=" + field.getMinAge() + ", max=" + field.getMaxAge() + ", df='" + dfFinal + "'");
		} else if (FieldType.DOUBLE == field.getFieldType() || FieldType.INT == field.getFieldType()) {
			if(lowestNum.equals(highestNum)) {
				field.clearOptions();
				field.setStaticValue("" + (FieldType.DOUBLE == field.getFieldType() ? lowestNum : "" + lowestNum.intValue()));
				logger.info("Field " + field.getName() + "(" + field.getFieldType() + ") set to " + FieldType.STATIC + ", val '" + field.getStaticValue() + "', OPTIONS CONSTANT VALUE");
				field.setFieldType(FieldType.STATIC);
			} else {
				field.setLow(lowestNum);
				field.setHigh(highestNum);
				sb.append("min=" + lowestNum + ", max=" + highestNum);
			}
		} else if(FieldType.BOOLEAN == field.getFieldType()) {
			StringBuilder boolVals = new StringBuilder();
			for(String val : values.get(FieldType.BOOLEAN)) {
				boolVals.append((boolVals.length() > 0 ? "|": "") + val);
			}
			field.setBooleanType(StringUtils.isNotBlank(boolVals.toString()) ? boolVals.toString() : "Y|N");
			sb.append("type=" + field.getBooleanType());
		}
		if(sb.length() > sbLen) {
			logger.info(sb.toString());
		}
	}	

	private void checkConstantValue(HashSet<String> values, Field field) throws Exception {
		if(FieldType.EMPTY == field.getFieldType()) { 
			field.clearOptions();
		}
		if(FieldType.STATIC == field.getFieldType()) {
			return;
		}
		if(values.size() == 1) {
			Generator gen = GeneratorFactory.getGenerator(field);
			String val = gen.generate();
			field.clearOptions();
			field.setStaticValue(val);
			logger.info("Field " + field.getName() + "(" + field.getFieldType() + ") set to " + FieldType.STATIC + ", val '" + val + "', CONSTANT VALUE");
			field.setFieldType(FieldType.STATIC);
			return;
		}
	}		
	
	private String checkForDate(String input) {
		if(input.matches("(?i)\\d{1,2}/\\d{1,2}/\\d{2,4}\\s+[0-9:]+\\s(?:AM|PM)")) {
			return "MM/dd/yyyy hh:mm:ss a";
		} else if(input.matches("(?i)\\d{1,2}/\\d{1,2}/\\d{2,4}\\s+[0-9:]+$")) {
			return "MM/dd/yyyy HH:mm:ss";
		} else if(input.matches("(?i)\\d{1,2}/\\d{1,2}/\\d{2,4}$")) {
			return "MM/dd/yyyy";
		} else if(input.matches("(?i)\\d{1,2}-\\d{1,2}-\\d{2,4}\\s+[0-9:]+\\s(?:AM|PM)")) {
			return "MM-dd-yyyy hh:mm:ss a";
		} else if(input.matches("(?i)\\d{1,2}-\\d{1,2}-\\d{2,4}\\s+[0-9:]+$")) {
			return "MM-dd-yyyy HH:mm:ss";
		} else if(input.matches("(?i)\\d{1,2}-\\d{1,2}-\\d{2,4}$")) {
			return "MM-dd-yyyy";
		} else if(input.matches("(?i)\\d{4}-\\d{2}-\\d{2}\\s+\\d{2}:\\d{2}:\\d{2}\\.\\d{3}$")) {
			return "yyyy-dd-MM HH:mm:ss.SSS";
		} else if(input.matches("(?i)\\d{4}-\\d{2}-\\d{2}\\s+\\d{2}:\\d{2}:\\d{2}$")) {
			return "yyyy-MM-dd HH:mm:ss";
		} else if(input.matches("(?i)\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}$")) { //2002-09-13T00:00:00
			return "yyyy-MM-dd'T'HH:mm:ss";
		} else {
			try{
				Date.parse(input);
				return Defaults.DATE_FORMAT;
			} catch(Exception e) {
				return null;
			}
		}
	}
}
