package de.gzockoll.pdfcategorizer;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

public class TelekomScanner extends AbstractInvoiceScanner implements DocumentScanner {
	private DataExtractor invoiceNumberExtractor;
	private DataExtractor customerNumberExtractor;
	private DataExtractor dateExtractor;
	private DataExtractor sumExtractor;
	private DataExtractor docChecker;
	
	public TelekomScanner() {
		docChecker=new DataExtractor(".*Mobilfunk-Rechnung für (\\w+ \\d+).*");
		invoiceNumberExtractor=new DataExtractor(".*Rechnungsnummer (\\d+).*");
		// customerNumberExtractor=new DataExtractor(".*Kundenkonto (\\d+).*");
		dateExtractor = new DataExtractor(".*Datum (\\d{2}\\.\\d{2}\\.\\d{2}).*");
		sumExtractor = new DataExtractor(".*Rechnungsbetrag (\\d+\\,\\d{2}).*");
	}
	/* (non-Javadoc)
	 * @see de.gzockoll.pdfcategorizer.DocumentScanner#scan(java.lang.String)
	 */
	@Override
	public DocumentInfo scan(String text) {
		Date date = getDate(dateExtractor,text);
		String title = "Rechnung Telekom vom " + DateFormat.getDateInstance(DateFormat.SHORT,Locale.GERMAN).format(date);
		String description = title  +"Mobilfunk-Rechnung " + docChecker.getData(text) + " Rechnungsnummer: " + invoiceNumberExtractor.getData(text) + ", Betrag: " + sumExtractor.getData(text);
		return new InvoiceInfo(text, date, title, description,getCategory(),invoiceNumberExtractor.getData(text),sumExtractor.getData(text));
	}
	/* (non-Javadoc)
	 * @see de.gzockoll.pdfcategorizer.DocumentScanner#supports(java.lang.String)
	 */
	@Override
	public boolean supports(String text) {
		return docChecker.matches(text) && invoiceNumberExtractor.matches(text) &&  dateExtractor.matches(text);
	}
	@Override
	public String suggestFileName(DocumentInfo info) {
		return suggestFileName((InvoiceInfo) info);
	}
	
	public String suggestFileName(InvoiceInfo info) {
		Date date = info.getDate();
		return super.suggestFileName(date, "Telekom", info.getInvoiceNumber().replace("/", "-"));
	}
}
