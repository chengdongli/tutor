package tutogef;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

public class MyEditorInput implements IEditorInput {

	public String name=null;

	public MyEditorInput(String name) {
		super();
		this.name = name;
	}

	
	@Override
	public boolean exists() {
		return this.name!=null;
	}

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof MyEditorInput))
			return false;
		return ((MyEditorInput)obj).getName().equals(getName());
	}


	@Override
	public ImageDescriptor getImageDescriptor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public IPersistableElement getPersistable() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getToolTipText() {
		// TODO Auto-generated method stub
		return this.name;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object getAdapter(Class adapter) {
		// TODO Auto-generated method stub
		return null;
	}

}
