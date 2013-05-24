package label;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.graphstream.algorithm.Algorithm;
import org.graphstream.algorithm.Toolkit;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

/**
 * <p>
 * A label propagation community detection algorithm.
 * </p>
 * 
 * <p>
 * At the beginning all nodes have different labels. At each iteration nodes are
 * processed in random order. Each node looks at the labels of its neighbors and
 * chooses the most frequent of them as new label. If there is a tie it is
 * broken arbitrarily. The algorithm stops when no label changes at the last
 * iteration.
 * </p>
 * 
 * @author Stefan Balev
 * 
 */
public class LabelAlgorithm implements Algorithm {
	/**
	 * Default community marker.
	 */
	public static final String DEFAULT_MARKER = "community";

	/**
	 * The graph where we detect communities
	 */
	protected Graph graph;

	/**
	 * The name of the attribute used as a marker. Each node is tagged with an
	 * Integer corresponding to its community.
	 */
	protected String marker = DEFAULT_MARKER;

	/**
	 * Source of randomness
	 */
	protected Random rnd = new Random();

	/**
	 * Stocks the neighbor communities of a node. The key is community and the
	 * value is the number of neighbors in this community.
	 */
	protected Map<Integer, Integer> neighborCommunities = new HashMap<Integer, Integer>();

	/**
	 * Stocks the neighbor communities of maximal size of a node
	 */
	protected List<Integer> maxCountNeighborCommunities = new ArrayList<Integer>();

	/**
	 * Order in which the nodes are processed. Changes at each iteration.
	 */
	protected List<Node> randomPermutation;

	public void init(Graph graph) {
		this.graph = graph;
		randomPermutation = new ArrayList<Node>(graph.getNodeSet());
		for (Node node : graph)
			node.addAttribute(marker, node.getIndex());
	}

	public void compute() {
		int it = 0;
		while (iteration()) {
			it++;
			System.err.printf("Iteration %3d   Modularity %6.4f%n", it,
					Toolkit.modularity(graph, marker));
		}
		System.err.println("Done");
	}

	/**
	 * Returns the community marker
	 * 
	 * @return The name of the attribute used to tag the nodes
	 */
	public String getMarker() {
		return marker;
	}

	/**
	 * Changes the community marker
	 * 
	 * @param marker
	 *            The name of the attribute used to tag the nodes
	 */
	public void setMarker(String marker) {
		this.marker = marker;
	}

	/**
	 * Computes the most frequent neighbor communities of a node and stores them
	 * in {@link #maxCountNeighborCommunities}
	 * 
	 * @param node
	 *            A node
	 */
	protected void compute(Node node) {
		neighborCommunities.clear();
		Iterator<Node> it = node.getNeighborNodeIterator();
		while (it.hasNext()) {
			Integer community = it.next().getAttribute(marker);
			Integer count = neighborCommunities.get(community);
			if (count == null)
				count = 0;
			neighborCommunities.put(community, count + 1);
		}
		int maxCount = Collections.max(neighborCommunities.values());
		maxCountNeighborCommunities.clear();
		for (Map.Entry<Integer, Integer> entry : neighborCommunities.entrySet())
			if (entry.getValue() == maxCount)
				maxCountNeighborCommunities.add(entry.getKey());
	}

	/**
	 * A single iteration of the algorithm
	 * 
	 * @return true if there are more iterations to execute after this one
	 */
	protected boolean iteration() {
		boolean more = false;
		Collections.shuffle(randomPermutation);
		for (Node node : randomPermutation) {
			compute(node);
			int oldCommunity = node.getAttribute(marker);
			if (!maxCountNeighborCommunities.contains(oldCommunity))
				more = true;
			int newCommunity = maxCountNeighborCommunities.get(rnd
					.nextInt(maxCountNeighborCommunities.size()));
			if (oldCommunity != newCommunity)
				node.addAttribute(marker, newCommunity);
		}
		return more;
	}
}
