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
	public final static int TYPE_SLOPE = 6;
	public final static int TYPE_ASPECT = 7;
	
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
	public int awayFrom;
	public int closeBy;
	// type Elevation
	public String[] aspectDescription;
	public boolean[] aspectInterest;
	public int[] aspectRange;
	public ColorAlpha[] aspectColor;
	
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
			parseTypeDevelopment(prop);
			break;
		case TYPE_HYDROLOGY:
			parseTypeHydrology(prop);
			break;
		case TYPE_VEGETATION:
			parseTypeVegetation(prop);
			break;
		case TYPE_ELEVATION:
		case TYPE_SLOPE:
		case TYPE_ASPECT:
			parseElevationVegetation(prop);
			break;
		default:
			break;
		}

	}

	private void parseElevationVegetation(Properties prop) {
		// TODO Auto-generated method stub
		// read Aspect property
		int num = 10;
		aspectDescription = new String[10];
		aspectInterest = new boolean[10];
		aspectRange = new int[10];
		aspectColor = new ColorAlpha[10];
		String key, des, color, range;
		boolean like;
		int v1=-1;
		for (int i=0; i<num; i++) {
			// get value
			key = "aspectvalue" + i;
			try {
				range = prop.getProperty(key);
				v1 = Integer.parseInt(range);
			} catch (NumberFormatException e) {
				v1 = -1;
			} finally {
				aspectRange[i] = v1;
			}
			// get Description
			key = "aspectdes" + i;
			des = prop.getProperty(key);
			aspectDescription[i] = des;
			// get Color
			key = "aspectcolor" + i;
			color = prop.getProperty(key);
			aspectColor[i] = new ColorAlpha(color);
			// get Interest
			key = "aspectinterest" + i;
			like = prop.getProperty(key).equals("yes") ? true : false;
			aspectInterest[i] = like;
		}
	}

	private void parseTypeHydrology(Properties prop) {
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
		for (int i = 0; i < num; i++) {
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

			colorAlphas.put(value, new ColorAlpha(color));
			interests.put(value, false);
		}
		
		try {
			closeBy = Integer.parseInt(prop.getProperty("close"));
		} catch (NumberFormatException e) {
			closeBy = 3;
		}
	}

	private void parseTypeDevelopment(Properties prop) {
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
		for (int i = 0; i < num; i++) {
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

			colorAlphas.put(value, new ColorAlpha(color));
			interests.put(value, false);
		}
				
		try {
			awayFrom = Integer.parseInt(prop.getProperty("away"));
		} catch (NumberFormatException e) {
			awayFrom = 3;
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
			createTypeDevelopment(prop);
			break;
		case TYPE_HYDROLOGY:
			prop.setProperty("type", ST_HYDROLOGY);
			createTypeHydrology(prop);
			break;
		case TYPE_VEGETATION:
			prop.setProperty("type", ST_VEGETATION);
			createTypeVegetation(prop);
			break;
		case TYPE_ELEVATION:
		case TYPE_SLOPE:
		case TYPE_ASPECT:
			prop.setProperty("type", ST_ELEVATION);
			createTypeElevation(prop);
			break;
		default:
			break;
		}
		
	}

	private void createTypeElevation(Properties prop) {
		// TODO Auto-generated method stub
		
	}

	private void createTypeHydrology(Properties prop) {
		createTypeVegetation(prop);
		prop.setProperty("close", Integer.toString(closeBy));
	}

	private void createTypeDevelopment(Properties prop) {
		createTypeVegetation(prop);
		prop.setProperty("away", Integer.toString(awayFrom));
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
