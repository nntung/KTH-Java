package kth.ag2311.mapalgebra.exercise;

import kth.ag2311.mapalgebra.model.Layer;

/**
 * <h1>Exercise 1<h1>
 * Test load and save Layer class
 * 
 * @author Nga Nguyen
 *
 */

public class ex01 {

	public static void main (String[] args) {
		if (args.length == 3) {
			//Instantiate a layer
			Layer layer = new Layer(args[0], args[1]);
			//Printing it on the console
			layer.print(false);
			//Saving it to the file output.txt
			layer.save(args[2]);
		} else {
			System.out.println("Too many or few arguments ...");
			System.out.println("Ex: ex01 Layer_Development c:/DataJava/dev_ascii.txt c:/DataJava/dev_ascii.out");
		}
	}
	
}
