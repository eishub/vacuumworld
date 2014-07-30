package util.config;

public class PropertyTypeException extends InvalidConfigurationException {

	private static final long serialVersionUID = 3109006934868498668L;

	public PropertyTypeException(String propertyName, String type) {
		super("Configuration property \"" + propertyName + "\" must be of type \"" + type + "\".");  
	}
}
