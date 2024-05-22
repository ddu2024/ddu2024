package org.nbme.dwbi.synthetic.generator;

import org.nbme.dwbi.synthetic.generator.BaseStringGenerator.AddrType;

public class AddressGenerator implements Generator {

	private BaseStringGenerator stringGen;
	AddrType type = AddrType.FULL;

	public AddressGenerator() {
		stringGen = new BaseStringGenerator();
	}

	public AddressGenerator(Integer maxLen) {
		stringGen = new BaseStringGenerator();
		stringGen.setMaxLen(maxLen);
	}

	public AddressGenerator(AddrType type) {
		this.type = type;
		stringGen = new BaseStringGenerator();
	}

	public AddressGenerator(AddrType type, Integer maxLen) {
		this.type = type;
		stringGen = new BaseStringGenerator();
		stringGen.setMaxLen(maxLen);
	}

	@Override
	public String generate() throws Exception {
		return stringGen.getUniqueAddress(type);
	}
}
