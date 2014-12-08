package kth.ag2311.mapalgebra.exercise;

import kth.ag2311.mapalgebra.model.Layer;

/**
 * <h1>Exercise 2<h1>
 * Test view Layer on the screen
 * 
 * @author Nga Nguyen
 *
 */
public class ex02 {
	public static void main (String[] args) {
		if (args.length >= 2) {
			//Instantiate a layer
			Layer layer = new Layer(args[0], args[1]);
			//Show it on the screen with gray scale level
			layer.map();
			
			if (args.length > 2) {
				int numberOfInterest = args.length - 2;
				//Show it on the screen with color
				double[] interests = new double[numberOfInterest];
				int j = 2;
				for (int i = 0; i<numberOfInterest; i++) {
					try {
						interests[i] = Double.parseDouble(args[j]);
					} catch (NumberFormatException ex) {
						interests[i] = layer.nullValue;
					}
					j++;
				}
					
				layer.map(interests);
			}
		} else {
			System.out.println("Too few arguments ...");
			System.out.println("Ex: ex02 Layer_Development c:/DataJava/dev_ascii.txt ");
			System.out.println("Ex: ex02 Layer_Development c:/DataJava/dev_ascii.txt 2.0 3.0");
		}
	}
}
