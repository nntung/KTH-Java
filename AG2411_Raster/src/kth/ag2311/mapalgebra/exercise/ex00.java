package kth.ag2311.mapalgebra.exercise;

import kth.ag2311.mapalgebra.model.Layer;

public class ex00 {

	public static void main (String[] args) {
	
 		Layer road = new Layer("road", "c:/DataJava/roadbk.txt");
		road.map();

		Layer shortest = road.getShortestPath(20, 20, 300, 300);
//		shortest.save("c:/tmp/shortest.txt");

		road.merge(shortest);
		double[] interests = {1.0};
		road.map(interests);
		
	}

}
