package tutogef.part;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import tutogef.model.Node;

public abstract class AppAbstractEditPart extends AbstractGraphicalEditPart 
	implements PropertyChangeListener{
	@Override
	public void activate(){
		super.activate();
		((Node)getModel()).addPropertyChangeListener(this);
	}

	@Override
	public void deactivate(){
		((Node)getModel()).removePropertyChangeListener(this);
		super.deactivate();
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if(evt.getPropertyName().equals(Node.PROPERTY_LAYOUT))
			refreshVisuals();
	    if (evt.getPropertyName().equals(Node.PROPERTY_ADD)) refreshChildren();
	    if (evt.getPropertyName().equals(Node.PROPERTY_REMOVE)) refreshChildren();
	}
	
	/**
     * Double click event will generate this REQ_OPEN request.
     */
	@Override
	public void performRequest(Request req) {
		if (req.getType().equals(RequestConstants.REQ_OPEN)) {
			try {
				IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
				page.showView(IPageLayout.ID_PROP_SHEET);
			}
			catch (PartInitException e) {
				e.printStackTrace();
			}
		}
	}
}
