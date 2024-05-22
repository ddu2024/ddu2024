package org.nbme.dwbi.synthetic.generator;

public class DoubleGenerator implements Generator {

	private NumberGenerator numGen;

	public DoubleGenerator() throws Exception {
		super();
		numGen = new NumberGenerator(NumberGenerator.DistributionType.DOUBLE, 0.0, 10.0);
	}

	public DoubleGenerator(double low, double high, Integer seed) throws Exception {
		super();
		numGen = new NumberGenerator(NumberGenerator.DistributionType.DOUBLE, low, high, seed);
	}

	@Override
	public String generate() throws Exception {
		return numGen.getNextVal();
	}

}
