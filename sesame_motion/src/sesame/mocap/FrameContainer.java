package sesame.mocap;

import sesame.mocap.constant.NodeType;

public class FrameContainer {

	private Node[] nodes;
	
	public FrameContainer() {
		initializeNode();
	}

	private void initializeNode() {
		nodes = new Node[NodeType.getTotalNodes()];
		for( NodeType nodeType: NodeType.values() )
			nodes[nodeType.getId()] = new Node( nodeType );
	}
	
	public void setNode(Node node) {
		int nodeId = node.getId();
		nodes[nodeId].copyParameterFrom(node);
	}
	
	private Node getNode(NodeType nodeType) { return nodes[nodeType.getId()]; }

	public double getLocation(NodeType nodeType, char coordinate) {
		Node node = getNode(nodeType);
		return node.getLocation(coordinate);
	}
	
	public double getLocation(int nodeId, char coordinate) {
		Node node = nodes[nodeId];
		return node.getLocation(coordinate);
	}
}