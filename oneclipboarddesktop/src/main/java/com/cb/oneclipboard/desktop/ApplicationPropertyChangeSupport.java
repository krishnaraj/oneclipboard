package com.cb.oneclipboard.desktop;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import com.cb.oneclipboard.desktop.ApplicationConstants.Property;

public class ApplicationPropertyChangeSupport {
	private PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);
	
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		changeSupport.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		changeSupport.removePropertyChangeListener(listener);
	}
	
	public void firePropertyChange(Property property, Object oldValue, Object newValue){
		changeSupport.firePropertyChange(property.name(), oldValue, newValue);
	}
}
