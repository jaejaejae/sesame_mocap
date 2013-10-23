package sesame.mocap.app;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Logger;

import sesame.mocap.Mocap10Nodes;
import sesame.mocap.constant.NodeType;

public class Mocap10NodeFileWriter {
	private static final Logger LOGGER = Logger.getLogger(
		    Thread.currentThread().getStackTrace()[0].getClassName() );
	
	String destination;
	Mocap10Nodes mocap;
	
	public static void writeToFile(Mocap10Nodes mocap, String destination) {
		new Mocap10NodeFileWriter(mocap, destination);
	}
	
	private Mocap10NodeFileWriter(Mocap10Nodes mocap, String destination) {
		this.mocap = mocap;
		this.destination = destination + ".csv";
		
		writeToFile();
	}

	private String createComments() {
		StringBuffer comments = new StringBuffer();
		comments.append("#Total Frames: " + mocap.getTotalFrame() + '\n');
		comments.append("#FPS: " + mocap.getFps() + '\n');
		return comments.toString();
	}
	
	private String createCoordinates() {
		StringBuffer data = new StringBuffer();
		for(int f = 0; f<mocap.getTotalFrame(); f++) {
			data.append(f);
			for(NodeType nodeType: NodeType.values()) {
				data.append("," + mocap.getLocation(nodeType, f, 'x'));
				data.append("," + mocap.getLocation(nodeType, f, 'y'));
				data.append("," + mocap.getLocation(nodeType, f, 'z'));
			}
			data.append('\n');
		}
		return data.toString();
	}
	
	private String createHeader() {
		StringBuffer header = new StringBuffer();
		header.append("Frame");
		for(NodeType nodeType: NodeType.values()) {
			header.append(',' + nodeType.getName() + "_x");
			header.append(',' + nodeType.getName() + "_y");
			header.append(',' + nodeType.getName() + "_z");
		}
		header.append('\n');
		return header.toString();
	}
	
	private void writeToFile() {
		File file = new File(destination);
		try {
			FileWriter fStream = new FileWriter(destination);
			BufferedWriter out = new BufferedWriter(fStream);
			out.write(createComments());
			out.write(createHeader());
			out.write(createCoordinates());
			out.close();
		} catch (IOException e) {
			LOGGER.warning("Cannot load file: " + destination);
			
			File theDir = new File(file.getParent());
			if(!theDir.exists()) {
				boolean result = theDir.mkdirs();
				LOGGER.warning("Create directory( " + theDir + " ): " + result);
				writeToFile();
			}
			else {
				System.exit(-1);
				LOGGER.severe("Unable to create the directory: " + theDir);
				e.printStackTrace();
			}
		}
	}
}
