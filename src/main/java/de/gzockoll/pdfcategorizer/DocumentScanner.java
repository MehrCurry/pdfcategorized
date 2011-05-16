package de.gzockoll.pdfcategorizer;

public interface DocumentScanner {

	public abstract DocumentInfo scan(String text);

	public abstract boolean supports(String text);
	
	public abstract DocumentCategory getCategory();
	
	public abstract String suggestFileName(DocumentInfo info);

}