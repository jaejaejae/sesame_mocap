package sesame.mocap.bvh.app;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.logging.Logger;

import sesame.mocap.Mocap10Nodes;
import sesame.mocap.app.Mocap10NodeFileWriter;
import sesame.mocap.bvh.core.BvhMocap;
import sesame.movap.bvh.parser.BvhFileParser;
import sesame.movap.bvh.parser.BvhMocapToMocap10NodesConverter;

public class BvhToTrajectoryConvertor {
	private static final Logger LOGGER = Logger.getLogger(
		    Thread.currentThread().getStackTrace()[0].getClassName() );
	
	private String parentDirectory;
	private String outputDirectory;
	private Setting setting;
	private ArrayList<String> bvhFilenames;
	
	public static void main(String[] args) {
		try {
			Setting setting = Setting.getSetting(args);
			new BvhToTrajectoryConvertor(setting);
		} catch (Exception e) {
			System.out.println(Setting.getMenu());
			e.printStackTrace();
		}
	}
	
	private BvhToTrajectoryConvertor(Setting setting) {
		this.setting = setting;	
		this.parentDirectory = setting.getParentDirectory();
		LOGGER.info("parent directory: " + parentDirectory);
		this.outputDirectory = setting.getOutputDirectory();
		LOGGER.info("output directory: " + outputDirectory);
		bvhFilenames = new ArrayList<String>();
		
		findAllBvhFilenames();
		beginWritingToFile();
	}
	
	private String format(String directory) {
		if(!directory.endsWith("\\"))
			return directory + "\\";
		return directory;
	}

	private void beginWritingToFile() {
		for(String bvhFilename: bvhFilenames) {
			LOGGER.info("Start writing: " + bvhFilename);
			BvhMocap mocap = new BvhMocap();
			BvhFileParser.parse(bvhFilename, mocap);
			
			if(setting.isNormalized()) mocap.normalize();
			mocap.rotate(Math.toRadians(setting.getDegree()));
			
			BvhMocapToMocap10NodesConverter convertor =
					new BvhMocapToMocap10NodesConverter(mocap);
			Mocap10Nodes convertedMocap = convertor.getBvhMocap10Nodes();
			Mocap10NodeFileWriter.writeToFile(convertedMocap, getOutputFilename(bvhFilename));
		}
	}

	private String getOutputFilename(String bvhFilename) {
		return getOutputDirectory(bvhFilename) + getBvhFilenameOnly(bvhFilename);
	}
	
	private String getOutputDirectory(String bvhFilename) {
		StringBuilder sb = new StringBuilder();
		sb.append(outputDirectory);							// -- end with '/'
		sb.append(getOutputSubdirectoryOf(bvhFilename));	// -- end with '/'	
		return sb.toString();
	}

	private String getOutputSubdirectoryOf(String bvhFilename) {
		String directory = removeBvhFilename(bvhFilename);
		String outputDirectory = removeParentDirectory(directory);
		return format(outputDirectory);
	}

	private String removeParentDirectory(String bvhFilename) {
		return bvhFilename.substring(parentDirectory.length() + 1);
	}
	
	private String removeBvhFilename(String bvhFilename) {
		return new File(bvhFilename).getParentFile().toString();		// - does not end with '/'
	}
	
	private String getBvhFilenameOnly(String bvhFilename) {
		String directory = format(new File(bvhFilename).getParentFile().toString());
		LOGGER.info("directory of" + bvhFilename +" : " + directory);
		String bvhFilenameWithoutExtension = bvhFilename.substring(directory.length()
			, bvhFilename.lastIndexOf(".bvh"));
		return bvhFilenameWithoutExtension;
	}

	private void findAllBvhFilenames() {
		findAllBvhFilenames(parentDirectory);
	}
	
	private void findAllBvhFilenames(String directory) {
		String[] subdirectories = getAllSubdirectories(directory);		
		for(String subdirectory: subdirectories) {
			LOGGER.info("Now looking at: " + subdirectory);
			findAllBvhFilenames(subdirectory);
		}
		String[] bvhFiles = getAllBvhFiles(directory);
		addToBvhFileNames(bvhFiles);
	}
	
	private void addToBvhFileNames(String[] bvhFiles) {
		for(String bvhFile: bvhFiles) {
			LOGGER.info("BvhFilename Found: " + bvhFile);
			bvhFilenames.add(bvhFile);
		}
	}

	private String[] getAllSubdirectories(String directory) {
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
	
	private String[] getAllBvhFiles(String directory) {
		File file = new File(directory);
		String[] bvhFiles = file.list(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.endsWith(".bvh");
			}
		});
		String[] fullDirectoryBvhFiles = new String[bvhFiles.length];
		for(int i=0; i<bvhFiles.length; i++)
			fullDirectoryBvhFiles[i] = directory + bvhFiles[i];
		return fullDirectoryBvhFiles;
	}
	
	static class Setting {
		private String parentDirectory;
		private String outputDirectory;
		
		private boolean normalize = false;
		private double degree = 0;
		
		public static Setting getSetting(String[] args) throws Exception {
			Setting setting = new Setting();
			int totalArguments = args.length;
			for(int i=0; i<args.length - 2; i++)
				setting.parseSetting(args[i]);
			
			int parentDirIndex = totalArguments - 2;
			setting.setParentDirectory(args[parentDirIndex]);
			int outputDirIndex = totalArguments - 1;
			setting.setOutputDirectory(args[outputDirIndex]);
			
			return setting;
		}

		public void setParentDirectory(String directory) { parentDirectory = format(directory); }
		public void setOutputDirectory(String directory) { outputDirectory = format(directory); }
		
		public String getParentDirectory() { return parentDirectory; }
		public String getOutputDirectory() { return outputDirectory; }
		public boolean isNormalized() { return normalize; }
		public double getDegree() { return degree; }
		
		private void parseSetting(String argument) throws Exception {
			if(argument.equalsIgnoreCase("-n")) normalize = true;
			else if(argument.contains("-r")) degree = Double.parseDouble(argument.substring(2));
			else throw new Exception("Invalid arguments\n");
		}

		private String format(String directory) {
			if(!directory.endsWith("\\"))
				return directory + "\\";
			return directory;
		}
		
		private static String getMenu() {
			return
					"Argument format:\n" +
					"[options] inputDirectory outputDirectory\n" +
					"Possible options:\n" +
					"-n enable normalization (default: false)\n" +
					"-r%number% rotate(degree) (default: 0) for example, -r100\n)";
		}
		
	}
}
