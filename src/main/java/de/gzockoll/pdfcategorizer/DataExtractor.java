package de.gzockoll.pdfcategorizer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataExtractor {
	private Pattern pattern;
	
	public DataExtractor(String pat) {
		pattern=Pattern.compile(pat,Pattern.DOTALL);
	}
	
	public boolean matches(String text) {
		return pattern.matcher(text).matches();
	}
	
	public String getData(String text) {
		Matcher matcher = pattern.matcher(text);
		if (matcher.matches())
			return matcher.group(1);
		else
			return null;
	}
}
