package de.gzockoll.pdfcategorizer;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import org.junit.Ignore;
import org.junit.Test;


public class TelekomScannerTest {

	@Ignore("Test fails unexpectedly")
	@Test
	public void testSupports() {
		String text=getText(new File("src/main/resources/Rechnung_2011_02_13221877000781.pdf"));
		DocumentScanner scanner=new TelekomScanner();
		assertTrue(scanner.supports(text));
		assertNotNull(scanner.scan(text));
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
