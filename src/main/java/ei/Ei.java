package ei;

import java.util.HashMap;
import java.util.Map;

import vac.VacBot;
import vac.VacWorld;
import eis.AbstractEISInterface;
import eis.exceptions.EntityException;
import eis.exceptions.ManagementException;
import eis.iilang.EnvironmentState;
import eis.iilang.Parameter;

public class Ei extends AbstractEISInterface {

	private static final long serialVersionUID = 6104242799596441135L;
	private static final String configFile = "ita.conf";
	VacWorld world;
	final Map<String, VacBot> vacBots = new HashMap<String, VacBot>();

	public Ei() {

	}

	@Override
	public void init(Map<String, Parameter> parameters)
			throws ManagementException {

		this.reset(parameters);

		for (VacBot vacBot : world.getVacBots()) {
			try {
				addEntity(new VacBotEntity(vacBot));
			} catch (EntityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		addAction(new EisLight());
		addAction(new EisClean());
		addAction(new EisMove());
	}

	@Override
	public void reset(Map<String, Parameter> parameters)
			throws ManagementException {

		world = VacWorld.createFromConfig(configFile);
		world.show();

		this.setState(EnvironmentState.PAUSED);
	}

	@Override
	public void kill() throws ManagementException {
		if (world != null) {
			world.close();
			world = null;
		}

		setState(EnvironmentState.KILLED);
	}

	// TODO: support percepts as notifications

}
