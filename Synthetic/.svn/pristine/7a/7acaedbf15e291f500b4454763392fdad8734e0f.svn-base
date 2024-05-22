package org.nbme.dwbi.synthetic.generator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Random;
import java.util.function.Supplier;

import org.apache.commons.lang3.StringUtils;
import org.nbme.dwbi.synthetic.constants.Defaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.datafaker.Faker;
import net.datafaker.service.FakeValuesService;
import net.datafaker.service.FakerContext;
import net.datafaker.service.RandomService;

public class BaseStringGenerator {
	private SimpleDateFormat sdf = new SimpleDateFormat(Defaults.DATE_FORMAT);
	private Faker faker;
	private FakeValuesService fakeValueService = new FakeValuesService();
	private Random random;
	private RandomService rs;
	private HashSet<String> names = new HashSet<String>();
	private HashSet<String> schools = new HashSet<String>();
	private HashSet<String> addresses = new HashSet<String>();
	private HashSet<String> phones = new HashSet<String>();
	private HashSet<String> ssns = new HashSet<String>();
	private HashSet<String> emails = new HashSet<String>();
	private HashSet<String> urls = new HashSet<String>();
	private HashSet<String> files = new HashSet<String>();
	private HashSet<String> strings = new HashSet<String>();
	private HashMap<String, HashSet<String>> patternFullfillments = new HashMap<String, HashSet<String>>();
	private int minAge = Defaults.DATE_MIN_AGE;
	private int maxAge = Defaults.DATE_MAX_AGE;
	private long minDate = Defaults.DATE_MIN_UNIX;
	private long maxDate = Defaults.DATE_MAX_UNIX;
	private int minSentLen = Defaults.SENTENCE_MIN_LEN;
	private int maxSentLen = Defaults.SENTENCE_MAX_LEN;
	private Integer maxLen;
	private NumberGenerator suffixGen;
	private static final Logger logger = LoggerFactory.getLogger(BaseStringGenerator.class);
	
	private int MAX_UNIQUE_ATTEMPTS = 100;


	public BaseStringGenerator() {
		this(GeneratorFactory.getSeed());
	}
	
	public BaseStringGenerator(Integer seed) {
		try {
			ArrayList<Integer> freqs = new ArrayList<Integer>();
			ArrayList<Generator> generators = new ArrayList<Generator>();
			for (String suffix : Defaults.NAME_SUFFIXES) {
				generators.add(new StaticGenerator(suffix));
				freqs.add(1);
			}
			suffixGen = new NumberGenerator(NumberGenerator.DistributionType.FREQUENCY, generators, freqs);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("you might fail later!");
		}
		random = seed != null ? new Random(seed) : new Random();
		rs = new RandomService(random);
		faker = new Faker(fakeValueService, new FakerContext(Locale.ENGLISH, rs));
	}
	
	public enum NameType {
		FULL, FIRST, FEMALE_FIRST, MALE_FIRST, LAST
	}
	 
	public String getUniqueName(NameType type) throws Exception {
		Supplier<String> supplier = () -> {
			try {
				return getNamePart(type);
			} catch (Exception e) {
				throw new RuntimeException();
			}
		};
		return getUnique(names, supplier, "name");
	}
	
	private String getNamePart(NameType type) throws Exception {
		String retVal;
		switch(type) {
			case FIRST:
				 retVal = faker.name().firstName();
				break;
			case FEMALE_FIRST:
				retVal = fakeValueService.resolve("name.female_first_name", faker.name(), faker.getContext());
				break;
			case MALE_FIRST:
				 retVal = fakeValueService.resolve("name.male_first_name", faker.name(), faker.getContext());
				break;
			case LAST:
				 retVal = faker.name().lastName();
				break;
			default:
				 retVal = faker.name().firstName() + " " + faker.name().lastName() + " " + faker.name().lastName() + (random.nextInt(Defaults.NAME_SUFFIX_PER_X_NAMES) == 0 ? " " + suffixGen.getNextVal() : "");
				break;
		}
		return retVal;
	}

	public enum AddrType {
		FULL, STREET, CITY, COUNTRY, ZIP
	}

	public String getUniqueAddress(AddrType type) throws Exception {
		Supplier<String> supplier = () -> getAddressPart(type);
		return getUnique(addresses, supplier, "address");
	}
	
	private String getAddressPart(AddrType type) {
		String retVal;
		switch(type) {
			case STREET:
				 retVal = faker.address().streetAddress();
				break;
			case CITY:
				retVal = faker.address().city();
				break;
			case COUNTRY:
				 retVal = faker.address().country();
				break;
			case ZIP:
				 retVal = faker.address().zipCode();
				break;
			default:
				 retVal = faker.address().fullAddress();
				break;
		}
		return retVal;
	}

