package org.nbme.dwbi.synthetic.generator;

public class PhoneGenerator  implements Generator {

	private BaseStringGenerator stringGen;

	public PhoneGenerator() {
		stringGen = new BaseStringGenerator();
	}

	public PhoneGenerator(Integer maxLen) {
		stringGen = new BaseStringGenerator();
		stringGen.setMaxLen(maxLen);
	}

	@Override
	public String generate() throws Exception {
		return stringGen.getUniquePhone();
	}

}
