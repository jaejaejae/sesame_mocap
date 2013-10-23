package sesame.mocap.bvh.core;

public abstract class BvhMocapNormalizer {
	double afterNormalizedHeight = 100.0;
	
	public abstract void normalize(BvhMocap bvhMocap);
}
