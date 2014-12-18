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
import eis.iilang.Percept;

public class AbstractEISInterface extends EIDefaultImpl {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/*
	 * This map contains references to any object that is registered as an
	 * entity with EIS.
	 */
	private Map<String, IEISEntity> entities;

	/*
	 * This map contains references to any action that is registered with EIS.
	 * The action identifier is used to match the action to one of the
	 * IEISActions in the associated list.
	 */
	private Map<String, List<IEISAction>> actions;

	public AbstractEISInterface() {
		entities = new HashMap<String, IEISEntity>();
		actions = new HashMap<String, List<IEISAction>>();
	}

	@SuppressWarnings("unchecked")
	public <T extends IEISEntity> T getEntity(String name, Class<T> theClass) {
		return (T) entities.get(name);
	}

	protected void addEntity(IEISEntity entity) throws EntityException {
		entities.put(entity.getName(), entity);
		addEntity(entity.getName(), entity.getType());
	}

	protected void addAction(IEISAction action) {
		List<IEISAction> list = actions.get(action.getIdentifier());
		if (list == null) {
			list = new LinkedList<IEISAction>();
			actions.put(action.getIdentifier(), list);
		}

		action.setInterface(this);
		list.add(action);
	}

	/**
	 * Return the perceptions of the associated entity.
	 */
	@Override
	protected LinkedList<Percept> getAllPerceptsFromEntity(String arg0)
			throws PerceiveException, NoEnvironmentException {
		IEISEntity entity = entities.get(arg0);
		if (entity == null) {
			throw new PerceiveException("No Such Entity: " + arg0);
		}
		return entity.perceive();
	}

	private IEISAction getAction(Action action) {
		List<IEISAction> list = actions.get(action.getName());
		if (list == null)
			return null;

		for (IEISAction act : list) {
			if (act.matches(action)) {
				return act;
			}
		}
		return null;
	}

	@Override
	protected boolean isSupportedByEntity(Action arg0, String arg1) {
		IEISAction action = getAction(arg0);
		if (action == null)
			return false;
		return action.isEntityAction(arg1);
	}

	@Override
	protected boolean isSupportedByEnvironment(Action arg0) {
		IEISAction action = getAction(arg0);
		return (action != null);
	}

	@Override
	protected boolean isSupportedByType(Action arg0, String arg1) {
		IEISAction action = getAction(arg0);
		if (action == null)
			return false;
		return action.isEntityTypeAction(arg1);
	}

	@Override
	protected Percept performEntityAction(String arg0, Action arg1)
			throws ActException {
		IEISAction action = getAction(arg1);
		if (action == null)
			throw new ActException(ActException.NOTSUPPORTEDBYENTITY);

		try {
			return action.act(arg0, arg1);
		} catch (ActException ae) { // TODO: check in this small change
			throw ae;
		} catch (Throwable th) {
			th.printStackTrace();
			throw new ActException(ActException.NOTSPECIFIC);
		}
	}
}
