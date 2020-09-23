package util.config;

public class PropertyTypeException extends InvalidConfigurationException {
	private static final long serialVersionUID = 3109006934868498668L;

	public PropertyTypeException(final String propertyName, final String type) {
		super("Configuration property \"" + propertyName + "\" must be of type \"" + type + "\".");
	}
}
