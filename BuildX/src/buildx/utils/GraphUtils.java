package buildx.utils;

import java.util.ArrayList;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

public class GraphUtils {

	private GraphUtils() {}
	
	public static void colourNode( Node node, String colour ) {
		
		if ( !colour.contains("fill-color") ) {
			
			colour = "fill-color: " + colour + ";";
			
		}
		
		node.setAttribute("ui.style", colour);
		
	}

	public static void colourNode( Node node ) {
		
		node.addAttribute("ui.style", "fill-color: blue; size: 50px, 50px;");
		
	}
	
	public static void display( Graph graph ) {
		
		display(graph, "50", "30", "30");
		
	}
	
	public static void display( Graph graph, String nodeSize, String textSize, String labelSize ) {
		
		for ( Node node : graph ) {

			node.addAttribute("label", node.getId());
			
			node.addAttribute("ui.style", "fill-color: black; size: " + nodeSize + "px, " + nodeSize + "px; text-size: " + textSize + "px;");
			
		}
	
		ArrayList<String> usedIDs = new ArrayList<String>();
		
		for ( Edge edge : graph.getEachEdge() ) {
			
			if ( usedIDs.contains(new StringBuilder(edge.getId()).reverse().toString()) ) continue;
			
			edge.addAttribute("label", "" + (int) edge.getNumber("length"));
			
			edge.addAttribute("ui.style", "text-size: " + labelSize + "px;");
			
			usedIDs.add(edge.getId());
	
		}
	
		graph.display();
		
	}
	
	public static void addReverse( Graph graph, Edge edge ) {
		
		graph.addEdge(new StringBuilder(edge.getId()).reverse().toString(), edge.getTargetNode().toString(), edge.getSourceNode().toString(), true).addAttribute("length", 14);
		
	}
}
