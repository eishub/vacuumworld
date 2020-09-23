package actions;

import grid.Grid;
import grid.GridObject;
import grid.GridPoint;
import grid.MovingObject;
import grid.RelativeDirection;
import grid.Square;

/**
 * Class to represent a one-square move in the current (i.e. forwards)
 * direction. Chain them together to go long distances :)
 */
public class Move extends Action {
	private static final int stepsPerSquare = 32;
	private final MovingObject movingObject;
	private final Grid grid;
	private GridPoint startPoint = null;
	private GridPoint endPoint = null;

	public Move(final MovingObject movingObject) {
		super(movingObject);
		this.movingObject = movingObject;
		this.grid = movingObject.getGrid();
	}

	@Override
	protected long duration() {
		return this.movingObject.getTimeToMove();
	}

	@Override
	protected void executeOneStep() {
		this.movingObject
				.setX(this.movingObject.getX() + (this.movingObject.getDirection().getXComponent() / stepsPerSquare));
		this.movingObject
				.setY(this.movingObject.getY() + (this.movingObject.getDirection().getYComponent() / stepsPerSquare));
	}

	/**
	 * Cleanup to stop floating point rounding errors from accumulating, and remove
	 * the MovingObject from the start square.
	 */
	@Override
	protected void finalise() {
		this.movingObject.setX(this.endPoint.x);
		this.movingObject.setY(this.endPoint.y);
		this.grid.getSquareAt(this.startPoint).remove(this.movingObject);
	}

	/**
	 * Check the destination square exists, and try to get exclusive occupancy.
	 *
	 * @throws ImpossibleActionException
	 * @throws UnavailableActionException
	 */
	@Override
	protected void initialise() throws ImpossibleActionException, UnavailableActionException {
		this.startPoint = this.movingObject.getLocation();
		this.endPoint = this.movingObject.getNearestPoint(RelativeDirection.forward);
		if (!this.grid.exists(this.endPoint)) {
			throw new ImpossibleActionException(
					"Grid square (" + this.endPoint.x + "," + this.endPoint.y + ") does not exist!");
		}
		final Square endSquare = this.grid.getSquareAt(this.endPoint);
		synchronized (endSquare) {
			final GridObject obstruction = this.movingObject.getObstruction(endSquare);
			if (obstruction == null) {
				endSquare.add(this.movingObject);
			} else {
				if (obstruction instanceof MovingObject) {
					throw new UnavailableActionException(
							"Grid square (" + this.endPoint.x + "," + this.endPoint.y + ") is temporarily obstructed.");
				} else {
					throw new ImpossibleActionException(
							"Grid square (" + this.endPoint.x + "," + this.endPoint.y + ") is permanently blocked.");
				}
			}
		}
	}

	@Override
	protected int numberOfSteps() {
		return stepsPerSquare;
	}
}
