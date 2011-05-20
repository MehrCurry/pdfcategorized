package de.gzockoll.pdfcategorizer;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;

public class PdfFilter implements FileFilter {
	@Override
	public boolean accept(File f) {
		try {
			return f.getCanonicalPath().toLowerCase() 
					.endsWith(".pdf")|| f.isDirectory();
		} catch (IOException e) {
			return false;
		}
	}
}