package draw2d;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FlowLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

// Mouse event: up/down
public class EventExample1 {
	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setBounds(100,100,400,400);

		LightweightSystem lws = new LightweightSystem(shell);

		IFigure panel = new Figure();

		panel.setLayoutManager(new FlowLayout());
		for (int i = 0; i < 5; i++) {
			IFigure label = new Label("Label" + i);
			new MouseEventTest(label);
			panel.add(label);
		}

		lws.setContents(panel);

		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}

		display.dispose();
	}

	static class MouseEventTest extends MouseListener.Stub {
		private IFigure figure;
		private Color color;

		public MouseEventTest(IFigure f) {
			figure = f;
			f.addMouseListener(this);
			// background opaque instead of transparency
			f.setOpaque(true);
			// save default background 
			color = f.getBackgroundColor();
		}

		public void mousePressed(MouseEvent me) {
			// change background color to yellow
			figure.setBackgroundColor(ColorConstants.yellow);
		}

		public void mouseReleased(MouseEvent me) {
			// restore background color
			figure.setBackgroundColor(color);
		}
	}
}
