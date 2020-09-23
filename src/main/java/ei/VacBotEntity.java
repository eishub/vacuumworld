package ei;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import actions.Action;
import actions.Move;
import actions.Turn;
import eis.IEISEntity;
import eis.PerceptUpdate;
import eis.iilang.Identifier;
import eis.iilang.Numeral;
import eis.iilang.Percept;
import grid.GridObject;
import grid.Square;
import vac.Clean;
import vac.Dust;
import vac.PerceptSquare;
import vac.VacBot;

public class VacBotEntity implements IEISEntity {
	public static final String PERCEPT_LIGHT = "light";
	public static final String PERCEPT_TASK = "task";
	public static final String PERCEPT_LOCATION = "location";
	public static final String PERCEPT_DIRECTION = "direction";
	public static final String PERCEPT_SQUARE = "square";

	public static final String STATE_ON = "on";
	public static final String STATE_OFF = "off";
	public static final String TASK_TURN = "turn";
	public static final String TASK_MOVE = "move";
	public static final String TASK_CLEAN = "clean";
	public static final String TASK_NONE = "none";
	public static final String ITEM_OBSTACLE = "obstacle";
	public static final String ITEM_VAC = "vac";
	public static final String ITEM_DUST = "dust";
	public static final String ITEM_EMPTY = "empty";

	private static final Identifier stateOn = new Identifier(STATE_ON);
	private static final Identifier stateOff = new Identifier(STATE_OFF);
	private static final Identifier taskTurn = new Identifier(TASK_TURN);
	private static final Identifier taskMove = new Identifier(TASK_MOVE);
	private static final Identifier taskClean = new Identifier(TASK_CLEAN);
	private static final Identifier taskNone = new Identifier(TASK_NONE);
	private static final Identifier itemObstacle = new Identifier(ITEM_OBSTACLE);
	private static final Identifier itemVac = new Identifier(ITEM_VAC);
	private static final Identifier itemDust = new Identifier(ITEM_DUST);
	private static final Identifier itemEmpty = new Identifier(ITEM_EMPTY);

	public final VacBot bot;
	private Identifier previousLight, previousTask, previousDirection;
	private List<Numeral> previousLocation;
	private List<Percept> previousSquares = new ArrayList<>(0);

	public VacBotEntity(final VacBot bot) {
		this.bot = bot;
	}

	@Override
	public String getName() {
		return this.bot.getName();
	}

	@Override
	public String getType() {
		return "vacbot";
	}

	@Override
	public PerceptUpdate perceive() {
		final List<Percept> addList = new LinkedList<>();
		final List<Percept> delList = new LinkedList<>();

		final Identifier light = getLight();
		if (this.previousLight == null) {
			addList.add(new Percept(PERCEPT_LIGHT, light));
			this.previousLight = light;
		} else if (!this.previousLight.equals(light)) {
			delList.add(new Percept(PERCEPT_LIGHT, this.previousLight));
			addList.add(new Percept(PERCEPT_LIGHT, light));
			this.previousLight = light;
		}

		final Identifier task = getTask();
		if (this.previousTask == null) {
			addList.add(new Percept(PERCEPT_TASK, task));
			this.previousTask = task;
		} else if (!this.previousTask.equals(task)) {
			delList.add(new Percept(PERCEPT_TASK, this.previousTask));
			addList.add(new Percept(PERCEPT_TASK, task));
			this.previousTask = task;
		}

		if (!task.equals(taskMove)) {
			final List<Numeral> location = getLocation();
			if (this.previousLocation == null) {
				addList.add(new Percept(PERCEPT_LOCATION, location.get(0), location.get(1)));
				this.previousLocation = location;
			} else if (!this.previousLocation.equals(location)) {
				delList.add(new Percept(PERCEPT_LOCATION, this.previousLocation.get(0), this.previousLocation.get(1)));
				addList.add(new Percept(PERCEPT_LOCATION, location.get(0), location.get(1)));
				this.previousLocation = location;
			}
		} else if (this.previousLocation != null) {
			delList.add(new Percept(PERCEPT_LOCATION, this.previousLocation.get(0), this.previousLocation.get(1)));
			this.previousLocation = null;
		}

		if (!task.equals(taskTurn) && !task.equals(taskClean)) {
			final Identifier direction = getDirection();
			if (this.previousDirection == null) {
				addList.add(new Percept(PERCEPT_DIRECTION, direction));
				this.previousDirection = direction;
			} else if (!this.previousDirection.equals(direction)) {
				delList.add(new Percept(PERCEPT_DIRECTION, this.previousDirection));
				addList.add(new Percept(PERCEPT_DIRECTION, direction));
				this.previousDirection = direction;
			}
		} else if (this.previousDirection != null) {
			delList.add(new Percept(PERCEPT_DIRECTION, this.previousDirection));
			this.previousDirection = null;
		}

		final List<Percept> squares = new LinkedList<>();
		if (task.equals(taskNone)) {
			// Status of the six squares the VacBot can see
			for (final PerceptSquare perceptSquare : this.bot.getFieldOfVision()) {
				final Square square = perceptSquare.getSquare();
				final Identifier squareName = new Identifier(perceptSquare.getRelativeDirection().getName());
				// Question: how to represent a square that contains both a VacBot and a Dust?
				// Options: two single-parameter percepts; one single-parameter percept; a
				// ParameterList?
				// Tristan & Rem agree that list processing is awkward in APLs and causes poor
				// performance.
				// For now, we use one single-parameter percept, and assume that a perceiving
				// VacBot cannot 'see' whether a square is clean or dusty, if there is already
				// another VacBot on it.
				if (square == null || perceptSquare.getSquare().has(GridObject.class)) {
					squares.add(new Percept(PERCEPT_SQUARE, squareName, itemObstacle));
				} else if (square.has(VacBot.class) && !square.get(VacBot.class).equals(this.bot)) {
					squares.add(new Percept(PERCEPT_SQUARE, squareName, itemVac));
				} else if (perceptSquare.getSquare().has(Dust.class)) {
					squares.add(new Percept(PERCEPT_SQUARE, squareName, itemDust));
				} else {
					squares.add(new Percept(PERCEPT_SQUARE, squareName, itemEmpty));
				}
			}
		}
		addList.addAll(squares);
		addList.removeAll(this.previousSquares);
		delList.addAll(this.previousSquares);
		delList.removeAll(squares);
		this.previousSquares = squares;

		return new PerceptUpdate(addList, delList);
	}

	private Identifier getLight() {
		return this.bot.isLightOn() ? stateOn : stateOff;
	}

	private Identifier getTask() {
		final Action action = this.bot.getAction();
		if (action != null) {
			final Class<?> actionClass = action.getClass();
			if (actionClass.equals(Turn.class)) {
				return taskTurn;
			} else if (actionClass.equals(Move.class)) {
				return taskMove;
			} else if (actionClass.equals(Clean.class)) {
				return taskClean;
			}
		}
		return taskNone;
	}

	private List<Numeral> getLocation() {
		// Rounded to nearest integer location - move may start while percept list is
		// being generated.
		final List<Numeral> location = new ArrayList<>(2);
		location.add(new Numeral(Math.round(this.bot.getX())));
		location.add(new Numeral(Math.round(this.bot.getY())));
		return location;
	}

	private Identifier getDirection() {
		// Rounded to n/s/e/w - turn may start while percept list is being generated.
		return new Identifier(this.bot.getDirection().round().toName());
	}
}
