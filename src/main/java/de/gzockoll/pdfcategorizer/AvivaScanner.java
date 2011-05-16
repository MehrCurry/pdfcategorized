package de.gzockoll.pdfcategorizer;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AvivaScanner extends AbstractInvoiceScanner implements DocumentScanner {
	private DataExtractor invoiceNumberExtractor;
	private DataExtractor dateExtractor;
	private DataExtractor documentChecker;
	
	public AvivaScanner() {
		documentChecker=new DataExtractor(".*Hotel Aviva GmbH.*");
		invoiceNumberExtractor=new DataExtractor(".*Rech\\.-Nr\\. (\\d+).*");
		dateExtractor = new DataExtractor(".*Datum (\\d{2}\\.\\d{2}\\.\\d{4}).*");
	}
	/* (non-Javadoc)
	 * @see de.gzockoll.pdfcategorizer.DocumentScanner#scan(java.lang.String)
	 */
	@Override
	public DocumentInfo scan(String text) {
		Date date = getDate(dateExtractor,text);
		String title = "Rechnung Hotel Aviva vom " + DateFormat.getDateInstance(DateFormat.SHORT,Locale.GERMAN).format(date);
		String description = title + " Rechnungsnummer: " + invoiceNumberExtractor.getData(text);
		return new InvoiceInfo(text, date, title, description,getCategory(),invoiceNumberExtractor.getData(text),"");
	}
	/* (non-Javadoc)
	 * @see de.gzockoll.pdfcategorizer.DocumentScanner#supports(java.lang.String)
	 */
	@Override
	public boolean supports(String text) {
		return documentChecker.matches(text) && invoiceNumberExtractor.matches(text)  && dateExtractor.matches(text);
	}
	
	@Override
	public String suggestFileName(DocumentInfo info) {
		InvoiceInfo inf=(InvoiceInfo) info;
		Date date = inf.getDate();
		return super.suggestFileName(date, "Aviva", inf.getInvoiceNumber());
	}
}
