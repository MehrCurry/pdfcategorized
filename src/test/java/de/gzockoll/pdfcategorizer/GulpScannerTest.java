package de.gzockoll.pdfcategorizer;

import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import org.junit.Test;
import static org.junit.Assert.*;


public class GulpScannerTest {
	@Test
	public void testSupports() {
		String text=getText(new File("src/main/resources/20110506083606.pdf"));
		DocumentScanner scanner=new GulpScanner();
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
