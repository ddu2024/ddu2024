package org.nbme.dwbi.synthetic;

import org.nbme.dwbi.synthetic.engine.SyntheticEngine;
import org.nbme.dwbi.synthetic.parser.Parser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Synthetic implements CommandLineRunner {

	private static final Logger logger = LoggerFactory.getLogger(Synthetic.class);


	public Synthetic() {
	}

	@Override
	public void run(String... args) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append("Synthetic started with arguments: ");
		for(String arg : args) {
			sb.append(arg + " ");
		}
		logger.info(sb.toString());
		String message = "Format:\n\tTo parse: -p source outputDir\n\tTo make data: -g metaDataFile paramFile outputDir";

		if(args.length == 3 && "-p".equals(args[0])) {
			new Parser(args[1], args[2]).parse();
		} else if(args.length == 4 && "-g".equals(args[0])) {
			new SyntheticEngine(args[1], args[2], args[3]).makeData();
		} else {
			logger.error(message);
		}

	}
}
