package sesame.mocap.bvh.app;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

import javax.swing.JFrame;
import javax.swing.Timer;

import org.ejml.simple.SimpleMatrix;

import sesame.mocap.bvh.core.BvhMocap;
import sesame.mocap.bvh.core.BvhNode;
import sesame.movap.bvh.parser.BvhFileParser;



/**
 * This class is to view our BVHMocap class
 * in 2-d using x and y coordinate
 * 
 * @author Ramon
 *
 */
@SuppressWarnings("serial")
public class BvhViewer extends JFrame {
	
	/**
	 * to scale the x dimension to make the motion capture easier to see.
	 */
	static final double XSCALE = 40;
	
	/**
	 * To scale the y dimension to make the motion capture easier to see
	 */
	static final double YSCALE = 160;
	
	/**
	 * radius of the circle that indicates the node.
	 */
	static final int RADIUS = 5;
	
	/**
	 * The top margin of the viewer.
	 */
	static final int CEIL = 40;
	
	/**
	 * height of the window
	 */
	static final int HEIGHT = 1000;
	
	/**
	 * width of the window
	 */
	static final int WIDTH = 1600;
	
	/**
	 * The x-location of (0,0) relative to the JFrame.
	 */
	static final double XPOLE = 1;
	
	/**
	 * The y-location of (0,0) relative to the JFrame.
	 */
	static final double YPOLE = 1;
	
	/**
	 * setting the time scale.
	 * 1 = normal time scale.
	 * <1 = higher fps
	 * >1 = lower fps
	 */
	static final int TIMESCALE = 1;
	
	/**
	 * Filename of the BVH file.
	 */
	static final String filename = "F:\\bow.bvh";
	
	/**
	 * setting the display format of coordinate x,y
	 */
	DecimalFormat df;
	
	/**
	 * The Mocap that will be displayed on the screen
	 */
	BvhMocap motion;
	
	
	public static void main(String[] args) {
		new BvhViewer();
	}

	/**
	 * The constructor of BVHViewer
	 * It will load mocap, initialize parameter,
	 * and display the motion on the JFrame.
	 */
	public BvhViewer() {
		loadMocap();
		initFrame();
		startMotion();
	}

	/**
	 * load the mocap by reading the BVH file.
	 */
	private void loadMocap() {
		motion = new BvhMocap();
		BvhFileParser.parse(filename, motion);
		motion.normalize();
	}
	
	
	/**
	 * start displaying the motion.
	 */
	private void startMotion() {
		
		// time for each frame.
		int speed = (int)((1.0/motion.getFps())*1000*TIMESCALE);
		repaint(0);
		repaint(1);
		
		Timer timer = new Timer(speed, new ActionListener() {
			int t=0;
			@Override
			
			public void actionPerformed(ActionEvent arg0) {
				
				Graphics g = BvhViewer.this.getGraphics();
				repaint(t);
				drawHeader(g,t);
				drawFigure(g,t++);
				t++;
				if(t==motion.getTotalFrame())
					t=0;
			}
		});
		
		timer.setRepeats(true);
		//timer.start(); 
		
	}
	
	/**
	 * repaint the motion
	 * @param t time t (frame index)
	 */
	public void repaint(int t) {
		Graphics g = this.getGraphics();
		
		g.setColor(Color.GRAY);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		g.setColor(Color.WHITE);
		drawHeader(g,t);
		drawFigure(g,t);
	}
	
	/**
	 * printing the header
	 * @param g graphics
	 * @param t time t (frame index)
	 */
	private void drawHeader(Graphics g, int t) {
		int xLocation = 20;
		int yLocation = 20;
		
		double yRoot = motion.getNode(0).getLocation(t).get(1, 0);
		
		g.drawString("ROOT(y) : " + df.format(yRoot) , xLocation, yLocation);
	}
	
	/**
	 * draw figure at time t
	 * @param g graphics
	 * @param t time (frame index)
	 */
	private void drawFigure(Graphics g, int t) {
		BvhNode n;
		
		for(int i=0; i<motion.getTotalNode(); i++) {
			n = motion.getNode(i);
			//int t=0;
			
			drawNode(n, t, g, i);
		}
	}
	
	private void drawNode(BvhNode n, int t, Graphics g, int i) {
		//int rotationDeg = 180;
		//double rotationRad = Math.toRadians(rotationDeg);
		//double[] location = n.getLocationAfterYRotation(t, rotationRad);
		SimpleMatrix location = n.getLocation(t);
		
		//draw a circle
		double x = location.get(0, 0);
		double y = location.get(1, 0);
		
		System.out.println(n.getName() + ": " + df.format(x) + " " + df.format(y));
		
		int xDisplay = getXdisplay(x);
		int yDisplay = getYdisplay(y);
		
		g.drawOval(xDisplay, yDisplay, RADIUS, RADIUS);
		
		g.drawString(n.getName() + ": " + df.format(x) + " " + df.format(y)
					, xDisplay+RADIUS
					, yDisplay+2*RADIUS);
		
		//draw a line
		if(n.getParent()!=null) {
			int xParentDisplay = getXdisplay(n.getParent().getLocation(t).get(0, 0));
			int yParentDisplay = getYdisplay(n.getParent().getLocation(t).get(1, 0));
			g.drawLine(xDisplay, yDisplay, xParentDisplay, yParentDisplay);
		}
		
	}

	/**
	 * draw each node and a line to its parent
	 * @param n node
	 * @param t time t (frame index)
	 * @param g graphics class on which the node is drawn
	 */
/*	private void drawNode(BVHNode n, int t, Graphics g) {
		int rotationDeg = 0;
		double rotationRad = Math.toRadians(rotationDeg);
		//double[] location = n.getLocationAfterYRotation(t, rotationRad);
		SimpleMatrix location  = n.getLocation(t);
		
		//draw a circle
		double x = location.get(0, 0);
		double y = location.get(1, 0);
		
		int xDisplay = getXdisplay(x);
		int yDisplay = getYdisplay(y);
		
		g.drawOval(xDisplay, yDisplay, RADIUS, RADIUS);
		
		
		g.drawString(n.getName() + ": " + df.format(x) + " " + df.format(y)
					, xDisplay+RADIUS
					, yDisplay+2*RADIUS);
		
		//draw a line
		if(n.getParent()!=null) {
			int xParentDisplay = getXdisplay(n.getParent().getLocation(t).get(0, 0));
			int yParentDisplay = getYdisplay(n.getParent().getLocation(t).get(1, 0));
			g.drawLine(xDisplay, yDisplay, xParentDisplay, yParentDisplay);
		}
	}
*/	
	/**
	 * get the location of x coordinate on the JFrame screen.
	 * @param x the BVH x coordinate
	 * @return x coordinate in JFrame.
	 */
	private int getXdisplay(double x) {
		return (int)((x+XPOLE)*XSCALE);
		//return (int)((XSCALE * x) - XPOLE);
	}
	
	/**
	 * Get the location of y on the JFrame.
	 * @param y the BVH y coordinate
	 * @return y coordinate in JFrame.
	 */
	private int getYdisplay(double y) {
		return (int)((y+YPOLE)*YSCALE);
		//return HEIGHT - (int)(YSCALE * (y-YPOLE)) + CEIL;
	}
	
	/**
	 * Initialize JFrame
	 */
	private void initFrame() {

		
		df = new DecimalFormat();
		df.setMinimumFractionDigits(2);
		df.setMaximumFractionDigits(2);
		
		this.setSize(WIDTH, HEIGHT);
		this.setFocusable(true);
		//frame.setDefaultCloseOperation(frame.DO_NOTHING_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
	
}
