package actions;

import grid.ModelObject;

public abstract class Action {

	public static final String START_EVENT = "start";
	public static final String STEP_EVENT = "step";
	public static final String STOP_EVENT = "stop";
	public static final String TIME_EVENT = "time";
	private final String actionType = getClass().getName();
	private final String startEvent = actionType + "." + START_EVENT;
	private final String stepEvent = actionType + "." + STEP_EVENT;
	private final String stopEvent = actionType + "." + STOP_EVENT;
	protected final ModelObject modelObject;
	protected int currentStep = 0;
	private long startedAt;

	protected Action(ModelObject modelObject) {
		this.modelObject = modelObject;
	}

	public void execute() throws InterruptedException,
			ImpossibleActionException, UnavailableActionException {
		currentStep = 0;
		synchronized (modelObject) {
			if (modelObject.getAction() == null) {
				modelObject.setAction(this);
			} else {
				throw new UnavailableActionException(
						"Cannot perform action simultaneously!");
			}
		}
		try {
			// Initialise can throw Impossible or Unavailable exceptions but if
			// so, always cleans up after itself
			initialise();
			modelObject.fireEvent(startEvent, modelObject);
			startedAt = System.currentTimeMillis();
			while (currentStep < numberOfSteps()) {
				++currentStep;
				waitOneStep();
				executeOneStep();
				if (currentStep < numberOfSteps())
					modelObject.fireEvent(stepEvent, modelObject);
			}
			finalise();
		} finally {
			modelObject.setAction(null);
		}
		modelObject.fireEvent(stopEvent, modelObject);
	}

	private void waitOneStep() throws InterruptedException {
		long nextStepTime = startedAt
				+ (currentStep * duration() / numberOfSteps());
		long sleepTime = nextStepTime - System.currentTimeMillis();
		if (sleepTime > 0) {
			Thread.sleep(sleepTime);
		}
	}

	/**
	 * The intended duration of this particular action, may vary between
	 * instances.
	 * 
	 * @return Time in milliseconds that this action will take.
	 */
	protected abstract long duration();

	/**
	 * The intended number of steps for this action, may vary between instances.
	 * 
	 * @return The intended step count for this action.
	 */
	protected abstract int numberOfSteps();

	/**
	 * Work to be performed immediately when the action is executed, before the
	 * start event is fired. Can be empty if no special setup is needed. If the
	 * action is impossible or unavailable, implementations MUST clean up before
	 * throwing an exception.
	 */
	protected abstract void initialise() throws ImpossibleActionException,
			UnavailableActionException;

	/**
	 * Work to be performed at the end of each step of the action, such as
	 * updating the model. For all action steps except the last, a step event is
	 * fired immediately after this method returns.
	 */
	protected abstract void executeOneStep();

	/**
	 * Work to be performed after the last action step has completed, before the
	 * end event is fired. Can be empty if no special cleanup is needed.
	 */
	protected abstract void finalise();
}
