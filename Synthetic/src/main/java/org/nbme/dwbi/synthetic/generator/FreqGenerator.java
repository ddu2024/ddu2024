package org.nbme.dwbi.synthetic.generator;

import java.util.ArrayList;

public class FreqGenerator implements Generator {

	private NumberGenerator numGen;

	public FreqGenerator(ArrayList<Generator> arg1, ArrayList<Integer> arg2) throws Exception {
		super();
		numGen = new NumberGenerator(NumberGenerator.DistributionType.FREQUENCY, arg1, arg2);
	}

	@Override
	public String generate() throws Exception {
		return numGen.getNextVal();
	}

}
