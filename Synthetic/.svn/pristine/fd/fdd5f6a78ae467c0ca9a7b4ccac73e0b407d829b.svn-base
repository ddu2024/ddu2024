package org.nbme.dwbi.synthetic.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.parquet.avro.AvroParquetWriter;
import org.apache.parquet.hadoop.ParquetWriter;
import org.apache.parquet.hadoop.metadata.CompressionCodecName;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class ParquetUtil {
	private Schema schema;
	private JSONObject jsonObject = new JSONObject();
	private JSONArray jsonArray = new JSONArray();
	private List<GenericData.Record> records = new ArrayList<>();
	private GenericData.Record record;
	private static final Logger logger = LoggerFactory.getLogger(ParquetUtil.class);


    public ParquetUtil() throws JSONException {
		super();
		jsonObject.put("type", "record");
		jsonObject.put("name", "spark_schema_synth");
		jsonObject.put("fields", jsonArray);
	}

	public void generateParquetFile(File file) {
        Path path = new Path(file.getAbsolutePath());

        try (ParquetWriter<GenericData.Record> writer = AvroParquetWriter.<GenericData.Record>builder(path)
                .withSchema(schema)
                .withCompressionCodec(CompressionCodecName.SNAPPY)
                .withRowGroupSize(ParquetWriter.DEFAULT_BLOCK_SIZE)
                .withPageSize(ParquetWriter.DEFAULT_PAGE_SIZE)
                .withConf(new Configuration())
                .withValidation(false)
                .withDictionaryEncoding(false)
                .build()) {

            for (GenericData.Record record : records) {
                writer.write(record);
            }            
        } catch (Exception e) {
            logger.error("Error:", e);
        }
    }
    
    public void generateSchema() throws JSONException {
        Schema.Parser parser = new Schema.Parser().setValidate(true);
        schema = parser.parse(jsonObject.toString(2));
    }
    
    public void addSchemaField(String fieldName, String fieldType) throws JSONException {
    	JSONObject jOb = new JSONObject();
    	jOb.put("name", fieldName);
    	
    	JSONArray jAr = new JSONArray();
    	jAr.put("null");
    	jAr.put(fieldType);    	
    	jOb.put("type", jAr);
    	
    	jOb.put("default", null);
    	
    	jsonArray.put(jOb);
    }
    
    public void newRecord() throws Exception {
    	if(schema == null) {
    		throw new Exception("You did not generate the schema first");
    	}
        record = new GenericData.Record(schema);
        records.add(record);
    }
    
    public void addRecordField(String name, Object value) {
    	record.put(name, value);
    }

}