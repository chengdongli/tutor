package draw2d;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.AbsoluteBendpoint;
import org.eclipse.draw2d.Bendpoint;
import org.eclipse.draw2d.BendpointConnectionRouter;
import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.CompoundBorder;
import org.eclipse.draw2d.ConnectionEndpointLocator;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.ManhattanConnectionRouter;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.MidpointLocator;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.PolylineDecoration;
import org.eclipse.draw2d.RotatableDecoration;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

// Two labels connected with different connections with different
// decorations and routers.
@SuppressWarnings("unused")
public class ConnectionExample extends ApplicationWindow {
	public ConnectionExample() {
		super(null);
	}

	public static void main(String[] args) {
		ConnectionExample window = new ConnectionExample();
		window.setBlockOnOpen(true);
		window.open();

		Display.getCurrent().dispose();
	}

	protected Control createContents(Composite parent) {
		FigureCanvas canvas = new FigureCanvas(parent);
		LightweightSystem lws = new LightweightSystem(canvas);

		IFigure contents = new Figure();
		contents.setLayoutManager(new XYLayout());

		CompoundBorder border = new CompoundBorder(new LineBorder(),
				new MarginBorder(3));
		// source of connection
		IFigure source = new Label("Source");
		source.setBorder(border);
		source.setOpaque(true);
		source.setBackgroundColor(ColorConstants.lightGreen);
		contents.add(source, new Rectangle(100, 100, -1, -1));

		// target of connection
		IFigure target = new Label("Target");
		target.setBorder(border);
		target.setOpaque(true);
		target.setBackgroundColor(ColorConstants.lightGray);
		contents.add(target, new Rectangle(300, 300, -1, -1));

		connect(contents, source, target);

		lws.setContents(contents);
		return canvas;
	}

	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setSize(600, 600);
	}

	// Connect the source and target with a connection (with possible decoration)
	private void connect(IFigure parent, IFigure source, IFigure target) {
		PolylineConnection connection = new PolylineConnection();

		connection.setSourceAnchor(new ChopboxAnchor(source));
		connection.setTargetAnchor(new ChopboxAnchor(target));

//		RotatableDecoration decoration = createPolylineDecoration();
		RotatableDecoration decoration = createPolygonDecoration();
		connection.setSourceDecoration(decoration);

//		setBendpointRouter(connection);
		setManhattanRouter(connection);
		
//		setMidpointLocator(connection);
		setConnectionEndLocator(connection);

		parent.add(connection);
	}

	// Create a diamond-shape PolygonDecoration
	private RotatableDecoration createPolygonDecoration() {
		PolygonDecoration decoration = new PolygonDecoration();
		// decoration.setBackgroundColor(ColorConstants.white);
		// draw a diamond decoration
		PointList pointList = new PointList();
		pointList.addPoint(0, 0);
		pointList.addPoint(-2, -2);
		pointList.addPoint(-4, 0);
		pointList.addPoint(-2, 2);
		decoration.setTemplate(pointList);
		return decoration;
	}

	// Create a PolylineDecoration
	private RotatableDecoration createPolylineDecoration() {
		PolylineDecoration decoration = new PolylineDecoration();
		PointList pointList = new PointList();
		pointList.addPoint(-3, 2);
		pointList.addPoint(-3, -2);
		pointList.addPoint(0, 0);
		decoration.setTemplate(pointList);
		return decoration;
	}

	// Set Bendpoint Router to a PolylineConnection
	private void setBendpointRouter(PolylineConnection connection) {
		BendpointConnectionRouter router = new BendpointConnectionRouter();

		List<Bendpoint> bendPoints = new ArrayList<Bendpoint>();
		bendPoints.add(new AbsoluteBendpoint(5, 0));
		bendPoints.add(new AbsoluteBendpoint(5, 80));
		bendPoints.add(new AbsoluteBendpoint(90, 100));
		router.setConstraint(connection, bendPoints);
		connection.setConnectionRouter(router);
	}
	
	// Set Manhattan Router to a PolylineConnection
	private void setManhattanRouter(PolylineConnection connection) {
		  ManhattanConnectionRouter router = new ManhattanConnectionRouter();
		  connection.setConnectionRouter(router);
	}
	
	// Set MidpointLocator to a PolylineConnection
	private void setMidpointLocator(PolylineConnection connection) {
		MidpointLocator locator = new MidpointLocator(connection, 0);
		Label label = new Label("Label");
		label.setOpaque(true);
		label.setBackgroundColor(ColorConstants.white);
		label.setBorder(new LineBorder());
		connection.add(label, locator);
	}
	
	// Set EndpointLocator to a PolylineConnection
	private void setConnectionEndLocator(PolylineConnection connection) {
		ConnectionEndpointLocator locator = new ConnectionEndpointLocator(
				connection, true);
		Label label = new Label("target side");
		connection.add(label, locator);

		locator = new ConnectionEndpointLocator(connection, false);
		locator.setVDistance(-10);
		label = new Label("source side");
		connection.add(label, locator);
	}
	
}
