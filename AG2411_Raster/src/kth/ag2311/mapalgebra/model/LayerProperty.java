package kth.ag2311.mapalgebra.model;

import java.util.HashMap;
import java.util.Properties;

public class LayerProperty {
	public final static int TYPE_UNDEFINED = 0;
	public final static int TYPE_ROAD = 1;
	public final static int TYPE_DEVELOPMENT = 2;
	public final static int TYPE_VEGETATION = 3;
	public final static int TYPE_HYDROLOGY = 4;
	public final static int TYPE_ELEVATION = 5;
	
	public final static String ST_UNDEFINED = "?";
	public final static String ST_ROAD = "road";
	public final static String ST_DEVELOPMENT = "development";
	public final static String ST_VEGETATION = "vegetation";
	public final static String ST_HYDROLOGY = "hydrology";
	public final static String ST_ELEVATION = "elevation";
	
	public int type;
	// type Road
	public int startX, startY;
	public int endX, endY;
	// type Development (far distance), Hydrology and Vegetation (near distance)
	public HashMap<Double, String> descriptions;
	public HashMap<Double, String> colors;
	public HashMap<Double, Boolean> interests;
	public HashMap<Double, ColorAlpha> colorAlphas;
	// type Elevation
	
	
	public LayerProperty() {
		type = TYPE_UNDEFINED;
	}

	public void setProperty(Properties prop) {
		// process property
		String sType = prop.getProperty("type");
		if (sType.equals(ST_ROAD))
			type = TYPE_ROAD;
		else if (sType.equals(ST_DEVELOPMENT))
			type = TYPE_DEVELOPMENT;
		else if (sType.equals(ST_HYDROLOGY))
			type = TYPE_HYDROLOGY;
		else if (sType.equals(ST_VEGETATION))
			type = TYPE_VEGETATION;
		else if (sType.equals(ST_ELEVATION))
			type = TYPE_ELEVATION;

		switch (type) {
		case TYPE_ROAD:
			parseTypeRoad(prop);
			break;
		case TYPE_DEVELOPMENT:

			break;
		case TYPE_HYDROLOGY:

			break;
		case TYPE_VEGETATION:
			parseTypeVegetation(prop);
			break;
		case TYPE_ELEVATION:

			break;
		default:
			break;
		}

	}

	private void parseTypeVegetation(Properties prop) {
		// get num of elements
		int num;
		try {
			num = Integer.parseInt(prop.getProperty("number"));
		} catch (NumberFormatException e) {
			num = -1;
		}
		
		// init HashMap
		descriptions = new HashMap<Double, String>();
		colors = new HashMap<Double, String>();
		interests = new HashMap<Double, Boolean>();
		colorAlphas = new HashMap<Double, ColorAlpha>();
		
		// start to parse
		Double value;
		String des;
		String color;
		String key;
		Boolean like;
		for (int i=0; i<num; i++) {
			// get value
			key = "value" + i;
			try {
				value = Double.parseDouble(prop.getProperty(key));
			} catch (NumberFormatException e) {
				value = new Double(-1);
			}
			// get Description
			key = "des" + i;
			des = prop.getProperty(key);
			descriptions.put(value, des);
			// get Color
			key = "RGBA" + i;
			color = prop.getProperty(key);
			colors.put(value, color);
			// get Interest
			key = "interest" + i;
			like = prop.getProperty(key).equals("yes") ? true : false;
			interests.put(value, like);
			
			colorAlphas.put(value, new ColorAlpha(color));
		}
		
	}

	private void parseTypeRoad(Properties prop) {
		int value;
		try {
			value = Integer.parseInt(prop.getProperty("startX"));
		} catch (NumberFormatException e) {
			value = -1;
		}
		startX = value;
		try {
			value = Integer.parseInt(prop.getProperty("startY"));
		} catch (NumberFormatException e) {
			value = -1;
		}
		startY = value;
		try {
			value = Integer.parseInt(prop.getProperty("endX"));
		} catch (NumberFormatException e) {
			value = -1;
		}
		endX = value;
		try {
			value = Integer.parseInt(prop.getProperty("endY"));
		} catch (NumberFormatException e) {
			value = -1;
		}
		endY = value;
	}

	public void createProperty(Properties prop) {
		switch (type) {
		case TYPE_ROAD:
			prop.setProperty("type", ST_ROAD);
			createTypeRoad(prop);
			break;
		case TYPE_DEVELOPMENT:
			prop.setProperty("type", ST_DEVELOPMENT);
			
			break;
		case TYPE_HYDROLOGY:
			prop.setProperty("type", ST_HYDROLOGY);

			break;
		case TYPE_VEGETATION:
			prop.setProperty("type", ST_VEGETATION);
			createTypeVegetation(prop);
			break;
		case TYPE_ELEVATION:
			prop.setProperty("type", ST_ELEVATION);

			break;
		default:
			break;
		}
		
	}

	private void createTypeVegetation(Properties prop) {
		int num = 0;
		String key, like;
		for (Double value : descriptions.keySet()) {
			key = "value" + num;
			prop.setProperty(key, Double.toString(value));
			key = "des" + num;
			prop.setProperty(key, descriptions.get(value));
			key = "RGBA" + num;
			prop.setProperty(key, colors.get(value));
			key = "interest" + num;
			like = (interests.get(value)) ? "yes" : "no";
			prop.setProperty(key, like);
			num ++;
		}
		prop.setProperty("number", Integer.toString(num));
	}

	private void createTypeRoad(Properties prop) {
		prop.setProperty("startX", Integer.toString(startX));
		prop.setProperty("startY", Integer.toString(startY));
		prop.setProperty("endX", Integer.toString(endX));
		prop.setProperty("endY", Integer.toString(endY));
	}

	
}
