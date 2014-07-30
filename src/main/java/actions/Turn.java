package actions;

import grid.Direction;
import grid.MovingObject;
import grid.MovingObject.Rotate;

public class Turn extends Action {
	
	private static final int stepsPerRevolution = 64;
	private final MovingObject movingObject;
	private final Direction targetDirection;
	private Direction currentDirection;
	private Rotate rotationDirection = Rotate.NONE;
	private long duration = 0;
	private int numberOfSteps = 0;

	public Turn(MovingObject movingObject, Direction targetDirection) {
		super(movingObject);
		this.targetDirection = targetDirection;
		this.movingObject = movingObject;
	}
	
	protected long duration() {
		return duration;
	}

	protected int numberOfSteps() {
		return numberOfSteps;
	}

	/**
	 * Calculate rotation direction, duration, and number of steps.
	 */
	protected void initialise() {
		currentDirection = new Direction(movingObject.getDirection().getRadians());
		if (currentDirection.equalsDirection(targetDirection)) {
			rotationDirection = Rotate.NONE;
			duration = 0;
			numberOfSteps = 0;
		} else {
			// Define a clockwise turn as positive
			double turn = targetDirection.getRadians() - currentDirection.getRadians();
			if (turn > Math.PI) {
				// Don't go more than 180 clockwise, go anti-clockwise instead
				turn -= (2.0 * Math.PI);
			} else if (turn < -Math.PI) {
				// Don't go more than 180 anti-clockwise, go clockwise instead
				turn += (2.0 * Math.PI);
			}
			if (turn < 0) rotationDirection = Rotate.ANTICLOCKWISE;
			else if (turn > 0) rotationDirection = Rotate.CLOCKWISE;

			// Figure out how long it will take and how many steps there will be
			double turnFraction = Math.abs(turn) / (2.0 * Math.PI);
			duration = Math.round(movingObject.getTimeToTurn() * turnFraction);
			numberOfSteps = (int)Math.round(stepsPerRevolution * turnFraction);
		}
	}

	protected void executeOneStep() {
		if (rotationDirection == Rotate.CLOCKWISE)
			currentDirection.setValue(currentDirection.getRadians() 
					+ ((2.0 * Math.PI) / stepsPerRevolution));
		else
			currentDirection.setValue(currentDirection.getRadians() 
					- ((2.0 * Math.PI) / stepsPerRevolution));
		movingObject.setDirection(currentDirection);
	}
	
	/**
	 * Cleanup to stop floating point rounding errors from accumulating.
	 */
	protected void finalise() {
		movingObject.setDirection(targetDirection);
		movingObject.setRotation(Rotate.NONE);
	}
	
}
