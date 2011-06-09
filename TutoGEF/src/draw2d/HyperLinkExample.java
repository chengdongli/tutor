package draw2d;

import org.eclipse.core.runtime.Assert;
import org.eclipse.draw2d.ActionEvent;
import org.eclipse.draw2d.ActionListener;
import org.eclipse.draw2d.Clickable;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Cursors;
import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.FlowLayout;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.Layer;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseMotionListener;
import org.eclipse.draw2d.Panel;
import org.eclipse.draw2d.StackLayout;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

// Use Transparent layer to create a label hyper-linked to a web page.
public class HyperLinkExample extends ApplicationWindow {

	public HyperLinkExample() {
		super(null);
	}

	public static void main(String[] args) {
		HyperLinkExample window = new HyperLinkExample();
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

	@Override
	protected Control createContents(Composite parent) {
		Composite composite = new Composite(parent, SWT.BORDER);
		composite.setLayout(new FillLayout());
		FigureCanvas canvas = new FigureCanvas(composite);

		canvas.setContents(getFigure());
		return composite;
	}

	private IFigure getFigure() {
		Layer layer = new Layer(); // layer is transparent, this way, hyperlink
									// label works
		layer.setBorder(new MarginBorder(5));
		layer.setLayoutManager(new FlowLayout(false));

		final String url = "http://www.eclipse.org/";
		HyperLinkLabel link = new HyperLinkLabel("Goto Eclipse.org");
		// action when clicking label
		link.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				// open web
				if (SWT.getPlatform().equals("gtk")) {
					Program.launch("firefox " + url);
				} else {
					Program.launch(url);
				}
			}
		});

		// add detail to status line
		link.addMouseMotionListener(new MouseMotionListener.Stub() {

			public void mouseEntered(MouseEvent me) {
				setStatus("Goto " + url);
			}

			public void mouseExited(MouseEvent me) {
				setStatus(null);
			}
		});

		// Tooltip background figure
		Panel tooltip = new Panel() {
			protected void paintFigure(Graphics graphics) {
				graphics.fillGradient(getBounds(), false);
			}
		};
		tooltip.setLayoutManager(new StackLayout());
		tooltip.setBackgroundColor(ColorConstants.lightGreen);
		tooltip.setForegroundColor(ColorConstants.cyan);

		// tooltip label
		Label label = new Label("Open Web Page");
		label.setForegroundColor(ColorConstants.darkBlue);
		tooltip.add(label);

		// set tooltip
		link.setToolTip(tooltip);

		HyperLinkLabel link2 = new HyperLinkLabel("Show Dialog");
		link2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				MessageDialog.openInformation(getShell(), "Title",
						"This dialog is invoked from Draw2d Figure.");
			}
		});

		layer.add(link);
		layer.add(link2);

		return layer;
	}

	public class HyperLinkLabel extends Clickable {
		// Clickable label
		class InnerLabel extends Label {
			public InnerLabel(String text) {
				super(text);
			}

			protected void paintFigure(Graphics graphics) {
				if (getModel().isMouseOver() && getText().length() != 0) {
					setForegroundColor(hoverColor);
					super.paintFigure(graphics);
					graphics.drawLine(getTextBounds().getBottomLeft(),
							getTextBounds().getBottomRight());
				} else {
					setForegroundColor(normalColor);
					super.paintFigure(graphics);
				}
			}
		}

		private InnerLabel innerLabel;

		private Color hoverColor, normalColor;

		public HyperLinkLabel(String label) {
			Assert.isNotNull(label);
			innerLabel = new InnerLabel(label);
			// when focused, margin of the rectangle
			innerLabel.setBorder(new MarginBorder(2));
			setCursor(Cursors.HAND);
			setContents(innerLabel);

			setRolloverEnabled(true);

			normalColor = ColorConstants.blue;
			hoverColor = ColorConstants.red;
		}

		public Color getNormalColor() {
			return normalColor;
		}

		public void setNormalColor(Color color) {
			normalColor = color;
			repaint();
		}

		public Color getHoverColor() {
			return hoverColor;
		}

		public void setHoverColor(Color color) {
			hoverColor = color;
		}

	}
}
