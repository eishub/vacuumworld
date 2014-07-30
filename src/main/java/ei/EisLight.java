package ei;

import eis.AbstractEISEntityAction;
import eis.exceptions.ActException;
import eis.iilang.Action;
import eis.iilang.Percept;

public class EisLight extends AbstractEISEntityAction {

	public EisLight() {
		super("light");
		addType("vacbot");
	}

	@Override
	public Percept act(String entity, Action action) throws ActException {
		VacBotEntity vacBotEntity = getEntity(entity, VacBotEntity.class);
		if (vacBotEntity == null) throw new ActException(ActException.WRONGENTITY);
		// TODO: this seems a very complicated way to get a boolean parameter - can we do better?
		String state = action.getParameters().get(0).toProlog();
		if (state.equalsIgnoreCase("on")) {
			vacBotEntity.bot.setLightOn(true);
		} else if (state.equalsIgnoreCase("off")) {
			vacBotEntity.bot.setLightOn(false);
		} else {
			throw new ActException(ActException.FAILURE,
					"Cannot set the light state to \"" + state + "\".");
		}
		return null;
	}
	
}
