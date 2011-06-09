package draw2d;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.Panel;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.XYAnchor;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.graph.DirectedGraph;
import org.eclipse.draw2d.graph.DirectedGraphLayout;
import org.eclipse.draw2d.graph.Edge;
import org.eclipse.draw2d.graph.EdgeList;
import org.eclipse.draw2d.graph.Node;
import org.eclipse.draw2d.graph.NodeList;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

// Layout a graph using DirectedGraphLayout.
public class GraphExample extends ApplicationWindow {

	public static void main(String[] args) {
		GraphExample window = new GraphExample();
		// add status line
		window.addStatusLine();
		window.setBlockOnOpen(true);
		window.open();

		Display.getCurrent().dispose();
	}

	public GraphExample() {
		super(null);
	}

	@Override
	protected void configureShell(Shell shell) {
		shell.setSize(400, 400);
		super.configureShell(shell);
	}

	protected Control createContents(Composite parent) {
		Composite composite = new Composite(parent, SWT.BORDER);
		composite.setLayout(new FillLayout());

		FigureCanvas canvas = new FigureCanvas(composite);
		Panel contents = new Panel();
		contents.setLayoutManager(new XYLayout());
		contents.setBackgroundColor(ColorConstants.listBackground);
		buildGraph(contents, getGraph());

		canvas.setContents(contents);

		return composite;
	}

	// create graph Node and Edge
	@SuppressWarnings("unchecked")
	private DirectedGraph getGraph() {

		Node node1, node2, node3, node4, node5;
		Edge edge1, edge2, edge3, edge4;

		NodeList nodes = new NodeList();
		EdgeList edges = new EdgeList();

		// create Node
		node1 = new Node("Node1");
		nodes.add(node1);
		node2 = new Node("Node2");
		nodes.add(node2);
		node3 = new Node("Node3");
		nodes.add(node3);
		node4 = new Node("Node4");
		nodes.add(node4);
		node5 = new Node("Node5");
		nodes.add(node5);

		// create Edge
		edge1 = new Edge(node1, node2);
		edges.add(edge1);
		edge2 = new Edge(node1, node3);
		edges.add(edge2);
		edge3 = new Edge(node2, node4);
		edges.add(edge3);
		edge4 = new Edge(node4, node5);
		edges.add(edge4);

		DirectedGraph graph = new DirectedGraph();
		graph.nodes = nodes;
		graph.edges = edges;

		// set Layout of the graph (Compute and Set the x,y of each Node)
		new DirectedGraphLayout().visit(graph);

		return graph;
	}

	// Based on DirectedGraphLayout computation result, add figure and
	// connection
	private void buildGraph(IFigure contents, DirectedGraph graph) {

		for (int i = 0; i < graph.nodes.size(); i++) {
			Node node = graph.nodes.getNode(i);
			buildNodeFigure(contents, node);
		}

		for (int i = 0; i < graph.edges.size(); i++) {
			Edge edge = graph.edges.getEdge(i);
			buildEdgeFigure(contents, edge);
		}

	}

	// configure Node Figure based on Node location info
	private void buildNodeFigure(IFigure contents, Node node) {
		Label label = new Label();
		label.setBackgroundColor(ColorConstants.orange);
		label.setOpaque(true);
		label.setBorder(new LineBorder());
		label.setText(node.data.toString());
		contents.add(label);

		// Set Figure constraint based on Node location
		contents.setConstraint(label, new Rectangle(node.x, node.y, node.width,
				node.height));
	}

	// Configure Edge Figure based on Node location info
	private void buildEdgeFigure(IFigure contents, Edge edge) {
		PolylineConnection connx = new PolylineConnection();
		PointList points = edge.getPoints();
		XYAnchor sourceAnchor = new XYAnchor(points.getFirstPoint());
		XYAnchor targetAnchor = new XYAnchor(points.getLastPoint());
		connx.setSourceAnchor(sourceAnchor);
		connx.setTargetAnchor(targetAnchor);
		contents.add(connx);
	}
}
