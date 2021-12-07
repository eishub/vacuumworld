package actions;

import grid.ModelObject;

public abstract class Action {
	public static final String START_EVENT = "start";
	public static final String STEP_EVENT = "step";
	public static final String STOP_EVENT = "stop";
	public static final String TIME_EVENT = "time";
	private final String actionType = getClass().getName();
	private final String startEvent = this.actionType + "." + START_EVENT;
	private final String stepEvent = this.actionType + "." + STEP_EVENT;
	private final String stopEvent = this.actionType + "." + STOP_EVENT;
	protected final ModelObject modelObject;
	protected final int speedFactor;
	protected int currentStep = 0;

	protected Action(final ModelObject modelObject, final int speedFactor) {
		this.modelObject = modelObject;
		this.speedFactor = speedFactor;
	}

	public void execute() throws InterruptedException, ImpossibleActionException, UnavailableActionException {
		this.currentStep = 0;
		synchronized (this.modelObject) {
			if (this.modelObject.getAction() == null) {
				this.modelObject.setAction(this);
			} else {
				throw new UnavailableActionException("Cannot perform action simultaneously!");
			}
		}
		try {
			// Initialise can throw Impossible or Unavailable exceptions but if
			// so, always cleans up after itself
			initialise();
			this.modelObject.fireEvent(this.startEvent, this.modelObject);
			while (this.currentStep < numberOfSteps()) {
				++this.currentStep;
				waitOneStep();
				executeOneStep();
				if (this.currentStep < numberOfSteps()) {
					this.modelObject.fireEvent(this.stepEvent, this.modelObject);
				}
			}
			finalise();
		} finally {
			this.modelObject.setAction(null);
		}
		this.modelObject.fireEvent(this.stopEvent, this.modelObject);
	}

	private void waitOneStep() throws InterruptedException {
		final long sleepTime = duration() / numberOfSteps();
		if (sleepTime > 0) {
			Thread.sleep(Math.round(sleepTime * (100.0 / this.speedFactor)));
		}
	}

	/**
	 * The intended duration of this particular action, may vary between instances.
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
	 *
	 * @throws ImpossibleActionException
	 * @throws UnavailableActionException
	 */
	protected abstract void initialise() throws ImpossibleActionException, UnavailableActionException;

	/**
	 * Work to be performed at the end of each step of the action, such as updating
	 * the model. For all action steps except the last, a step event is fired
	 * immediately after this method returns.
	 */
	protected abstract void executeOneStep();

	/**
	 * Work to be performed after the last action step has completed, before the end
	 * event is fired. Can be empty if no special cleanup is needed.
	 */
	protected abstract void finalise();
}
