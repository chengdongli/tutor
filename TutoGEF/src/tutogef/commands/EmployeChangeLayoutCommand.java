package tutogef.commands;

import org.eclipse.draw2d.geometry.Rectangle;

import tutogef.model.Employe;

public class EmployeChangeLayoutCommand extends AbstractLayoutCommand {
	  private Employe model;
	  private Rectangle layout;
	  private Rectangle oldLayout;

	  public void execute() {
		  this.model.setLayout(layout);
	  }

	  public void setConstraint(Rectangle rect) {
		  this.layout = rect;
	  }

	  public void setModel(Object model) {
		  this.model = (Employe)model;
		  this.oldLayout = ((Employe)model).getLayout();
	  }
	  
	  public void undo() {
		  this.model.setLayout(this.oldLayout);
	  }

}
