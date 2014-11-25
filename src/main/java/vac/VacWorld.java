package vac;

import grid.Direction;
import grid.Grid;
import grid.GridObject;
import grid.GridPoint;
import grid.ModelListener;
import grid.ModelObject;
import grid.Square;
import gui.AppView;
import gui.LookAndFeel;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import util.config.Configuration;
import util.config.InvalidConfigurationException;
import util.config.PropertyTypeException;
import actions.Action;

/**
 * Convenience methods to construct, populate, and show a VacBot grid world.
 */
public class VacWorld implements ModelListener {

	private static final String configLevel = "level";
	private static final String configRegeneration = "regeneration";

	private static final int defaultSize = 4;

	private final String CLEAN_STOP = Clean.class.getName() + "."
			+ Action.STOP_EVENT;
	private VacBot[] vacBots = null;
	public final Grid grid;
	private Timer timer;
	private AppView view;

	/**
	 * Constructor to create an empty grid of specified size. Size is then fixed
	 * for the life of the grid.
	 * 
	 * @param sizeX
	 *            x-axis size of the grid, must be >= 2.
	 * @param sizeY
	 *            y-axis size of the grid, must be >= 2.
	 */
	VacWorld(int sizeX, int sizeY) {
		if (sizeX < 2)
			throw new IllegalArgumentException("X-axis size must be >= 2");
		if (sizeY < 2)
			throw new IllegalArgumentException("Y-axis size must be >= 2");
		grid = new Grid(sizeX, sizeY);
	}

	/**
	 * Constructor to create a randomly-populated grid with the specified number
	 * of VacBots. The grid size, number of obstacles, and amount of dust, is in
	 * proportion to the VacBot count.
	 * 
	 * @param numberOfVacBots
	 *            number of VacBots, from 1 to 8, to place in the grid.
	 */
	public VacWorld(int numberOfVacBots) {
		if (numberOfVacBots < 1) {
			throw new IllegalArgumentException(
					"VacWorld must have at least one VacBot!");
		} else if (numberOfVacBots > 8) {
			throw new IllegalArgumentException(
					"VacWorld can support at most 8 VacBots.");
		}
		int gridArea = numberOfVacBots * 32;
		int sizeX = (int) Math.round(Math.sqrt(gridArea * 2.0));
		int sizeY = (int) Math.round(Math.sqrt(gridArea * 0.5));
		grid = new Grid(sizeX, sizeY);
		addRandomObstructions(numberOfVacBots * 2);
		addRandomDust(numberOfVacBots * 8);
		addRandomVacBots(numberOfVacBots);
	}

	/**
	 * Constructs a grid world from the specified input stream. Format is
	 * plain-text ASCII, where each character represents one square: ' ' = empty
	 * square (space) '.' = dust (period) 'X' = obstacle (capital 'x') 'N|S|E|W'
	 * = vac bot facing north|south|east|west and dust 'n|s|e|w' = vac bot
	 * facing north|south|east|west
	 * 
	 * @throws IOException
	 *             if one occurs while reading from the stream
	 * @throws InvalidVacWorldException
	 *             if the world is less than 2x2, not rectangular, has >8 or <1
	 *             VacBots, or contains any unrecognised characters.
	 */
	public VacWorld(InputStream in) throws IOException,
			InvalidVacWorldException {
		// Read the given input into a list of strings
		List<String> lines = new ArrayList<String>();
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		String fileLine;
		while ((fileLine = reader.readLine()) != null) {
			if (!fileLine.isEmpty() && !fileLine.startsWith("#"))
				lines.add(fileLine);
		}
		reader.close();

		// Check grid is a rectangle of at least 2 x 2
		int sizeY = lines.size();
		if (sizeY < 2)
			throw new InvalidVacWorldException(
					"Minimum y-dimension of grid is 2.");
		int sizeX = -1;
		for (String configLine : lines) {
			if (sizeX < 0) {
				sizeX = configLine.length();
			} else {
				if (configLine.length() != sizeX)
					throw new InvalidVacWorldException(
							"Grid must be a rectangle!");
			}
		}
		if (sizeX < 2)
			throw new InvalidVacWorldException(
					"Minimum x-dimension of grid is 2.");

		// Populate the grid
		List<VacBot> vacBots = new ArrayList<VacBot>();
		grid = new Grid(sizeX, sizeY);
		for (int i = 0; i < sizeX; i++) {
			for (int j = 0; j < sizeY; j++) {
				Square square = grid.getSquareAt(new GridPoint(i, j));
				char c = lines.get(j).charAt(i);
				switch (c) {
				case ' ':
					break;
				case '.':
					new Dust(grid, square.location);
					break;
				case 'X':
					new GridObject(grid, square.location);
					break;
				case 'N':
				case 'S':
				case 'E':
				case 'W':
					if (!addVacBotAndDust(vacBots, square,
							Character.toString(c))) {
						throw new InvalidVacWorldException(
								"Cannot create world with more than 8 VacBots!");
					}
					break;
				case 'n':
				case 's':
				case 'e':
				case 'w':
					if (!addVacBot(vacBots, square, Character.toString(c))) {
						throw new InvalidVacWorldException(
								"Cannot create world with more than 8 VacBots!");
					}
					break;
				default:
					throw new InvalidVacWorldException("Illegal character \""
							+ c + "\" in world description.");
				}
			}
		}
		if (vacBots.size() < 1)
			throw new InvalidVacWorldException(
					"World must have at least 1 VacBot!");
		this.vacBots = vacBots.toArray(new VacBot[0]);
	}

