package org.nbme.dwbi.synthetic.generator;

import org.nbme.dwbi.synthetic.generator.BaseStringGenerator.NameType;

public class NameGenerator implements Generator {
	private BaseStringGenerator stringGen;
	NameType type = NameType.FULL;

	public NameGenerator() {
		stringGen = new BaseStringGenerator();
	}

	public NameGenerator(Integer maxLen) {
		stringGen = new BaseStringGenerator();
		stringGen.setMaxLen(maxLen);
	}

	public NameGenerator(NameType type) {
		this.type = type;
		stringGen = new BaseStringGenerator();
	}

	public NameGenerator(NameType type, Integer maxLen) {
		this.type = type;
		stringGen = new BaseStringGenerator();
		stringGen.setMaxLen(maxLen);
	}

	@Override
	public String generate() throws Exception {
		return stringGen.getUniqueName(type);
	}

}
