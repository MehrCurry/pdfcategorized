package de.gzockoll.pdfcategorizer.io;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.io.FileUtils;

public class WatchDir {

	private Map<File, Long> fingerprints;

	public static void main(String[] args) {
		new WatchDir().watch(new File("/tmp"));
	}

	private void watch(File file) {
		List<File> fileList = Arrays.asList(file.listFiles());
		fingerprints = new HashMap<File, Long>();
		updateFingerprints(fileList);
		for (;;) {
			List<File> newList = Arrays.asList(file.listFiles());
			Collection<File> deleteFiles = CollectionUtils.subtract(fileList,
					newList);
			Collection<File> newFiles = CollectionUtils.subtract(newList,
					fileList);
			Collection<File> changedFiles = CollectionUtils.intersection(
					fileList, newFiles);
			CollectionUtils.filter(changedFiles, new Predicate() {

				@Override
				public boolean evaluate(Object arg0) {
					File f = (File) arg0;
					return fingerprints.get(f) != null
							&& fingerprints.get(f).equals(f.hashCode());
				}
			});
			System.out.println("Deleted: " + deleteFiles);
			System.out.println("New: " + newFiles);
			System.out.println("Changed: " + changedFiles);
			fileList = newList;
			updateFingerprints(fileList);
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				break;
			}
		}
	}

	private void updateFingerprints(List<File> fileList) {
		fingerprints.clear();
		for (File f : fileList)
			try {
				if (f.isFile())
					fingerprints.put(f, FileUtils.checksumCRC32(f));
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
}