	private boolean addVacBot(List<VacBot> vacBots, Square square, String d) {
		if (vacBots.size() < 8) {
			Direction direction = null;
			if (d.equalsIgnoreCase("N")) {
				direction = Direction.north;
			} else if (d.equalsIgnoreCase("S")) {
				direction = Direction.south;
			} else if (d.equalsIgnoreCase("E")) {
				direction = Direction.east;
			} else if (d.equalsIgnoreCase("W")) {
				direction = Direction.west;
			}
			vacBots.add(new VacBot(grid, square.location, direction,
					LookAndFeel.getVacBotName(), LookAndFeel.getVacBotColour()));
			return true;
		} else {
			return false;
		}
	}

	private boolean addVacBotAndDust(List<VacBot> vacBots, Square square,
			String d) {
		new Dust(grid, square.location);
		return addVacBot(vacBots, square, d);
	}

	public VacBot[] getVacBots() {
		return vacBots;
	}

	void addRandomDust(int targetDustCount) {
		if (targetDustCount < 0)
			throw new IllegalArgumentException("Too small: " + targetDustCount);
		if (targetDustCount > (grid.sizeX * grid.sizeY))
			throw new IllegalArgumentException("Too large: " + targetDustCount);
		Iterator<Square> iterator = grid.randomIterator();
		int currentDustCount = 0;
		while (currentDustCount < targetDustCount) {
			if (!iterator.hasNext())
				break;
			Square square = iterator.next();
			// If this square already has anything in it, ignore it - place a
			// Dust next time
			if (square.hasInstanceOf(GridObject.class))
				continue;
			new Dust(grid, square.location);
			++currentDustCount;
		}
	}

	void addRandomObstructions(int targetObstructionCount) {
		Iterator<Square> iterator = grid.randomIterator();
		int currentObstructionCount = 0;
		while (currentObstructionCount < targetObstructionCount) {
			if (!iterator.hasNext())
				break;
			Square square = iterator.next();
			// If this square already has anything in it, ignore it - place an
			// obstruction next time
			if (square.hasInstanceOf(GridObject.class))
				continue;
			new GridObject(grid, square.location);
			++currentObstructionCount;
		}
	}

	void addRandomVacBots(int targetVacBotCount) {
		if (targetVacBotCount > 8) {
			throw new IllegalArgumentException(
					"VacWorld can support at most 8 VacBots.");
		}
		List<VacBot> vacBots = new ArrayList<VacBot>(targetVacBotCount);
		Iterator<Square> iterator = grid.randomIterator();
		int currentVacBotCount = 0;
		while (currentVacBotCount < targetVacBotCount) {
			if (!iterator.hasNext())
				break;
			Square square = iterator.next();
			// If this square already has a vacbot or an obstacle in it, ignore
			// it - place a VacBot next time
			if (square.has(VacBot.class) || square.has(GridObject.class))
				continue;
			// TODO: remove this dependency on gui.LookAndFeel
			vacBots.add(new VacBot(grid, square.location, Direction.random(),
					LookAndFeel.getVacBotName(), LookAndFeel.getVacBotColour()));
			// TODO: depending on config, attach an event logger to each VacBot
			// new StreamEventLogger(vacBots.get(currentVacBotCount),
			// System.out);
			++currentVacBotCount;
		}
		this.vacBots = vacBots.toArray(new VacBot[0]);
	}

