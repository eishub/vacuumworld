package eis;

import java.util.LinkedList;
import java.util.List;

import eis.exceptions.EntityException;
import eis.iilang.Action;

public abstract class AbstractEISEntityAction implements IEISAction {
	private AbstractEISInterface iface;
	private final String identifier;
	private final List<String> entities;
	private final List<String> types;

	@Override
	public String getIdentifier() {
		return this.identifier;
	}

	protected <T extends IEISEntity> T getEntity(final String name, final Class<T> c) {
		return this.iface.getEntity(name, c);
	}

	protected void addEntity(final String entity) {
		this.entities.add(entity);
	}

	protected void addType(final String type) {
		this.types.add(type);
	}

	public AbstractEISEntityAction(final String name) {
		this.identifier = name;
		this.entities = new LinkedList<>();
		this.types = new LinkedList<>();
	}

	@Override
	public boolean isEntityAction(final String name) {
		// Step 1: Check if the entities type is in the type list
		// and if so, return true
		try {
			if (this.types.contains(this.iface.getType(name))) {
				return true;
			}
		} catch (final EntityException e) {
			e.printStackTrace(); // TODO
			return false;
		}

		return this.entities.contains(name);
	}

	@Override
	public boolean isEntityTypeAction(final String name) {
		return this.types.contains(name);
	}

	@Override
	public boolean isEnvironmentAction() {
		return false;
	}

	@Override
	public boolean matches(final Action action) {
		return action.getName().equals(this.identifier);
	}

	@Override
	public void setInterface(final AbstractEISInterface iface) {
		this.iface = iface;
	}
}
