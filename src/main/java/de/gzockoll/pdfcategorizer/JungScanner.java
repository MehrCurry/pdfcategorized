package de.gzockoll.pdfcategorizer;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

public class JungScanner extends AbstractInvoiceScanner implements DocumentScanner {
	private DataExtractor invoiceNumberExtractor;
	private DataExtractor customerNumberExtractor;
	private DataExtractor dateExtractor;
	private DataExtractor sumExtractor;
	
	public JungScanner() {
		invoiceNumberExtractor=new DataExtractor(".*Rechnungsnummer: (\\d{4}/\\d+).*");
		customerNumberExtractor=new DataExtractor(".*Mandant: (\\d+).*");
		dateExtractor = new DataExtractor(".*Rechnungsdatum: (\\d{2}\\.\\d{2}\\.\\d{4}).*");
		sumExtractor = new DataExtractor(".*zu zahlender Betrag.*(\\d+\\,\\d{2}).*");
	}
	/* (non-Javadoc)
	 * @see de.gzockoll.pdfcategorizer.DocumentScanner#scan(java.lang.String)
	 */
	@Override
	public DocumentInfo scan(String text) {
		Date date = getDate(dateExtractor,text);
		String title = "Rechnung Jung vom " + DateFormat.getDateInstance(DateFormat.SHORT,Locale.GERMAN).format(date);
		String description = title + " Mandant: " + customerNumberExtractor.getData(text) + " Rechnungsnummer: " + invoiceNumberExtractor.getData(text) + ", Betrag: " + sumExtractor.getData(text);
		return new InvoiceInfo(text, date, title, description,getCategory(), invoiceNumberExtractor.getData(text),sumExtractor.getData(text));
	}
	/* (non-Javadoc)
	 * @see de.gzockoll.pdfcategorizer.DocumentScanner#supports(java.lang.String)
	 */
	@Override
	public boolean supports(String text) {
		return invoiceNumberExtractor.matches(text) && customerNumberExtractor.matches(text) && dateExtractor.matches(text);
	}
	@Override
	public String suggestFileName(DocumentInfo info) {
		InvoiceInfo inf=(InvoiceInfo) info;
		Date date = inf.getDate();
		return super.suggestFileName(date, "Jung", inf.getInvoiceNumber().replace("/", "-"));
	}
}
