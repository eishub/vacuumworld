package vac;

import grid.Grid;
import grid.GridObject;
import grid.GridPoint;

public class Dust extends GridObject {

	private final String createEvent = getClass().getName() + "." + CREATE;
	private int speckCount = 10;
	
	public Dust(Grid world, GridPoint location) {
		super(world, location);
		fireEvent(createEvent, this);
	}
	
	public String toString() {
		return "Dust";
	}
	
	public int getSpeckCount() {
		return speckCount;
	}
	
	public boolean needsCleaning() {
		return (speckCount > 0);
	}
	
	public void cleanOne() {
		if (this.needsCleaning()) --speckCount;
	}
}
