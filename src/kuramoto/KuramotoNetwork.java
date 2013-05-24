package kuramoto;

import java.util.Iterator;
import java.util.Random;

import org.graphstream.graph.Graph;
import org.graphstream.graph.NodeFactory;
import org.graphstream.graph.implementations.AbstractGraph;
import org.graphstream.graph.implementations.SingleGraph;

public class KuramotoNetwork extends SingleGraph {
	protected double[] coefK, coefX;

	public KuramotoNetwork(String id) {
		super(id);
		setNodeFactory(
			new NodeFactory<KuramotoNode>() {
				public KuramotoNode newInstance(String id, Graph graph) {
					return new KuramotoNode((AbstractGraph) graph, id);
				}
			});
		
		coefK = new double[4];
		coefX = new double[4];
		coefK[0] = 0;
		coefK[1] = coefK[2] = Parameters.DELTA_T / 2;
		coefK[3] = Parameters.DELTA_T;
		coefX[0] = coefX[3] = Parameters.DELTA_T / 6;
		coefX[1] = coefX[2] = Parameters.DELTA_T / 3;
	}
	
	public void initNodes() {
		Random rnd = new Random();
		Iterator<KuramotoNode> it = getNodeIterator();
		while (it.hasNext()) {
			KuramotoNode node = it.next();
			node.w = 0.5 + rnd.nextDouble();
			node.currentTheta = node.nextTheta = Math.PI * (2 * rnd.nextDouble() - 1);
		}
	}
	
	public void step() {
		Iterator<KuramotoNode> it;
		for (int i = 0; i < 4; i++) {
			it = getNodeIterator();
			while (it.hasNext())
				it.next().calcK(coefK[i]);

			it = getNodeIterator();
			while (it.hasNext())
				it.next().coupling();
			
			it = getNodeIterator();
			while (it.hasNext())
				it.next().stepK(coefX[i]);
		}
		it = getNodeIterator();
		while(it.hasNext())
			it.next().step();		
	}
}
