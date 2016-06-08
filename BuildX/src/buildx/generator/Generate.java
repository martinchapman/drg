package buildx.generator;

import org.graphstream.algorithm.generator.BarabasiAlbertGenerator;
import org.graphstream.algorithm.generator.Generator;
import org.graphstream.algorithm.generator.RandomGenerator;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;

/**
 * Relies on GraphStream library (included in lib folder): 
 * http://graphstream-project.org/
 * 
 * @author Martin
 *
 */
public class Generate {

	/**
	 * 
	 */
	private static int numberOfNodes = 50;
	
	/**
	 * Degree = (average) number of edges attached to a node
	 */
	private static int degree = 10;
	
	/**
	 * 
	 */
	public Generate() {
		
		//generateRandom();
		
		generateScaleFree();
	
	}
	
	/**
	 * Bollobás and Erdős–Rényi
	 * 
	 * Two generation approaches:
	 * 
	 * 1. The whole graph is chosen at random
	 * 2. The edges are chosen at random (i.e. two nodes are connected at random)
	 * 
	 * *Insert meme of dog who doesn't know what he's doing*
	 * 
	 */
	private void generateRandom() {
		
		Graph graph = new SingleGraph("Random");
		
		Generator gen = new RandomGenerator(degree);
	    
		// Allows the generator (source) to add things to the graph
		gen.addSink(graph);
	    
		gen.begin();
	    
		for ( int i = 0; i < numberOfNodes; i++ ) {
			
	        gen.nextEvents();
	       
		}
	    
		gen.end();
		
		graph.display();
		
	}
	
	/**
	 * Albert-László Barabási and Eric Bonabeau. 
	 * 
	 * Principle of Preferential attachment: add to nodes
	 * that already have a high degree with higher probability.
	 * 
	 */
	private void generateScaleFree() {
		
		Graph graph = new SingleGraph("Barabàsi-Albert");
		
		Generator gen = new BarabasiAlbertGenerator(degree);
	
		gen.addSink(graph); 
		
		gen.begin();

		for( int i = 0; i < numberOfNodes; i++ ) {
			
			gen.nextEvents();
		
		}

		gen.end();
		
		graph.display();
		
	}
	
	public static void main(String[] args) {
		
		new Generate();
	
	}
	
}