	public String getUniqueSchool() throws Exception {
		Supplier<String> supplier = () ->  faker.university().name();
		return getUnique(schools, supplier, "school");
	}

	public String getUniquePhone() throws Exception {
		Supplier<String> supplier = () -> faker.phoneNumber().cellPhone();
		return getUnique(phones, supplier, "phone");
	}

	public String getUniqueSSN() throws Exception {
		Supplier<String> supplier = () -> faker.idNumber().ssnValid();
		return getUnique(ssns, supplier, "SSN");
	}

	public String getUniqueEmail() throws Exception {
		Supplier<String> supplier = () -> {return faker.internet().safeEmailAddress();};
		return getUnique(emails, supplier, "email");
	}

	public String getUniqueUrl() throws Exception {
		Supplier<String> supplier = () -> faker.internet().url();
		return getUnique(urls, supplier, "url");
	}

	public String getUniqueFile() throws Exception {
		Supplier<String> supplier = () -> faker.file().fileName();
		return getUnique(files, supplier, "file");
	}
	
	public String getUniqueString(int length) throws Exception {
		Supplier<String> supplier = () -> fakeValueService.letterify(StringUtils.repeat("?", length), faker.getContext());
		return getUnique(strings, supplier, "string");
	}

	private String getUnique(HashSet<String> vals, Supplier<String> supplier, String type) throws Exception {
		String val = maxLen == null ? supplier.get() : setToLength(supplier.get(), maxLen);
		int i = 0;
		while(!vals.add(val)) {
			val = maxLen == null ? supplier.get() : setToLength(supplier.get(), maxLen);
			logger.info("****************** had nonunique " + type);
			if(i++ > MAX_UNIQUE_ATTEMPTS) {
				throw new Exception("Hit limit of " + MAX_UNIQUE_ATTEMPTS + " attempts when trying to find unique " + type);
			}
		}
		return val;
	}
	
	private String setToLength(String input, int maxLen) {
				return input.length() > maxLen ? input.substring(0, maxLen) : input;
	}
	
	private String setToLength(String input, int minLen, int maxLen) {
				return input.length() > maxLen ? 
						input.substring(0, maxLen) : 
							input.length() < minLen ? 
								input + fakeValueService.letterify(StringUtils.repeat("?", minLen - input.length()), faker.getContext()) :
									input;
	}
	
	public String getSentence() {
		int len = random.nextInt(maxSentLen + 1 - minSentLen) + minSentLen;
		return faker.lorem().maxLengthSentence(len);
	}
	
	public String getChar() {
		return fakeValueService.letterify("?", faker.getContext());
	}
	
	public String getBirthday() {
		return sdf.format(faker.date().birthday(minAge, maxAge));
	}
	
	public String getDate() {
		return sdf.format(faker.date().between(new Date(minDate), new Date(maxDate)));
	}

	public String getPatternFullfillment(String pattern) {
		String patternFullfillment = maxLen == null ? faker.regexify(pattern) : setToLength(faker.regexify(pattern), maxLen);
		HashSet<String> set = patternFullfillments.get(pattern);
		if(set == null) {
			set = new HashSet<String>();
			patternFullfillments.put(pattern, set);
		}
		while(!set.add(patternFullfillment)) {
			patternFullfillment = maxLen == null ? faker.regexify(pattern) : setToLength(faker.regexify(pattern), maxLen);
			logger.info("****************** had nonunique PatternFullfillment for '" + pattern + "'");
		}
		return patternFullfillment;
	}

	public int getMinAge() {
		return minAge;
	}

	public void setMinAge(int minAge) {
		this.minAge = minAge;
	}

	public int getMaxAge() {
		return maxAge;
	}

	public void setMaxAge(int maxAge) {
		this.maxAge = maxAge;
	}

	public void setMinDate(long minDate) {
		this.minDate = minDate;
	}

	public void setMaxDate(long maxDate) {
		this.maxDate = maxDate;
	}

	public void setDateFormat(String dateFormat) {
		this.sdf = new SimpleDateFormat(dateFormat);
	}

	public int getMinSentLen() {
		return minSentLen;
	}

	public void setMinSentLen(int minSentLen) {
		this.minSentLen = minSentLen;
	}

	public int getMaxSentLen() {
		return maxSentLen;
	}

	public void setMaxSentLen(int maxSentLen) {
		this.maxSentLen = maxSentLen;
	}

	public Integer getMaxLen() {
		return maxLen;
	}

	public void setMaxLen(Integer maxLen) {
		this.maxLen = maxLen;
	}

}
