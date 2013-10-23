package sesame.mocap;

import java.util.logging.Logger;

import sesame.mocap.constant.NodeType;

public class Node {
	private static final Logger LOGGER = Logger.getLogger(
		    Thread.currentThread().getStackTrace()[0].getClassName() );
	
	private NodeType nodeType;
	
	private double x;
	private double y;
	private double z;
	
	public Node() {	}
	
	public Node(NodeType nodeType) {
		this();
		this.nodeType = nodeType;
	}
	
	public Node(NodeType nodeType, double x, double y) {
		this(nodeType);
		this.x = x;
		this.y = y;
	}
	
	public Node(NodeType nodeType, double x, double y, double z) {
		this(nodeType, x, y);
		this.z = z;
	}
	
	public int getId() {
		return nodeType.getId();
	}

	public void copyParameterFrom(Node node) {
		this.x = node.x;
		this.y = node.y;
		this.z = node.z;
	}

	public double getLocation(char coordinate) {
		switch(coordinate) {
		case 'x': case 'X': return x;
		case 'y': case 'Y': return y;
		case 'z': case 'Z': return z;
		}
		LOGGER.severe("invalid coordinate: " + coordinate);
		System.exit(-1);
		return -1;
	}
}