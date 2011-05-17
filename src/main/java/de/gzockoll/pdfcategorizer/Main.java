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
	FileScanner scanner = new FileScanner();

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		new Main().run();
	}

	public void run() {

		// File dir = new File("C:\\Users\\guidp\\Dropbox\\eaw\\Buchhaltung");
		// File dir = new File("X:\\Sites\\eaw\\documentLibrary\\Buchhaltung");
		File dir = new File("\\\\QNAP\\Qunsafe\\tmp\\scans");
		scanFile(dir);
	}

	private void scanFile(File f) {
		if (f.isDirectory()) {
			for (File sub : f.listFiles(new PdfFilter())) {
				scanFile(sub);
			}
		} else {
			DocumentInfo info = scanner.getInfo(f);
			// try {
			// info.setFileName(f.getCanonicalPath());
			// } catch (IOException e1) {
			// log.warn(e1);
			// }
			// System.out.println(f.getName() + ": "
			// + info);
			// String suggestedFileName = scanner
			// .suggestFileName(info);
			// System.out.println(suggestedFileName);
			// try {
			// String oldName = f.getCanonicalPath();
			// String newPathAndFileName =
			// createNewName("d:\\tmp\\Buchhaltung",info,scanner.suggestFileName(info));
			// System.out.println(newPathAndFileName);
			// String newFileName = oldName.replace(
			// FilenameUtils.getBaseName(oldName),
			// suggestedFileName);
			// FileUtils.copyFile(f, new File(newPathAndFileName));
			// System.out.println(newFileName);
			// if (!newFileName.endsWith(oldName))
			// f.renameTo(new File(newFileName));
			// } catch (IOException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }

		}
	}

	private String createNewName(String path, DocumentInfo info, String fileName) {
		return path + File.separator
				+ new SimpleDateFormat("yyyy").format(info.getDate())
				+ File.separator
				+ new SimpleDateFormat("MM").format(info.getDate())
				+ File.separator + info.getCategory().getName()
				+ File.separator + fileName + ".pdf";
	}

}
