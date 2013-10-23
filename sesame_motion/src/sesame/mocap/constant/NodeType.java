package sesame.mocap.constant;

public enum NodeType {
	Hip(0),
	Head(1),
	LeftArm(2),
	LeftHand(3),
	RightArm(4),
	RightHand(5),
	LeftKnee(6),
	LeftFoot(7),
	RightKnee(8),
	RightFoot(9);
	
	private int nodeId;

	public static final String[][] BVHNODENAME = {
		{"ROOT", "Hips"},
		{"HEADEND"},
		{"L_LOWER_ARM", "LeftForeArm"},
		{"L_HAND", "LeftHand"},
		{"R_LOWER_ARM", "RightForeArm"},
		{"R_HAND", "RightHand"},
		{"L_LOWER_LEG", "LeftLeg"},
		{"L_FOOT", "LeftFoot"},
		{"R_LOWER_LEG", "RightLeg"},
		{"R_FOOT", "RightFoot"}};
	
	NodeType(int id) {
		this.nodeId = id;
	}
	public static int getTotalNodes() { return NodeType.values().length; }
	
	public int getId() { return nodeId; }
	public String[] getBvhEquivalentNames() { return BVHNODENAME[nodeId];}
	public String getName() { return this.toString(); }
}
