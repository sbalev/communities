package label;

import java.io.IOException;

import org.graphstream.graph.Edge;
import org.graphstream.graph.ElementNotFoundException;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.stream.GraphParseException;
import org.graphstream.stream.SinkAdapter;

public class LabelDemo extends SinkAdapter {
	private Graph graph;


	@Override
	public void nodeAttributeChanged(String sourceId, long timeId,
			String nodeId, String attribute, Object oldValue, Object newValue) {
		if (attribute.equals(LabelAlgorithm.DEFAULT_MARKER)) {
			colorAndWeight(graph.getNode(nodeId), (Integer)newValue);
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
			}
		}
	}
	
	private void colorAndWeight(Node node, int community) {
		node.addAttribute("ui.color", (double)community / graph.getNodeCount());
		for (Edge edge : node) {
			int neighborCommunity = edge.getOpposite(node).getAttribute(LabelAlgorithm.DEFAULT_MARKER);
			if (community == neighborCommunity) {
				edge.addAttribute("ui.style", "fill-color: #888;");
				edge.removeAttribute("layout.weight");
			} else {
				edge.addAttribute("ui.style", "fill-color: #DDD8;");
				edge.addAttribute("layout.weight", 10);
			}
		}
	}


	public static void main(String[] args) throws ElementNotFoundException,
			IOException, GraphParseException {
		Graph g = new SingleGraph("imdb");
		g.read("data/imdb.dgs");
		for (Node node : g) {
			node.removeAttribute("ui.size");
			node.removeAttribute("label");
			node.removeAttribute("weight");
			node.removeAttribute("xyz");
			node.addAttribute("ui.color", (double)node.getIndex() / g.getNodeCount());
		}
		for (Edge e : g.getEachEdge()) {
			e.addAttribute("layout.weight", 10);
		}
		g.addAttribute("ui.antialias", true);
		g.addAttribute(
				"ui.stylesheet",
				"edge {fill-color: #DDD8;} node {size: 10px; fill-mode: dyn-plain; fill-color: #F0F, #00F, #0FF, #0F0, #FF0, #F00; }");
		g.display();

		LabelDemo demo = new LabelDemo();
		demo.graph = g;
		g.addAttributeSink(demo);

		LabelAlgorithm algo = new LabelAlgorithm();
		algo.init(g);
		algo.compute();
	}
}