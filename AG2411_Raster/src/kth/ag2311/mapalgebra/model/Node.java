package kth.ag2311.mapalgebra.model;

import java.util.LinkedList;

/**
 * Storing a Node
 * 
 * @author Nga Nguyen
 *
 */
public class Node {
	
	/**
	 * Name of Node
	 */
	public String name;
	
	/**
	 * Value of node in the shortest path
	 */
	public double value;
	
	/**
	 * Set of arcs connecting with this note
	 */
	public LinkedList<Arc> outArcs;
	
	/**
	 * Previous node in the shortest path
	 */
	public Node previous;
	
	/**
	 * Previous node in the shortest path
	 */
	public boolean isPermanent;

	
	/**
	 * Construction method of Node
	 * 
	 * @param name Node name
	 */
	public Node(String name) {
		this.name = name;
		this.value = Double.MAX_VALUE;
		this.previous = null;
		this.isPermanent = false;
		outArcs = new LinkedList<Arc>();
	}
}
