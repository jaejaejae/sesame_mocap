package sesame.mocap.constant;

public enum FeatureType {
	Delta,
	Position;

	FeaturesPerNodeCalculator calculator;

	FeatureType() {
		initializeCalculator();
	}
	
	
	private void initializeCalculator() {
		switch(this) {
		case Delta: calculator = new DeltaFeaturesPerNodeCalculator(); break;
		default: calculator = new FeaturesPerNodeCalculator();
		}
	}


	public int getTotalFeaturesPerNode( int totalFrames ) { return calculator.getNumberOfFeaturesPerNodes(totalFrames); }
	
	class FeaturesPerNodeCalculator {
		public FeaturesPerNodeCalculator() {}
		public int getNumberOfFeaturesPerNodes(int totalFrames) { return totalFrames; }
	}
	
	class DeltaFeaturesPerNodeCalculator extends FeaturesPerNodeCalculator{
		public DeltaFeaturesPerNodeCalculator() {}
		public int getNumberOfFeaturesPerNodes(int totalFrames) { return (totalFrames-1); }	
	}
}
