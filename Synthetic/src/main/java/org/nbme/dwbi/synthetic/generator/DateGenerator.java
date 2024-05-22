package org.nbme.dwbi.synthetic.generator;

import org.apache.commons.lang3.StringUtils;
import org.nbme.dwbi.synthetic.model.FieldType;

public class DateGenerator implements Generator {
	private BaseStringGenerator stringGen;
	private FieldType type = FieldType.B_DATE;

	public DateGenerator() {
		stringGen = new BaseStringGenerator();
	}

	public DateGenerator(String dateFormat, long minDate, long maxDate) {
		this.type = FieldType.DATE;

		stringGen = new BaseStringGenerator();
		if (StringUtils.isNotBlank(dateFormat)) {
			stringGen.setDateFormat(dateFormat);
		}
		if (minDate != -1L) {
			stringGen.setMinDate(minDate);
		}
		if (maxDate != -1L) {
			stringGen.setMaxDate(maxDate);
		}
	}

	public DateGenerator(String dateFormat, int minAge, int maxAge) {
		this.type = FieldType.B_DATE;

		stringGen = new BaseStringGenerator();
		if (StringUtils.isNotBlank(dateFormat)) {
			stringGen.setDateFormat(dateFormat);
		}
		if (minAge != -1) {
			stringGen.setMinAge(minAge);
		}
		if (maxAge != -1) {
			stringGen.setMaxAge(maxAge);
		}
	}

	@Override
	public String generate() {
		return FieldType.B_DATE == type ? stringGen.getBirthday() : stringGen.getDate();
	}
}
