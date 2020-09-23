package eis;

import eis.exceptions.ActException;
import eis.iilang.Action;

public interface IEISAction {
	String getIdentifier();

	void setInterface(AbstractEISInterface iface);

	void act(String arg0, Action arg1) throws ActException;

	boolean matches(Action action);

	boolean isEntityAction(String name);

	boolean isEntityTypeAction(String name);

	boolean isEnvironmentAction();
}
