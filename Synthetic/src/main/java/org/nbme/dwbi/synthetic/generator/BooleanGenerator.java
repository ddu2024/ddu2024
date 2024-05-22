package org.nbme.dwbi.synthetic.generator;

import java.util.ArrayList;

import org.nbme.dwbi.synthetic.constants.Defaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BooleanGenerator implements Generator {

	private NumberGenerator numGen;
	private static final Logger logger = LoggerFactory.getLogger(BooleanGenerator.class);

	public BooleanGenerator() {
		this(Defaults.BOOLEAN_TYPE);
	}

	public BooleanGenerator(String booleanType) {
		try {
			String[] splitUp = booleanType.split("\\|");
			ArrayList<Integer> freqs = new ArrayList<Integer>();
			ArrayList<Generator> generators = new ArrayList<Generator>();
			for (String split : splitUp) {
				freqs.add(1);
				generators.add(new StaticGenerator(split));
			}
			numGen = new NumberGenerator(NumberGenerator.DistributionType.FREQUENCY, generators, freqs);
		} catch (Exception e) {
			logger.error("you might fail later!");
		}
	}

	@Override
	public String generate() throws Exception {
		return numGen.getNextVal();
	}
}
