package actions;

public class ImpossibleActionException extends Exception {
	private static final long serialVersionUID = 1L;

	public ImpossibleActionException(final String message) {
		super(message);
	}
}
