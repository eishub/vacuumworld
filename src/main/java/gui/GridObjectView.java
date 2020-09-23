package gui;

import java.awt.Graphics;
import java.awt.Point;

import grid.GridObject;

/**
 * Abstract superclass for all grid-visible objects.
 */
public abstract class GridObjectView {
	protected final GridView parent;
	private final GridObject gridObject;
	private static int squareSize = LookAndFeel.getSquareSize();
	private static int gridStepSize = squareSize + LookAndFeel.getGridLineWidth();

	public GridObjectView(final GridView parent, final GridObject gridObject) {
		this.parent = parent;
		this.gridObject = gridObject;
	}

	public GridView getParent() {
		return this.parent;
	}

	public Point getUpperLeft() {
		return new Point((int) Math.round(this.gridObject.getX() * gridStepSize),
				(int) Math.round(this.gridObject.getY() * gridStepSize));
	}

	public Point getLowerRight() {
		return new Point((int) Math.round(this.gridObject.getX() * gridStepSize) + squareSize,
				(int) Math.round(this.gridObject.getY() * gridStepSize) + squareSize);
	}

	public abstract void paint(Graphics g);
}
