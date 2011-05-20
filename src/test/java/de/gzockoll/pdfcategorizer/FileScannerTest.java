package de.gzockoll.pdfcategorizer;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

public class FileScannerTest {
	FileScanner scanner=new FileScanner();
	
	@Test
	public void testGetInfoByName() {
		String name="src/main/resources/20110414-Amazon0001.pdf";
		DocumentInfo info = scanner.getInfoByName(name);
	}

	@Test
	public void testGetInfoFile() {
		String name="src/main/resources/20110414-Amazon0001.pdf";
		DocumentInfo info = scanner.getInfo(new File(name));
	}

	@Test
	public void testGetInfoString() {
		String name="src/main/resources/20110414-Amazon0001.pdf";
		String text=scanner.getText(new File(name));
		DocumentInfo info = scanner.getInfo(text);
	}

}
