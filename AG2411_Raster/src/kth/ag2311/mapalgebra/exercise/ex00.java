package kth.ag2311.mapalgebra.exercise;

import kth.ag2311.mapalgebra.model.Layer;

public class ex00 {

	public static void main (String[] args) {
	
 		Layer road = new Layer("road", "c:/DataJava/road.txt");
		//road.map();

		// Layer shortest = road.getShortestPath(900, 5, 120, 120); // wrong case
		Layer shortest = road.getShortestPath(900, 35, 120, 120);
		//shortest.save("c:/DataJava/shortest.txt");

		road.mergeShortest(shortest);
		double[] interests = {Layer.shortestPoint, Layer.shortestValue};
		road.map(interests);
		
	}

}
