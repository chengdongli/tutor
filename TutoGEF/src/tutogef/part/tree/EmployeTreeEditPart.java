package tutogef.part.tree;

import java.beans.PropertyChangeEvent;
import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import tutogef.editpolicies.AppDeletePolicy;
import tutogef.model.Employe;
import tutogef.model.Node;
import tutogef.model.Service;

public class EmployeTreeEditPart extends AppAbstractTreeEditPart implements
		EditPart {
	@Override
	protected List<Node> getModelChildren() {
		return ((Employe)getModel()).getChildrenArray();
	}

	@Override
	public void refreshVisuals(){
		Employe model = (Employe)getModel();
		setWidgetText(model.getName()+" "+model.getPrenom());
		setWidgetImage(PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_DEF_VIEW));
	}

	@Override
	protected void createEditPolicies() {	
		installEditPolicy(EditPolicy.COMPONENT_ROLE,new AppDeletePolicy());
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		super.propertyChange(evt);
	    if (evt.getPropertyName().equals(Employe.PROPERTY_FIRSTNAME)) refreshChildren();
	}

}
