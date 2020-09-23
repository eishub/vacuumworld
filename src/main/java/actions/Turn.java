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

	public Turn(final MovingObject movingObject, final Direction targetDirection) {
		super(movingObject);
		this.targetDirection = targetDirection;
		this.movingObject = movingObject;
	}

	@Override
	protected long duration() {
		return this.duration;
	}

	@Override
	protected int numberOfSteps() {
		return this.numberOfSteps;
	}

	/**
	 * Calculate rotation direction, duration, and number of steps.
	 */
	@Override
	protected void initialise() {
		this.currentDirection = new Direction(this.movingObject.getDirection().getRadians());
		if (this.currentDirection.equalsDirection(this.targetDirection)) {
			this.rotationDirection = Rotate.NONE;
			this.duration = 0;
			this.numberOfSteps = 0;
		} else {
			// Define a clockwise turn as positive
			double turn = this.targetDirection.getRadians() - this.currentDirection.getRadians();
			if (turn > Math.PI) {
				// Don't go more than 180 clockwise, go anti-clockwise instead
				turn -= (2.0 * Math.PI);
			} else if (turn < -Math.PI) {
				// Don't go more than 180 anti-clockwise, go clockwise instead
				turn += (2.0 * Math.PI);
			}
			if (turn < 0) {
				this.rotationDirection = Rotate.ANTICLOCKWISE;
			} else if (turn > 0) {
				this.rotationDirection = Rotate.CLOCKWISE;
			}

			// Figure out how long it will take and how many steps there will be
			final double turnFraction = Math.abs(turn) / (2.0 * Math.PI);
			this.duration = Math.round(this.movingObject.getTimeToTurn() * turnFraction);
			this.numberOfSteps = (int) Math.round(stepsPerRevolution * turnFraction);
		}
	}

	@Override
	protected void executeOneStep() {
		if (this.rotationDirection == Rotate.CLOCKWISE) {
			this.currentDirection.setValue(this.currentDirection.getRadians() + ((2.0 * Math.PI) / stepsPerRevolution));
		} else {
			this.currentDirection.setValue(this.currentDirection.getRadians() - ((2.0 * Math.PI) / stepsPerRevolution));
		}
		this.movingObject.setDirection(this.currentDirection);
	}

	/**
	 * Cleanup to stop floating point rounding errors from accumulating.
	 */
	@Override
	protected void finalise() {
		this.movingObject.setDirection(this.targetDirection);
		this.movingObject.setRotation(Rotate.NONE);
	}
}
