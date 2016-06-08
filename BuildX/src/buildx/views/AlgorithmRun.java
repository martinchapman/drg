package buildx.views;

import buildx.algorithm.Dijkstra;
import buildx.algorithm.HamiltonianCycle;
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
		
		//Build2();
		
	}
	
	private void Build1() {
		
		Utils.debug = true;
		
		Dijkstra build1 = new Dijkstra();
		
		build1.execute(build1.getGraph().getNode("E"));
		
	}
	
	private void Build2() {
		
		Utils.debug = true;
		
		HamiltonianCycle build2 = new HamiltonianCycle();
		
		build2.findPath();
		
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		new AlgorithmRun();
		
	}
	
}
