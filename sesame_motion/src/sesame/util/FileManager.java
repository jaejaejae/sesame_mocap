package sesame.util;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;

public class FileManager {
	private static ArrayList<String> returnedFilenames;	
	
	public static ArrayList<String> findAll(String directory, String extension) {
		returnedFilenames = new ArrayList<String>();
		findAllFilenames(addSlashAtTheBack(directory), extension);
		return returnedFilenames;
	}
	
	private static String addSlashAtTheBack(String string) {
		return string.endsWith("\\") ? string : string + "\\";
	}
	
	private static void findAllFilenames(String directory, String extension) {
		String[] subdirectories = getAllSubdirectories(directory);		
		for(String subdirectory: subdirectories)
			findAllFilenames(subdirectory, extension);
		String[] fileNames = getAllFiles(directory, extension);
		addToReturnedFileNames(fileNames);
	}
	
	private static void addToReturnedFileNames(String[] fileNames) {
		for(String fileName: fileNames)
			returnedFilenames.add(fileName);
	}

	private static String[] getAllSubdirectories(String directory) {
		File file = new File(directory);
		String[] directories = file.list(new FilenameFilter() {
		  public boolean accept(File dir, String name) {
		    return new File(dir, name).isDirectory();
		  }
		});
		String[] fullDirectorySubDirectories = new String[directories.length];
		for(int i=0; i<directories.length; i++) {
			fullDirectorySubDirectories[i] = directory + directories[i] + "\\";
		}
		
		return fullDirectorySubDirectories;
	}
	
	private static String[] getAllFiles(String directory, final String extension) {
		File file = new File(directory);
		String[] filenamesWithExtension = file.list(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.endsWith(extension);
			}
		});
		String[] fullDirectoryFilenamess = new String[filenamesWithExtension.length];
		for(int i=0; i<filenamesWithExtension.length; i++)
			fullDirectoryFilenamess[i] = directory + filenamesWithExtension[i];
		return fullDirectoryFilenamess;
	}

}
