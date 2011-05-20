package de.gzockoll.pdfcategorizer;

import java.util.Date;
import java.util.regex.Pattern;

import org.apache.commons.lang.Validate;

public class AirBerlinScanner extends AbstractInvoiceScanner implements DocumentScanner {
	private static final Pattern pattern=Pattern.compile(".*Buchungs/Rechnungsnummer (\\d+).*Buchungsdatum (\\d{2}\\.\\d{2}\\.\\d{4}).*",Pattern.DOTALL);
	private DataExtractor invoiceNumberExtractor;
	private DataExtractor dateExtractor;
	private DataExtractor documentChecker;

	public AirBerlinScanner() {
		documentChecker=new DataExtractor(".*Air Berlin PLC & Co. Luftverkehrs KG.*Rechnung und Reisebestätigung.*");
		invoiceNumberExtractor=new DataExtractor(".*Buchungs/Rechnungsnummer (\\d+).*");
		dateExtractor=new DataExtractor(".*Buchungsdatum (\\d{2}\\.\\d{2}\\.\\d{4}).*");
	}
	/* (non-Javadoc)
	 * @see de.gzockoll.pdfcategorizer.DocumentScanner#scan(java.lang.String)
	 */
	@Override
	public DocumentInfo scan(String text) {
		Date date=getDate(dateExtractor,text);
		String title = "AirBerlin Rechnung " + invoiceNumberExtractor.getData(text);
		String description = title;
		InvoiceInfo info = new InvoiceInfo(text, date, title, description,getCategory(),invoiceNumberExtractor.getData(text),"");
		info.setSuggestedFileName(suggestFileName(info));
		return info;
	}

	/* (non-Javadoc)
	 * @see de.gzockoll.pdfcategorizer.DocumentScanner#supports(java.lang.String)
	 */
	@Override
	public boolean supports(String text) {
		Validate.notNull(text);
		return documentChecker.matches(text) && invoiceNumberExtractor.matches(text) && dateExtractor.matches(text);
	}
	
	@Override
	public String suggestFileName(DocumentInfo info) {
		Validate.notNull(info);
		InvoiceInfo inf=(InvoiceInfo) info;
		Date date=info.getDate();
		return super.suggestFileName(date, "AirBerlin", inf.getInvoiceNumber());
	}
}
