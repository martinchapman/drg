package buildx.algorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;

import buildx.BuildGraph;
import buildx.Steppable;
import buildx.hideandseek.strategy.DepthFirstSearch;
import buildx.utils.GraphUtils;
import buildx.utils.Utils;

/**
 * Algorithm by Dijkstra: https://en.wikipedia.org/wiki/Dijkstra%27s_algorithm
 * 
 * Based upon implementation by Vogella.
 * 
 * @author Martin
 *
 */
public class Dijkstra extends BuildGraph implements Steppable {

	// Important sets of information we need...
	
	/**
	 * The nodes we have visited
	 */
	private Set<Node> settledNodes;

	/**
	 * Nodes we have yet to examine
	 */
	private Set<Node> unsettledNodes;

	/**
	 * Current estimate of the distance to each node from the source
	 */
	private Map<Node, Integer> distanceEstimate;

	/**
	 * Utility: to calculate path
	 */
	private Map<Node, Node> predecessors;
	
	// Create our graph...
	//        B---9--E
	//       /|      |
	//      / |      |
	//     /  |      |
	//    14  2      6
	//   /    |      |
	//  /     |      |
	// A---9--C--11--F
	//  \     |     /
	//   \    |    /
	//    7  10   15
	//     \  |  /
	//      \ | /
	//       \|/
	//        D      
	protected Graph generateGraph() {
		
		graph = new SingleGraph( "Dijkstra" );

		graph.addNode("A").addAttribute("xy", 0, 1);
		graph.addNode("B").addAttribute("xy", 1, 2);
		graph.addNode("C").addAttribute("xy", 1, 1);
		graph.addNode("D").addAttribute("xy", 1, 1);
		graph.addNode("E").addAttribute("xy", 2, 2);
		graph.addNode("F").addAttribute("xy", 2, 1);

		graph.addEdge("AB", "A", "B", true).addAttribute("length", 14);
		GraphUtils.addReverse(graph, graph.getEdge("AB"));
		graph.addEdge("AC", "A", "C", true).addAttribute("length", 9);
		GraphUtils.addReverse(graph, graph.getEdge("AC"));
		graph.addEdge("AD", "A", "D", true).addAttribute("length", 7);
		GraphUtils.addReverse(graph, graph.getEdge("AD"));
		graph.addEdge("CB", "C", "B", true).addAttribute("length", 2);
		GraphUtils.addReverse(graph, graph.getEdge("CB"));
		graph.addEdge("CD", "C", "D", true).addAttribute("length", 10);
		GraphUtils.addReverse(graph, graph.getEdge("CD"));
		graph.addEdge("BE", "B", "E", true).addAttribute("length", 9);
		GraphUtils.addReverse(graph, graph.getEdge("BE"));
		graph.addEdge("CF", "C", "F", true).addAttribute("length", 11);
		GraphUtils.addReverse(graph, graph.getEdge("CF"));
		graph.addEdge("DF", "D", "F", true).addAttribute("length", 15);
		GraphUtils.addReverse(graph, graph.getEdge("DF"));
		graph.addEdge("EF", "E", "F", true).addAttribute("length", 6);
		GraphUtils.addReverse(graph, graph.getEdge("EF"));
		
		return graph;
		
	}

	// Setting everything up...
	
	/**
	 * 
	 */
	public Dijkstra() {
		
		this("A", "E");
		
	}
	
	/**
	 * @param graph
	 */
	public Dijkstra( String source, String target ) {

		this.graph = generateGraph();
		
		settledNodes = new HashSet<Node>();

		unsettledNodes = new HashSet<Node>();

		distanceEstimate = new HashMap<Node, Integer>();

		predecessors = new HashMap<Node, Node>();

		// Distance to source is always 0
		distanceEstimate.put( graph.getNode(source), 0 );

		// Source is first node that needs to be settled
		unsettledNodes.add( graph.getNode(source) );

	}
	
	// Then we can either execute the full algorithm, or step through it...
	
	/**
	 * Run algorithm in loop until complete (uses step).
	 * 
	 * @param target
	 */
	public void execute( Node target ) {

		/* While there are still nodes in the graph that 
		 * have not had their neighbours considered
		 */
		while ( unsettledNodes.size() > 0 ) {
			
			// One step of the algorithm
			step( target );
			
		}
		
		// Finally print path
		System.out.println(getPath(target));

	}
	
	// What happens in one step?
	
	/**
	 * One step of the algorithm
	 * @param target
	 */
	public Node step( Node target ) {
		
		if ( unsettledNodes.size() == 0 ) {
			
			// Nothing more to be done
			return null;
			
		}
		
		// Always deal with the next closest node first (determined via our estimated distance)
		Node node = getClosestFromEstimate( unsettledNodes );
		
		Utils.debug("========================");
		Utils.debug("At node: " + node);
		
		// See if we have any new information on shortest paths
		updateAdjacentNodeDistances(node);

		// Switch current node from settled to unsettled
		settledNodes.add( node );
		unsettledNodes.remove( node );
		
		Utils.debug("========================");
		
		Utils.debug("Distance estimate: " + distanceEstimate);
		
		Utils.debug("========================");
		
		return node;
					
	}
	
