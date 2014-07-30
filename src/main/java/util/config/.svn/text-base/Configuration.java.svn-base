package util.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class Configuration extends Properties {

	private static final long serialVersionUID = 3162894778833140364L;
	private List<String> requiredStringProperties = new LinkedList<String>();
	private List<String> requiredStringArrayProperties = new LinkedList<String>();
	private Map<String, Integer> stringArrayMinimumElements = new HashMap<String, Integer>();
	private List<String> requiredBooleanProperties = new LinkedList<String>();
	
	/**
	 * Set the given string property as 'required'.
	 * If the property is not present when check() is called, an EmptyPropertyException is thrown.
	 * @param propertyName name of the property that must be present in the configuration file.
	 */
	public void requireString(String propertyName) {
		checkPropertyNameNotEmpty(propertyName);
		requiredStringProperties.add(propertyName);
	}
	
	/**
	 * Sets the given string array property as 'required'.
	 * If the property is not present when check() is called, an EmptyPropertyException is thrown.
	 * @param propertyName the name of the property that must be present in the configuration file.
	 * @param minimumElements array must have at least this number of elements.
	 */
	public void requireStringArray(String propertyName, int minimumElements) {
		if (minimumElements < 1)
			throw new IllegalArgumentException("Required minimum number of elements must be at least 1.");
		checkPropertyNameNotEmpty(propertyName);
		requiredStringArrayProperties.add(propertyName);
		stringArrayMinimumElements.put(propertyName, minimumElements);
	}
	
	public void requireBoolean(String propertyName) {
		checkPropertyNameNotEmpty(propertyName);
		requiredBooleanProperties.add(propertyName);
	}
	
	private void checkPropertyNameNotEmpty(String propertyName) {
		if (propertyName == null || propertyName.isEmpty())
			throw new IllegalArgumentException("Cannot require a property with an empty name!");
	}
	
	/**
	 * Calls getProperty(key) from java.util.Properties, and splits the result into a string array.
	 * The separation sequence can be any combination of space, comma, semicolon, and colon.
	 * @param key the property key.
	 * @return the given property as a string array, or null if it was not found.
	 */
	public String[] getPropertyAsStringArray(String key) {
		String value = this.getProperty(key);
		if (value == null || value.isEmpty())
			return null;
		else
			return value.split("[ ,;:]+");
	}
	
	public boolean getPropertyAsBoolean(String key) throws PropertyTypeException {
		String value = this.getProperty(key);
		if (value == null || value.isEmpty())
			throw new PropertyTypeException(key, "boolean");
		else {
			if (value.equalsIgnoreCase("yes") || value.equalsIgnoreCase("true")) return true;
			else if (value.equalsIgnoreCase("no") || value.equalsIgnoreCase("false")) return false;
			else throw new PropertyTypeException(key, "boolean");
		}
	}
	
	/**
	 * Convenience method which loads the properties, closes the input stream, and calls check().
	 * Ignores the (highly unlikely) IOException thrown by InputStream.close().
	 * @param inputStream InputStream to read the properties from.
	 * @throws IOException If an IO error occurs while loading the properties from the stream.
	 * @throws InvalidConfigurationException If the properties violate the requireXXX expectations.
	 */
	public void loadCloseAndCheck(InputStream inputStream) throws IOException, InvalidConfigurationException {
		try {
			this.load(inputStream);
		} finally {
			try {
				inputStream.close();
			} catch (IOException ignored) {
				// If this happens, inputStream is most likely a FileInputStream,
				// and your hard disk is on fire. You probably already noticed.
			}
		}
		this.check();
	}
	
	/**
	 * Checks that all the required properties are present and of the correct type.
	 * @throws EmptyPropertyException if an empty property is found.
	 * @throws NotEnoughElementsException if an array property does not have at least the minimum number of elements.
	 */
	public void check() throws InvalidConfigurationException {
		checkPropertiesNotEmpty(requiredStringProperties);
		checkPropertiesNotEmpty(requiredStringArrayProperties);
		checkStringArrayElements();
		checkPropertiesNotEmpty(requiredBooleanProperties);
		checkValuesBoolean(requiredBooleanProperties);
	}
	
	/**
	 * Checks that the property values corresponding to the given list of property names are not empty.
	 * @param propertyNames a list of property names to look up and check.
	 * @throws EmptyPropertyException if an empty property is found.
	 */
	private void checkPropertiesNotEmpty(List<String> propertyNames) throws EmptyPropertyException {
		for (String propertyName : propertyNames) {
			String value = this.getProperty(propertyName);
			if (value == null || value.isEmpty())
				throw new EmptyPropertyException(propertyName);
		}
	}
	
	private void checkStringArrayElements() throws NotEnoughElementsException {
		for (String propertyName : stringArrayMinimumElements.keySet()) {
			String[] stringArray = getPropertyAsStringArray(propertyName);
			int minimumLength = stringArrayMinimumElements.get(propertyName);
			if (stringArray.length < minimumLength)
				throw new NotEnoughElementsException(propertyName, minimumLength);
		}
	}
	
	private void checkValuesBoolean(List<String> propertyNames) throws PropertyTypeException {
		for (String propertyName : propertyNames) {
			String value = this.getProperty(propertyName);
			if (!(value.equalsIgnoreCase("yes") || value.equalsIgnoreCase("no")
					|| value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")))
				throw new PropertyTypeException(propertyName, "boolean");
		}
	}
}
