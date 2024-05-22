package org.nbme.dwbi.synthetic.generator;

public class StringGenerator implements Generator {
	private BaseStringGenerator stringGen;
	int length = 10;

	public StringGenerator() {
		stringGen = new BaseStringGenerator();
	}
	
	public StringGenerator(int length) {
		super();
		this.length = length;
		stringGen = new BaseStringGenerator();
	}


	@Override
	public String generate() throws Exception {
		return length == 1 ? stringGen.getChar() : stringGen.getUniqueString(length);
	}

}
