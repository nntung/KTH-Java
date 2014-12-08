package kth.ag2311.mapalgebra.exercise;

import kth.ag2311.mapalgebra.model.Network;

/**
 * <h1>Exercise 4<h1>
 * Test operation on Network data
 * 
 * @author Nga Nguyen
 *
 */
public class ex04 {

	public static void main (String[] args) {
		if (args.length == 3) {
			//Instantiate a network
			Network network = new Network(args[0], args[1]);
			
			//Printing it on the console
			network.printNodes();
			network.printArcs();

			//Saving it to the file output.txt
			network.save(args[2]);
		} else {
			System.out.println("Too many or few arguments ...");
			System.out.println("Ex: ex04 Network_Sample c:/DataJava/smallnetwork.txt c:/DataJava/smallnetwork.out");
		}
	}
	
}
