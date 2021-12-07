package ei;

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

		vacBotEntity.bot.clean();
	}
}
