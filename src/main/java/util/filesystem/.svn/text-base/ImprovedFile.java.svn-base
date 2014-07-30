package util.filesystem;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class ImprovedFile extends File {

	private static final long serialVersionUID = 6456545511856754841L;
	
	public ImprovedFile(String pathname) {
		super(pathname);
	}

	/**
	 * Recursively finds the files in this directory.
	 * Returns a list of filenames, relative to the current working directory.
	 * Filenames that represent directories end with a trailing slash.
	 * If this is not a directory, returns an empty list.
	 * @return a (possibly empty) list of file and directory names.
	 */	
	public List<String> findRecursively() {
		List<String> matchingFiles = new LinkedList<String>();
		if (this.isDirectory()) {
			for (String fileName : this.list()) {
				ImprovedFile file = new ImprovedFile(this.getPath() + "/" + fileName);
				if (file.isDirectory()) {
					matchingFiles.add(file.getPath() + "/");
					matchingFiles.addAll(file.findRecursively());
				} else {
					matchingFiles.add(file.getPath());
				}
			}
		}
		return matchingFiles;
	}
	
	/**
	 * Recursively finds the files in this directory which have the given extension.
	 * Returns a list of filenames, relative to the current working directory.
	 * If this is not a directory, returns an empty list.
	 * @param extension file extension to look for, either with or without the dot separator.
	 * @return a (possibly empty) list of file names with the given extension.
	 */	
	public List<String> findRecursivelyByExtension(String extension) {
		if (extension.startsWith(".")) extension = extension.substring(1);
		List<String> matchingFiles = new LinkedList<String>();
		for (String fileName : findRecursively()) {
			if (fileName.endsWith("." + extension)) matchingFiles.add(fileName);
		}
		return matchingFiles;
	}
	
	/**
	 * Recursively finds the files in this directory matching the given regular expressions.
	 * The file name must match the first regular expression, and not match the second regular expression.
	 * (Of course it is possible to do the same with one regular expression, but it would be unreadable.)
	 * If this is not a directory, or no matching files are found, returns an empty list.
	 * @param inclusionRegex Files that do not match this regular expression are excluded.
	 * @param exclusionRegex Files matching this regular expression are excluded.
	 * @return a (possibly empty) list of file names matching the given regular expressions.
	 */	
	public List<String> findRecursivelyByRegex(String inclusionRegex, String exclusionRegex) {
		List<String> matchingFiles = new LinkedList<String>();
		for (String fileName : findRecursively()) {
			if (fileName.matches(inclusionRegex) && (!fileName.matches(exclusionRegex))) matchingFiles.add(fileName);
		}
		return matchingFiles;
	}
	
	/**
	 * Recursively deletes the files in this directory.
	 */	
	public void deleteRecursively() {
		if (this.isDirectory()) {
			for (String fileName : this.list()) {
				ImprovedFile file = new ImprovedFile(this.getAbsolutePath() + "/" + fileName);
				file.deleteRecursively();
			}
		}
		this.delete();
	}
	
	public boolean isModifiedSince(long timeStamp) {
		if (this.lastModified() >= timeStamp) return true;
		else return false;
	}
}
