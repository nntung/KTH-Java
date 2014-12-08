package kth.ag2311.mapalgebra.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Storing a Network of nodes and arcs
 * 
 * @author Nga Nguyen
 *
 */
public class Network {

	/**
	 * Name of Network
	 */
	public String name;

	/**
	 * Set of nodes in network
	 */
	public HashMap<String, Node> nodeMap;

	/**
	 * Construction method
	 * 
	 * @param name
	 *            Network name
	 * @param inputPath
	 *            input path of network data
	 */
	public Network(String name, String inputPath) {
		// Initialize the attributes
		this.name = name;
		nodeMap = new HashMap<String, Node>();

		System.out.print("Loading data: " + inputPath);
		BufferedReader bufReader = null; // obj for reading file

		// try to read content of fileName (path)
		try {
			// put data of filename to obj Reader
			bufReader = new BufferedReader(new FileReader(inputPath));

			// Read the first line, and do nothing to skip it
			String line = bufReader.readLine();

			// Read the second line, which represents the first
			// (undirected) arc stored in the file
			line = bufReader.readLine();

			// Store each element of the network in forward star.
			while (line != null) {
				// Split each line into an array of 4 Strings
				// using ; as separator.
				String[] tokens = line.split(";");
				String arcID = tokens[0];
				String tailName = tokens[1];
				String headName = tokens[2];
				double weight = Double.parseDouble(tokens[3]);

				// Try to retrieve head and tail from node Map,
				// return null in there is no such node in node Map
				Node head = nodeMap.get(headName);
				Node tail = nodeMap.get(tailName);

				// If not, create it, assign it to tail or head,
				// and add them to nodeMap.
				if (head == null) {
					head = new Node(headName);
					nodeMap.put(headName, head);
				}

				if (tail == null) {
					tail = new Node(tailName);
					nodeMap.put(tailName, tail);
				}

				// create two Arcs:
				// one from head to tail, to be added to outArc of head.
				Arc arcHeadTail = new Arc(arcID, head, tail, weight);
				head.outArcs.add(arcHeadTail);
				// one from tail to head, to be added to outArc of tail
				Arc arcTailHead = new Arc(arcID, tail, head, weight);
				tail.outArcs.add(arcTailHead);

				// Read the next line
				line = bufReader.readLine();
			}
		} catch (NumberFormatException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				bufReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		System.out.print(" ..Loaded");
	}

	/**
	 * Save data in to file
	 * 
	 * @param outputPath
	 *            output path of network data
	 */
	public void save(String outputPath) {
		System.out.print("Saving network: " + outputPath);

		// use buffering, writing one line at a time
		// Exception may be thrown while reading (and writing) a file.
		BufferedWriter bufWriter = null;

		// try to write content into fileName (path)
		try {
			// use buffering, writing one line at a time
			// Exception may be thrown while reading (and writing) a file.
			bufWriter = new BufferedWriter(new FileWriter(outputPath));

			// header
			String line = "ARCID;HEAD;TAIL;WEIGHT";
			bufWriter.write(line);
			bufWriter.newLine();

		} catch (NumberFormatException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				bufWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		System.out.print(" ..Saved");
	}

	/**
	 * Print all nodes in network
	 */
	public void printNodes() {
		System.out.println("\tNODE NAME\tVALUE");
		for (String nodeName : nodeMap.keySet()) {
			Node node = nodeMap.get(nodeName);
			System.out.print("\t" + node.name);
			System.out.print("\t" + node.value);
			System.out.println();
		}
	}

	/**
	 * Print all arcs in network
	 */
	public void printArcs() {
		System.out.println("\tARC ID\tHEAD\tTAIL\tWEIGHT");
		for (String nodeName : nodeMap.keySet()) {
			Node node = nodeMap.get(nodeName);
			int numOfOutArc = node.outArcs.size();
			for (int i = 0; i < numOfOutArc; i++) {
				Arc arc = node.outArcs.get(i);
				System.out.print("\t" + arc.name);
				System.out.print("\t" + arc.head.name);
				System.out.print("\t" + arc.tail.name);
				System.out.print("\t" + arc.weight);
				System.out.println();
			}
		}
	}

	/**
	 * Find the shortest path from the origin to every other nodes in network
	 * 
	 * @param origin
	 */
	public void dijkstra(Node origin) {
		// set the value of origin equal 0
		origin.value = 0;
		origin.isPermanent = true;

		// Create a set of nodes, called tempNodes, whose shortest path
		// distances are not permanently determined. Initially, this
		// set contains all nodes.
		ArrayList<Node> tempNodes = new ArrayList<Node>();
		for (String nodeName : nodeMap.keySet()) {
			tempNodes.add(nodeMap.get(nodeName));
		}

		// Perform Dijkstra
		while (!tempNodes.isEmpty()) {
			// repeat until all nodes become permanent

			// Find a node with minimum weight in tempNodes
			Node minNode = new Node("minNode");
			for (Node node : tempNodes) {
				if (minNode.value > node.value) {
					minNode = node;
					minNode.value = node.value;
				}
			}

			// Update the weight of each node that is adjacent
			// to the minimum-weight node AND not permanent
			for (Arc arc : minNode.outArcs) {
				Node adjacent = arc.tail;
				if (adjacent.isPermanent == false) {
					// check current path and the path through minNode
					// which one is smaller, update path through minNode is
					// smaller
					if (adjacent.value > minNode.value + arc.weight) {
						adjacent.value = minNode.value + arc.weight;
						adjacent.previous = minNode;
					}
				}
			}

			// set minNode is permanent
			minNode.isPermanent = true;
			// Remove the minimum-weight node from tempNodes
			tempNodes.remove(minNode);
		}

	}

	/**
	 * Assign 0 to all arcs
	 */
	public void clearArcWeight() {
		Node n;
		for (String nodeName : nodeMap.keySet()) {
			n = nodeMap.get(nodeName);
			for (Arc a : n.outArcs) {
				a.weight = 0;
			}
		}
	}

	/**
	 * Assign 1 to all arcs that precede a specified node along a path
	 */
	public void traceNode(Node n) {
		Node cur = n;
		Node prev = n.previous;

		while (prev != null) {
			// find arc connecting between prev and node
			// then assign weight equal 1
			for (Arc a : prev.outArcs) {
				if (a.tail.name.equals(cur.name)) {
					a.weight = 1;
					break;
				}
			}
			cur = prev;
			prev = cur.previous;
		}
	}

}
