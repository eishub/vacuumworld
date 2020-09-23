package vac;

import grid.Grid;
import grid.GridObject;
import grid.GridPoint;

public class Dust extends GridObject {
	private final String createEvent = getClass().getName() + "." + CREATE;
	private int speckCount = 10;

	public Dust(final Grid world, final GridPoint location) {
		super(world, location);
		fireEvent(this.createEvent, this);
	}

	@Override
	public String toString() {
		return "Dust";
	}

	public int getSpeckCount() {
		return this.speckCount;
	}

	public boolean needsCleaning() {
		return (this.speckCount > 0);
	}

	public void cleanOne() {
		if (needsCleaning()) {
			--this.speckCount;
		}
	}
}
