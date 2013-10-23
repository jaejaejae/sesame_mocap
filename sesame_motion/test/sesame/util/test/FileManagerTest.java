package sesame.util.test;

import java.util.ArrayList;

import sesame.util.FileManager;

public class FileManagerTest {
	public static void main(String[] args) {
		try {
			ArrayList<String> outputs = FileManager.findAll("F:\\sesame_intern\\csv\\motion_output0", "csv");
			
			for(String output: outputs)
				System.out.println(output);
		}
		catch(Exception e) {
			System.out.println("invalid directory");
		}
	}
}
