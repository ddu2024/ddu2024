package org.nbme.dwbi.synthetic.parser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.introspector.Property;
import org.yaml.snakeyaml.nodes.CollectionNode;
import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.NodeTuple;
import org.yaml.snakeyaml.nodes.SequenceNode;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Representer;

public class YmlParser {
	private static final Logger logger = LoggerFactory.getLogger(YmlParser.class);
	
    static void iterateObj(Object obj, String indent) {
    	if(obj instanceof LinkedHashMap) {
            Iterator<String> keys = ((LinkedHashMap)obj).keySet().iterator();
            while(keys.hasNext()) {
            	String key = keys.next();
            	Object newObj = ((LinkedHashMap)obj).get(key);
            	logger.info(indent + key);
            	iterateObj(newObj, indent + "    ");
            }
    	} else if(obj instanceof ArrayList) {
        	logger.info(indent + "[");
            for (int i = 0; i < ((ArrayList)obj).size(); i++) {
            	iterateObj(((ArrayList)obj).get(i), indent);
          }
        	logger.info(indent + "]");
    	}
    	else {
            logger.info(indent + obj.getClass().getSimpleName());
    	}
    }
    
    public static Yaml getYamlWithOptions() {
        DumperOptions options = new DumperOptions();
        options.setIndent(2);
        options.setPrettyFlow(true);
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        return new Yaml(representer, options);
    }    
	

	//blocks writing of NULL values to the output
	@SuppressWarnings("deprecation")
	private static Representer representer = new Representer() {
		@Override
		protected NodeTuple representJavaBeanProperty(Object javaBean, Property property, Object propertyValue, Tag customTag) {
			NodeTuple tuple = super.representJavaBeanProperty(javaBean, property, propertyValue, customTag);
			Node valueNode = tuple.getValueNode();
			if (Tag.NULL.equals(valueNode.getTag())) {
				return null;// skip 'null' values
			}
			if (valueNode instanceof CollectionNode) {
				if (Tag.SEQ.equals(valueNode.getTag())) {
					SequenceNode seq = (SequenceNode) valueNode;
					if (seq.getValue().isEmpty()) {
						return null;// skip empty lists
					}
				}
				if (Tag.MAP.equals(valueNode.getTag())) {
					MappingNode seq = (MappingNode) valueNode;
					if (seq.getValue().isEmpty()) {
						return null;// skip empty maps
					}
				}
			}
			return tuple;
		}
	};
}
