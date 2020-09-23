package util.config;

public class EmptyPropertyException extends InvalidConfigurationException {
	private static final long serialVersionUID = -276014139653222695L;

	public EmptyPropertyException(final String propertyName) {
		super("Required configuration property \"" + propertyName + "\" must not be empty.");
	}
}
