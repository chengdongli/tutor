package tutogef;

import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.ui.actions.ActionFactory;

// Context menu for editor - used by both editor and outline viewer
public class AppContextMenuProvider extends ContextMenuProvider {

	private ActionRegistry actionRegistry;
	public ActionRegistry getActionRegistry() {
		return actionRegistry;
	}


	public void setActionRegistry(ActionRegistry actionRegistry) {
		this.actionRegistry = actionRegistry;
	}


	public AppContextMenuProvider(EditPartViewer viewer, ActionRegistry registry) {
		super(viewer);
		setActionRegistry(registry);
	}

	
	@Override
	public void buildContextMenu(IMenuManager menu) {
		IAction action; 
		GEFActionConstants.addStandardActionGroups(menu); 
		action = getActionRegistry().getAction(ActionFactory.UNDO.getId()); 
		menu.appendToGroup(GEFActionConstants.GROUP_UNDO, action); 
		action = getActionRegistry().getAction(ActionFactory.REDO.getId()); 
		menu.appendToGroup(GEFActionConstants.GROUP_UNDO, action); 
		action = getActionRegistry().getAction(ActionFactory.DELETE.getId()); 
		menu.appendToGroup(GEFActionConstants.GROUP_EDIT, action);
		action = getActionRegistry().getAction(ActionFactory.RENAME.getId()); 
		menu.appendToGroup(GEFActionConstants.GROUP_EDIT, action);
		action = getActionRegistry().getAction(ActionFactory.COPY.getId()); 
		menu.appendToGroup(GEFActionConstants.GROUP_EDIT, action);
		action = getActionRegistry().getAction(ActionFactory.PASTE.getId()); 
		menu.appendToGroup(GEFActionConstants.GROUP_EDIT, action);

	}
	
	

}
