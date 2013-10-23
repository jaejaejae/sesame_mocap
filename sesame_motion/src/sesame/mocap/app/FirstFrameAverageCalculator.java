package sesame.mocap.app;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class FirstFrameAverageCalculator extends AverageCalculator {
	
	public static void main(String[] args) {
		String csvDir = "F:\\sesame_intern\\csv\\motion_output315";
		FirstFrameAverageCalculator avc = new FirstFrameAverageCalculator(csvDir);
	}
	
	private FirstFrameAverageCalculator(String directory) {
		super(directory);
	}
	
	@Override
	protected void addData(String csvFileName) {
	    try {
			BufferedReader br = new BufferedReader(new FileReader(csvFileName));
			skipCommentLine(br);
			skipHeader(br);
			
			String line;
			line = br.readLine();
		} catch (FileNotFoundException e) {
			System.out.println("invalid filename.");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("no more line to be read");
		}
	}
}
