package draw2d;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FocusBorder;
import org.eclipse.draw2d.FocusEvent;
import org.eclipse.draw2d.FocusListener;
import org.eclipse.draw2d.GroupBoxBorder;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.KeyEvent;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.draw2d.MouseMotionListener;
import org.eclipse.draw2d.KeyListener;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class EventExample2 {
	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setBounds(100, 100, 400, 400);

		LightweightSystem lws = new LightweightSystem(shell);

		Figure panel = new Figure();
		panel.setLayoutManager(new XYLayout());

		Label l1 = new Label("Label 1");
		// r1.setLocation(new Point(0, 0));
		l1.setBounds(new Rectangle(0, 0, 200, 200));
		l1.setBorder(new GroupBoxBorder("Tab with SHIFT\n to change focus"));
		panel.add(l1);

		Label l2 = new Label("Label 2");
		// r2.setLocation(new Point(100, 100));
		l2.setBounds(new Rectangle(200, 200, 200, 200));
		l2.setBorder(new GroupBoxBorder("Tab with SHIFT\n to change focus"));
		panel.add(l2);

		new DragTest(l1);
		new DragTest(l2);
		new FocusEventTest(l1);
		new FocusEventTest(l2);
		new KeyEventTest(l1);
		new KeyEventTest(l2);
		
		lws.setContents(panel);

		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}

		display.dispose();
	}

	static class DragTest extends MouseMotionListener.Stub implements
			MouseListener {
		// previous location
		Point last;

		public DragTest(IFigure figure) {
			figure.addMouseMotionListener(this);
			figure.addMouseListener(this);
		}

		public void mouseReleased(MouseEvent e) {
			last = null;
		}

		public void mouseDoubleClicked(MouseEvent e) {
		}

		public void mousePressed(MouseEvent e) {
			last = e.getLocation();
			// consume event
			e.consume();
		}

		public void mouseDragged(MouseEvent e) {
			if (last == null)
				return;

			Point p = e.getLocation();
			Dimension delta = p.getDifference(last);
			last = p;
			Figure f = ((Figure) e.getSource());
			f.setBounds(f.getBounds().getTranslated(delta.width, delta.height));
		}
	};

	static class FocusEventTest extends FocusListener.Stub {
		public FocusEventTest(IFigure figure) {
			figure.addFocusListener(this);
			// allow figure to gain focus on traverse event
			figure.setFocusTraversable(true);
		}

		public void focusGained(FocusEvent fe) {
			// Add focus border when focus gained
			fe.gainer.setBorder(new FocusBorder());
			if (fe.loser != null)
				fe.loser.setBorder(new GroupBoxBorder(
						"Tab with SHIFT\n to change focus")); // cancel focus
																// border
		}
	};

	static class KeyEventTest extends KeyListener.Stub {
		public KeyEventTest(IFigure figure) {
			figure.addKeyListener(this);
		}

		public void keyPressed(KeyEvent ke) {
			String string = "KeyEvent source Widget: [";
			string += ((Label) ke.getSource()).getText();
			string += "]\tPressed: ";

			string += "character= [" + ke.character+"]";

			System.out.println(string);
		}
	}
}
