package org.nbme.dwbi.synthetic.generator;

import java.util.ArrayList;

import org.nbme.dwbi.synthetic.constants.Defaults;
import org.nbme.dwbi.synthetic.generator.BaseStringGenerator.AddrType;
import org.nbme.dwbi.synthetic.generator.BaseStringGenerator.NameType;
import org.nbme.dwbi.synthetic.model.Field;

public class GeneratorFactory {
	private static Integer seed = (int)System.currentTimeMillis();

	public static Generator getGenerator(Field field) throws Exception {
		switch (field.getFieldType()) {
			case INT:
				int low = field.getLow() != null ? field.getLow().intValue() : Defaults.INT_MIN;
				int high = field.getHigh() != null ? field.getHigh().intValue() : Defaults.INT_MAX;
				return new IntGenerator(low, high);
			case INC_INT:
				int start = field.getStart() != null ? field.getStart() : Defaults.INC_INT_START;
				int increment = field.getIncrement() != null ? field.getIncrement() : Defaults.INC_INT_INCREMENT;
				return new IncIntGenerator(start, increment);
			case DOUBLE:
				double lowD = field.getLow() != null ? field.getLow() : Defaults.DOUBLE_MIN;
				double highD = field.getHigh() != null ? field.getHigh() : Defaults.DOUBLE_MAX;
				return new DoubleGenerator(lowD, highD, seed);
			case STR:
				if(field.getMinLen() != null && field.getMaxLen() != null) {
					return new RegexGenerator("[A-Za-z]{" + field.getMinLen() + "," + field.getMaxLen() + "}");
				}
				return new StringGenerator(field.getMinLen() != null ? field.getMinLen() : field.getMaxLen() != null ? field.getMaxLen() : Defaults.STR_LEN);
			case CHAR:
				return new StringGenerator(1);
			case SENTENCE:
				return new SentenceGenerator(field.getMinLen(), field.getMaxLen());
			case BOOLEAN:
				return new BooleanGenerator(field.getBooleanType() != null ? field.getBooleanType() : Defaults.BOOLEAN_TYPE);
			case B_DATE:
				String format = field.getDateFormat() != null ? field.getDateFormat() : "";
				Integer minAge = field.getMinAge() != null ? field.getMinAge() : -1;
				Integer maxAge = field.getMaxAge() != null ? field.getMaxAge() : -1;
				return new DateGenerator(format, minAge, maxAge);
			case DATE:
				String format2 = field.getDateFormat() != null ? field.getDateFormat() : "";
				Long minDate = field.getMinDate() != null ? field.getMinDate() : -1L;
				Long maxDate = field.getMaxDate() != null ? field.getMaxDate() : -1L;
				return new DateGenerator(format2, minDate, maxDate);
			case REGEX:
				String regex = field.getRegex() != null ? field.getRegex() : "-1";
				return new RegexGenerator(regex, field.getMaxLen());
			case FULL_NAME:
				return new NameGenerator(field.getMaxLen());
			case FIRST_NAME:
				return new NameGenerator(NameType.FIRST, field.getMaxLen());
			case LAST_NAME:
				return new NameGenerator(NameType.LAST, field.getMaxLen());
			case MIDDLE_NAME:
				return new NameGenerator(NameType.LAST, field.getMaxLen()); //reuse last names for middle
			case SCHOOL:
				return new SchoolGenerator(field.getMaxLen());
			case ADDRESS:
				return new AddressGenerator(AddrType.FULL, field.getMaxLen());
			case STREET:
				return new AddressGenerator(AddrType.STREET, field.getMaxLen());
			case CITY:
				return new AddressGenerator(AddrType.CITY, field.getMaxLen());
			case COUNTRY:
				return new AddressGenerator(AddrType.COUNTRY, field.getMaxLen());
			case ZIP:
				return new AddressGenerator(AddrType.ZIP, field.getMaxLen());
			case PHONE:
				return new PhoneGenerator(field.getMaxLen());
			case SSN:
				return new SSNGenerator(field.getMaxLen());
			case EMAIL:
				return new EmailGenerator(field.getMaxLen());
			case URL:
				return new UrlGenerator(field.getMaxLen());
			case FILE:
				return new FileGenerator(field.getMaxLen());
			case FREQ:
				ArrayList<Generator> generators = new ArrayList<Generator>();
				for(Field fieldX : field.getFrequency().getFields()) {
					generators.add(GeneratorFactory.getGenerator(fieldX));
				}
				return new FreqGenerator(generators, field.getFrequency().getCounts());
			case ARRAY:
			case SUBSET:
				return new SubfieldGenerator();
			case STATIC:
				String staticVal = field.getStaticValue() != null ? field.getStaticValue() : Defaults.STATIC_VALUE;
				return new StaticGenerator(staticVal);
			case NULL:
			case EMPTY:
				return new StaticGenerator("");
			default:
				return null;
		}
	}

	public static Integer getSeed() {
		return seed;
	}

	public static void setSeed(Integer seed) {
		GeneratorFactory.seed = seed;
	}

	public static void incrementSeed(int increment) {
		if(seed != null) {
			seed += increment;
		}
	}
}
