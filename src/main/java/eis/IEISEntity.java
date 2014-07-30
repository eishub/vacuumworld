package eis;


import java.util.LinkedList;

import eis.iilang.Percept;

public interface IEISEntity {
	public String getName();
	public String getType();
	public LinkedList<Percept> perceive();
}
