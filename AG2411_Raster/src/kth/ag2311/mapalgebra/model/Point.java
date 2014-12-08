package kth.ag2311.mapalgebra.model;

/**
 * A point(x,y)
 * 
 * @author Nga Nguyen
 *
 */
public class Point {
	public int x;
	public int y;
	
	public Point(int xx, int yy) {
		this.x = xx;
		this.y = yy;
	}
	
	public boolean isEqual(Point p) {
		if (this.x == p.x && this.y == p.y)
			return true;
		else 
			return false;
	}
	
}
