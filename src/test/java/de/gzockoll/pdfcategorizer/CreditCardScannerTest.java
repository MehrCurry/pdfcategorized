package de.gzockoll.pdfcategorizer;


import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Locale;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import org.junit.Before;
import org.junit.Test;

public class CreditCardScannerTest {
	private static DocumentScanner scanner;

	private static final String CORRECT_MATCH="\n\nIhre Commerzbank Kundennummer 3619418\n\nKreditkartennummer\n5232 2490 0043 6685\n\nRechnungsdatum\n21.02.2011\n\n....";
	@Before
	public void setUp() throws Exception {
		scanner=new CreditCardScanner();
	}
	
	@Test
	public void testSupports() {
		assertFalse(scanner.supports("some random string"));
		assertTrue(scanner.supports(CORRECT_MATCH));
	}
	@Test
	public void testGetDate() throws ParseException {
		DocumentInfo info = scanner.scan(CORRECT_MATCH);
		assertThat(info.getDate(),is(DateFormat.getDateInstance(DateFormat.SHORT,Locale.GERMAN).parse("21.02.2011")));
	}

	@Test
	public void testGetTitle() throws ParseException {
		DocumentInfo info = scanner.scan(CORRECT_MATCH);
		assertThat(info.getTitle(),is("Kreditkartenabrechnung für Feb 2011"));
	}
	
	@Test 
	public void testWithRealData() throws IOException, ParseException {
		PDFTextStripper stripper = new PDFTextStripper();
		String text=null;
		try {
			text = stripper.getText(PDDocument.load("src/main/resources/Kreditkarte201102.pdf"));
		} catch (NullPointerException e) {
		}
		assertTrue(scanner.supports(text));
		DocumentInfo info = scanner.scan(text);
		assertThat(info.getDate(),is(DateFormat.getDateInstance(DateFormat.SHORT,Locale.GERMAN).parse("21.02.2011")));
		assertThat(info.getTitle(),is("Kreditkartenabrechnung für Feb 2011"));
		
	}
}
