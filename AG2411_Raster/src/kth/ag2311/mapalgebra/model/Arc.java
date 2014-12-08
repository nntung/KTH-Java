package kth.ag2311.mapalgebra.model;

/**
 * Storing a Arc content 2 nodes (head and tail) and weight of this arc
 * 
 * @author Nga Nguyen
 *
 */
public class Arc {
	
	/**
	 * Name of Arc
	 */
	public String name;
	
	/**
	 * Head node
	 */
	public Node head;

	/**
	 * Tail node
	 */
	public Node tail;
	
	/**
	 * Weight of arc
	 */
	public double weight;
	
	/**
	 * Construction method of Arc
	 * 
	 * @param name
	 * @param head
	 * @param tail
	 * @param weight
	 */
	public Arc(String name, Node head, Node tail, double weight) {
		this.name = name;
		this.head = head;
		this.tail = tail;
		this.weight = weight;
	}
	
}
