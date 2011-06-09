package tutogef.part.tree;

import java.beans.PropertyChangeEvent;
import java.util.List;

import tutogef.model.Entreprise;
import tutogef.model.Node;
import tutogef.model.Service;

// root part
public class EntrepriseTreeEditPart extends AppAbstractTreeEditPart {

	protected List<Node> getModelChildren(){
		return ((Entreprise)getModel()).getChildrenArray();
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		super.propertyChange(evt);
	    if (evt.getPropertyName().equals(Entreprise.PROPERTY_CAPITAL)) refreshChildren();
	}

}
