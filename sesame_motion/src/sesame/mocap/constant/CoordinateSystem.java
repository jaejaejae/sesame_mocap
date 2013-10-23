package sesame.mocap.constant;

public enum CoordinateSystem {
	xy,
	xyz;
	
	public int getTotalCoordinates() {
		switch(this) {
		case xy: return 2;
		case xyz: return 3;
		default: return -1;
		}
	}
}
