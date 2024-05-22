package org.nbme.dwbi.synthetic.generator;

public class EmailGenerator implements Generator {
		private BaseStringGenerator stringGen;

		public EmailGenerator() {
			stringGen = new BaseStringGenerator();
		}

		public EmailGenerator(Integer maxLen) {
			stringGen = new BaseStringGenerator();
			stringGen.setMaxLen(maxLen);
		}

		@Override
		public String generate() throws Exception {
			return stringGen.getUniqueEmail();
		}

}
