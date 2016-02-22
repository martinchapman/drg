package buildx.views;

import buildx.algorithm.Dijkstra;
import buildx.utils.Utils;

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
		
		Utils.debug = true;
		
		Dijkstra build1 = new Dijkstra();
		
		build1.execute(build1.getGraph().getNode("E"));
		
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		new AlgorithmRun();
		
	}
	
}
