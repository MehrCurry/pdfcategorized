package de.gzockoll.pdfcategorizer;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.UserDataHandler;

public abstract class AbstractDateScanner {

	protected Date getDate(DataExtractor extractor, String text) {
		Date date;
		String dateString=extractor.getData(text);
		if (dateString==null)
			return null;
		try {
			date = getDateFormat().parse(dateString);
		} catch (ParseException e) {
			throw new IllegalArgumentException("Unparsable date: " + dateString);
		}
		return date;
	}

	protected DateFormat getDateFormat() {
		return DateFormat.getDateInstance(DateFormat.SHORT,Locale.GERMAN);
	}
}
