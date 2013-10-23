package sesame.mocap;

import sesame.mocap.constant.NodeType;

public class Mocap10Nodes {
	
	double fps;
	FrameContainer[] frameContainers;
		
	public Mocap10Nodes( int totalNumberOfFrames ) {
		setTotalFrames( totalNumberOfFrames );
	}
	
	public Mocap10Nodes (Mocap10NodesSetupUtil setupUtility) {
		this( setupUtility.getTotalFrames() );
		setupUtility.setup( this );
	}
	
	private void setTotalFrames( int totalNumberOfFrames) {
		frameContainers = new FrameContainer[totalNumberOfFrames];
		initFrameContainers();
	
	}
	
	public void setFps(double fps) {
	this.fps = fps;
	}

	public void addNode(Node node, int frame) {
		frameContainers[frame].setNode(node);
	}
	
	private void initFrameContainers() {
		for(int i=0; i<frameContainers.length; i++)
			frameContainers[i] = new FrameContainer();
	}

	public int getTotalFrame() {
		return frameContainers.length;
	}

	public double getFps() {
		return fps;
	}

	public double getLocation(NodeType nodeType, int f, char coordinate) {
		FrameContainer frame = frameContainers[f];
		return frame.getLocation(nodeType, coordinate);
	}
	
	public FrameContainer getFrameContainer(int f) { return frameContainers[f];}
	public FrameContainer getLastFrameContainer() {	return frameContainers[frameContainers.length - 1]; }
}