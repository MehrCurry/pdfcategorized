package de.gzockoll.pdfcategorizer;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

public class BahnTicketScanner extends AbstractInvoiceScanner implements DocumentScanner {
	private DataExtractor invoiceNumberExtractor;
	private DataExtractor dateExtractor;
	private DataExtractor docChecker;
	private DataExtractor sumExtractor;
	
	public BahnTicketScanner() {
		docChecker=new DataExtractor(".*BAHN.*Online-Ticket.*");
		invoiceNumberExtractor=new DataExtractor(".*Auftragsnummer:.*([A-Z]{6}).*");
		dateExtractor = new DataExtractor(".*Datum (\\d{2}\\.\\d{2}\\.\\d{4}).*");
		sumExtractor = new DataExtractor(".*Summe (\\d+\\,\\d{2}).*");
	}
	/* (non-Javadoc)
	 * @see de.gzockoll.pdfcategorizer.DocumentScanner#scan(java.lang.String)
	 */
	@Override
	public DocumentInfo scan(String text) {
		Date date = getDate(dateExtractor,text);
		String title = "Bahn Online-Ticket vom " + DateFormat.getDateInstance(DateFormat.SHORT,Locale.GERMAN).format(date);
		String description = title + " Zertifikat: " + invoiceNumberExtractor.getData(text) + " Rechnungsnummer: " + invoiceNumberExtractor.getData(text) + ", Betrag: " + sumExtractor.getData(text);
		return new InvoiceInfo(text,date, title, description,getCategory(),invoiceNumberExtractor.getData(text),sumExtractor.getData(text));
	}
	/* (non-Javadoc)
	 * @see de.gzockoll.pdfcategorizer.DocumentScanner#supports(java.lang.String)
	 */
	@Override
	public boolean supports(String text) {
		return docChecker.matches(text) && invoiceNumberExtractor.matches(text) && dateExtractor.matches(text);
	}
	@Override
	public String suggestFileName(DocumentInfo info) {
		InvoiceInfo inf=(InvoiceInfo) info;
		Date date = inf.getDate();
		return super.suggestFileName(date, "Bahn",inf.getInvoiceNumber().replace("/", "-"));
	}
}
