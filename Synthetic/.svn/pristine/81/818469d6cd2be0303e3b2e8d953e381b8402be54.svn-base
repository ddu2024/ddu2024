package org.nbme.dwbi.synthetic.generator;

public class IntGenerator implements Generator {
	private NumberGenerator numGen;

	public IntGenerator() throws Exception {
		super();
		numGen = new NumberGenerator(NumberGenerator.DistributionType.INTEGER, 0, 10);
		numGen.setDecimalFormat("#");
	}

	public IntGenerator(int low, int high) throws Exception {
		super();
		numGen = new NumberGenerator(NumberGenerator.DistributionType.INTEGER, low, high);
		numGen.setDecimalFormat("#");
	}

	@Override
	public String generate() throws Exception {
		return numGen.getNextVal();
	}
}
