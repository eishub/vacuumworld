package ei;

import eis.AbstractEISEntityAction;
import eis.exceptions.ActException;
import eis.iilang.Action;
import eis.iilang.Identifier;
import eis.iilang.Numeral;
import eis.iilang.Parameter;
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

		String direction = "";
		if (action.getParameters().size() >= 1 && action.getParameters().get(0) instanceof Identifier) {
			direction = ((Identifier) action.getParameters().get(0)).getValue();
		}

		int steps = 1;
		if (action.getParameters().size() == 2) {
			final Parameter stepParam = action.getParameters().get(1);
			if (stepParam instanceof Numeral) {
				steps = (int) ((Numeral) stepParam).getValue();
			} else if (stepParam instanceof Identifier) {
				steps = Integer.parseInt(((Identifier) stepParam).getValue());
			}
		}

		move(vacBotEntity.getBot(), direction, steps, vacBotEntity.getSpeedFactor());
	}

	private void move(final VacBot vacBot, final String directionName, final int steps, final int speedFactor)
			throws ActException {
		Direction directionToMove = null;
		if (directionName.equals(Direction.NORTH)) {
			directionToMove = Direction.north;
		} else if (directionName.equals(Direction.SOUTH)) {
			directionToMove = Direction.south;
		} else if (directionName.equals(Direction.EAST)) {
			directionToMove = Direction.east;
		} else if (directionName.equals(Direction.WEST)) {
			directionToMove = Direction.west;
		} else if (directionName.equals(RelativeDirection.FORWARD)) {
			directionToMove = RelativeDirection.forward.toAbsolute(vacBot.getDirection());
		} else if (directionName.equals(RelativeDirection.LEFT)) {
			directionToMove = RelativeDirection.left.toAbsolute(vacBot.getDirection());
		} else if (directionName.equals(RelativeDirection.RIGHT)) {
			directionToMove = RelativeDirection.right.toAbsolute(vacBot.getDirection());
		} else if (directionName.equals(RelativeDirection.BACK)) {
			directionToMove = RelativeDirection.back.toAbsolute(vacBot.getDirection());
		} else {
			throw new ActException(ActException.FAILURE, "Invalid direction \"" + directionName + "\".");
		}

		vacBot.move(steps, directionToMove, speedFactor);
	}
}
