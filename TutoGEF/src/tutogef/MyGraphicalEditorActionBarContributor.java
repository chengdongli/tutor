package tutogef;

import org.eclipse.gef.ui.actions.ActionBarContributor;
import org.eclipse.gef.ui.actions.DeleteRetargetAction;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.gef.ui.actions.RedoRetargetAction;
import org.eclipse.gef.ui.actions.UndoRetargetAction;
import org.eclipse.gef.ui.actions.ZoomComboContributionItem;
import org.eclipse.gef.ui.actions.ZoomInRetargetAction;
import org.eclipse.gef.ui.actions.ZoomOutRetargetAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.RetargetAction;

public class MyGraphicalEditorActionBarContributor extends
		ActionBarContributor {

	public MyGraphicalEditorActionBarContributor() {
		// TODO Auto-generated constructor stub
	}

	// Add action to Workbench and activate the global action key
	@Override
	protected void buildActions() {
	    
	    addRetargetAction(new UndoRetargetAction());
	    addRetargetAction(new RedoRetargetAction());
	    addRetargetAction(new DeleteRetargetAction());

	    addRetargetAction(new ZoomInRetargetAction());
	    addRetargetAction(new ZoomOutRetargetAction());

	    IWorkbenchWindow iww=getPage().getWorkbenchWindow();
	    addRetargetAction((RetargetAction)ActionFactory.COPY.create(iww));
	    addRetargetAction((RetargetAction)ActionFactory.PASTE.create(iww));
	}

	@Override
	protected void declareGlobalActionKeys() {
		// TODO Auto-generated method stub
		
	}
	
	// Add to toolbar for editor
	@Override
	public void contributeToToolBar(IToolBarManager toolBarManager) {
		toolBarManager.add(getAction(ActionFactory.UNDO.getId()));
		toolBarManager.add(getAction(ActionFactory.REDO.getId()));
		toolBarManager.add(new Separator());
		toolBarManager.add(getAction(ActionFactory.COPY.getId()));
		toolBarManager.add(getAction(ActionFactory.PASTE.getId()));
		toolBarManager.add(getAction(ActionFactory.DELETE.getId())); 
//		toolBarManager.add(getAction(ActionFactory.RENAME.getId())); 
	    toolBarManager.add(new Separator());
	    toolBarManager.add(getAction(GEFActionConstants.ZOOM_IN));
	    toolBarManager.add(getAction(GEFActionConstants.ZOOM_OUT));
	    toolBarManager.add(new ZoomComboContributionItem(getPage()));
	}

	@Override
	public void contributeToMenu(IMenuManager menuManager) {
	}

}
