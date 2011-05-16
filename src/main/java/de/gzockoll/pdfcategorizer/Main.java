package de.gzockoll.pdfcategorizer;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;

public class Main {
	private static final Log log=LogFactory.getLog(Main.class);
	
	List<DocumentScanner> chain = new ArrayList<DocumentScanner>();
	private FileFilter pdfFilter = new FileFilter() {

		@Override
		public boolean accept(File f) {
			try {
				return f.getCanonicalPath().toLowerCase() 
						.endsWith(".pdf")|| f.isDirectory();
			} catch (IOException e) {
				return false;
			}
		}
	};

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		new Main().run();
	}

	public void run() {
		chain.add(new AirBerlinScanner());
		chain.add(new CreditCardScanner());
		chain.add(new GiroScanner());
		chain.add(new JungScanner());
		chain.add(new AvivaScanner());
		chain.add(new GulpScanner());
		chain.add(new AmazonScanner());
		chain.add(new BahnTicketScanner());
		chain.add(new TelekomScanner());

		// File dir = new File("C:\\Users\\guidp\\Dropbox\\eaw\\Buchhaltung");
		// File dir = new File("X:\\Sites\\eaw\\documentLibrary\\Buchhaltung");
		File dir = new File("\\\\QNAP\\Qunsafe\\tmp\\scans");
		scanFile(dir);
	}

	private void scanFile(File f) {
		if (f.isDirectory()) {
			for (File sub : f.listFiles(pdfFilter)) {
				scanFile(sub);
			}
		} else {

			String text = getText(f);
			System.out.print(f.getName() + ": ");
			if (text != null) {
				for (DocumentScanner scanner : chain) {
					if (scanner.supports(text)) {
						DocumentInfo info = scanner.scan(text);
						try {
							info.setFileName(f.getCanonicalPath());
						} catch (IOException e1) {
							log.warn(e1);
						}
						System.out.println(f.getName() + ": "
								+ info);
						String suggestedFileName = scanner
								.suggestFileName(info);
						System.out.println(suggestedFileName);
						try {
							String oldName = f.getCanonicalPath();
							String newPathAndFileName = createNewName("d:\\tmp\\Buchhaltung",info,scanner.suggestFileName(info));
							System.out.println(newPathAndFileName);
							String newFileName = oldName.replace(
									FilenameUtils.getBaseName(oldName),
									suggestedFileName);
							FileUtils.copyFile(f, new File(newPathAndFileName));
							System.out.println(newFileName);
							if (!newFileName.endsWith(oldName))
								f.renameTo(new File(newFileName));
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				}
			}
			System.out.println("");
		}
	}

	private String createNewName(String path, DocumentInfo info,
			String fileName) {
		return path + File.separator + new SimpleDateFormat("yyyy").format(info.getDate())+File.separator+new SimpleDateFormat("MM").format(info.getDate()) + File.separator + info.getCategory().getName() + File.separator + fileName + ".pdf";
	}

	private String getText(File f) {
		PDDocument doc = null;
		try {
			PDFTextStripper stripper = new PDFTextStripper();
			doc = PDDocument.load(f);
			return stripper.getText(doc);
		} catch (Throwable e) {
			System.err.println(e);
			return null;
		} finally {
			if (doc != null) {
				try {
					doc.close();
				} catch (IOException e) {
				}
			}
		}

	}
}
