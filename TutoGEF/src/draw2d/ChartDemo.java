package draw2d;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

// Create a rectangle chart with shading
public class ChartDemo {

	static class ChartFigure extends Figure {

		int values[] = { 20, 65, 15, 30 };

		protected void paintFigure(Graphics g) {

			Rectangle clientArea = getClientArea();

			for (int i = 0; i < values.length; i++) {

				Rectangle bar = clientArea.getCopy();

				bar.width /= values.length;

				bar.x += i * bar.width;

				bar.shrink(15, 15);

				int chop = bar.height * (100 - values[i]) / 100;

				bar.crop(new Insets(chop, 0, 0, 0));

				g.setBackgroundColor(ColorConstants.black);

				g.fillRectangle(bar.getTranslated(4, 4));

				g.setBackgroundColor(ColorConstants.red);

				g.fillRectangle(bar);

			}

		}

	}

	public static void main(String args[]) {

		Display d = new Display();

		final Shell shell = new Shell(d);

		shell.setBackground(ColorConstants.white);

		shell.setSize(400, 290);

		LightweightSystem lws = new LightweightSystem(shell);

		lws.setContents(new ChartFigure());

		shell.open();

		while (!shell.isDisposed())

			while (!d.readAndDispatch())

				d.sleep();

	}

}