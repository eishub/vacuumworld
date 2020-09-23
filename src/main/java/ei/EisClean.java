package ei;

import actions.UnavailableActionException;
import eis.AbstractEISEntityAction;
import eis.exceptions.ActException;
import eis.iilang.Action;

public class EisClean extends AbstractEISEntityAction {
	public EisClean() {
		super("clean");
		addType("vacbot");
	}

	@Override
	public void act(final String entity, final Action action) throws ActException {
		final VacBotEntity vacBotEntity = getEntity(entity, VacBotEntity.class);
		if (vacBotEntity == null) {
			throw new ActException(ActException.WRONGENTITY);
		}

		try {
			vacBotEntity.bot.clean();
			// return new Percept("success");
		} catch (final InterruptedException e) {
			// return new Percept("busy", new Identifier("Clean interrupted!"));
		} catch (final UnavailableActionException e) {
			// return new Percept("bump", new Identifier("Nothing to clean here!"));
		}
	}
}
