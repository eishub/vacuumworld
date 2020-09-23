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

	protected Clean(final VacBot vacBot) {
		super(vacBot);
		this.vacBot = vacBot;
	}

	@Override
	protected long duration() {
		return this.vacBot.getTimeToClean();
	}

	@Override
	protected void executeOneStep() {
		++this.frameNumber;
		switch (this.frameNumber) {
		case 1:
			this.vacBot.setDirection(this.leftDirection);
			break;
		case 2:
			this.vacBot.setDirection(this.originalDirection);
			break;
		case 3:
			this.vacBot.setDirection(this.rightDirection);
			break;
		case 4:
			this.vacBot.setDirection(this.originalDirection);
			this.dust.cleanOne();
			this.frameNumber = 0;
		}
	}

	@Override
	protected void finalise() {
		this.dust.cleanOne();
		this.square.remove(this.dust);
	}

	@Override
	protected void initialise() throws UnavailableActionException {
		this.square = this.vacBot.getSquare();
		if (!this.square.has(Dust.class)) {
			throw new UnavailableActionException("No dust here!");
		}
		this.dust = (Dust) (this.square.get(Dust.class));
		this.initialSpeckCount = this.dust.getSpeckCount();
		// Create two directions just to the left and right of current direction
		this.originalDirection = this.vacBot.getDirection();
		this.leftDirection = new Direction(this.originalDirection.getRadians() - 0.06);
		this.rightDirection = new Direction(this.originalDirection.getRadians() + 0.06);
		this.frameNumber = 0;
	}

	@Override
	protected int numberOfSteps() {
		return this.initialSpeckCount * 4;
	}
}
