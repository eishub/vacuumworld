package grid;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import actions.Action;

/**
 * Top of the model hierarchy.
 */
public abstract class ModelObject {

	// Allows listeners to be safely added and removed while an event is being
	// fired
	protected List<ModelListener> listeners = new CopyOnWriteArrayList<ModelListener>();
	// Allows percepts to safely read the list of current actions while an
	// action is finishing
	protected volatile Action currentAction = null;

	public void addListener(ModelListener listener) {
		// FIXME listeners is a set. This allows duplicates to get into the
		// list.
		listeners.add(listener);
	}

	public void removeListener(ModelListener listener) {
		listeners.remove(listener);
	}

	/**
	 * Indicate that an event has occurred. NB we allow clients to fire events
	 * on model objects they have updated; hence this method has public (rather
	 * than protected) access.
	 * 
	 * @param eventName
	 *            Non-empty string, ideally with format
	 *            fully.qualified.ActionClassName.eventName
	 * @param source
	 *            ModelObject that is the logical source of this event.
	 */
	public void fireEvent(String eventName, ModelObject source) {
		if (eventName == null || eventName.isEmpty())
			throw new IllegalArgumentException("Event name cannot be empty.");
		if (source == null)
			source = this;
		for (ModelListener listener : listeners) {
			listener.eventFired(eventName, source);
		}
	}

	/**
	 * Indicate that an event has occurred.
	 * 
	 * @param eventName
	 */
	protected void fireEvent(String eventName) {
		this.fireEvent(eventName, this);
	}

	public void setAction(Action action) {
		currentAction = action;
	}

	public Action getAction() {
		return currentAction;
	}
}
