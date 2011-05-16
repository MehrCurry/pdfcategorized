package de.gzockoll.pdfcategorizer;

import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import org.junit.Test;
import static org.junit.Assert.*;


public class AvivaScannerTest {
	@Test
	public void testSupports() {
		String text=getText(new File("src/main/resources/Hotel Aviva0001.pdf"));
		DocumentScanner scanner=new AvivaScanner();
		assertTrue(scanner.supports(text));
	}
	
	@Test
	public void testSupports2() {
		String text=getText(new File("src/main/resources/20110421-Hotel Aviva0001.pdf"));
		DocumentScanner scanner=new AvivaScanner();
		assertTrue(scanner.supports(text));
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
