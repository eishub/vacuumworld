package vac;

import actions.Action;
import actions.UnavailableActionException;
import grid.Direction;
import grid.Square;

public class Clean extends Action {

	private final VacBot vacBot;
	private Square square;
	private int initialSpeckCount;
	private Direction originalDirection;
	private Direction leftDirection;
	private Direction rightDirection;
	private int frameNumber;
	private Dust dust;
	
	protected Clean(VacBot vacBot) {
		super(vacBot);
		this.vacBot = vacBot;
	}

	protected long duration() {
		return vacBot.getTimeToClean();
	}

	protected void executeOneStep() {
		++frameNumber;
		switch (frameNumber) {
		case 1:
			vacBot.setDirection(leftDirection);
			break;
		case 2:
			vacBot.setDirection(originalDirection);
			break;
		case 3:
			vacBot.setDirection(rightDirection);
			break;
		case 4:
			vacBot.setDirection(originalDirection);
			dust.cleanOne();
			frameNumber = 0;
		}
	}

	protected void finalise() {
		dust.cleanOne();
		square.remove(dust);
	}

	protected void initialise() throws UnavailableActionException {
		square = vacBot.getSquare();
		if (!square.has(Dust.class)) {
			throw new UnavailableActionException("No dust here!");
		}
		dust = (Dust)(square.get(Dust.class));
		initialSpeckCount = dust.getSpeckCount();
		// Create two directions just to the left and right of current direction
		originalDirection = vacBot.getDirection();
		leftDirection = new Direction(originalDirection.getRadians() - 0.06);
		rightDirection = new Direction(originalDirection.getRadians() + 0.06);
		frameNumber = 0;
	}

	protected int numberOfSteps() {
		return initialSpeckCount * 4;
	}

}
