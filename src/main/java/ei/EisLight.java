package ei;

import eis.AbstractEISEntityAction;
import eis.exceptions.ActException;
import eis.iilang.Action;
import eis.iilang.Identifier;

public class EisLight extends AbstractEISEntityAction {
	public EisLight() {
		super("light");
		addType("vacbot");
	}

	@Override
	public void act(final String entity, final Action action) throws ActException {
		final VacBotEntity vacBotEntity = getEntity(entity, VacBotEntity.class);
		if (vacBotEntity == null) {
			throw new ActException(ActException.WRONGENTITY);
		}
		// TODO: this is a complicated way to get a boolean parameter
		String state = "";
		if (action.getParameters().size() == 1 && action.getParameters().get(0) instanceof Identifier) {
			state = ((Identifier) action.getParameters().get(0)).getValue();
		}
		if (state.equalsIgnoreCase("on")) {
			vacBotEntity.getBot().light(true);
		} else { // just assume off in any other case
			vacBotEntity.getBot().light(false);
		}
	}
}
