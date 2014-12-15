package kth.ag2311.mapalgebra.model;

public class ColorAlpha {
	public int red;
	public int green;
	public int blue;
	public int alpha;
	
	public ColorAlpha(int r, int g, int b, int a) {
		red = r;
		green = g;
		blue = b;
		alpha = a;
	}
	
	public ColorAlpha(String colors) {
		// convert String to RGBA
		String[] cl = colors.split(" ");
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
	public void getColor(int[] colors) {
		if (colors.length != 4) return;
		colors[0] = red;
		colors[1] = green;
		colors[2] = blue;
		colors[3] = alpha;
	}
}
