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
		try {
			return this.types.contains(this.iface.getType(name));
		} catch (final EntityException ignore) {
			return false;
		}
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
