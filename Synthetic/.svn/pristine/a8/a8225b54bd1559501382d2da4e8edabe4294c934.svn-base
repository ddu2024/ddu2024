package org.nbme.dwbi.synthetic.generator;

public class UrlGenerator implements Generator {
	private BaseStringGenerator stringGen;

	public UrlGenerator() {
		stringGen = new BaseStringGenerator();
	}

	public UrlGenerator(Integer maxLen) {
		stringGen = new BaseStringGenerator();
		stringGen.setMaxLen(maxLen);
	}

	@Override
	public String generate() throws Exception {
		return stringGen.getUniqueUrl();
	}

}
