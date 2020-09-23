package actions;

public class UnavailableActionException extends Exception {
	private static final long serialVersionUID = 1L;

	public UnavailableActionException(final String message) {
		super(message);
	}
}
