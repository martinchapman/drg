package buildx;

import java.util.Observable;

import org.graphstream.graph.Graph;

public abstract class BuildGraph extends Observable {

	protected Graph graph;
	
	public Graph getGraph() {
		
		return graph;
		
	}
	
	protected abstract Graph generateGraph();
}
