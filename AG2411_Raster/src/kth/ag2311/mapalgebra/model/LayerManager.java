package kth.ag2311.mapalgebra.model;

import java.util.ArrayList;

public class LayerManager {
	/**
	 * List of ordering layers 
	 */
	private static ArrayList<Layer> layers = new ArrayList<Layer>();
	
	
	public static void add(Layer layer) {
		layers.add(layer);
	}
	
	public static void remove(Layer layer) {
		layers.remove(layer);
	}
	
	public static void up() {
		// TODO
	
	}

	public static void down() {
		// TODO

	}
	
	public static void save() {
		// TODO
		
	}
	
	public static void load() {
		// TODO
		
	}

}
