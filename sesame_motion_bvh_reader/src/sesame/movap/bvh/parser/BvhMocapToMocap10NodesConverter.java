package sesame.movap.bvh.parser;

import java.util.logging.Logger;

import sesame.mocap.Mocap10Nodes;
import sesame.mocap.Node;
import sesame.mocap.bvh.core.BvhMocap;
import sesame.mocap.bvh.core.BvhNode;
import sesame.mocap.constant.NodeType;

public class BvhMocapToMocap10NodesConverter {

	private static final Logger LOGGER = Logger.getLogger(
		    Thread.currentThread().getStackTrace()[0].getClassName() );
	
	private BvhMocap bvhMocap;
	private Mocap10Nodes bvhMocap10Nodes;
	
	public BvhMocapToMocap10NodesConverter(BvhMocap bvhMocap) {
		this.bvhMocap = bvhMocap;
		beginConverting();
	}
	
	public Mocap10Nodes getBvhMocap10Nodes() {
		return bvhMocap10Nodes;
	}
	
	private void beginConverting() {
		bvhMocap10Nodes = new Mocap10Nodes(bvhMocap.getTotalFrame());
		bvhMocap10Nodes.setFps(bvhMocap.getFps());
		for(int t=0; t<bvhMocap.getTotalFrame(); t++) {
			for(NodeType nodeType: NodeType.values()) {
				BvhNode matchingNode = findMatchingBvhNode(bvhMocap, nodeType);
				Node node = new Node(nodeType, matchingNode.getX(t), matchingNode.getY(t), matchingNode.getZ(t));
				bvhMocap10Nodes.addNode(node, t);
			}
		}
	}
	
	private BvhNode findMatchingBvhNode(BvhMocap bvhMocap, NodeType nodeType) {
		for(int n=0; n<bvhMocap.getTotalNode(); n++) {
			BvhNode bvhNode = bvhMocap.getNode(n);
			for( String alternativeNodeName: nodeType.getBvhEquivalentNames() )
				if ( bvhNode.getName().equalsIgnoreCase(alternativeNodeName) )
					return bvhNode;
		}
		LOGGER.severe("can't find matching bvhNode for " + nodeType.toString());
		System.exit(-1);
		return null;
	}
}