	/**
	 * Find the perceived closest node from the set of estimates.
	 * 
	 * (At first will just return start node)
	 * 
	 * @param nodees
	 * @return
	 */
	private Node getClosestFromEstimate( Set<Node> nodes ) {
		
		Node minimum = null;
		
		/* Classic technique for finding the minimum of a set: compare with
		 * constantly updating value.
		 */
		for (Node node : nodes) {
		
			if (minimum == null) {
			
				minimum = node;
			
			} else {
			
				if ( getDistanceEstimate( node ) < getDistanceEstimate( minimum ) ) {
				
					minimum = node;
				
				}
			
			}
		
		}
		
		return minimum;
	
	}

	/**
	 * Given a node, determines if any of the adjacent nodes can be
	 * reached `faster' than its current estimate, by going through this one.
	 * 
	 * @param node
	 */
	private void updateAdjacentNodeDistances( Node node ) {

		List<Node> adjacentNodes = getNeighbours( node );

		for ( Node neighbour : adjacentNodes ) {

			Utils.debug("Is going through " + node + " (+ previous path) to get to " + neighbour + " (" + (getDistanceEstimate( node ) + getDistance( node, neighbour )) + ") faster than our previous route there (" + getDistanceEstimate( neighbour ) + ")?");
			
			// If going through this node to get to a neighbour is less than the current estimate for that neighbour
			if ( getDistanceEstimate( node ) + getDistance( node, neighbour ) < getDistanceEstimate( neighbour ) ) {

				Utils.debug("Yes! New estimate to get to " + neighbour + " is " + (getDistanceEstimate(node) + getDistance(node, neighbour)));
				
				// Update the estimate to the neighbour, based on going through this node 
				distanceEstimate.put(neighbour, getDistanceEstimate(node) + getDistance(node, neighbour));
				
				// Record that to reach the neighbour in the (currently known) fastest way, we must go via this node
				predecessors.put(neighbour, node);
				
				// This neighbour is something that needs to be explored further
				unsettledNodes.add(neighbour);

			} else {
				
				Utils.debug("Nope!");
				
			}

		}

	}
	
	/**
	 * Get the distance estimate to this node, or if no
	 * estimate exists return large value.
	 * 
	 * Serves to make the initial conservative estimation.
	 * 
	 * @param destination
	 * @return
	 */
	private int getDistanceEstimate( Node destination ) {
		
		Integer distance = distanceEstimate.get(destination);
		
		// If there is no estimate, return a conservatively large value
		if ( distance == null ) {
		
			return Integer.MAX_VALUE;
		
		} else {
		
			return distance;
		
		}
	}

	/**
	 * Steps backwards through the list of predecessors
	 * 
	 * @param target
	 * @return
	 */
	public LinkedList<Node> getPath( Node target ) {
		
		Utils.debug( "Predecessors: " + predecessors );
		
		LinkedList<Node> path = new LinkedList<Node>();
		
		// First step (going backwards) is the target
		Node step = target;
		
		// If there is no information about the target, return null;
		if ( predecessors.get(step) == null ) {
		
			return null;
		
		}
		
		// Add the target step as the first step on the path
		path.add(step);
		
		// While there is still step information
		while ( predecessors.get(step) != null ) {
		
			/* Next step is the one behind the current step,
			 * in our list of `fastest paths'.
			 */
			step = predecessors.get(step);
			
			path.add(step);
		
		}
		
		// Put it into the correct order (because we were going backwards)
		Collections.reverse(path);
		
		return path;
	
	}
	
	/*
	 * Utility below
	 * 
	 * 
	 */

	/**
	 * Utility: returns the distance between two nodes
	 * 
	 * @param node
	 * @param target
	 * @return
	 */
	private int getDistance(Node node, Node target) {

		for (Edge edge : graph.getEdgeSet()) {

			if (edge.getSourceNode().equals(node) && edge.getTargetNode().equals(target)) {

				return edge.getAttribute("length");

			}

		}
		
		return -1;
		
	}


	/**
	 * Utility: finds the neighbours of a node
	 * 
	 * Importantly, only returns to unsettled nodes.
	 * 
	 * @param node
	 * @return
	 */
	private List<Node> getNeighbours( Node node ) {

		List<Node> neighbours = new ArrayList<Node>();

		for ( Edge edge : graph.getEdgeSet() ) {

			if ( edge.getSourceNode().equals(node) && !isSettled(edge.getTargetNode()) ) {

				neighbours.add( edge.getTargetNode() );

			}

		}

		return neighbours;

	}

	/**
	 * Utility: returns settled node
	 * 
	 * @param node
	 * @return
	 */
	private boolean isSettled( Node node ) {
	
		return settledNodes.contains( node );
	
	}
	@Override
	public void step() {
		
		// Bad interface design :-( todo: design better!
		
	}

} 