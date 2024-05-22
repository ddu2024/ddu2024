package org.nbme.dwbi.synthetic.generator;

import org.nbme.dwbi.synthetic.constants.Defaults;

public class SentenceGenerator implements Generator {

	private BaseStringGenerator stringGen;

	public SentenceGenerator(Integer minSentLen, Integer maxSentLen) {
		stringGen = new BaseStringGenerator();
		stringGen.setMinSentLen(minSentLen != null ? minSentLen : Defaults.SENTENCE_MIN_LEN);
		stringGen.setMaxSentLen(maxSentLen != null ? maxSentLen : Defaults.SENTENCE_MAX_LEN);
	}

	@Override
	public String generate() throws Exception {
		return stringGen.getSentence();
	}
}
