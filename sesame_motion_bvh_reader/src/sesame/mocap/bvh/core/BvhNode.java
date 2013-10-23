package sesame.mocap.bvh.core;

import static sesame.mocap.bvh.core.BvhConst.*;

import java.util.ArrayList;
import java.util.HashMap;

import org.ejml.simple.SimpleMatrix;


public class BvhNode {
	
	private String name;
	private int maxFrame;
	private double offsetX, offsetY, offsetZ;
	private HashMap<String, Integer> channelOrders;
	private ArrayList<ArrayList<Double>> chParam;
	private BvhNode parent;
	private ArrayList<SimpleMatrix> locations;
	private boolean hadComputedLocation = false;
	 
	
	public BvhNode() {
		channelOrders = new HashMap<String, Integer>();
		parent = null;
		chParam = new ArrayList<ArrayList<Double>>(6);
		for(int i=0; i<6; i++) {
			chParam.add(new ArrayList<Double>());
		}
		locations = new ArrayList<SimpleMatrix>();
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public void setOffsetX(double offset) {
		this.offsetX = offset;
	}

	public void setOffsetY(double offset) {
		this.offsetY = offset;
	}

	public void setOffsetZ(double offset) {
		this.offsetZ = offset;
	}

	public void addChannel(String channelName) {
		channelOrders.put(channelName, channelOrders.size());
	}

	public void setParent(BvhNode parent) {
		this.parent = parent;
	}

	public int getTotalChannel() {
		return channelOrders.size();
	}

	public void setTotalFrame(int totalFrame) {
		maxFrame = totalFrame;
		for(int ch=0; ch<channelOrders.size(); ch++) {
			for(int f=0; f<maxFrame; f++) {
				chParam.get(ch).add(0.0);
			}
		}
		
		for(int f=0; f<maxFrame; f++) {
			locations.add(SimpleMatrix.identity(4));	//dummy simple matrix
		}
	}

	public String getName() {
		return this.name;
	}

	public void setChannelParam(int ch, int frame, double param) {
		chParam.get(ch).set(frame, param);
	}

	public void computeLocation() {
		for(int f=0; f<maxFrame; f++) {
			locations.set(f, getLocationFirstComputation(f));
			if(TEST)
				System.out.println(name + locations.get(f));
		}
	}
	
	public SimpleMatrix getLocation(int frame) {
		if(!hadComputedLocation) {
			computeLocation();
			hadComputedLocation = true;
		}
		return locations.get(frame);
	}
	
	public double getX(int frame) {
		return getLocation(frame).get(0, 0);
	}
	
	public double getY(int frame) {
		return getLocation(frame).get(1, 0);
	}
	
	public double getZ(int frame) {
		return getLocation(frame).get(2, 0);
	}
	
	
	public void rotate(double rad) {		
		double sin = Math.sin(rad);
		double cos = Math.cos(rad);
		double rotationY[][] = {{cos, 0, sin, 0},
							{0, 1, 0, 0},
							{-sin, 0, cos, 0},
							{0, 0, 0, 1}};
		SimpleMatrix rotationMatrix = new SimpleMatrix(rotationY);
		for(int f=0; f<maxFrame; f++) {
			locations.set(f, rotationMatrix.mult(locations.get(f)));
		}
	}
	
	private SimpleMatrix getLocationFirstComputation(int frame) {
		double[][] o = {{0, 0, 0, 1}};
		SimpleMatrix origin = new SimpleMatrix(o).transpose();
		return this.getLocalToWorld(frame). mult(origin);
	}
	
	private SimpleMatrix getLocalToWorld(int frame) {
		if(this.parent!=null)
			return parent.getTranslation(frame).mult(this.getMatrixOffset());
		else
			return this.getTransitionmatrix(frame).mult(this.getMatrixOffset());
		
	}
	
	private SimpleMatrix getTranslation(int frame) {
		return this.getLocalToWorld(frame).mult(this.getRotationMatrix(frame));
	}

	private SimpleMatrix getMatrixOffset() {
		double matrix[][] = {{1, 0, 0, offsetX},
							{0, 1, 0, offsetY},
							{0, 0, 1, offsetZ},
							{0, 0, 0, 1}};
		return new SimpleMatrix(matrix);
	}
	
	private SimpleMatrix getRotationMatrix(int frame) {
		SimpleMatrix matrix = SimpleMatrix.identity(4);
		
		if(TEST)
			System.out.println(channelOrders);
		
		int j=0;
		
		if(channelOrders.size() == 6)
			j+=3;
		
		for(int i=j; i<3+j; i++) {
			if(channelOrders.get(X_ROTATION) == i) {
				matrix = matrix.mult(getRotationMatrixX(frame));
			} else if(channelOrders.get(Y_ROTATION) == i) {
				matrix = matrix.mult(getRotationMatrixY(frame));
			} else if(channelOrders.get(Z_ROTATION) == i) {
				matrix = matrix.mult(getRotationMatrixZ(frame));
			} else {
				System.out.println("BVHNode: Rotation Matrix is missing");
				System.exit(-1);
			}
		}
		
		return matrix;
	}
	
	private SimpleMatrix getTransitionmatrix(int frame) {
		if(channelOrders.containsKey(X_POSITION)) {
			double matrix[][] = {{1, 0, 0, chParam.get(channelOrders.get(X_POSITION)).get(frame)},
								{0, 1, 0, chParam.get(channelOrders.get(Y_POSITION)).get(frame)},
								{0, 0, 1, chParam.get(channelOrders.get(Z_POSITION)).get(frame)},
								{0, 0, 0, 1}};
			return new SimpleMatrix(matrix);
		}
		else
			return SimpleMatrix.identity(4);
	}
	
	private SimpleMatrix getRotationMatrixX(int frame) {		
		double s_x = Math.sin(Math.toRadians(chParam.get(channelOrders.get(X_ROTATION)).get(frame)));
		double c_x = Math.cos(Math.toRadians(chParam.get(channelOrders.get(X_ROTATION)).get(frame)));
		double matrix[][] = {{1, 0, 0, 0},
							{0, c_x, -s_x, 0},
							{0, s_x, c_x, 0},
							{0, 0, 0, 1}};
		return new SimpleMatrix(matrix);
	}
	
	private SimpleMatrix getRotationMatrixY(int frame) {		
		double sin = Math.sin(Math.toRadians(chParam.get(channelOrders.get(Y_ROTATION)).get(frame)));
		double cos = Math.cos(Math.toRadians(chParam.get(channelOrders.get(Y_ROTATION)).get(frame)));
		double matrix[][] = {{cos, 0, sin, 0},
							{0, 1, 0, 0},
							{-sin, 0, cos, 0},
							{0, 0, 0, 1}};
		return new SimpleMatrix(matrix);
	}
	
	private SimpleMatrix getRotationMatrixZ(int frame) {		
		double sin = Math.sin(Math.toRadians(chParam.get(channelOrders.get(Z_ROTATION)).get(frame)));
		double cos = Math.cos(Math.toRadians(chParam.get(channelOrders.get(Z_ROTATION)).get(frame)));
		double matrix[][] = {{cos, -sin, 0, 0},
							{sin, cos, 0, 0},
							{0, 0, 1, 0},
							{0, 0, 0, 1}};
		return new SimpleMatrix(matrix);
	}

	public void translate(double deltaX, double deltaY, double deltaZ) {
		for(int f=0; f<maxFrame; f++)
			this.translate(f, deltaX, deltaY, deltaZ);
	}
	
	public void translate(int frame, double deltaX, double deltaY, double deltaZ) {
		SimpleMatrix mat = getLocation(frame);
		double[][] add = {{deltaX}, {deltaY}, {deltaZ}, {0}};
		SimpleMatrix addMat = new SimpleMatrix(add);
		if(TEST) {
			System.out.println(mat);
			System.out.println(addMat);
		}
		locations.set(frame, mat.plus(addMat));
	}
	
	public BvhNode getParent() {
		return parent;
	}

	public void scale(double d) {
		for(int f=0; f<maxFrame; f++) {
			SimpleMatrix mat = getLocation(f);
			locations.set(f, mat.scale(d));
		}
	}
}