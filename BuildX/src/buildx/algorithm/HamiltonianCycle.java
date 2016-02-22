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
	 * 
	 */
	public void solve() {
		
		hackierBoolean = false;
		
		System.out.println(findPath());
		
	}
	
	/**
	 * @return
	 */
	public ArrayList<Node> findPath() {
		
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
	 * @param node
	 * @param solutionPath
	 * @return
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
			
			throw new Exception("Solution found");
			
		}

		if ( path.size() == graph.getNodeSet().size() ) {
			
			return solutionPath;
			
		}

		for ( Node otherNode : graph.getNodeSet() ) {
			
			if ( node.hasEdgeBetween(otherNode) ) {
				
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
	            
				path.add( otherNode ) ;
				
				graph.removeEdge(node, otherNode);
				graph.removeEdge(otherNode, node);

				Utils.debug( "Removing: " + node + " <-> " + otherNode + ". Edges in graph: " + graph.getEdgeCount() );
				
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

				graph.addEdge(node.getId() + otherNode.getId(), node, otherNode);
				graph.addEdge(otherNode.getId() + node.getId(), otherNode, node);
				
				Utils.debug( "Restoring: " + node + " <-> " + otherNode + ". Edges in graph: " + graph.getEdgeCount() );
				
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

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		Utils.debug = true;
		
		HamiltonianCycle build2 = new HamiltonianCycle();
		
		build2.findPath();
		
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