	// TODO: remove this dependency on gui.AppView
	public void show() {
		view = new AppView(grid);
	}

	public void close() {
		if (timer != null)
			timer.cancel();
		view.close();
	}

	public void setRegeneratingDust() {
		timer = new Timer();
		grid.addListener(this);
	}

	public void eventFired(String eventName, ModelObject source) {
		if (eventName.equals(CLEAN_STOP)) {
			timer.schedule(new TimerTask() {
				public void run() {
					addRandomDust(1);
				}
			}, Math.round(Math.random() * 20000));
		}
	}

	/**
	 * tries to read the given config file. The config file must contain a
	 * configLevel and configRegeneration value.
	 * 
	 * @param configFile
	 * @return {@link VacWorld} object.
	 */
	public static VacWorld createFromConfig(String configFile) {
		Configuration configuration = new Configuration();

		try {
			// Read configuration file
			configuration.load(configFile);
			return createVacWorld(configuration);

		} catch (FileNotFoundException fnfe) {
			System.out.println("Configuration file \"" + configFile
					+ "\" not found, using defaults.");
		} catch (IOException ioe) {
			System.out.println("Error while reading \"" + configFile
					+ "\", using defaults.");
			ioe.printStackTrace();
		} catch (InvalidConfigurationException ice) {
			System.out.println("Config file \"" + configFile
					+ "\" is invalid, using defaults.");
			ice.printStackTrace();
		}
		// Error in config, return a default world
		return new VacWorld(defaultSize);
	}

	/**
	 * create a vacuum world from a configuration.
	 * 
	 * @param configuration
	 * @return
	 * @throws InvalidConfigurationException
	 *             If the properties violate the requireXXX expectations.
	 */
	public static VacWorld createVacWorld(Configuration configuration) {
		configuration.requireString(configLevel);
		configuration.requireBoolean(configRegeneration);
		try {
			configuration.check();
		} catch (InvalidConfigurationException e1) {
			System.out.println("Invalid configuration " + configuration
					+ ", using defaults.");
			e1.printStackTrace();
			return new VacWorld(defaultSize);
		}

		VacWorld world = null;
		String level = configuration.getProperty(configLevel);

		try {
			// If level is an integer 1-8, create a random world of that
			// size
			int levelSize = Integer.parseInt(level.trim());
			if (levelSize < 1) {
				System.out.println("Warning: minimum level size is 1 VacBot!");
				world = new VacWorld(1);
			} else if (levelSize > 8) {
				System.out.println("Warning: maximum level size is 8 VacBots!");
				world = new VacWorld(8);
			} else {
				world = new VacWorld(levelSize);
			}
		} catch (NumberFormatException nfe) {
			// Try to read level as a file -
			try {
				world = new VacWorld(new FileInputStream(
						configuration.findFile(level)));
				// if this fails, create a default
				// random level
			} catch (FileNotFoundException fnfe) {
				System.out.println("Level file \"" + level
						+ "\" not found, generating random level.");
				world = new VacWorld(defaultSize);
			} catch (IOException ioe) {
				System.out.println("Error while reading \"" + level
						+ "\", generating random level.");
				ioe.printStackTrace();
				world = new VacWorld(defaultSize);
			} catch (InvalidVacWorldException ivwe) {
				System.out.println("Level \"" + level
						+ "\" is invalid, generating random level.");
				ivwe.printStackTrace();
				world = new VacWorld(defaultSize);
			}
		}
		// Apply regenerating dust if configured
		try {
			if (configuration.getPropertyAsBoolean(configRegeneration)) {
				world.setRegeneratingDust();
			}
		} catch (PropertyTypeException e) {
		} // Can't happen
		return world;
	}
}
