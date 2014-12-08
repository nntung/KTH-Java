package kth.ag2311.mapalgebra.exercise;

import kth.ag2311.mapalgebra.model.Layer;

/**
 * <h1>Exercise 3<h1>
 * Test operation on Layer - localSum - focalVariety - zonalMinimum
 * 
 * @author Nga Nguyen
 *
 */
public class ex03 {
	public static void main(String[] args) {
		try {
			String operation = args[0];
			if (operation.equals("localSum")) {
				Layer inLayer1 = new Layer("", args[1]);
				Layer inLayer2 = new Layer("", args[2]);

				Layer outLayer = inLayer1.localSum(inLayer2, "");
				outLayer.save(args[3]);
				outLayer.map();

			} else if (operation.equals("focalVariety")) {
				Layer inLayer = new Layer("", args[1]);
				int radius = Integer.parseInt(args[2]);
				boolean square = true;
				if (args[3].equals("s"))
					square = true;
				else
					square = false;

				Layer outLayer = inLayer.focalVariety(radius, square, "");
				outLayer.map();

			} else if (operation.equals("zonalMinimum")) {
				Layer inLayer = new Layer("", args[1]);
				Layer zoneLayer = new Layer("", args[2]);

				Layer outLayer = inLayer.zonalMinimum(zoneLayer, "");
				outLayer.map();

			} else {
				System.out.println(operation + " is NOT currently available.");
			}
			
		} catch (ArrayIndexOutOfBoundsException ex) {
			System.out.println("Examples of available operation: ");
			System.out.println("ex03 localSum PathOfLayer1 PathOfLayer2 PathOfOutput");
			System.out.println("ex03 focalVariety PathOfLayer radius [s][c]");
			System.out.println("ex03 zonalMinimum PathOfLayer PathOfZone ");
		}
	}
}
