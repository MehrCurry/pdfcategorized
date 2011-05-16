package de.gzockoll.pdfcategorizer;

import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class AbstractInvoiceScanner extends AbstractDateScanner implements DocumentScanner {
	protected String suggestFileName(Date date,String name,String invoiceNumber) {
		return new SimpleDateFormat("yyyyMMdd").format(date) + "-"  + name + " #" + invoiceNumber;
	}

	@Override
	public DocumentCategory getCategory() {
		return Bookkeeping.Invoice;
	}

}
