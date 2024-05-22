package org.nbme.dwbi.synthetic.engine;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.util.FileSystemUtils;
public class SyntheticEngineTest {
	private static String inDir1 = "inputForEngine1/";
	private static String inDir2 = "inputForEngine2/";
	private static String metadataFile1 = inDir1 + "input3.metadata.yml";
	private static String paramsFile1 = inDir1 + "params.properties";
	private static String metadataFile2 = inDir2 + "input4.metadata.yml";
	private static String paramsFile2 = inDir2 + "params.properties";
	private static String outDir1 = "outputFromEngine1/";
	private static String outDir2 = "outputFromEngine2/";
	private static String outFile1 = outDir1 + "table_1_synth.csv";
	private static String outFile2 = outDir1 + "table_2_synth.csv";
	private static String outFile3 = outDir1 + "table_3_synth.csv";
	private static String outFile4 = outDir1 + "table_4_synth.csv";
	private static String outFile5 = outDir1 + "table_5_synth.csv";
	private static String outFile6 = outDir1 + "table_6_synth.csv";
	private static String outFile7 = outDir1 + "table_7_synth.csv";
	private static String outFile8 = outDir1 + "table_8_synth.csv";
	private static String outFile9 = outDir1 + "table_9_synth.csv";
	private static String outFile10 = outDir1 + "table_10_synth.csv";
	private static String outFile2_1 = outDir2 + "SAMPLE_OUTPUT_REDUCED_synth.json";
	
	@BeforeClass
	public static void setUp() throws IOException {
		// in case these exist from a prior run
		delete();

		// project written to take input from a file system folder, so copy from
		// resources to a real folder
		new File(inDir1).mkdir();
		new File(inDir2).mkdir();
		InputStream is = SyntheticEngineTest.class.getResourceAsStream("/" + metadataFile1);
		Files.copy(is, Paths.get(metadataFile1), StandardCopyOption.REPLACE_EXISTING);
		is = SyntheticEngineTest.class.getResourceAsStream("/" + paramsFile1);
		Files.copy(is, Paths.get(paramsFile1), StandardCopyOption.REPLACE_EXISTING);
		is = SyntheticEngineTest.class.getResourceAsStream("/" + metadataFile2);
		Files.copy(is, Paths.get(metadataFile2), StandardCopyOption.REPLACE_EXISTING);
		is = SyntheticEngineTest.class.getResourceAsStream("/" + paramsFile2);
		Files.copy(is, Paths.get(paramsFile2), StandardCopyOption.REPLACE_EXISTING);
		new File(outDir1).mkdir();
		new File(outDir2).mkdir();
	}

	@Test
	public void testSyntheticEngine() throws Exception {
		SyntheticEngine se = new SyntheticEngine(metadataFile1, paramsFile1, outDir1);
		se.makeData();
		for(String fileName : new String[] {outFile1, outFile2, outFile3, outFile4, outFile5, outFile6, outFile7, outFile8, outFile9, outFile10}) {
			assertTrue(new File(fileName).exists());
		}
		
		se = new SyntheticEngine(metadataFile2, paramsFile2, outDir2);
		se.makeData();
		for(String fileName : new String[] {outFile2_1}) {
			assertTrue(new File(fileName).exists());
		}
	}
	
	//missing files
	@Test(expected = FileNotFoundException.class)
	public void testFileNotFoundException() throws Exception {
		new SyntheticEngine(metadataFile2 + ".bak", paramsFile2, outDir2);
	}
	
	@AfterClass
	public static void tearDown() {
		delete();
	}	

	private static void delete() {
		FileSystemUtils.deleteRecursively(new File(inDir1));
		FileSystemUtils.deleteRecursively(new File(inDir2));
		FileSystemUtils.deleteRecursively(new File(outDir1));
		FileSystemUtils.deleteRecursively(new File(outDir2));
	}	
}
