package org.nbme.dwbi.synthetic.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@EnableAutoConfiguration()
@Import({ SpringRepositoryConfig.class })
public class SpringConfig {
	
}
