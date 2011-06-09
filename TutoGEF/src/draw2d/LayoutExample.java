package draw2d;

import java.util.Random;

import org.eclipse.draw2d.Button;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

// Create an XYLayout, and randomly layout 10 buttons.
public class LayoutExample extends ApplicationWindow {
	public LayoutExample() {
		super(null);
	}

	public static void main(String[] args) {
		// Top shell window
		LayoutExample window = new LayoutExample();
		window.setBlockOnOpen(true);
		window.open();
		Display.getCurrent().dispose();
	}

	// Client area
	protected Control createContents(Composite parent) {
		Canvas canvas = new Canvas(parent, SWT.NONE);
		LightweightSystem lws = new LightweightSystem(canvas);

		IFigure panel = new Figure();
		panel.setLayoutManager(new XYLayout());
		Button button;
		Random r = new Random();
		for (int i = 0; i < 10; i++) {
			button = new Button("button" + i);
			Rectangle constraint = new Rectangle(r.nextInt(200),
					r.nextInt(200), -1, -1);

			panel.add(button);
			panel.setConstraint(button, constraint);
		}

		lws.setContents(panel);
		return canvas;
	}

	protected void configureShell(Shell shell) {

		super.configureShell(shell);
		shell.setBounds(100,100,400,400);
	}
}
