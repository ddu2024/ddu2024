package org.nbme.dwbi.synthetic.generator;

public class RegexGenerator implements Generator {
	private BaseStringGenerator stringGen;
	private String regex;

	public RegexGenerator(String regex) {
		this.regex = regex;
		stringGen = new BaseStringGenerator();
	}
	
	public RegexGenerator(String regex, Integer maxLen) {
		this.regex = regex;
		stringGen = new BaseStringGenerator();
		stringGen.setMaxLen(maxLen);
	}

	@Override
	public String generate() {
		return stringGen.getPatternFullfillment(regex);
	}

}
