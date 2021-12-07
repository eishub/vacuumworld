package actions;

import vac.VacBot;

public class Light extends Action {
	private final boolean lightOn;

	public Light(final VacBot vacBot, final boolean lightOn) {
		super(vacBot);
		this.lightOn = lightOn;
	}

	@Override
	protected long duration() {
		return 0;
	}

	@Override
	protected int numberOfSteps() {
		return 0;
	}

	@Override
	protected void initialise() {
	}

	@Override
	protected void executeOneStep() {
		this.modelObject.fireEvent(this.lightOn ? VacBot.LIGHT_ON : VacBot.LIGHT_OFF, this.modelObject);
		if (this.modelObject instanceof VacBot) {
			((VacBot) this.modelObject).setLightOn(this.lightOn);
		}
	}

	@Override
	protected void finalise() {
	}
}
