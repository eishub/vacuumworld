package util.config;

public class NotEnoughElementsException extends InvalidConfigurationException {
	private static final long serialVersionUID = -6674656634306724780L;

	public NotEnoughElementsException(final String propertyName, final int minimum) {
		super("List property \"" + propertyName + "\" must have at least " + minimum + " element(s).");
	}
}
