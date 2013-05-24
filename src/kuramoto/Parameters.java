package kuramoto;

public class Parameters {
	public static final String FILENAME = "data/polbooks.gml";
	
	public static final String STYLESHEET = "node {text-mode: hidden;} " +
			"node.a {fill-color: red;} node.b {fill-color: blue;} node.c {fill-color: magenta;} " +
			"edge {fill-color: grey;} " +
			"sprite {fill-color: white;}";
	
	public static final boolean DISPLAY_ONLY = false;
	
	public static final double INNER_RADIUS = 0.8;

	public static final long DELAY = 10;
		
	protected static final double DELTA_T = 1.0e-3;

	protected static final double COUPLING_STRENGTH = 1.0;
}
