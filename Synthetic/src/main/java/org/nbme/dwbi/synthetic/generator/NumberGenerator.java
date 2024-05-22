package org.nbme.dwbi.synthetic.generator;

import java.text.DecimalFormat;
import java.util.ArrayList;

import org.apache.commons.math3.distribution.BinomialDistribution;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.UniformIntegerDistribution;
import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NumberGenerator {

	private NormalDistribution normalDist;
	private UniformRealDistribution uniDist;
	private UniformIntegerDistribution uniIntDist;
	private BinomialDistribution binomialDist;
	private int[] prefix;
	private ArrayList<Generator> arr;
	private ArrayList<Integer> freq;
	private DecimalFormat df = new DecimalFormat("#.00");
	
	DistributionType distType;
	private static final Logger logger = LoggerFactory.getLogger(NumberGenerator.class);
	
	public enum DistributionType {
		NORMAL,
		BINOMIAL,
		INTEGER,
		DOUBLE,
		FREQUENCY
		}
	
	public NumberGenerator(DistributionType distType, double arg1, double arg2) throws Exception {
		this(distType, arg1, arg2, new Well19937c(GeneratorFactory.getSeed()));
	}

	public NumberGenerator(DistributionType distType, double arg1, double arg2, Integer seed) throws Exception {
		this(distType, arg1, arg2, new Well19937c(seed));
	}
	
	public NumberGenerator(DistributionType distType, double arg1, double arg2, RandomGenerator random) throws Exception {
		this.distType = distType;
		switch (distType) {
			case NORMAL:
				normalDist = new NormalDistribution(random, arg1, arg2);
				break;
			case DOUBLE:
				uniDist = new UniformRealDistribution(random, arg1, arg2);
				break;
			default:
				throw new Exception("Wrong arguments types for selected distribution");
		}
	}
	
	public NumberGenerator(DistributionType distType, int arg1, double arg2) throws Exception {
		this(distType, arg1, arg2, new Well19937c(GeneratorFactory.getSeed()));
	}

	public NumberGenerator(DistributionType distType, int arg1, double arg2, Integer seed) throws Exception {
		this(distType, arg1, arg2, new Well19937c(seed));
	}
	
	public NumberGenerator(DistributionType distType, int arg1, double arg2, RandomGenerator random) throws Exception {
		this.distType = distType;
		switch (distType) {
			case BINOMIAL:
				binomialDist = new BinomialDistribution(random, arg1, arg2);
				break;
			default:
				throw new Exception("Wrong arguments types for selected distribution");
		}
	}
	
	public NumberGenerator(DistributionType distType, int arg1, int arg2) throws Exception {
		this(distType, arg1, arg2, new Well19937c(GeneratorFactory.getSeed()));
	}

	public NumberGenerator(DistributionType distType, int arg1, int arg2, Integer seed) throws Exception {
		this(distType, arg1, arg2, new Well19937c(seed));
	}
	
	public NumberGenerator(DistributionType distType, int arg1, int arg2, RandomGenerator random) throws Exception {
		this.distType = distType;
		switch (distType) {
			case INTEGER:
				uniIntDist = new UniformIntegerDistribution(random, arg1, arg2);
				break;
			default:
				throw new Exception("Wrong arguments types for selected distribution");
		}
	}
	
	public NumberGenerator(DistributionType distType, ArrayList<Generator> arg1, ArrayList<Integer> arg2) throws Exception {
		this(distType, arg1, arg2, new Well19937c(GeneratorFactory.getSeed()));
	}

	public NumberGenerator(DistributionType distType, ArrayList<Generator> arg1, ArrayList<Integer> arg2, Integer seed) throws Exception {
		this(distType, arg1, arg2, new Well19937c(seed));
	}
	
	public NumberGenerator(DistributionType distType, ArrayList<Generator> arg1, ArrayList<Integer> arg2, RandomGenerator random) throws Exception {
		this.distType = distType;
		switch (distType) {
			case FREQUENCY:
				if(arg1.size() != arg2.size() || arg1.size() < 1) {
					throw new Exception("Arrays must have same length and contain something");
				}
				arr = arg1;
				freq = arg2;
				generatePrefix(random);
				break;
			default:
				throw new Exception("Wrong arguments types for selected distribution");
		}
	}	
	
	public void setDecimalFormat(String format) {
		df = new DecimalFormat(format);
	}
	
	public String getNextVal() throws Exception {

		switch (distType) {
			case NORMAL:
				return df.format(normalDist.sample());
			case DOUBLE:
				return df.format(uniDist.sample());
			case FREQUENCY:
				return sample();
			case INTEGER:
				return df.format(uniIntDist.sample());
			case BINOMIAL:
				return df.format(binomialDist.sample());
			default:
				return "-1";
		}
	}
	

	//FREQUENCY
	//https://www.geeksforgeeks.org/random-number-generator-in-arbitrary-probability-distribution-fashion/
	private int findCeil(int[] arr, int r, int l, int h) {
		int mid;
		while (l < h) {
			mid = (l + h) / 2;
			if (r > arr[mid]) {
				l = mid + 1;
			} else {
				h = mid;
			}
		}
		return (arr[l] >= r) ? l : -1;
	}

	private String sample() throws Exception {
		int r = uniIntDist.sample();

		int indexc = findCeil(prefix, r, 0, freq.size() - 1);
		return arr.get(indexc).generate();
	}

	private void generatePrefix(RandomGenerator random) {
		prefix = new int[freq.size()];
		prefix[0] = freq.get(0);
		for (int i = 1; i < freq.size(); ++i) {
			prefix[i] = prefix[i - 1] + freq.get(i);
		}
		uniIntDist = new UniformIntegerDistribution(random, 1, prefix[freq.size() - 1]);
	}
}
