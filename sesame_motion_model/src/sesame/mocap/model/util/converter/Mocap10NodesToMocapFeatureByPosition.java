package sesame.mocap.model.util.converter;

import sesame.mocap.Mocap10Nodes;
import sesame.mocap.model.MocapFeature;

public class Mocap10NodesToMocapFeatureByPosition {
	Mocap10Nodes mocap;
	MocapFeature feature;
	
	public Mocap10NodesToMocapFeatureByPosition(Mocap10Nodes mocap, MocapFeature feature) {
		this.mocap = mocap;
		this.feature = feature;
	 
		beginConverting();
	}
	
	public void beginConverting() {
		double interval = computeInterval();
		
		for(int f=0; f< feature.getTotalFrame()-1; f++) {
			int matchingFrame = round(f*interval);
			feature.setFrameContainer(f, mocap.getFrameContainer(matchingFrame));
		}
		feature.setFrameContainer(feature.getTotalFrame()-1, mocap.getLastFrameContainer());
	}
	
	private double computeInterval() { return (mocap.getTotalFrame() - 1)/ (feature.getTotalFrame() - 1); }
	
	private int round(double number) { return (int)(number + 0.5); }
}
