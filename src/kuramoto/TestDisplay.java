package kuramoto;

import java.io.IOException;
import java.util.Iterator;

import org.graphstream.graph.ElementNotFoundException;
import org.graphstream.graph.Node;
import org.graphstream.stream.GraphParseException;
import org.graphstream.ui.spriteManager.Sprite;
import org.graphstream.ui.spriteManager.SpriteManager;

public class TestDisplay {

	public static void main(String[] args) throws ElementNotFoundException,
			IOException, GraphParseException, InterruptedException {
		KuramotoNetwork g = new KuramotoNetwork("test");
		g.read(Parameters.FILENAME);
		g.addAttribute("ui.antialias", true);
		g.addAttribute("ui.stylesheet", Parameters.STYLESHEET);

		for (Node n : g)
			n.addAttribute("ui.class", n.getAttribute("value"));

		if (Parameters.DISPLAY_ONLY)
			g.display();
		else {
			SpriteManager sm = new SpriteManager(g);
			Sprite s = sm.addSprite("1");
			s.setPosition(-1.1, -1.1, 0);
			s = sm.addSprite("2");
			s.setPosition(-1.1, +1.1, 0);
			s = sm.addSprite("3");
			s.setPosition(+1.1, -1.1, 0);
			s = sm.addSprite("4");
			s.setPosition(+1.1, +1.1, 0);
			g.display(false);

			g.initNodes();

			while (true) {
				Thread.sleep(Parameters.DELAY);
				g.step();
				Iterator<KuramotoNode> it = g.getNodeIterator();
				while (it.hasNext()) {
					KuramotoNode node = it.next();
					double r = Parameters.INNER_RADIUS + node.getIndex() * (1 - Parameters.INNER_RADIUS) / g.getNodeCount();
					node.addAttribute("xy", r * Math.cos(node.currentTheta), r
							* Math.sin(node.currentTheta));
				}
			}
		}
	}
}
