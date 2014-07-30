package vac;

import grid.GridPoint;
import grid.RelativeDirection;

/**
 * Wrapper class to encapsulate an attempted perception.
 * The enclosed GridPoint may or may not actually exist.
 */
public class PerceptPoint {
	
	private GridPoint point;
	private RelativeDirection relativeDirection;
	
	public PerceptPoint(GridPoint point, RelativeDirection relativeDirection) {
		this.point = point;
		this.relativeDirection = relativeDirection;
	}
	
	public GridPoint getGridPoint() {
		return point;
	}
	
	public RelativeDirection getRelativeDirection() {
		return relativeDirection;
	}
}
