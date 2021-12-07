package vac;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import actions.Action;
import actions.Clean;
import actions.ImpossibleActionException;
import actions.Light;
import actions.Move;
import actions.Turn;
import actions.UnavailableActionException;
import grid.Direction;
import grid.Grid;
import grid.GridObject;
import grid.GridPoint;
import grid.MovingObject;
import grid.RelativeDirection;
import grid.Square;

public class VacBot extends MovingObject {
	// VacBot light events
	public static final String LIGHT_ON = "Light on";
	public static final String LIGHT_OFF = "Light off";
	// Time in milliseconds to turn full circle, move one square, and clean,
	// respectively
	private final long timeToTurn = 2000;
	private final long timeToMove = 1500;
	private final long timeToClean = 3000;
	private final String name;
	private final Color colour;
	private boolean lightOn = false;

	// action queue
	private volatile boolean isAlive = true;
	private final Queue<Action> pendingActions;

	public VacBot(final Grid grid, final GridPoint startPoint, final Direction direction, final String name,
			final Color colour) {
		super(grid, startPoint, direction);
		this.name = name;
		this.colour = colour;
		this.pendingActions = new ConcurrentLinkedQueue<>();
	}

	public String getName() {
		return this.name;
	}

	public void start() {
		new Thread(() -> {
			while (VacBot.this.isAlive) {
				if (VacBot.this.pendingActions.isEmpty()) {
					synchronized (VacBot.this) {
						try {
							wait();
						} catch (final InterruptedException ignore) {
						}
					}
				} else {
					final Action next = VacBot.this.pendingActions.peek();
					try {
						next.execute();
					} catch (InterruptedException | ImpossibleActionException | UnavailableActionException e) {
						e.printStackTrace();
					} finally {
						this.pendingActions.remove();
					}
				}
			}
		}).start();
	}

	private void queueActions(final Action... actions) {
		if (this.pendingActions.isEmpty()) { // ignore the request if we're already doing something
			Collections.addAll(this.pendingActions, actions);
			synchronized (VacBot.this) {
				notifyAll();
			}
		}
	}

	public void kill() {
		this.isAlive = false;
		synchronized (VacBot.this) {
			notifyAll();
		}
	}

	@Override
	public void move(int steps, final Direction moveDirection)
			throws InterruptedException, ImpossibleActionException, UnavailableActionException {
		if (steps < 0) {
			throw new IllegalArgumentException("Cannot move a negative number of steps.");
		}

		final boolean needTurn = !this.direction.equalsDirection(moveDirection);
		if (needTurn) {
			++steps;
		}

		final Action[] actionList = new Action[steps];
		if (needTurn) {
			actionList[0] = new Turn(this, moveDirection);
		}
		for (int i = (needTurn ? 1 : 0); i < steps; i++) {
			actionList[i] = new Move(this);
		}
		queueActions(actionList);
	}

	public void clean() {
		queueActions(new Clean(this));
	}

	public void light(final boolean lightOn) {
		queueActions(new Light(this, lightOn));
	}

	public void setLightOn(final boolean lightOn) {
		this.lightOn = lightOn;
	}

	public boolean isLightOn() {
		return this.lightOn;
	}

	@Override
	public String toString() {
		return "VacBot \"" + this.name + "\"";
	}

	@Override
	public long getTimeToTurn() {
		return this.timeToTurn;
	}

	@Override
	public long getTimeToMove() {
		return this.timeToMove;
	}

	/**
	 * A GridObject or a VacBot counts as an obstruction.
	 */
	@Override
	public GridObject getObstruction(final Square square) {
		final GridObject obstruction = square.get(GridObject.class);
		return (obstruction == null) ? square.get(VacBot.class) : obstruction;
	}

	public long getTimeToClean() {
		return this.timeToClean;
	}

	/**
	 * Get the colour of this VacBot.
	 *
	 * @return the VacBot colour.
	 */
	public Color getColour() {
		return this.colour;
	}

	/**
	 * Get a list of the squares this VacBot can currently see.
	 *
	 * @return a List of real, actual squares which the VacBot can see
	 */
	public List<PerceptSquare> getFieldOfVision() {
		// Calculate co-ordinates for the five squares in vision
		final List<PerceptPoint> points = new ArrayList<>(5);
		points.add(new PerceptPoint(getNearestPoint(RelativeDirection.left), RelativeDirection.left));
		points.add(new PerceptPoint(getNearestPoint(RelativeDirection.forwardLeft), RelativeDirection.forwardLeft));
		points.add(new PerceptPoint(getNearestPoint(RelativeDirection.forward), RelativeDirection.forward));
		points.add(new PerceptPoint(getNearestPoint(RelativeDirection.forwardRight), RelativeDirection.forwardRight));
		points.add(new PerceptPoint(getNearestPoint(RelativeDirection.right), RelativeDirection.right));

		// If a point exists in the grid world, add its square to the field of vision
		final List<PerceptSquare> fieldOfVision = new ArrayList<>(6);
		for (final PerceptPoint point : points) {
			if (this.grid.exists(point.getGridPoint())) {
				fieldOfVision.add(
						new PerceptSquare(this.grid.getSquareAt(point.getGridPoint()), point.getRelativeDirection()));
			} else {
				fieldOfVision.add(new PerceptSquare(null, point.getRelativeDirection()));
			}
		}
		// The current square is always in the field of vision
		fieldOfVision.add(new PerceptSquare(getSquare(), RelativeDirection.here));
		return fieldOfVision;
	}

	public boolean canSee(final Square square) {
		for (final PerceptSquare perceptSquare : getFieldOfVision()) {
			if (perceptSquare.getSquare().equals(square)) {
				return true;
			}
		}
		return false;
	}
}
