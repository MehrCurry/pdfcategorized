package de.gzockoll.pdfcategorizer;

import java.util.Date;

public class DocumentInfo {
	private String text;
	private Date date;
	private String title;
	private String description;
	private DocumentCategory category;
	private String fileName;
	private String suggestedFileName;
	
	public DocumentInfo(String text, Date date, String title, String description, DocumentCategory category) {
		super();
		this.text = text;
		this.date = date;
		this.title = title;
		this.description = description;
		this.category = category;
	}
	
	public Date getDate() {
		return date;
	}
	public String getTitle() {
		return title;
	}
	public String getDescription() {
		return description;
	}
	
	public DocumentCategory getCategory() {
		return category;
	}
	@Override
	public String toString() {
		return "Title: " + title + ", Description: " + description + ", Date: " + date;
	}
	public void setFileName(String fileName) {
		this.fileName=fileName;
	}
	
	public String getText() {
		return text;
	}

	public String getSuggestedFileName() {
		return suggestedFileName;
	}

	public void setSuggestedFileName(String suggestedFileName) {
		this.suggestedFileName = suggestedFileName;
	}
	
}