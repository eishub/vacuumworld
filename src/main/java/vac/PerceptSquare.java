package vac;

import grid.RelativeDirection;
import grid.Square;

/**
 * Simple wrapper class to encapsulate a square which has been perceived by a
 * Movable object with direction.
 */
public class PerceptSquare {
	private final Square square;
	private final RelativeDirection relativeDirection;

	public PerceptSquare(final Square square, final RelativeDirection relativeDirection) {
		this.square = square;
		this.relativeDirection = relativeDirection;
	}

	public Square getSquare() {
		return this.square;
	}

	public RelativeDirection getRelativeDirection() {
		return this.relativeDirection;
	}
}
