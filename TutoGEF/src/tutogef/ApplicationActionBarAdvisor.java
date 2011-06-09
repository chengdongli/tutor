package tutogef;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;

public class ApplicationActionBarAdvisor extends ActionBarAdvisor {

    public ApplicationActionBarAdvisor(IActionBarConfigurer configurer) {
        super(configurer);
    }

    protected IWorkbenchAction makeAction(IWorkbenchWindow window, ActionFactory af) {
    	IWorkbenchAction action=af.create(window);
    	register(action);
    	return action;
    }
    
    // Workbench window and editor actions are different set of actions.
    // Following setup the default workbench window actions globally, but not show them,
    // just hook the actions keys
    protected void makeActions(IWorkbenchWindow window) {
    	makeAction(window, ActionFactory.UNDO);
    	makeAction(window, ActionFactory.REDO);
    	makeAction(window, ActionFactory.DELETE);
    	makeAction(window, ActionFactory.RENAME);
    	makeAction(window, ActionFactory.COPY);
    	makeAction(window, ActionFactory.PASTE);
    }

    protected void fillMenuBar(IMenuManager menuBar) {
    }
    
}
