package eis;


import eis.exceptions.ActException;
import eis.iilang.Action;
import eis.iilang.Percept;

public interface IEISAction {
	public String getIdentifier();
	public void setInterface(AbstractEISInterface iface);
	public Percept act(String arg0, Action arg1) throws ActException;
	public boolean matches(Action action);
	public boolean isEntityAction(String name);
	public boolean isEntityTypeAction(String name);
	public boolean isEnvironmentAction();
}
