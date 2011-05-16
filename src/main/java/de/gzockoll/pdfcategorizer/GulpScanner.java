package de.gzockoll.pdfcategorizer;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class GulpScanner extends AbstractDateScanner implements DocumentScanner {
	private DataExtractor invoiceNumberExtractor;
	private DataExtractor dateExtractor;
	private DataExtractor docChecker;
	private DataExtractor monthExtractor;
	
	public GulpScanner() {
		docChecker = new DataExtractor(".*GULP Consulting Services GmbH.*");
		invoiceNumberExtractor=new DataExtractor(".*Gutschrifts-Nr\\.: (\\d+).*");
		monthExtractor=new DataExtractor(".*Abrechnungsmonat (\\d+/\\d+).*");
		dateExtractor = new DataExtractor(".*Gutschriftsdatum: (\\d{2}\\.\\d{2}\\.\\d{4}).*");
	}
	/* (non-Javadoc)
	 * @see de.gzockoll.pdfcategorizer.DocumentScanner#scan(java.lang.String)
	 */
	@Override
	public DocumentInfo scan(String text) {
		Date date = getDate(dateExtractor,text);
		String title = "Gutschrift Gulp vom " + DateFormat.getDateInstance(DateFormat.SHORT,Locale.GERMAN).format(date);
		String description = title + " Abrechnungsmonat: " + monthExtractor.getData(text) + " Gutschrifts-Nr.: " + invoiceNumberExtractor.getData(text);
		return new DocumentInfo(text, date, title, description,getCategory());
	}
	/* (non-Javadoc)
	 * @see de.gzockoll.pdfcategorizer.DocumentScanner#supports(java.lang.String)
	 */
	@Override
	public boolean supports(String text) {
		return docChecker.matches(text) && monthExtractor.matches(text) && invoiceNumberExtractor.matches(text) && dateExtractor.matches(text);
	}
	@Override
	public DocumentCategory getCategory() {
		return Bookkeeping.Outgoing;
	}
	
	@Override
	public String suggestFileName(DocumentInfo info) {
		Date date = info.getDate();
		return new SimpleDateFormat("yyyyMMdd").format(date) + "-Gulp " + monthExtractor.getData(info.getText()).replace("/", "-");
	}

}
