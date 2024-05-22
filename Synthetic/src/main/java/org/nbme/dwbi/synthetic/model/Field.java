package org.nbme.dwbi.synthetic.model;

import java.util.ArrayList;
import java.util.Objects;

public class Field {
	private String name;
	private FieldType fieldType;
//	private String id;
	private String reference;

	//fieldParameters
	private Integer start;
	private Integer increment;
	private Double low;
	private Double high;
	private String regex;
	private String staticValue;
	private Integer minLen;
	private Integer maxLen;
	
	private String dateFormat;
	private Integer minAge;
	private Integer maxAge;
	private Long minDate;
	private Long maxDate;
	private Frequency frequency;
	private ArrayList<Field> subFields = new ArrayList<Field>();
	private Integer minSubFields;
	private Integer maxSubFields;
	private String booleanType;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public FieldType getFieldType() {
		return fieldType;
	}

	public void setFieldType(FieldType fieldType) {
		this.fieldType = fieldType;
	}

//	public String getId() {
//		return id;
//	}
//
//	public void setId(String id) {
//		this.id = id;
//	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

//	public Boolean isReferenced() {
//		return isReferenced;
//	}
//
//	public void setReferenced(Boolean isReferenced) {
//		this.isReferenced = isReferenced;
//	}

	public Integer getStart() {
		return start;
	}

	public void setStart(Integer start) {
		this.start = start;
	}

	public Integer getIncrement() {
		return increment;
	}

	public void setIncrement(Integer increment) {
		this.increment = increment;
	}

	public Double getLow() {
		return low;
	}

	public void setLow(Double low) {
		this.low = low;
	}

	public Double getHigh() {
		return high;
	}

	public void setHigh(Double high) {
		this.high = high;
	}

	public String getRegex() {
		return regex;
	}

	public void setRegex(String regex) {
		this.regex = regex;
	}

	public String getStaticValue() {
		return staticValue;
	}

	public void setStaticValue(String staticValue) {
		this.staticValue = staticValue;
	}

	public Integer getMinLen() {
		return minLen;
	}

	public void setMinLen(Integer minLen) {
		this.minLen = minLen;
	}

	public Integer getMaxLen() {
		return maxLen;
	}

	public void setMaxLen(Integer maxLen) {
		this.maxLen = maxLen;
	}

	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	public Integer getMinAge() {
		return minAge;
	}

	public void setMinAge(Integer minAge) {
		this.minAge = minAge;
	}

	public Integer getMaxAge() {
		return maxAge;
	}

	public void setMaxAge(Integer maxAge) {
		this.maxAge = maxAge;
	}

	public Long getMinDate() {
		return minDate;
	}

	public void setMinDate(Long minDate) {
		this.minDate = minDate;
	}

	public Long getMaxDate() {
		return maxDate;
	}

	public void setMaxDate(Long maxDate) {
		this.maxDate = maxDate;
	}

	public Frequency getFrequency() {
		return frequency;
	}

	public void setFrequency(Frequency frequency) {
		this.frequency = frequency;
	}

	public ArrayList<Field> getSubFields() {
		return subFields;
	}

	public void setSubFields(ArrayList<Field> subFields) {
		this.subFields = subFields;
	}

	public Integer getMinSubFields() {
		return minSubFields;
	}

	public void setMinSubFields(Integer minSubFields) {
		this.minSubFields = minSubFields;
	}

	public Integer getMaxSubFields() {
		return maxSubFields;
	}

	public void setMaxSubFields(Integer maxSubFields) {
		this.maxSubFields = maxSubFields;
	}

	public String getBooleanType() {
		return booleanType;
	}

	public void setBooleanType(String booleanType) {
		this.booleanType = booleanType;
	}

	public void clearOptions() {
		start = null;
		increment = null;
		low = null;
		high = null;
		regex = null;
		staticValue = null;
		minLen = null;
		maxLen = null;
		
		dateFormat = null;
		minAge = null;
		maxAge = null;
		minDate = null;
		maxDate = null;
		frequency = null;

		minSubFields = null;
		maxSubFields = null;
		booleanType = null;
	}
	
	@Override
	public String toString() {
		return "Field [name=" + name + ", fieldType=" + fieldType + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(fieldType,name, reference);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Field other = (Field) obj;
		return fieldType == other.fieldType && Objects.equals(name, other.name)
				&& Objects.equals(reference, other.reference);
	}
}
