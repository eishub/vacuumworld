package actions;

import grid.Grid;
import grid.GridObject;
import grid.GridPoint;
import grid.MovingObject;
import grid.RelativeDirection;
import grid.Square;

/**
 * Class to represent a one-square move in the current (i.e. forwards) direction.
 * Chain them together to go long distances :)
 */
public class Move extends Action {

	private static final int stepsPerSquare = 32;
	private final MovingObject movingObject;
	private final Grid grid;
	private GridPoint startPoint = null;
	private GridPoint endPoint = null;
	
	public Move(MovingObject movingObject) {
		super(movingObject);
		this.movingObject = movingObject;
		this.grid = movingObject.getGrid();
	}

	protected long duration() {
		return movingObject.getTimeToMove();
	}

	protected void executeOneStep() {
		movingObject.setX(movingObject.getX() 
				+ (movingObject.getDirection().getXComponent() / stepsPerSquare));
		movingObject.setY(movingObject.getY() 
				+ (movingObject.getDirection().getYComponent() / stepsPerSquare));
	}

	/**
	 * Cleanup to stop floating point rounding errors from accumulating, and remove the
	 * MovingObject from the start square.
	 */
	protected void finalise() {
		movingObject.setX(endPoint.x);
		movingObject.setY(endPoint.y);
		grid.getSquareAt(startPoint).remove(movingObject);
	}

	/**
	 * Check the destination square exists, and try to get exclusive occupancy.
	 */
	protected void initialise() throws ImpossibleActionException, UnavailableActionException {
		startPoint = movingObject.getLocation();
		endPoint = movingObject.getNearestPoint(RelativeDirection.forward);
		if (!grid.exists(endPoint)) {
			throw new ImpossibleActionException(
					"Grid square (" + endPoint.x + "," + endPoint.y + ") does not exist!");
		}
		Square endSquare = grid.getSquareAt(endPoint);
		synchronized(endSquare) {
			GridObject obstruction = movingObject.getObstruction(endSquare);
			if (obstruction == null) {
				endSquare.add(movingObject);
			} else {
				if (obstruction instanceof MovingObject) {
					throw new UnavailableActionException(
							"Grid square (" + endPoint.x + "," + endPoint.y + ") is temporarily obstructed.");
				} else {
					throw new ImpossibleActionException(
							"Grid square (" + endPoint.x + "," + endPoint.y + ") is permanently blocked.");
				}
			}
		}
	}

	protected int numberOfSteps() {
		return stepsPerSquare;
	}

}
