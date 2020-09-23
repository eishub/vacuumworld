package ei;

import actions.ImpossibleActionException;
import actions.UnavailableActionException;
import eis.AbstractEISEntityAction;
import eis.exceptions.ActException;
import eis.iilang.Action;
import grid.Direction;
import grid.RelativeDirection;
import vac.VacBot;

public class EisMove extends AbstractEISEntityAction {
	public EisMove() {
		super("move");
		addType("vacbot");
	}

	@Override
	public void act(final String entity, final Action action) throws ActException {
		final VacBotEntity vacBotEntity = getEntity(entity, VacBotEntity.class);
		if (vacBotEntity == null) {
			throw new ActException(ActException.WRONGENTITY);
		}

		if (action.getParameters().size() == 1) {
			move(vacBotEntity.bot, action.getParameters().get(0).toProlog(), 1);
		} else if (action.getParameters().size() >= 2) {
			move(vacBotEntity.bot, action.getParameters().get(0).toProlog(),
					Integer.parseInt(action.getParameters().get(1).toProlog()));
		} else {
			throw new ActException(ActException.FAILURE, "\"Move\" action requires at least one parameter!");
		}
	}

	private void move(final VacBot vacBot, final String directionName, final int steps) throws ActException {
		Direction directionToMove = null;
		final Direction currentDirection = vacBot.getDirection();
		if (directionName.equals(Direction.NORTH)) {
			directionToMove = Direction.north;
		} else if (directionName.equals(Direction.SOUTH)) {
			directionToMove = Direction.south;
		} else if (directionName.equals(Direction.EAST)) {
			directionToMove = Direction.east;
		} else if (directionName.equals(Direction.WEST)) {
			directionToMove = Direction.west;
		} else if (directionName.equals(RelativeDirection.FORWARD)) {
			directionToMove = RelativeDirection.forward.toAbsolute(currentDirection);
		} else if (directionName.equals(RelativeDirection.LEFT)) {
			directionToMove = RelativeDirection.left.toAbsolute(currentDirection);
		} else if (directionName.equals(RelativeDirection.RIGHT)) {
			directionToMove = RelativeDirection.right.toAbsolute(currentDirection);
		} else if (directionName.equals(RelativeDirection.BACK)) {
			directionToMove = RelativeDirection.back.toAbsolute(currentDirection);
		} else {
			throw new ActException(ActException.FAILURE, "Invalid direction \"" + directionName + "\".");
		}

		try {
			vacBot.move(steps, directionToMove);
			// return new Percept("success");
		} catch (final InterruptedException e) {
			// return new Percept("bump", new Identifier("Move was interrupted!"));
		} catch (final ImpossibleActionException e) {
			// return new Percept("bump", new Identifier("Move into permanent obstacle not
			// possible."));
		} catch (final UnavailableActionException e) {
			// return new Percept("bump", new Identifier("Move into moveable object not
			// possible - seems another VacBot in the way"));
		}
	}
}
