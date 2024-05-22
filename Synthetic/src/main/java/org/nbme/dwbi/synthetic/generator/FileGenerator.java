package org.nbme.dwbi.synthetic.generator;

public class FileGenerator implements Generator {
	private BaseStringGenerator stringGen;

	public FileGenerator() {
		stringGen = new BaseStringGenerator();
	}

	public FileGenerator(Integer maxLen) {
		stringGen = new BaseStringGenerator();
		stringGen.getMaxLen();
	}

	@Override
	public String generate() throws Exception {
		return stringGen.getUniqueFile();
	}

}
