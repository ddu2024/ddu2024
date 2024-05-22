package org.nbme.dwbi.synthetic.parser;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.FileSystemUtils;

public class ParserTest {
	private static String inDir1 = "input1/";
	private static String inDir2 = "input2/";
	private static String inDir3 = "input3/";
	private static String inDir4 = "input4/";
	private static String inDir5 = "input5/";
	private static String outDir1 = "output1/";
	private static String outDir2 = "output2/";
	private static String outDir3 = "output3/";
	private static String outDir4 = "output4/";
	private static String outDir5 = "output5/";
	
	@BeforeClass
	public static void setUp() throws IOException {
		// in case these exist from a prior run
		delete();
		
		InputStream is;		

		ClassLoader cl = new ParserTest().getClass().getClassLoader(); 
		ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(cl);
		for (String dir : new String[] { inDir1, inDir2, inDir3, inDir4, inDir5}) {
			new File(dir).mkdir();
			Resource[] resources = resolver.getResources(dir + "/*");
			for (Resource res : resources) {
				is = ParserTest.class.getResourceAsStream("/" + dir + res.getFile().getName());
				Files.copy(is, Paths.get(dir + res.getFile().getName()), StandardCopyOption.REPLACE_EXISTING);
			}
		}
		
		new File(outDir1).mkdir();
		new File(outDir2).mkdir();
		new File(outDir3).mkdir();
		new File(outDir4).mkdir();
		new File(outDir5).mkdir();
	}

	@Test
	public void testParser() throws Exception {
		//just ddl
		new Parser(inDir1, outDir1).parse();
		
		//just csv/xlsx
		new Parser(inDir2, outDir2).parse();		
		
		//ddl + xlsx/csv (with and without BOM)
		String error = "";
		try {
			new Parser(inDir3, outDir3).parse();
		} catch (Exception e) {
			error = e.getMessage();
		}
		assertFalse(error.contains("not found in the existing container")); //this error would occur with a BOM issue

		//json
		new Parser(inDir4, outDir4).parse();		

		//empty folder or no valid files
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		System.setOut(new PrintStream(buffer));
		new Parser(inDir5, outDir5).parse();
		assertTrue(buffer.toString().contains("No valid files found to parse. Extension must match regex:"));
		
		System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
	}
	
	//missing files
	@Test(expected = FileNotFoundException.class)
	public void testFileNotFoundException() throws Exception {
		new Parser(inDir5 +"bak", outDir5);
	}
	
	@AfterClass
	public static void tearDown() {
		delete();
	}
	
	private static void delete() {
		FileSystemUtils.deleteRecursively(new File(inDir1));
		FileSystemUtils.deleteRecursively(new File(inDir2));
		FileSystemUtils.deleteRecursively(new File(inDir3));
		FileSystemUtils.deleteRecursively(new File(inDir4));
		FileSystemUtils.deleteRecursively(new File(inDir5));
		FileSystemUtils.deleteRecursively(new File(outDir1));
		FileSystemUtils.deleteRecursively(new File(outDir2));
		FileSystemUtils.deleteRecursively(new File(outDir3));
		FileSystemUtils.deleteRecursively(new File(outDir4));
		FileSystemUtils.deleteRecursively(new File(outDir5));
	}	
}
