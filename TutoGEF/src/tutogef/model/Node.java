package tutogef.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.ui.views.properties.IPropertySource;

import tutogef.model.property.NodePropertySource;

public class Node implements IAdaptable {
	public static final String PROPERTY_LAYOUT = "NodeLayout";
	public static final String PROPERTY_ADD = "NodeAddChild";
	public static final String PROPERTY_REMOVE = "NodeRemoveChild";
	public static final String PROPERTY_RENAME = "NodeRename";

	private PropertyChangeSupport listeners;
	private String name;
	private Rectangle layout;
	private List<Node> children;
	private Node parent;
	
	private IPropertySource propertySource=null;
	
	public Node(){
		this.name = "Unknown";
		this.layout = new Rectangle();
		this.children = new ArrayList<Node>();
		this.parent = null;
		this.listeners=new PropertyChangeSupport(this);
	}
	
	public void setName(String name) {
		String oldName=this.name;
		this.name = name;
		getListeners().firePropertyChange(PROPERTY_RENAME,oldName,name);
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setLayout(Rectangle layout) {
		Rectangle oldLayout=this.layout;
		this.layout = layout;
		getListeners().firePropertyChange(PROPERTY_LAYOUT,oldLayout,layout);
	}
	
	public Rectangle getLayout() {
		return this.layout;
	}
	
	public boolean addChild(Node child) {
		boolean b=this.children.add(child);
		if(b){
			child.setParent(this);
			getListeners().firePropertyChange(PROPERTY_ADD,null,child);
		}
		return b;
	}
	
	public boolean removeChild(Node child) {
		boolean b = this.children.remove(child);
		if(b){
			getListeners().firePropertyChange(PROPERTY_REMOVE, child, null);
		}
		return b;
	}
	
	public List<Node> getChildrenArray() {
		return this.children;
	}
	
	public void setParent(Node parent) {
		this.parent = parent;
	}
	
	public Node getParent() {
		return this.parent;
	}

	public void addPropertyChangeListener(PropertyChangeListener listener){
		listeners.addPropertyChangeListener(listener);
	}
	
	public void removePropertyChangeListener(PropertyChangeListener listener){
		listeners.removePropertyChangeListener(listener);
	}
	
	public PropertyChangeSupport getListeners(){
		return listeners;
	}

	@Override
	public Object getAdapter(Class adapter) {
		if(adapter == IPropertySource.class){
			if(propertySource == null)
				propertySource = new NodePropertySource(this);
		}
		return propertySource;
	}

	public boolean contains(Node child){
		return children.contains(child);		
	}
}
