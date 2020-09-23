package vac;

import grid.GridPoint;
import grid.RelativeDirection;

/**
 * Wrapper class to encapsulate an attempted perception. The enclosed GridPoint
 * may or may not actually exist.
 */
public class PerceptPoint {
	private final GridPoint point;
	private final RelativeDirection relativeDirection;

	public PerceptPoint(final GridPoint point, final RelativeDirection relativeDirection) {
		this.point = point;
		this.relativeDirection = relativeDirection;
	}

	public GridPoint getGridPoint() {
		return this.point;
	}

	public RelativeDirection getRelativeDirection() {
		return this.relativeDirection;
	}
}
