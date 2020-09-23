package util.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class Configuration extends Properties {
	private static final long serialVersionUID = 3162894778833140364L;
	private final List<String> requiredStringProperties = new LinkedList<>();
	private final List<String> requiredStringArrayProperties = new LinkedList<>();
	private final Map<String, Integer> stringArrayMinimumElements = new HashMap<>();
	private final List<String> requiredBooleanProperties = new LinkedList<>();

	/**
	 * Set the given string property as 'required'. If the property is not present
	 * when check() is called, an EmptyPropertyException is thrown.
	 *
	 * @param propertyName name of the property that must be present in the
	 *                     configuration file.
	 */
	public void requireString(final String propertyName) {
		checkPropertyNameNotEmpty(propertyName);
		this.requiredStringProperties.add(propertyName);
	}

	/**
	 * Sets the given string array property as 'required'. If the property is not
	 * present when check() is called, an EmptyPropertyException is thrown.
	 *
	 * @param propertyName    the name of the property that must be present in the
	 *                        configuration file.
	 * @param minimumElements array must have at least this number of elements.
	 */
	public void requireStringArray(final String propertyName, final int minimumElements) {
		if (minimumElements < 1) {
			throw new IllegalArgumentException("Required minimum number of elements must be at least 1.");
		}
		checkPropertyNameNotEmpty(propertyName);
		this.requiredStringArrayProperties.add(propertyName);
		this.stringArrayMinimumElements.put(propertyName, minimumElements);
	}

	public void requireBoolean(final String propertyName) {
		checkPropertyNameNotEmpty(propertyName);
		this.requiredBooleanProperties.add(propertyName);
	}

	private void checkPropertyNameNotEmpty(final String propertyName) {
		if (propertyName == null || propertyName.isEmpty()) {
			throw new IllegalArgumentException("Cannot require a property with an empty name!");
		}
	}

	/**
	 * Calls getProperty(key) from java.util.Properties, and splits the result into
	 * a string array. The separation sequence can be any combination of space,
	 * comma, semicolon, and colon.
	 *
	 * @param key the property key.
	 * @return the given property as a string array, or null if it was not found.
	 */
	public String[] getPropertyAsStringArray(final String key) {
		final String value = this.getProperty(key);
		if (value == null || value.isEmpty()) {
			return null;
		} else {
			return value.split("[ ,;:]+");
		}
	}

	public boolean getPropertyAsBoolean(final String key) throws PropertyTypeException {
		final String value = this.getProperty(key);
		if (value == null || value.isEmpty()) {
			throw new PropertyTypeException(key, "boolean");
		} else {
			if (value.equalsIgnoreCase("yes") || value.equalsIgnoreCase("true")) {
				return true;
			} else if (value.equalsIgnoreCase("no") || value.equalsIgnoreCase("false")) {
				return false;
			} else {
				throw new PropertyTypeException(key, "boolean");
			}
		}
	}

	/**
	 * Convenience method which loads the properties.
	 *
	 * @param inputStream InputStream to read the properties from.
	 * @throws IOException                   If an IO error occurs while loading the
	 *                                       properties from the stream.
	 * @throws InvalidConfigurationException
	 */
	public void load(final String filename) throws IOException, InvalidConfigurationException {
		InputStream inputStream = null;
		try {
			final File configfile = findFile(filename);
			inputStream = new FileInputStream(configfile);
			load(inputStream);
			System.out.println("loaded configuration from " + configfile);
		} finally {
			if (inputStream != null) {
				inputStream.close();
			}
		}
	}

	/**
	 * Find a file with given filename in directory where our jar is.
	 *
	 * @param filename filename, absolute or relative to path where our jar is.
	 * @throws FileNotFoundException if file not found.
	 */
	public File findFile(final String filename) throws FileNotFoundException {
		final URL url = getClass().getProtectionDomain().getCodeSource().getLocation();
		final Path p = Paths.get(url.getFile());
		final File file = p.getParent().resolve(filename).toFile();
		if (!file.exists()) {
			throw new FileNotFoundException("vacuum env can't open " + file);
		}
		return file;
	}

	/**
	 * Checks that all the required properties are present and of the correct type.
	 *
	 * @throws EmptyPropertyException        if an empty property is found.
	 * @throws NotEnoughElementsException    if an array property does not have at
	 *                                       least the minimum number of elements.
	 * @throws InvalidConfigurationException * If the properties violate the
	 *                                       requireXXX expectations.
	 */
	public void check() throws InvalidConfigurationException {
		checkPropertiesNotEmpty(this.requiredStringProperties);
		checkPropertiesNotEmpty(this.requiredStringArrayProperties);
		checkStringArrayElements();
		checkPropertiesNotEmpty(this.requiredBooleanProperties);
		checkValuesBoolean(this.requiredBooleanProperties);
	}

	/**
	 * Checks that the property values corresponding to the given list of property
	 * names are not empty.
	 *
	 * @param propertyNames a list of property names to look up and check.
	 * @throws EmptyPropertyException if an empty property is found.
	 */
	private void checkPropertiesNotEmpty(final List<String> propertyNames) throws EmptyPropertyException {
		for (final String propertyName : propertyNames) {
			final String value = this.getProperty(propertyName);
			if (value == null || value.isEmpty()) {
				throw new EmptyPropertyException(propertyName);
			}
		}
	}

	private void checkStringArrayElements() throws NotEnoughElementsException {
		for (final String propertyName : this.stringArrayMinimumElements.keySet()) {
			final String[] stringArray = getPropertyAsStringArray(propertyName);
			final int minimumLength = this.stringArrayMinimumElements.get(propertyName);
			if (stringArray.length < minimumLength) {
				throw new NotEnoughElementsException(propertyName, minimumLength);
			}
		}
	}

	private void checkValuesBoolean(final List<String> propertyNames) throws PropertyTypeException {
		for (final String propertyName : propertyNames) {
			final String value = this.getProperty(propertyName);
			if (!(value.equalsIgnoreCase("yes") || value.equalsIgnoreCase("no") || value.equalsIgnoreCase("true")
					|| value.equalsIgnoreCase("false"))) {
				throw new PropertyTypeException(propertyName, "boolean");
			}
		}
	}
}
