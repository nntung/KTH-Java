package kth.ag2311.mapalgebra.model;

public class Element {
	public Double value;
	public String description;
	public int red;
	public int green;
	public int blue;
	public int alpha;
	public Boolean interest;
	public static int iconwidth = 24;
	public Element(Double val, String des, String color, Boolean like) {
		value = val;
		description = des;
		interest = like;
		
		// convert String to RGBA
		String[] cl = color.split(" ");
		try {
			red = Integer.parseInt(cl[0]);
			green = Integer.parseInt(cl[1]);
			blue = Integer.parseInt(cl[2]);
			alpha = Integer.parseInt(cl[3]);
		} catch (Exception ex) {
			red = 0;
			green = 0;
			blue = 0;
			alpha = 0;
		}
	}
} 
