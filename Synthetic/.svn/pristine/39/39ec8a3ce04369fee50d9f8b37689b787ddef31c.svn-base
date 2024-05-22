package org.nbme.dwbi.synthetic;

import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

import org.junit.Test;

public class SyntheticTest {

	//wrong/missing arguments
	@Test
	public void testSynthetic() throws Exception {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		System.setOut(new PrintStream(buffer));

		new Synthetic().run(new String[0]);
		String content = buffer.toString();

		assertTrue(content.contains("To parse: -p source outputDir"));
		assertTrue(content.contains("To make data: -g metaDataFile paramFile outputDir"));
		buffer.reset();
		
		new Synthetic().run(new String[] {"-h","file1","file2"});
		content = buffer.toString();

		assertTrue(content.contains("To parse: -p source outputDir"));
		assertTrue(content.contains("To make data: -g metaDataFile paramFile outputDir"));
		
		System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
	}
	
	//missing files (but otherwise correct arguments)
	@Test(expected = FileNotFoundException.class)
	public void testFileNotFoundException() throws Exception {
		new Synthetic().run(new String[] {"-p","file1","file2"});
	}
}
