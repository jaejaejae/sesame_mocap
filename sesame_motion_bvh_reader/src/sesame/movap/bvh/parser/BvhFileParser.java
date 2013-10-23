package sesame.movap.bvh.parser;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;
import java.util.Stack;

import sesame.mocap.bvh.core.BvhConst;
import sesame.mocap.bvh.core.BvhMocap;
import sesame.mocap.bvh.core.BvhNode;



public class BvhFileParser extends BvhConst {

	private static BvhMocap motion;
	private static Stack<BvhNode> bvhnodes;
	private static Scanner scanner;

	public static void parse(String filename, BvhMocap mocap_) {
		motion = mocap_;
		setupScanner(filename);
		bvhnodes = new Stack<BvhNode>();
		startParsing();
	}

	private static void setupScanner(String filename) {
		try {
			scanner = new Scanner(new FileReader(filename));
		} catch (FileNotFoundException e) {
			System.out.println("invalid filename: " + filename);
			System.exit(-1);
			e.printStackTrace();
		}
	}

	private static void startParsing() {
		parseNodes();
		parseMotionParam();
		parseFrame();
	}

	private static void parseFrame() {
		int line = 0;
		while(scanner.hasNextLine()) {
			String str = scanner.nextLine();
			str.trim();
			
			String delim = "[ ]+";
			
			if(TEST)
				System.out.println(str);
			
			String tokens[] = str.trim().split(delim);
			
			if(TEST)
				System.out.println("tokens[3] " + tokens[3]);
			
			int t=0;
			
			for(int n=0; n<motion.getTotalNode(); n++) {
				BvhNode node = motion.getNode(n);
				for(int ch=0; ch<node.getTotalChannel(); ch++) {
					if(TEST)
						System.out.println(tokens[t]);
					node.setChannelParam(ch, line, Double.parseDouble(tokens[t++]));
				}
			}
			line++;
		}
		if(line!=motion.getTotalFrame()) {
			System.out.println("BVHParser: missing frame.");
			System.exit(-1);
		}
	}

	private static void parseNodes() {
		readNextEqual(HIERARCHY);
		
		/**
		 * ROOT
		 */
		readNextEqual(ROOT);
		BvhNode node = new BvhNode();
		node.setName(scanner.next());
		bvhnodes.add(node);
		motion.addNode(node);
		
		readNextEqual("{");
		readNextEqual(OFFSET);
		
		node.setOffsetX(scanner.nextDouble());
		node.setOffsetY(scanner.nextDouble());
		node.setOffsetZ(scanner.nextDouble());
		
		readNextEqual(CHANNELS);
		
		int totalChannels = scanner.nextInt();
			
		while(totalChannels-- >0) {
			node.addChannel(scanner.next());
		}
		
		while(!bvhnodes.isEmpty()) {
			String nextNode = scanner.next();
			
			if(TEST)
				System.out.println(nextNode);
			/**
			 * JOINT
			 */
			if(nextNode.equalsIgnoreCase(JOINT)) {
				node = new BvhNode();
				node.setName(scanner.next());
				node.setParent(bvhnodes.peek());
				bvhnodes.push(node);
				motion.addNode(node);
				
				readNextEqual("{");
				
				readNextEqual(OFFSET);
				node.setOffsetX(scanner.nextDouble());
				node.setOffsetY(scanner.nextDouble());
				node.setOffsetZ(scanner.nextDouble());
				
				readNextEqual(CHANNELS);
				totalChannels = scanner.nextInt();
				while(totalChannels-- >0) {
					node.addChannel(scanner.next());
				}
			}
			//ENDSITE
			else if(nextNode.equals("End")) {
				readNextEqual("Site");
				
				node = new BvhNode();
				node.setParent(bvhnodes.peek());
				node.setName(node.getParent().getName() + "END");
				motion.addNode(node);
				bvhnodes.push(node);
				
				readNextEqual("{");
				readNextEqual(OFFSET);
				
				node.setOffsetX(scanner.nextDouble());
				node.setOffsetY(scanner.nextDouble());
				node.setOffsetZ(scanner.nextDouble());
			}
			else if(nextNode.equalsIgnoreCase("}")) {
				bvhnodes.pop();
			}
			else {
				System.out.println("BVHParser: " + CHANNELS + " " + ENDSITE + " or } not found.");
				System.exit(-1);
			}
		}
	}

	private static void parseMotionParam() {
		readNextEqual(MOTION);
		readNextEqual(FRAMES);
		motion.setTotalFrame(scanner.nextInt());
		readNextEqual("Frame");
		readNextEqual("Time:");
		motion.setTime(scanner.nextDouble());
		scanner.nextLine();					//dummy
	}


	private static void readNextEqual(String nextExpectedString) {
		String next = scanner.next();
		if(TEST)
			System.out.println(next);
		if (!next.equalsIgnoreCase(nextExpectedString)) {
			System.out.println("BVHParser: " + nextExpectedString
					+ " not found.");
			System.exit(-1);
		}
	}
}
