package sesame.mocap;

import sesame.mocap.constant.CoordinateSystem;
import sesame.mocap.constant.FeatureType;
import sesame.mocap.constant.FillingMethod;
import sesame.mocap.constant.NodeType;

public class FeatureParameter {
	
	private int fps = 5;
	private int totalTime = 2;
	private int totalFrames = fps * totalTime;
	private NodeType root = NodeType.Head;
	private FeatureType featureType = FeatureType.Position;
	private FillingMethod fillingMethod = FillingMethod.Interpolate;
	private CoordinateSystem coordinateSystem = CoordinateSystem.xy;
	private double threshold = 0.01;
	
	
	public static FeatureParameter getDefaultParameter() {
		return new FeatureParameter();
	}
	
	private FeatureParameter() {}
	
	public void setFps(int fps) { this.fps = fps; }
	public void setTotalTime(int totalTime) {this.totalTime = totalTime; }
	public void setRoot(NodeType root) { this.root = root; }
	public void setFeatureType(FeatureType featureType) { this.featureType = featureType; }
	public void setFillingMethod(FillingMethod fillingMethod) { this.fillingMethod = fillingMethod; }

	public double getThreshold() { return threshold; }
	public int getFps() { return fps; }
	public int getTotalTime() { return totalTime; }
	public int getTotalFrames() { return totalFrames; }
	public int getNumberOfTotalFrameContainers() {
		if(featureType == FeatureType.Delta) return totalFrames-1;
		else return totalFrames;
	}
	public FeatureType getFeatureType() { return featureType; }
	public NodeType getRoot() { return root; }
	public FeatureType featureType() { return featureType; }
	public FillingMethod getFillingMethod() { return fillingMethod; }
	public int getTotalFeatures() {	return NodeType.getTotalNodes() * featureType.getTotalFeaturesPerNode(totalFrames); }
	public int getTotalNode() { return NodeType.getTotalNodes(); }
	public int getNumberOfCoordinate() { return coordinateSystem.getTotalCoordinates(); }
}
