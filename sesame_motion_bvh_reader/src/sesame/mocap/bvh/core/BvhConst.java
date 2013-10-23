package sesame.mocap.bvh.core;

public class BvhConst {
	
	public final static boolean TEST = true;

	/**
	 * PARSER CONSTANT
	 */
	public final static int MAXDOF = 6;
	
	public final static String HIERARCHY = "HIERARCHY";
	
	public final static String ROOT = "ROOT";
	public final static String JOINT = "JOINT";
	public final static String ENDSITE = "End Site";
	
	public final static String OFFSET = "OFFSET";
	public final static String CHANNELS = "CHANNELS";
		
	public final static String X_POSITION = "Xposition";
	public final static String Y_POSITION = "Yposition";
	public final static String Z_POSITION = "Zposition";
	
	public final static String X_ROTATION = "Xrotation";
	public final static String Y_ROTATION = "Yrotation";
	public final static String Z_ROTATION = "Zrotation";
	
	public final static String CH_CONST[] = {X_POSITION, Y_POSITION, Z_POSITION,
									 X_ROTATION, Y_ROTATION, Z_ROTATION};
	
	public final static String MOTION = "MOTION";
	public final static String FRAMES = "Frames:";
	public final static String FRAME_TIME = "Frame Time:";
}
