package kuramoto;

import org.graphstream.graph.Edge;
import org.graphstream.graph.implementations.AbstractGraph;
import org.graphstream.graph.implementations.SingleNode;

public class KuramotoNode extends SingleNode {
	protected double w;
	protected double currentTheta, nextTheta;
	protected double currentKTheta, nextKTheta, tmpTheta;

	protected KuramotoNode(AbstractGraph graph, String id) {
		super(graph, id);
	}
	
	protected void calcK(double coef) {
		tmpTheta = currentTheta + coef * currentKTheta;
		nextKTheta = w;
	}
	
	protected void coupling() {
		for (Edge edge : getEnteringEdgeSet()) {
			KuramotoNode neighbor = edge.getOpposite(this);
			nextKTheta += Parameters.COUPLING_STRENGTH * Math.sin(neighbor.tmpTheta - tmpTheta);
		}		
	}
	
	protected void stepK(double coef) {
		nextTheta += coef * nextKTheta;
		currentKTheta = nextKTheta;
	}
	
	protected void step() {
		while (nextTheta < - Math.PI)
			nextTheta += 2 * Math.PI;
		while (nextTheta > Math.PI)
			nextTheta -= 2 * Math.PI;
		
		currentTheta = nextTheta;
	}
}
