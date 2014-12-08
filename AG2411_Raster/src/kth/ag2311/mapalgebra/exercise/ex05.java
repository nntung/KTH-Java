package kth.ag2311.mapalgebra.exercise;

import kth.ag2311.mapalgebra.model.Network;
import kth.ag2311.mapalgebra.model.Node;

/**
 * <h1>Exercise 5<h1>
 * Test dijkstra on Network data
 * 
 * @author Nga Nguyen
 *
 */
public class ex05 {

	public static void main (String[] args) {
		if (args.length == 3) {
			//Instantiate a network
			Network network = new Network("", args[0]);
			
			Node origin = network.nodeMap.get(args[2]);
			// find the shortest from origin
			if (origin != null) {
				// run dijkstra to find the shortest path
				network.dijkstra(origin);

				// update all arc from origin to all other nodes
				network.clearArcWeight();
				for (String nodeName : network.nodeMap.keySet()) {
					network.traceNode(network.nodeMap.get(nodeName));
				}
			}
			
			//Printing it on the console
			network.printNodes();
			network.printArcs();

			//Saving it to the file output.txt
			network.save(args[1]);
		} else if (args.length == 4) {
			//Instantiate a network
			Network network = new Network("", args[0]);
			
			Node origin = network.nodeMap.get(args[2]);
			// find the shortest from origin
			if (origin != null) {
				// run dijkstra to find the shortest path
				network.dijkstra(origin);

				// update all arc from origin to destination nodes
				Node destination = network.nodeMap.get(args[3]);
				network.clearArcWeight();
				if (destination!=null)
					network.traceNode(destination);
			}
			
			//Printing it on the console
			network.printNodes();
			network.printArcs();

			//Saving it to the file output.txt
			network.save(args[1]);	
		} else {
			System.out.println("Too many or few arguments ...");
			System.out.println("Ex1: ex05 c:/DataJava/smallnetwork.txt c:/DataJava/smallnetwork.out 1");
			System.out.println("Ex2: ex05 c:/DataJava/smallnetwork.txt c:/DataJava/smallnetwork.out 1 5");
		}
	}
	
}
