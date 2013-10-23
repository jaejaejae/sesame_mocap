package sesame.mocap.model.util;

import sesame.mocap.Mocap10Nodes;
import sesame.mocap.constant.FeatureType;
import sesame.mocap.model.MocapFeature;
import sesame.mocap.model.util.converter.Mocap10NodesToMocapFeatureByPosition;

public class Converter {
	public static void convert(Mocap10Nodes mocap, MocapFeature feature) {
		switch(feature.getFeatureType()) {
		case Position:
			new Mocap10NodesToMocapFeatureByPosition(mocap, feature);
		default:
			break;
		}
	}
}
