package sesame.mocap.bvh.app;

import sesame.mocap.bvh.core.BvhMocap;
import sesame.movap.bvh.parser.BvhFileParser;


public class BvhParserTest {
	public static void main(String[] args) {
		BvhMocap mocap = new BvhMocap();
		BvhFileParser.parse("F:\\bow.bvh", mocap);
	}
}
