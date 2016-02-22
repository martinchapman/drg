package buildx.views;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JFrame;

import org.graphstream.graph.Node;

import buildx.BuildGraph;
import buildx.Steppable;
import buildx.algorithm.Dijkstra;
import buildx.algorithm.HamiltonianCycle;
import buildx.utils.GraphUtils;
import buildx.utils.Utils;

/**
 * 
 * @author Martin
 *
 */
public class AlgorithmStep implements Observer {
	
	/**
	 * 
	 */
	public AlgorithmStep() {
		
		Utils.debug = true;
		
		//Build1();
		
		Build2();
		
	}
	
	/**
	 * 
	 */
	private Steppable algorithm;
	
	/**
	 * 
	 */
	public void Build2() {
		
		previousNodeColour = null;
		
		algorithm = new HamiltonianCycle();
		
		((HamiltonianCycle)algorithm).addObserver(this);
		
		GraphUtils.display(((BuildGraph)algorithm).getGraph());
		
		GraphUtils.colourNode(((BuildGraph)algorithm).getGraph().getNode(((HamiltonianCycle)algorithm).getCurrentNode().getId()), "red");
		
		stepButton().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				algorithm.step();
				
			}
			
		});
		
		Thread t = new Thread(((HamiltonianCycle)algorithm), "");
		
		t.run();
		
	}
	
	/* (non-Javadoc)
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(Observable o, Object arg) {
		
		colourBlueStorePrevious(((BuildGraph)algorithm).getGraph().getNode(((HamiltonianCycle)algorithm).getCurrentNode().getId()));
		
	}
	
	/**
	 * @param node
	 */
	private void colourBlueStorePrevious( Node node  ) {
	
		if ( previousNodeColour != null ) { 
			
			GraphUtils.colourNode(previousNodeColour.getNode(), previousNodeColour.getColour()); 
			
		}
		
		previousNodeColour = new PreviousNodeColour(node, node.getAttribute("ui.style").toString());
		
		GraphUtils.colourNode(node);
		
	}
	
	/**
	 * 
	 */
	private void Build1() {
		
		previousNodeColour = null;
		
		Steppable algorithm = new Dijkstra();
		
		GraphUtils.display(((BuildGraph)algorithm).getGraph());
		
		Node source = ((BuildGraph)algorithm).getGraph().getNode("A");
		
		Node target = ((BuildGraph)algorithm).getGraph().getNode("E");
		
		GraphUtils.colourNode(source, "red");
		
		GraphUtils.colourNode(target, "green");
		
		stepButton().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				Node stepped = algorithm.step( target );
				
				int pathIndex = 1;
				
				if ( stepped == null ) {
					
					List<Node> path = ((Dijkstra)algorithm).getPath(target);
					
					System.out.println(path);
					
					for ( Node node : path ) {
						
						GraphUtils.colourNode(node, "yellow");
						
						node.setAttribute("label", node.getAttribute("label") + "(" + (pathIndex++) + ")");
						
					}
					
				} else {

					colourBlueStorePrevious(stepped);
				
				}
				
			}	
			
		});
		
	}
	
	/**
	 * @return
	 */
	private JButton stepButton() {
		
		JFrame stepFrame = new JFrame();
		
		stepFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		stepFrame.setPreferredSize(new Dimension(50, 50));
		
		JButton step = new JButton("Step");
		
		stepFrame.add(step);
		
		stepFrame.pack();
		
		stepFrame.setVisible(true);
		
		return step;
		
	}
	
	/**
	 * 
	 */
	private PreviousNodeColour previousNodeColour;
	
	/**
	 * @author Martin
	 *
	 */
	private class PreviousNodeColour {
		
		Node node;
		
		String colour;
		
		public PreviousNodeColour(Node node, String colour) {
			
			this.node = node;
			
			this.colour = colour;
			
		}
		
		public Node getNode() {
			
			return node;
			
		}
		
		public String getColour() {
			
			return colour;
			
		}
		
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		new AlgorithmStep();
		
	}

}
