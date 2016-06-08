package buildx.algorithm;

import java.util.ArrayList;
import java.util.Arrays;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;

import buildx.BuildGraph;
import buildx.Steppable;
import buildx.utils.GraphUtils;
import buildx.utils.Utils;

/**
 * Implementation of a Hamiltonian Cycle algorithm: https://en.wikipedia.org/wiki/Hamiltonian_path
 * 
 * Recursive approach used by: https://github.com/ajitkoti/Algorithms/blob/master/src/com/interview/algorithms/graph/HamiltonianCycle.java
 * 
 * @author Martin
 *
 */
public class HamiltonianCycle extends BuildGraph implements Steppable, Runnable {

	/**
	 * 
	 */
	private ArrayList<Node> path;
	
	/**
	 * 
	 */
	private Node start;
	
	/**
	 * 
	 */
	private Node currentNode;
	
	/**
	 * @return
	 */
	public Node getCurrentNode() {
		
		return currentNode;
		
	}

	/**
	(A)--(B)--(C)
	 |   / \   |
	 |  /   \  | 
	 | /     \ |
	(D)-------(E)
	 */
	protected Graph generateGraph() {

		Graph graph = new MultiGraph( "Hamiltonian" );

		graph.addNode("A").addAttribute("xy", 0, 1);
		graph.addNode("B").addAttribute("xy", 1, 2);
		graph.addNode("C").addAttribute("xy", 1, 1);
		graph.addNode("D").addAttribute("xy", 1, 0);
		graph.addNode("E").addAttribute("xy", 2, 2);

		graph.addEdge("AB", "A", "B", true);
		GraphUtils.addReverse(graph, graph.getEdge("AB"));
		graph.addEdge("BC", "B", "C", true);
		GraphUtils.addReverse(graph, graph.getEdge("BC"));
		graph.addEdge("CE", "C", "E", true);
		GraphUtils.addReverse(graph, graph.getEdge("CE"));
		graph.addEdge("ED", "E", "D", true);
		GraphUtils.addReverse(graph, graph.getEdge("ED"));
		graph.addEdge("DA", "D", "A", true);
		GraphUtils.addReverse(graph, graph.getEdge("DA"));
		graph.addEdge("DB", "D", "B", true);
		GraphUtils.addReverse(graph, graph.getEdge("DB"));
		graph.addEdge("EB", "E", "B", true);
		GraphUtils.addReverse(graph, graph.getEdge("EB"));
	
		start = graph.getNode("A");
		
		currentNode = start;
		
		return graph;

	}
	
	/**
	 * 
	 */
	public HamiltonianCycle() {	
		
		graph = generateGraph();
		
		path = new ArrayList<Node>();
		
	}
	
	/* (non-Javadoc)
	 * @see BuildX.Steppable#step()
	 */
	public synchronized void step() {
		
		hackySuspend = false;
		
		notify();
		
	}


	/**
	 * 
	 */
	private boolean hackySuspend;
	
	/**
	 * 
	 */
	private boolean hackierBoolean;
	
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		
		hackierBoolean = true;
		
		findPath();
		
	}
	
	/**
	 * Will solve without steps
	 */
	public void solve() {
		
		hackierBoolean = false;
		
		System.out.println(findPath());
		
	}
	
	/**
	 * Starts the recursive solution
	 * 
	 * @return The path taken
	 */
	public ArrayList<Node> findPath() {
		
		// Use the below to track the recursion
		ArrayList<Node> solutionPath = null;
				
		try {
			
			path.add(start);
			
			solutionPath = solve(start, new ArrayList<Node>());
			
			Utils.debug("No solution");
		
		} catch (Exception e) {
		
			Utils.debug(e.getMessage());
			
			Utils.debug(path + "");
			
		}
		
		return path;
		
	}
	
	/**
	 * Main recursive method for moving through graph to construct path
	 * 
	 * @param node
	 * @param solutionPath
	 * @return The hamiltonian path
	 * @throws Exception
	 */
	public ArrayList<Node> solve( Node node, ArrayList<Node> solutionPath ) throws Exception {
		
		// If there is a link back to the start node, assume this is the end of the path
		if ( node.hasEdgeBetween( start ) && path.size() == graph.getNodeSet().size() ) {
			
			Utils.debug( "At node: " + node + " looking at " + start);
			
			currentNode = node;
			
			setChanged();
		      
			notifyObservers();
			
			// Add start node.
			path.add( start );
			
			// Breaks recursion once solution is found.
			throw new Exception("Found a solution.");
			
		}

		// If our path is complete, but we are not back at the start, return this as a `best effort' solution.
		if ( path.size() == graph.getNodeSet().size() ) {
			
			return solutionPath;
			
		}
		
		// Look through all neighbours
		for ( Node otherNode : graph.getNodeSet() ) {
			
			if ( node.hasEdgeBetween(otherNode) ) {
				
				Utils.debug("====================");
				Utils.debug("Path: " + path);
				Utils.debug("====================");
				
				Utils.debug( "At node: " + node + " looking at " + otherNode);
				
				currentNode = node;
				
				setChanged();
			      
				notifyObservers();
				
				if ( hackierBoolean ) { 
					
					hackySuspend = true;
				
					Thread.sleep(300);
					
				}
				
	            synchronized ( this ) {
	            
	            	while ( hackySuspend) {
	            
	            		wait();
	            	
	            	}
	            	
	            }
	            
	            // Record that we are at this neighbour
				path.add( otherNode );
				
				// Remove edge to prevent backtracking (potential critique: wouldn't looking ahead be better?)
				graph.removeEdge(node, otherNode);
				graph.removeEdge(otherNode, node);

				Utils.debug( "Removing: " + node + " <-> " + otherNode + ". Edges in graph: " + graph.getEdgeCount() );
				
				// If we haven't already been here, 
				if ( !isPresent(otherNode) ) {
					
					solve(otherNode, solutionPath);
					
				} else {
					
					Utils.debug( otherNode + " already on graph, putting edge back, removing from path." );
				
				}
				
				synchronized ( this ) {
		            
	            	while ( hackySuspend) {
	            
	            		wait();
	            	
	            	}
	            	
	            }

				/* Otherwise, don't recurse, restore edge OR these edges would be restored after the recursive call,
				 * once each nested call to solve completes (i.e. restored in reverse order via recursion unwinding).
				 */
				graph.addEdge(node.getId() + otherNode.getId(), node, otherNode);
				graph.addEdge(otherNode.getId() + node.getId(), otherNode, node);
				
				Utils.debug( "Restoring: " + node + " <-> " + otherNode + ". Edges in graph: " + graph.getEdgeCount() );
				
				/* Relying on the solution exception never to reach this point unless a edge has been
				 * added to a priorly explored node (otherwise would reach here when unwinding and affect file solution).
				 */
				path.remove(path.size() - 1);
				
			}
			
		}
		
		return solutionPath;
		
	}

	/*
	 * Utility below.
	 * 
	 * 
	 */
	
	/**
	 * Checks if a node is already on our path.
	 * 
	 * Required because GraphStream doesn't necessarily provide 
	 * a way to compare nodes, unfortunately.
	 * 
	 * @param node
	 * @return
	 */
	public boolean isPresent(Node node) {
	
		for (int i = 0; i < path.size(); i++) {
			
			if ( path.get(0).equals(node) ) {
				
				return true;
			
			}
			
		}
		
		return false;
	
	}


	/* (non-Javadoc)
	 * @see BuildX.Steppable#step(org.graphstream.graph.Node)
	 */
	@Override
	public Node step(Node target) {
		
		// Bad interface design :-( todo: design better!
		return null;
		
	}

}