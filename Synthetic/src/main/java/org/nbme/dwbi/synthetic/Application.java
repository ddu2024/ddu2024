package org.nbme.dwbi.synthetic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@PropertySource(
//        ignoreResourceNotFound=false,
//        value = {
//                "classpath:config.properties",
//                "classpath:config-${e}.properties"
//        })
public class Application {
	private static final Logger logger = LoggerFactory.getLogger(Application.class);

	public static void main(String[] args) {
		try {
//			if (System.getProperty("d") != null) {
//				System.setProperty("d", System.getProperty("d").toLowerCase());
//				String e = null;
//				switch (System.getProperty("d")) {
//					case "tstb":
//					case "tsta":
//						e = "test";
//						break;
//					case "tsuat":
//						e = "uat";
//						break;
//					case "prod":
//						e = "prod";
//						break;
//					default:
//						e = "dev";
//				}
//				System.setProperty("e", e);
//			}
			SpringApplication.run(Application.class, args);
		} catch (Exception e) {
			logger.error("Synthetic Process could not start: " + e.getMessage());
		}
	}
}
