package sesame.mocap.app;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

import javax.sound.sampled.Line;

import sesame.util.FileManager;

public class AverageThreeFramesCalculator {
	private static final int TOTALCOLUMNS = 30;
	private static final int TOTALFRAMES = 3;
	
	BigDecimal summation[][] = new BigDecimal[TOTALCOLUMNS ][TOTALFRAMES];	
	BigDecimal average[][] = new BigDecimal[TOTALCOLUMNS ][TOTALFRAMES];
	BigDecimal totalFrame = new BigDecimal("0");
	
	public static void main(String[] args) {
		String csvDir = "F:\\sesame_intern\\csv\\motion_output315";
		AverageThreeFramesCalculator avc = new AverageThreeFramesCalculator(csvDir);
	}
	
	protected AverageThreeFramesCalculator(String directory) {
		initialize();
		ArrayList<String> csvFileNames = FileManager.findAll(directory, "csv");
		print(csvFileNames);
		for(String csvFile: csvFileNames) {
			System.out.println("---- now reading: " + csvFile);
			addData(csvFile);
		}
		print(summation);
		System.out.println(totalFrame);
		computeAverage();
		print(average);
	}

	private void print(BigDecimal bd[][]) {
		System.out.println("-------------------------------------");
		for(int i=0; i<TOTALFRAMES; i++) {
			System.out.println("nextFrame --->" + i);
			for(int j=0; j<TOTALCOLUMNS; j++)
				System.out.print(bd[j][i] + ", ");
			System.out.println();
		}
		System.out.println("-------------------------------------");
	}
	
	private void print(BigDecimal bd[]) {
		System.out.println("-------------------------------------");
		for(int i=0; i<bd.length; i++)
			System.out.print(bd[i] + ", ");
		System.out.println();
		System.out.println("-------------------------------------");
	}
	
	private void print(ArrayList<String> arr) {
		System.out.println("-------------------------------------");
		for(String str: arr)
			System.out.println(str);
		System.out.println();
		System.out.println("-------------------------------------");
	}
	
	private void initialize() {
		for(int i=0; i<TOTALFRAMES; i++)
			for(int n = 0; n<TOTALCOLUMNS; n++)
				summation[n][i] = new BigDecimal("0");
	}
	
	protected void addData(String csvFileName) {
	    try {
			BufferedReader br = new BufferedReader(new FileReader(csvFileName));
			skipCommentLine(br);
			skipHeader(br);
			
			String line;
			int skip = 0;
			while(true) {
				line = br.readLine();
				if(line == null) break;
				if( skip++ %24 == 0) {
					if(skip/24 >= TOTALFRAMES) break;
					else addDataRow(line, skip/24);
				}
			}
			
		} catch (FileNotFoundException e) {
			System.out.println("invalid filename.");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("no more line to be read");
		}
	}
	
	private void computeAverage() {
		for(int i=0; i<TOTALFRAMES; i++)
			for(int n=0; n<TOTALCOLUMNS ; n++)
				average[n][i] = summation[n][i].divide(totalFrame, 4, RoundingMode.HALF_UP);
	}

	protected void addDataRow(String line, int frame) {
		totalFrame = totalFrame.add(new BigDecimal("1"));
		
		String tokens[] = line.split(",");
		for(int t = 1; t<tokens.length; t++) {
			System.out.print(tokens[t] + ", ");
			summation[t-1][frame] = summation[t-1][frame].add(new BigDecimal(tokens[t]));
		}
		System.out.println();
	}
	
	protected void skipCommentLine(BufferedReader br) {
		skipLines(br, 2);
	}
	
	protected void skipHeader(BufferedReader br) {
		skipLines(br, 1);
	}
	
	private void skipLines(BufferedReader br, int totalLines) {
		try {
			for(int i=0; i<totalLines; i++)
				br.readLine();
		} catch (IOException e) {
			System.out.println("no more line to be read");
			e.printStackTrace();
		}
	}
	
	
}