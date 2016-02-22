package buildx.views;

import buildx.algorithm.Dijkstra;

/**
 * @author Martin
 *
 */
public class AlgorithmRun {

	/**
	 * 
	 */
	public AlgorithmRun() {
		
		Build1();
		
	}
	
	private void Build1() {
		
		Dijkstra build1 = new Dijkstra();
		
		build1.execute(build1.getGraph().getNode("A"));
		
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		new AlgorithmRun();
		
	}
	
}
