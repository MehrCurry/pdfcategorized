package de.gzockoll.pdfcategorizer;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

public class AmazonScanner extends AbstractInvoiceScanner implements DocumentScanner {
	private DataExtractor invoiceNumberExtractor;
	private DataExtractor dateExtractor;
	private DataExtractor docChecker;
	private DataExtractor sumExtractor=new DataExtractor(".*EUR\\d+\\,\\d+ EUR\\d+\\,\\d+ EUR\\d+\\,\\d+ EUR(\\d+\\,\\d+).*");
	
	public AmazonScanner() {
		docChecker=new DataExtractor(".*Amazon EU.*");
		invoiceNumberExtractor=new DataExtractor(".*Rechnungsnr\\. (\\w+).*");
		dateExtractor = new DataExtractor(".*Rechnungsdatum\\. (\\d{2}\\. \\w+ \\d{4}).*");
	}
	/* (non-Javadoc)
	 * @see de.gzockoll.pdfcategorizer.DocumentScanner#scan(java.lang.String)
	 */
	@Override
	public DocumentInfo scan(String text) {
		Date date = getDate(dateExtractor,text);
		String title = "Rechnung Amazon vom " + DateFormat.getDateInstance(DateFormat.SHORT,Locale.GERMAN).format(date);
		String description = title;
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
	protected DateFormat getDateFormat() {
		return DateFormat.getDateInstance(DateFormat.LONG,Locale.GERMAN);
	}
	@Override
	public DocumentCategory getCategory() {
		
		return Bookkeeping.Invoice;
	}
	@Override
	public String suggestFileName(DocumentInfo info) {
		InvoiceInfo inf=(InvoiceInfo)info;
		Date date = inf.getDate();
		return super.suggestFileName(date, "Amazon", inf.getInvoiceNumber());
	}
}
