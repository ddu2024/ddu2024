package org.nbme.dwbi.synthetic.generator;

public class SSNGenerator  implements Generator {

	private BaseStringGenerator stringGen;

	public SSNGenerator() {
		stringGen = new BaseStringGenerator();
	}

	public SSNGenerator(Integer maxLen) {
		stringGen = new BaseStringGenerator();
		stringGen.setMaxLen(maxLen);
	}

	@Override
	public String generate() throws Exception {
		return stringGen.getUniqueSSN();
	}


}
