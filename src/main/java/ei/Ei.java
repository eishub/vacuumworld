package ei;

import java.util.HashMap;
import java.util.Map;

import util.config.Configuration;
import vac.VacBot;
import vac.VacWorld;
import eis.AbstractEISInterface;
import eis.exceptions.EntityException;
import eis.exceptions.ManagementException;
import eis.iilang.EnvironmentState;
import eis.iilang.Identifier;
import eis.iilang.Parameter;

public class Ei extends AbstractEISInterface {

	private static final long serialVersionUID = 6104242799596441135L;
	VacWorld world;
	final Map<String, VacBot> vacBots = new HashMap<String, VacBot>();

	public Ei() {

	}

	enum configKeys {
		configfile,
		/**
		 * The level, either a map name or a number.
		 */
		level,

		/**
		 * true or false. To be replaced with string
		 */
		regeneration
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
	public void pause() throws ManagementException {
		world.setRunning(false);
		super.pause();
	}

	@Override
	public void start() throws ManagementException {
		world.setRunning(true);
		super.start();
	}

	@Override
	public void reset(Map<String, Parameter> parameters)
			throws ManagementException {
		closeWorld();
		if (parameters.containsKey(configKeys.configfile.toString())) {
			Parameter fileparam = parameters.get(configKeys.configfile
					.toString());
			world = VacWorld.createFromConfig(((Identifier) fileparam)
					.getValue());
		} else {
			world = VacWorld.createVacWorld(toConfiguration(parameters));
		}

		world.show();

		world.setRunning(false);
		this.setState(EnvironmentState.PAUSED);
	}

	/**
	 * Close old world if it's still open.
	 */
	private void closeWorld() {
		if (world != null) {
			world.close();
			world = null;
		}
	}

	@Override
	public void kill() throws ManagementException {
		closeWorld();
		setState(EnvironmentState.KILLED);
	}

	// TODO: support percepts as notifications

	/**
	 * creates a {@link Configuration} from the EIS parameters..
	 * 
	 * @param parameters
	 * @return
	 */
	private Configuration toConfiguration(Map<String, Parameter> parameters) {
		// load default config
		Configuration config = new Configuration();
		config.put(configKeys.level.toString(), "4");
		config.put(configKeys.regeneration.toString(), "no");

		// copy our parameter values.
		for (String param : parameters.keySet()) {
			String value = ((Identifier) parameters.get(param)).getValue();
			config.put(param, value);
		}
		return config;
	}

}
