package eis;


import java.util.LinkedList;
import java.util.List;

import eis.exceptions.EntityException;
import eis.iilang.Action;

public abstract class AbstractEISEntityAction implements IEISAction {
	private AbstractEISInterface iface;
	private String identifier;
	private List<String> entities;
	private List<String> types;
	
	public String getIdentifier() {
		return identifier;
	}
	
	protected <T extends IEISEntity> T getEntity(String name, Class<T> c) {
		return iface.getEntity(name, c);
	}
	protected void addEntity(String entity) {
		entities.add(entity);
	}
	
	protected void addType(String type) {
		types.add(type);
	}
	
	public AbstractEISEntityAction(String name) {
		this.identifier = name;
		entities = new LinkedList<String>();
		types = new LinkedList<String>();
	}
	
	@Override
	public boolean isEntityAction(String name) {
		// Step 1: Check if the entities type is in the type list
		// and if so, return true
		try {
			if (types.contains(iface.getType(name))) return true;
		} catch (EntityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		return entities.contains(name);
	}

	@Override
	public boolean isEntityTypeAction(String name) {
		// TODO Auto-generated method stub
		return types.contains(name);
	}

	@Override
	public boolean isEnvironmentAction() {
		return false;
	}

	@Override
	public boolean matches(Action action) {
		return action.getName().equals(identifier);
	}

	@Override
	public void setInterface(AbstractEISInterface iface) {
		this.iface = iface;
	}

}
