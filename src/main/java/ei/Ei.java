package ei;

import java.util.Map;

import eis.AbstractEISInterface;
import eis.exceptions.EntityException;
import eis.exceptions.ManagementException;
import eis.iilang.EnvironmentState;
import eis.iilang.Identifier;
import eis.iilang.Numeral;
import eis.iilang.Parameter;
import util.config.Configuration;
import vac.VacBot;
import vac.VacWorld;

public class Ei extends AbstractEISInterface {
	private static final long serialVersionUID = 6104242799596441135L;

	private VacWorld world;

	public Ei() {
	}

	enum configKeys {
		/**
		 * The path to a config file
		 */
		configfile,
		/**
		 * The level, either a map name or a number.
		 */
		level,
		/**
		 * true or false, to be replaced with string
		 */
		regeneration,
		/**
		 * an optional speed factor (100 by default)
		 */
		speed
	}

	@Override
	public void init(final Map<String, Parameter> parameters) throws ManagementException {
		reset(parameters);

		int speedFactor = 100; // default=100%
		final Parameter speedParam = parameters.get(configKeys.speed.name());
		if (speedParam instanceof Numeral) {
			speedFactor = (int) ((Numeral) speedParam).getValue();
		} else if (speedParam instanceof Identifier) {
			try {
				speedFactor = Integer.parseInt(((Identifier) speedParam).getValue());
			} catch (final NumberFormatException e) {
				throw new ManagementException(speedParam + " is not a whole number!");
			}
		}
		this.world.setSpeedFactor(speedFactor);

		for (final VacBot vacBot : this.world.getVacBots()) {
			try {
				addEntity(new VacBotEntity(vacBot, speedFactor));
			} catch (final EntityException e) {
				throw new ManagementException("failed to add vacbot '" + vacBot.getName() + "'", e);
			}
		}
		addAction(new EisLight());
		addAction(new EisClean());
		addAction(new EisMove());
	}

	@Override
	public void pause() throws ManagementException {
		this.world.setRunning(false);
		super.pause();
	}

	@Override
	public void start() throws ManagementException {
		this.world.setRunning(true);
		super.start();
	}

	@Override
	public void reset(final Map<String, Parameter> parameters) throws ManagementException {
		closeWorld();
		if (parameters.containsKey(configKeys.configfile.toString())) {
			final Parameter fileparam = parameters.get(configKeys.configfile.toString());
			this.world = VacWorld.createFromConfig(((Identifier) fileparam).getValue());
		} else {
			this.world = VacWorld.createVacWorld(toConfiguration(parameters));
		}

		this.world.show();

		this.world.setRunning(false);
		setState(EnvironmentState.PAUSED);
	}

	/**
	 * Close old world if it's still open.
	 */
	private void closeWorld() {
		if (this.world != null) {
			this.world.close();
			this.world = null;
		}
	}

	@Override
	public void kill() throws ManagementException {
		closeWorld();
		setState(EnvironmentState.KILLED);
	}

	/**
	 * creates a {@link Configuration} from the EIS parameters..
	 *
	 * @param parameters
	 * @return
	 */
	private Configuration toConfiguration(final Map<String, Parameter> parameters) {
		// load default config
		final Configuration config = new Configuration();
		config.put(configKeys.level.toString(), "4");
		config.put(configKeys.regeneration.toString(), "no");

		// copy our parameter values.
		for (final String param : parameters.keySet()) {
			final String value = ((Identifier) parameters.get(param)).getValue();
			config.put(param, value);
		}
		return config;
	}
}
