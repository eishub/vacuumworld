package eis;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import eis.exceptions.ActException;
import eis.exceptions.EntityException;
import eis.exceptions.NoEnvironmentException;
import eis.exceptions.PerceiveException;
import eis.iilang.Action;

public class AbstractEISInterface extends EIDefaultImpl {
	private static final long serialVersionUID = 1L;

	/*
	 * This map contains references to any object that is registered as an entity
	 * with EIS.
	 */
	private final Map<String, IEISEntity> entities;

	/*
	 * This map contains references to any action that is registered with EIS. The
	 * action identifier is used to match the action to one of the IEISActions in
	 * the associated list.
	 */
	private final Map<String, List<IEISAction>> actions;

	public AbstractEISInterface() {
		this.entities = new HashMap<>();
		this.actions = new HashMap<>();
	}

	@SuppressWarnings("unchecked")
	public <T extends IEISEntity> T getEntity(final String name, final Class<T> theClass) {
		return (T) this.entities.get(name);
	}

	protected void addEntity(final IEISEntity entity) throws EntityException {
		this.entities.put(entity.getName(), entity);
		addEntity(entity.getName(), entity.getType());
	}

	protected void addAction(final IEISAction action) {
		List<IEISAction> list = this.actions.get(action.getIdentifier());
		if (list == null) {
			list = new LinkedList<>();
			this.actions.put(action.getIdentifier(), list);
		}

		action.setInterface(this);
		list.add(action);
	}

	/**
	 * Return the perceptions of the associated entity.
	 */
	@Override
	protected PerceptUpdate getPerceptsForEntity(final String arg0) throws PerceiveException, NoEnvironmentException {
		final IEISEntity entity = this.entities.get(arg0);
		if (entity == null) {
			throw new PerceiveException("No Such Entity: " + arg0);
		}
		return entity.perceive();
	}

	private IEISAction getAction(final Action action) {
		final List<IEISAction> list = this.actions.get(action.getName());
		if (list == null) {
			return null;
		}

		for (final IEISAction act : list) {
			if (act.matches(action)) {
				return act;
			}
		}
		return null;
	}

	@Override
	protected boolean isSupportedByEntity(final Action arg0, final String arg1) {
		final IEISAction action = getAction(arg0);
		if (action == null) {
			return false;
		}
		return action.isEntityAction(arg1);
	}

	@Override
	protected boolean isSupportedByEnvironment(final Action arg0) {
		final IEISAction action = getAction(arg0);
		return (action != null);
	}

	@Override
	protected boolean isSupportedByType(final Action arg0, final String arg1) {
		final IEISAction action = getAction(arg0);
		if (action == null) {
			return false;
		}
		return action.isEntityTypeAction(arg1);
	}

	@Override
	protected void performEntityAction(final Action arg1, final String arg0) throws ActException {
		final IEISAction action = getAction(arg1);
		if (action == null) {
			throw new ActException(ActException.NOTSUPPORTEDBYENTITY);
		}

		try {
			action.act(arg0, arg1);
		} catch (final ActException ae) { // TODO: check in this small change
			throw ae;
		} catch (final Throwable th) {
			th.printStackTrace(); // TODO
			throw new ActException(ActException.NOTSPECIFIC);
		}
	}
}
