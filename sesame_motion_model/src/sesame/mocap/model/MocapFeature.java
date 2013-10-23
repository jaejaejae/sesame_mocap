package sesame.mocap.model;

import sesame.mocap.FeatureParameter;
import sesame.mocap.FrameContainer;
import sesame.mocap.constant.FeatureType;
import sesame.mocap.constant.NodeType;

public class MocapFeature {
	FrameContainer frameContainers[];
	FeatureParameter parameter;
	
	public MocapFeature() {
		this(FeatureParameter.getDefaultParameter());
	}
	
	public MocapFeature( FeatureParameter featureParameter ) {
		parameter = featureParameter;
		frameContainers = new FrameContainer[featureParameter.getNumberOfTotalFrameContainers()];
	}

	public void set(int currentFrame, NodeType nodeType, double xFeature,
			double yFeature, double zFeature) {		
	}
	
	public FeatureType getFeatureType() { return parameter.getFeatureType(); }
	public int getTotalFrame() { return parameter.getTotalFrames(); }
	public void setFrameContainer(int f, FrameContainer frameContainer) { frameContainers[f] = frameContainer; }
	public double getThreshold() { return parameter.getThreshold(); }
	public int getTotalFeature() { return parameter.getTotalFeatures(); }
	
	public double getFeatureValue(int index) {
		int totalNodes = parameter.getTotalNode();
		int frame = index/totalNodes;
		int nodeId = index - frame*totalNodes;
		char coordinate = getCoordinate(index);
		
		return frameContainers[frame].getLocation( nodeId, coordinate);
	}
	
	private char getCoordinate(int index) {
		switch(index % parameter.getNumberOfCoordinate()) {
		case 0: return 'x';
		case 1: return 'y';
		case 2: return 'z';
		default: return 'a';
		}
	}
}
