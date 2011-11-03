package de.gzockoll.pdfcategorizer;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CreditCardScanner extends AbstractDateScanner implements DocumentScanner {
	private DataExtractor cardNumberExtractor;
	private DataExtractor customerNumberExtractor;
	private DataExtractor dateExtractor;
	
	public CreditCardScanner() {
		cardNumberExtractor=new DataExtractor(".*Kreditkartennummer.*(\\d{4} \\d{4} \\d{4} \\d{4}).*");
		customerNumberExtractor=new DataExtractor(".*Ihre Commerzbank Kundennummer (\\d+).*");
		dateExtractor = new DataExtractor(".*Rechnungsdatum\\s+(\\d{2}\\.\\d{2}\\.\\d{4}).*");
	}
	/* (non-Javadoc)
	 * @see de.gzockoll.pdfcategorizer.DocumentScanner#scan(java.lang.String)
	 */
	@Override
	public DocumentInfo scan(String text) {
		Date date = getDate(dateExtractor,text);
		String title = "Kreditkartenabrechnung für " + new SimpleDateFormat("MMM yyyy",Locale.GERMAN).format(date);
		String description = title + " Kundennummer: " + customerNumberExtractor.getData(text) + " Kreditkartennummer: " + cardNumberExtractor.getData(text);
		return new DocumentInfo(text,date, title, description,getCategory());
	}
	/* (non-Javadoc)
	 * @see de.gzockoll.pdfcategorizer.DocumentScanner#supports(java.lang.String)
	 */
	@Override
	public boolean supports(String text) {
		return cardNumberExtractor.matches(text) && customerNumberExtractor.matches(text) && dateExtractor.matches(text);
	}
	@Override
	public DocumentCategory getCategory() {
		return Bookkeeping.Bank;
	}
	
	@Override
	public String suggestFileName(DocumentInfo info) {
		Date date = info.getDate();
		return new SimpleDateFormat("yyyyMMdd").format(date) + "-Abrechnung #" + cardNumberExtractor.getData(info.getText());
	}
}
