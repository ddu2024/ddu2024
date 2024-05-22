package org.nbme.dwbi.synthetic.generator;

public class SchoolGenerator implements Generator {
	private BaseStringGenerator stringGen;

	public SchoolGenerator() {
		stringGen = new BaseStringGenerator();
	}
	public SchoolGenerator(Integer maxLen) {
		stringGen = new BaseStringGenerator();
		stringGen.setMaxLen(maxLen);
	}
	
	@Override
	public String generate() throws Exception {
		return stringGen.getUniqueSchool();
	}

}
