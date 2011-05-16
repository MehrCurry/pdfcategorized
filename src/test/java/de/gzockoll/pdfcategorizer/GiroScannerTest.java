package de.gzockoll.pdfcategorizer;

import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.core.Is.*;

public class GiroScannerTest {
	@Test
	public void testSupports() {
		String text=getText(new File("src/main/resources/201104-Giro.pdf"));
		DocumentScanner scanner=new GiroScanner();
		assertTrue(scanner.supports(text));
	}
	
	@Test
	public void testAccountNumberExtractor() {
		GiroScanner scanner=new GiroScanner();
		DataExtractor ex = scanner.getAccountNumberExtractor();
		String text = "Kontonummer: 361940000";
		assertTrue(ex.matches(text));
		text = "Kontonummer: 3619400 00";
		assertTrue(ex.matches(text));
		assertThat(ex.getData(text),is("3619400 00"));
		text = "Kontonummer: 3619400 75";
		assertTrue(ex.matches(text));
		assertThat(ex.getData(text),is("3619400 75"));
	}
	
	private static String getText(File f) {
		PDDocument doc=null;
		try {
			PDFTextStripper stripper = new PDFTextStripper();
			doc=PDDocument.load(f);
			return stripper.getText(doc);
		} catch (IOException e) {
			System.err.println(e);
			return null;
		} finally
	    {
	        if( doc != null )
	        {
	            try {
					doc.close();
				} catch (IOException e) {
				}
	        }
	    }

	}

}
