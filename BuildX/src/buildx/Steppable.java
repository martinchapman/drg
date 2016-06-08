package buildx;

import org.graphstream.graph.Node;

public interface Steppable {

	public abstract Node step(Node target);
	
	public abstract void step();
	
}
