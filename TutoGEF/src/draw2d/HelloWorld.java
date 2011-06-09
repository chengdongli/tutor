package draw2d;

import org.eclipse.draw2d.Button;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FlowLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

// A figure with a button
public class HelloWorld {

  public static void main(String[] args) {
    // Shell using default display
    Shell shell = new Shell(); 
    shell.setSize(400,400);

    // Create LightweightSystem
    LightweightSystem lws = new LightweightSystem(shell);
    
    // Root Figure
    IFigure panel = new Figure();
    panel.setLayoutManager(new FlowLayout());
    // add child to root figure
    panel.add(new Button("Hello World"));
    // set root figure
    lws.setContents(panel);

    // Following are same as normal SWT applicaion
    shell.open();

    Display display = Display.getDefault();
    while (!shell.isDisposed ()) {
      if (!display.readAndDispatch ())
        display.sleep ();
    }
  }
}