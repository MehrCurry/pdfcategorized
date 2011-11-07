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

public class AirBerlinScannerTest {
    private static DocumentScanner scanner;

    private static final String CORRECT_MATCH = "Air Berlin PLC & Co. Luftverkehrs KG\n\nRechnung und Reisebestätigung\n\nBuchungs/Rechnungsnummer 12345\n\nBuchungsdatum 26.04.2011\n\n....";

    @Before
    public void setUp() throws Exception {
        scanner = new AirBerlinScanner();
    }

    @Test
    public void testGetDate() throws ParseException {
        DocumentInfo info = scanner.scan(CORRECT_MATCH);
        assertThat(info.getDate(), is(DateFormat.getDateInstance(DateFormat.SHORT, Locale.GERMAN).parse("26.04.2011")));
    }

    @Test
    public void testGetTitle() throws ParseException {
        DocumentInfo info = scanner.scan(CORRECT_MATCH);
        assertThat(info.getTitle(), is("AirBerlin Rechnung 12345"));
    }

    @Test
    public void testSupports() {
        assertFalse(scanner.supports("some random string"));
        assertTrue(scanner.supports(CORRECT_MATCH));
    }

    @Test
    public void testWithRealData() throws IOException, ParseException {
        PDFTextStripper stripper = new PDFTextStripper();
        String text = null;
        try {
            text = stripper.getText(PDDocument.load("src/main/resources/12529529.pdf"));
        } catch (NullPointerException e) {
            // catching some stange exceptions while loading document
        }
        assertTrue(scanner.supports(text));
        DocumentInfo info = scanner.scan(text);
        assertThat(info.getDate(), is(DateFormat.getDateInstance(DateFormat.SHORT, Locale.GERMAN).parse("26.04.2011")));
        assertThat(info.getTitle(), is("AirBerlin Rechnung 12529529"));

    }
}
