package org.nbme.dwbi.synthetic.model;

import java.util.ArrayList;

public class Frequency {
	private ArrayList<Integer> counts = new ArrayList<Integer>();
	private ArrayList<Field> fields = new ArrayList<Field>();

	public ArrayList<Integer> getCounts() {
		return counts;
	}

	public void setCounts(ArrayList<Integer> counts) {
		this.counts = counts;
	}

	public void addCount(Integer count) {
		counts.add(count);
	}

	public ArrayList<Field> getFields() {
		return fields;
	}

	public void setFields(ArrayList<Field> fields) {
		this.fields = fields;
	}

	public void addField(Field field) {
		fields.add(field);
	}
}
