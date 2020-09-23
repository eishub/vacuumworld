package vac;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import actions.ImpossibleActionException;
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

	public VacBot(final Grid grid, final GridPoint startPoint, final Direction direction, final String name,
			final Color colour) {
		super(grid, startPoint, direction);
		this.name = name;
		this.colour = colour;
	}

	public String getName() {
		return this.name;
	}

	public void clean() throws InterruptedException, UnavailableActionException {
		try {
			new Clean(this).execute();
		} catch (final ImpossibleActionException e) {
			// Cleaning is never impossible - dust could potentially be added later to any
			// square.
		}
	}

	public void setLightOn(final boolean lightOn) {
		this.lightOn = lightOn;
		// Fire a light state change (i.e. on/off) event
		if (lightOn) {
			this.fireEvent(LIGHT_ON);
		} else {
			this.fireEvent(LIGHT_OFF);
		}
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
		if (obstruction != null) {
			return obstruction;
		}
		return square.get(VacBot.class);
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
