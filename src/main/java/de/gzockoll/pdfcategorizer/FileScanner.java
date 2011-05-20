package de.gzockoll.pdfcategorizer;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.management.RuntimeErrorException;

import org.apache.commons.lang.Validate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;

public class FileScanner {
	private static final Log log = LogFactory.getLog(FileScanner.class);

	List<DocumentScanner> chain = new ArrayList<DocumentScanner>();

	public FileScanner() {
		chain.add(new AirBerlinScanner());
		chain.add(new CreditCardScanner());
		chain.add(new GiroScanner());
		chain.add(new JungScanner());
		chain.add(new AvivaScanner());
		chain.add(new GulpScanner());
		chain.add(new AmazonScanner());
		chain.add(new BahnTicketScanner());
		chain.add(new TelekomScanner());
	}

	public DocumentInfo getInfoByName(String name) {
		Validate.notNull(name);
		log.debug("getInfoByName(" + name + ")");
		return getInfo(new File(name));
	}

	public DocumentInfo getInfo(File f) {
		log.debug("getInfo(" + f + ")");
		Validate.notNull(f);
		DocumentInfo info = getInfo(getText(f));
		try {
			info.setFileName(f.getCanonicalPath());
			
		} catch (IOException e) {
			log.error(e);
		}
		return info;
	}

	public DocumentInfo getInfo(String text) {
		log.debug("getInfo(" + text + ")");
		Validate.notNull(text);
		DocumentInfo info = null;
		for (DocumentScanner scanner : chain) {
			if (scanner.supports(text)) {
				info = scanner.scan(text);
				break;
			}
		}
		return info;
	}

	public String getText(byte[] data) {
		return getText(new ByteArrayInputStream(data));
	}

	public String getText(InputStream is) {
		Validate.notNull(is);
		PDDocument doc = null;
		try {
			PDFTextStripper stripper = new PDFTextStripper();
			doc = PDDocument.load(is);
			return stripper.getText(doc);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		} finally {
			if (doc != null) {
				try {
					doc.close();
				} catch (IOException e) {
				}
			}
		}
	}

	public String getText(File f) {
		try {
			return getText(new FileInputStream(f));
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
}
