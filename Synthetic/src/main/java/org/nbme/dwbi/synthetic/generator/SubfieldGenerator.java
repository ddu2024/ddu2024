package org.nbme.dwbi.synthetic.generator;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.nbme.dwbi.synthetic.constants.Defaults;
import org.nbme.dwbi.synthetic.model.Field;
import org.nbme.dwbi.synthetic.model.FieldType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SubfieldGenerator  implements Generator {
	private Field field;
	private LinkedList<String> sources = new LinkedList<String>();
	private LinkedList<String> references = new LinkedList<String>();
	private HashMap<String, ArrayList<String>> sourceData;
	private Integer minRows;
	private Integer maxRows;
	private Random random = new Random(GeneratorFactory.getSeed());
	private String[] header;

	private LinkedList<Generator> generators = new LinkedList<Generator>();
	private static final Logger logger = LoggerFactory.getLogger(SubfieldGenerator.class);

	public SubfieldGenerator() {
		super();
	}

	public SubfieldGenerator(Field field, String trackingName, HashSet<String> referencedFields, LinkedList<String> sources, LinkedList<String> references,
			HashMap<String, ArrayList<String>> sourceData, Integer minRows, Integer maxRows) throws Exception {
		super();
		init(field, trackingName, referencedFields, sources, references, sourceData, minRows, maxRows);
	}
	
	public void init(Field fieldIn, String trackingName, HashSet<String> referencedFields, LinkedList<String> sourcesIn, LinkedList<String> references, HashMap<String, ArrayList<String>> sourceData, Integer minRows, Integer maxRows) throws Exception {
		this.field = fieldIn;
		this.references = references;
		this.sourceData = sourceData;
		this.minRows = minRows != null ? minRows : 1;
		this.maxRows = maxRows != null ? maxRows : 1;

		header = new String[field.getSubFields().size()];
		int j = 0;
		for(Field fieldX : field.getSubFields()) {
			header[j++] = fieldX.getName();
			if(referencedFields.contains(trackingName + "." + fieldX.getName())) {
				sources.add(trackingName + "." + fieldX.getName());
				sourceData.put(trackingName + "." + fieldX.getName(), new ArrayList<String>());
			} else {
				sources.add("");
			}
			if(fieldX.getReference() != null) {
				references.add(fieldX.getReference());
			} else {
				references.add("");
			}

			Generator gen = GeneratorFactory.getGenerator(fieldX);
			if(gen instanceof SubfieldGenerator) {
				((SubfieldGenerator)gen).init(fieldX, trackingName + "." + fieldX.getName(), referencedFields, sources, references, sourceData, fieldX.getMinSubFields(), fieldX.getMaxSubFields());
			}
			generators.add(gen);
			GeneratorFactory.incrementSeed(1);
		}
	}
 
	@Override
	public String generate() throws Exception {
		int numRows = random.nextInt(maxRows + 1 - minRows)  + minRows;
		Object[][] rows = new Object[numRows][];
		for (int i = 0; i < numRows; i++) {
			Iterator<String> sourceIter = sources.iterator();
			Iterator<String> referenceIter = references.iterator();
			Object[] row = new Object[generators.size()];
			int k = 0;
			for (Generator generator : generators) {
				String source = sourceIter.next();
				String reference = referenceIter.next();
				String val= null;
				try {
					val = !"".equals(reference) ? sourceData.get(reference).get(i % sourceData.get(reference).size()): generator.generate();
				} catch (Exception e) {
					throw new Exception("Could not find source data for reference: " + reference +", k: " + k);
				}
				row[k++] = val;
				if(!"".equals(source)) {
					ArrayList<String> vals = sourceData.get(source);
					vals.add(val);
				}
			}
			rows[i] = row;
		}
		
		StringWriter sw = new StringWriter();
	    CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
	        .setHeader(header)
	        .build();
	    
	    try (final CSVPrinter printer = new CSVPrinter(sw, csvFormat)) {
	        for(Object[] row : rows) {
	        	printer.printRecord(row);
	        }
	    }
		
		return Defaults.JSON_SUB_ST + (field.getFieldType() == FieldType.ARRAY ? Defaults.JSON_SUB_OPT1 : Defaults.JSON_SUB_OPT2) + sw.toString() + Defaults.JSON_SUB_END;
	}

}
