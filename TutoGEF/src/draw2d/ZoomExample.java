package draw2d;

import java.text.DecimalFormat;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.CompoundBorder;
import org.eclipse.draw2d.Ellipse;
import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LayeredPane;
import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.RoundedRectangle;
import org.eclipse.draw2d.ScalableLayeredPane;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.parts.ScrollableThumbnail;
import org.eclipse.jface.action.ControlContribution;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

// A scalable canvas plus a thumnail canvas.
public class ZoomExample extends ApplicationWindow {

	// Zoom level
	private double zoomLevel[] = { 0.25, 0.5, 1.0, 1.5, 2.0, 3.0, 4.0 };
	private ScalableLayeredPane scalableLayer;
	private ScrollableThumbnail thumbnail;

	private FigureCanvas viewCanvas; // view canvas
	private LayeredPane mainPane; // layer to zoom in/out

	public ZoomExample() {
		super(null);
	}

	public static void main(String[] args) {
		ZoomExample window = new ZoomExample();
		window.addToolBar(SWT.FLAT);
		// add status line
		window.addStatusLine();
		window.setBlockOnOpen(true);
		window.open();

		Display.getCurrent().dispose();
	}

	@Override
	protected void configureShell(Shell shell) {
		shell.setSize(400, 400);
		super.configureShell(shell);
	}

	protected Control createContents(Composite parent) {
		Composite composite = new Composite(parent, SWT.BORDER);
		composite.setLayout(new FillLayout());

		SashForm sashForm = new SashForm(composite, SWT.HORIZONTAL);
		createView(sashForm);
		createThumbNail(sashForm);
		return composite;
	}

	private void createThumbNail(SashForm sashForm) {
		Canvas thumbCanvas = new Canvas(sashForm, SWT.BORDER);
		LightweightSystem lws = new LightweightSystem(thumbCanvas);
		thumbnail = new ScrollableThumbnail();

		thumbnail.setViewport(viewCanvas.getViewport()); 

		thumbnail.setSource(mainPane);

		lws.setContents(thumbnail);
	}

	private void createView(SashForm sashForm) {

		viewCanvas = new FigureCanvas(sashForm);

		// Create ScalableLayeredPane for zooming
		scalableLayer = new ScalableLayeredPane();

		// create a layer for zooming
		mainPane = new LayeredPane();
		scalableLayer.add(mainPane);
		mainPane.setLayoutManager(new XYLayout());

		Label label = new Label("zoom");
		label.setBackgroundColor(ColorConstants.tooltipBackground);
		label.setOpaque(true);
		label.setBorder(new CompoundBorder(new LineBorder(),
				new MarginBorder(3)));

		mainPane.add(label);
		mainPane.setConstraint(label, new Rectangle(5, 5, -1, -1));

		Ellipse ellipse = new Ellipse();
		ellipse.setBackgroundColor(ColorConstants.orange);
		mainPane.add(ellipse);
		mainPane.setConstraint(ellipse, new Rectangle(40, 40, 50, 30));

		RoundedRectangle roundedRect = new RoundedRectangle();
		roundedRect.setBackgroundColor(ColorConstants.cyan);
		mainPane.add(roundedRect);
		mainPane.setConstraint(roundedRect, new Rectangle(30, 100, 50, 50));

		viewCanvas.setContents(scalableLayer);

	}

	protected ToolBarManager createToolBarManager(int style) {
		ToolBarManager manager = new ToolBarManager(style);
		manager.add(new ZoomCombo());
		return manager;
	}

	protected void zoomChange(int level) {
		scalableLayer.setScale(zoomLevel[level]);
	}

	public boolean close() {
		// dispose image
		thumbnail.deactivate();
		return super.close();
	}

	class ZoomCombo extends ControlContribution {
		ZoomCombo() {
			super("zoomCombo");
		}

		// create combo box
		protected Control createControl(Composite parent) {
			Combo combo = new Combo(parent, SWT.READ_ONLY);

			for (int i = 0; i < zoomLevel.length; i++) {
				DecimalFormat decimal = new DecimalFormat("###%");
				combo.add(decimal.format(zoomLevel[i]));
			}

			combo.addSelectionListener(new SelectionListener() {

				public void widgetSelected(SelectionEvent e) {
					zoomChange(((Combo) e.getSource()).getSelectionIndex());
				}

				public void widgetDefaultSelected(SelectionEvent e) {
				}
			});

			combo.select(2); // default 100%
			return combo;
		}
	}

}
