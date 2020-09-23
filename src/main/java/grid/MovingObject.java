package grid;

import actions.ImpossibleActionException;
import actions.Move;
import actions.Turn;
import actions.UnavailableActionException;

public abstract class MovingObject extends GridObject {
	public static final String MOVE_START = "Move.start";
	public static final String MOVE_STEP = "Move.step";
	public static final String MOVE_STOP = "Move.stop";
	public static final String TURN_START = "Turn.start";
	public static final String TURN_STEP = "Turn.step";
	public static final String TURN_STOP = "Turn.stop";

	public enum Rotate {
		CLOCKWISE, ANTICLOCKWISE, NONE
	}

	/**
	 * Get the time it would take to turn full circle.
	 *
	 * @return the time in milliseconds to turn 360 degrees.
	 */
	public abstract long getTimeToTurn();

	/**
	 * Get the time it would take to move a distance of one square.
	 *
	 * @return the time in milliseconds to move one square.
	 */
	public abstract long getTimeToMove();

	/**
	 * Test for GridObjects in the given square that prevent this MovingObject from
	 * moving into it.
	 *
	 * @param square Square to inspect for obstructions.
	 * @return Null if a move to the given square is possible, or the obstruction
	 *         that prevents it.
	 */
	public abstract GridObject getObstruction(Square square);

	protected Direction direction;
	protected Rotate rotation = Rotate.NONE;

	/**
	 * Construct a new moving object. Only one moving object can be in a given
	 * square at a time.
	 *
	 * @param grid A grid to contain this object.
	 */
	public MovingObject(final Grid grid, final GridPoint location, final Direction direction) {
		super(grid, location);
		setDirection(direction);
	}

	public void setDirection(final Direction direction) {
		this.direction = direction;
	}

	public Direction getDirection() {
		return this.direction;
	}

	public void setRotation(final Rotate rotate) {
		this.rotation = rotate;
	}

	public Rotate getRotation() {
		return this.rotation;
	}

	/**
	 * Wrapper for move(int, Direction) to allow moves relative to the current
	 * direction.
	 *
	 * @param steps             non-negative number of steps to move
	 * @param relativeDirection forward, left, right, or back
	 * @throws InterruptedException
	 * @throws UnavailableActionException
	 * @throws ImpossibleActionException
	 */
	public void move(final int steps, final RelativeDirection relativeDirection)
			throws InterruptedException, ImpossibleActionException, UnavailableActionException {
		move(steps, relativeDirection.toAbsolute(this.direction));
	}

	/**
	 * Move the given number of steps in the given direction.
	 *
	 * @param steps         non-negative number of steps to move
	 * @param moveDirection north, south, east, or west
	 * @throws InterruptedException
	 * @throws UnavailableActionException
	 * @throws ImpossibleActionException
	 */
	public void move(final int steps, final Direction moveDirection)
			throws InterruptedException, ImpossibleActionException, UnavailableActionException {
		if (steps < 0) {
			throw new IllegalArgumentException("Cannot move a negative number of steps.");
		}
		if (!this.direction.equalsDirection(moveDirection)) {
			new Turn(this, moveDirection).execute();
		}
		final Move forwardsMove = new Move(this);
		for (int i = 0; i < steps; i++) {
			forwardsMove.execute();
		}
	}

	public void setX(final double newX) {
		this.x = newX;
	}

	public void setY(final double newY) {
		this.y = newY;
	}

	public GridPoint getNearestPoint(final RelativeDirection relativeDirection) {
		if (relativeDirection.equals(RelativeDirection.here)) {
			return new GridPoint((int) Math.round(this.x), (int) Math.round(this.y));
		} else {
			return new GridPoint(
					(int) Math.round(this.x + relativeDirection.toAbsolute(this.direction).getXComponent()),
					(int) Math.round(this.y + relativeDirection.toAbsolute(this.direction).getYComponent()));
		}
	}
}