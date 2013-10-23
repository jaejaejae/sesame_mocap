package sesame.mocap.bvh.core;

import java.util.ArrayList;
import java.util.HashMap;

import org.ejml.simple.SimpleMatrix;

public class BvhMocap {
	private int totalFrame;
	ArrayList<BvhNode> nodes;
	HashMap<String, BvhNode> nodesMap;
	BvhNode headEnd;

	double frameTime;

	public BvhMocap() {
		nodes = new ArrayList<BvhNode>();
		nodesMap = new HashMap<String, BvhNode>();
	}

	public void setTotalFrame(int totalFrame) {
		this.totalFrame = totalFrame;
		for (int n = 0; n < nodes.size(); n++)
			nodes.get(n).setTotalFrame(totalFrame);
	}

	public void setTime(double frameTime) {
		this.frameTime = frameTime;
	}

	public void addNode(BvhNode node) {
		nodes.add(node);
		nodesMap.put(node.getName(), node);
	}

	public int getTotalFrame() {
		return totalFrame;
	}

	public int getTotalNode() {
		return nodes.size();
	}

	public BvhNode getNode(int n) {

		return nodes.get(n);
	}

	public BvhNode getNode(String nodeName) {
		if (nodesMap.containsKey(nodeName))
			return nodesMap.get(nodeName);
		else {
			System.out
					.println("BVHNode::getNode() " + nodeName + " not found.");
			return null;
		}
	}

	public void updateLocation() {
		for (int n = 0; n < nodes.size(); n++) {
			nodes.get(n).computeLocation();
		}
	}

	public void rotate(double rad) {
		for (BvhNode node : nodes)
			node.rotate(rad);
	}

	private void translate(double deltaX, double deltaY, double deltaZ) {
		for (BvhNode node : nodes)
			node.translate(deltaX, deltaY, deltaZ);
	}

	public void normalize(BvhMocapNormalizer bvhMocapNormalizer) {
		bvhMocapNormalizer.normalize(this);
	}
	
	public void normalize() {
		makeHeadOrigin();
		scaleHeightTo(100.0);
	}

	private void interpolate(int totalExpectedFrames) {
		for (BvhNode node : nodes) {
			node.setTotalFrame(totalExpectedFrames);
		}
	}

	private void makeHeadOrigin() {
		BvhNode headEnd = this.getNode("HeadEND");
		SimpleMatrix headLocation = headEnd.getLocation(0);
		double deltaX = headLocation.get(0, 0);
		double deltaY = headLocation.get(1, 0);
		double deltaZ = headLocation.get(2, 0);
		this.translate(-deltaX, -deltaY, -deltaZ);
	}

	private void scaleHeightTo(double newHeight) {
		double height = this.computeHeight();
		for (BvhNode node : nodes) {
			node.scale(newHeight / height);
		}
	}

	private double computeHeight() {
		BvhNode headEnd = this.getNode("HeadEND");
		// getLeftFoot
		BvhNode leftFoot = this.getNode("L_FootEND");
		if (leftFoot == null)
			leftFoot = this.getNode("LeftToeBaseEND");
		// getRightFoot
		BvhNode rightFoot = this.getNode("R_FootEND");
		if (rightFoot == null)
			rightFoot = this.getNode("RightToeBaseEND");

		double headEndY = headEnd.getLocation(0).get(1, 0);
		double leftFootY = leftFoot.getLocation(0).get(1, 0);
		double rightFootY = rightFoot.getLocation(0).get(1, 0);

		double meanFootY = (leftFootY + rightFootY) / 2;

		return Math.abs(headEndY - meanFootY);
	}

	public double getFps() {
		return 1.0 / frameTime;
	}
}
