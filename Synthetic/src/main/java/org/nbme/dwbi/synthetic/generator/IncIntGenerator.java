package org.nbme.dwbi.synthetic.generator;

import org.nbme.dwbi.synthetic.constants.Defaults;

public class IncIntGenerator implements Generator {
	private int start = Defaults.INC_INT_START;
	private int increment = Defaults.INC_INT_INCREMENT;

	public IncIntGenerator() throws Exception {
		super();
	}
	
	public IncIntGenerator(int start, int increment) throws Exception {
		super();
		this.start = start - increment;
		this.increment = increment;
	}

	@Override
	public String generate() {
		start += increment;
		return "" + start;
	}
}
