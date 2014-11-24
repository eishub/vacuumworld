package ei;

import vac.VacBot;
import actions.ImpossibleActionException;
import actions.UnavailableActionException;
import eis.AbstractEISEntityAction;
import eis.exceptions.ActException;
import eis.iilang.Action;
import eis.iilang.Percept;
import grid.Direction;
import grid.RelativeDirection;

public class EisMove extends AbstractEISEntityAction {

	public EisMove() {
		super("move");
		addType("vacbot");
	}

	@Override
	public Percept act(String entity, Action action) throws ActException {
		VacBotEntity vacBotEntity = getEntity(entity, VacBotEntity.class);
		if (vacBotEntity == null)
			throw new ActException(ActException.WRONGENTITY);

		if (action.getParameters().size() == 1) {
			return move(vacBotEntity.bot, action.getParameters().get(0)
					.toProlog(), 1);
		} else if (action.getParameters().size() >= 2) {
			return move(vacBotEntity.bot, action.getParameters().get(0)
					.toProlog(),
					Integer.parseInt(action.getParameters().get(1).toProlog()));
		} else {
			throw new ActException(ActException.FAILURE,
					"\"Move\" action requires at least one parameter!");
		}
	}

	private Percept move(VacBot vacBot, String directionName, int steps)
			throws ActException {
		Direction directionToMove = null;
		Direction currentDirection = vacBot.getDirection();
		if (directionName.equals(Direction.NORTH)) {
			directionToMove = Direction.north;
		} else if (directionName.equals(Direction.SOUTH)) {
			directionToMove = Direction.south;
		} else if (directionName.equals(Direction.EAST)) {
			directionToMove = Direction.east;
		} else if (directionName.equals(Direction.WEST)) {
			directionToMove = Direction.west;
		} else if (directionName.equals(RelativeDirection.FORWARD)) {
			directionToMove = RelativeDirection.forward
					.toAbsolute(currentDirection);
		} else if (directionName.equals(RelativeDirection.LEFT)) {
			directionToMove = RelativeDirection.left
					.toAbsolute(currentDirection);
		} else if (directionName.equals(RelativeDirection.RIGHT)) {
			directionToMove = RelativeDirection.right
					.toAbsolute(currentDirection);
		} else if (directionName.equals(RelativeDirection.BACK)) {
			directionToMove = RelativeDirection.back
					.toAbsolute(currentDirection);
		} else {
			throw new ActException(ActException.FAILURE, "Invalid direction \""
					+ directionName + "\".");
		}

		try {
			vacBot.move(steps, directionToMove);
		} catch (InterruptedException e) {
			throw new ActException(ActException.FAILURE,
					"Move was interrupted!");
		} catch (ImpossibleActionException e) {
			throw new ActException(ActException.FAILURE,
					"Move into permanent obstacle not possible.");
		} catch (UnavailableActionException e) {
			throw new ActException(ActException.FAILURE,
					"Move into moveable object not possible - seems another VacBot in the way");
		}
		return new Percept("success");
	}

}
