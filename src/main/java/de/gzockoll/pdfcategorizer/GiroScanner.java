package de.gzockoll.pdfcategorizer;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class GiroScanner extends AbstractDateScanner implements DocumentScanner {
	private DataExtractor dateExtractor;
	private DataExtractor accountNumberExtractor;
	private DataExtractor sheetExtractor;

	public GiroScanner() {
		dateExtractor = new DataExtractor(
				".*Kontoauszug vom (\\d{2}\\.\\d{2}\\.\\d{4}).*");
		accountNumberExtractor = new DataExtractor(".*Kontonummer: (\\d+( \\d{2})?).*");
		sheetExtractor = new DataExtractor(".*Auszug-Nr. (\\d+).*");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.gzockoll.pdfcategorizer.DocumentScanner#scan(java.lang.String)
	 */
	@Override
	public DocumentInfo scan(String text) {
		Date date=getDate(dateExtractor,text);
		String title = "Kontoauszug vom "
				+ DateFormat.getDateInstance(DateFormat.SHORT, Locale.GERMAN)
						.format(date);
		String description = title + " Kontonummer: "
				+ accountNumberExtractor.getData(text);
		String sheet;
		if ((sheet = sheetExtractor.getData(text)) != null)
			description = description + ", Auszug-Nr. " + sheet;
		return new DocumentInfo(text,date, title, description,getCategory());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.gzockoll.pdfcategorizer.DocumentScanner#supports(java.lang.String)
	 */
	@Override
	public boolean supports(String text) {
		return dateExtractor.matches(text)
				&& accountNumberExtractor.matches(text);
	}
	@Override
	public DocumentCategory getCategory() {
		return Bookkeeping.Bank;
	}
	
	@Override
	public String suggestFileName(DocumentInfo info) {
		Date date = info.getDate();
		return new SimpleDateFormat("yyyyMMdd").format(date) + "-Auszug #" + accountNumberExtractor.getData(info.getText());
	}

	public DataExtractor getDateExtractor() {
		return dateExtractor;
	}

	public DataExtractor getAccountNumberExtractor() {
		return accountNumberExtractor;
	}

	public DataExtractor getSheetExtractor() {
		return sheetExtractor;
	}

}
