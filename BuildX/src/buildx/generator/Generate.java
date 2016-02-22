package buildx.generator;

import org.graphstream.algorithm.generator.BarabasiAlbertGenerator;
import org.graphstream.algorithm.generator.Generator;
import org.graphstream.algorithm.generator.RandomGenerator;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;

public class Generate {

	/**
	 * 
	 */
	private static int numberOfNodes = 100;
	
	/**
	 * Degree = (average) number of edges attached to a node
	 */
	private static int degree = 5;
	
	/**
	 * 
	 */
	public Generate() {
		
		generateRandom();
		
		//generateScaleFree();
	
	}
	
	/**
	 * Erdős–Rényi, Bollobás
	 * 
	 * Two generation approaches:
	 * 
	 * 1. The whole graph is chosen at random
	 * 2. The edges are chosen at random
	 * 
	 * *Insert meme of dog who doesn't know what he's doing*
	 * 
	 */
	private void generateRandom() {
		
		Graph graph = new SingleGraph("Random");
		
		Generator gen = new RandomGenerator(degree);
	    
		gen.addSink(graph);
	    
		gen.begin();
	    
		for ( int i = 0; i < numberOfNodes; i++ ) {
			
	        gen.nextEvents();
	       
		}
	    
		gen.end();
	    
		graph.display();
		
	}
	
	/**
	 * Albert-László BY Barabási and Eric Bonabeau. 
	 * 
	 * Preferential attachment
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
