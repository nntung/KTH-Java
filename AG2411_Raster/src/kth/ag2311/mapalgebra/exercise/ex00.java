package kth.ag2311.mapalgebra.exercise;

import kth.ag2311.mapalgebra.model.Layer;

public class ex00 {

	public static void main (String[] args) {
	
 		Layer road = new Layer("road", "c:/DataJava/road.txt");
		road.map();

		Layer shortest = road.getShortestPath(490, 120, 150, 450);
//		shortest.save("c:/tmp/shortest.txt");

		road.merge(shortest);
		double[] interests = {1,2};
		road.map(interests);
		
	}

}
