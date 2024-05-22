package org.nbme.dwbi.synthetic.util;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericRecord;
import org.apache.hadoop.fs.Path;
import org.apache.parquet.avro.AvroParquetReader;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.nbme.dwbi.synthetic.engine.Data;
import org.springframework.util.FileSystemUtils;

public class ParquetUtilTest {
	private static String outDir = "outputParquet/";
	private static Data data;
	
	@BeforeClass
	public static void setUp() throws IOException {
		// in case these exist from a prior run
		delete();
		
		new File(outDir).mkdir();
		
		data = new Data();
		data.setHeader(new String[] {"col1","col2"});
		String[][] rows = new String[5][];
		rows[0] = new String[] {"a","1"};
		rows[1] = new String[] {"bc","2"};
		rows[2] = new String[] {"def","3"};
		rows[3] = new String[] {"g","45"};
		rows[4] = new String[] {"hij","678"};
		data.setRows(rows);
	}

	@Test
	public void testParquet() throws Exception {
		ParquetUtil pu = new ParquetUtil();

		for (String head : data.getHeader()) {
			pu.addSchemaField(head, "string");
		}
		pu.generateSchema();

		for (Object[] row : data.getRows()) {
			pu.newRecord();
			for (int i = 0; i < row.length; i++) {
				pu.addRecordField(data.getHeader()[i], row[i]);
			}
		}
		File file = new File(outDir, "parquetTest");

		pu.generateParquetFile(file);
		
		assertTrue(file.exists() && file.length() > 100);
		
        Path path = new Path(file.getAbsolutePath());

        AvroParquetReader<GenericRecord> reader = new AvroParquetReader<GenericRecord>(path);
        GenericRecord record;
        List<GenericRecord> allRecords = new ArrayList<GenericRecord>();
        Schema schema = null;
        while((record = reader.read()) != null) {
              //add once
              allRecords.add(record);
              if(schema == null) {
                 schema = record.getSchema();
              }
        }
        reader.close();
        
        assertTrue(allRecords.size() == 5);
		assertTrue(schema.getFields().size() == 2);
	}
	
	@AfterClass
	public static void tearDown() {
		delete();
	}
	
	private static void delete() {
		FileSystemUtils.deleteRecursively(new File(outDir));
	}	
}
