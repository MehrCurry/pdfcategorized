package de.gzockoll.pdfcategorizer;

import java.util.Date;

public class InvoiceInfo extends DocumentInfo {
	private String invoiceNumber;
	private String value;
	
	public InvoiceInfo(String text, Date date, String title, String description,
			DocumentCategory category, String invoiceNumber, String value) {
		super(text, date, title, description, category);
		this.invoiceNumber=invoiceNumber;
		this.value=value;
	}

	public String getInvoiceNumber() {
		return invoiceNumber;
	}
	
	public String getValue() {
		return value;
	}
}
