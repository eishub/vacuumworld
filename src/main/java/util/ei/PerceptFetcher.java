package util.ei;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;

import eis.EnvironmentInterfaceStandard;
import eis.exceptions.NoEnvironmentException;
import eis.exceptions.PerceiveException;
import eis.iilang.Percept;

/**
 * Convenience methods to read data out of percept lists in EIS environments.
 * When used as a runnable, sleeps for a pre-determined time, then takes a
 * snapshot of the environment percepts which can be queried later.
 */
public class PerceptFetcher implements Runnable {

	private LinkedList<Percept> storedPercepts = new LinkedList<Percept>();
	private final EnvironmentInterfaceStandard ei;
	private String testAgent;
	private long sleepTime = 100;

	public PerceptFetcher(EnvironmentInterfaceStandard eis) {
		if (eis == null)
			throw new IllegalArgumentException(
					"Environment interface cannot be null!");
		this.ei = eis;
	}

	public void setTestAgent(String testAgent) {
		if (testAgent == null)
			throw new IllegalArgumentException("Test agent cannot be null!");
		this.testAgent = testAgent;
	}

	public void setSleepTime(long sleepTime) {
		this.sleepTime = sleepTime;
	}

	public void run() {
		try {
			Thread.sleep(sleepTime);
			storedPercepts = getFreshPercepts();
		} catch (InterruptedException | PerceiveException
				| NoEnvironmentException e) {
			e.printStackTrace();
		}
	}

	private Percept getPerceptFrom(String perceptName,
			LinkedList<Percept> percepts) {
		for (Percept percept : percepts) {
			if (percept.getName().equals(perceptName))
				return percept;
		}
		return null;
	}

	public String getFirstParameterFrom(Percept percept) {
		return percept.getParameters().getFirst().toProlog();
	}

	public String getSecondParameterFrom(Percept percept) {
		return percept.getParameters().get(1).toProlog();
	}

	private String getSingleParamPerceptFrom(String perceptName,
			LinkedList<Percept> percepts) {
		return getFirstParameterFrom(getPerceptFrom(perceptName, percepts));
	}

	private LinkedList<Percept> getPerceptsFrom(String perceptName,
			LinkedList<Percept> percepts) {
		LinkedList<Percept> results = new LinkedList<Percept>();
		for (Percept percept : percepts) {
			if (percept.getName().equals(perceptName))
				results.add(percept);
		}
		return results;
	}

	public LinkedList<Percept> getStoredPercepts() {
		return storedPercepts;
	}

	public LinkedList<Percept> getFreshPercepts() throws PerceiveException,
			NoEnvironmentException {
		LinkedList<Percept> result = new LinkedList<Percept>();
		Map<String, Collection<Percept>> map = ei.getAllPercepts(testAgent);
		// Apparently, the keys for this map are the entities associated with
		// testAgent
		for (String key : map.keySet()) {
			Collection<Percept> percepts = map.get(key);
			for (Percept percept : percepts) {
				result.add(percept);
			}
		}
		return result;
	}

	public Percept getStoredPercept(String perceptName) {
		return getPerceptFrom(perceptName, storedPercepts);
	}

	public Percept getFreshPercept(String perceptName)
			throws PerceiveException, NoEnvironmentException {
		return getPerceptFrom(perceptName, this.getFreshPercepts());
	}

	public LinkedList<Percept> getStoredPercepts(String perceptName) {
		return getPerceptsFrom(perceptName, storedPercepts);
	}

	public LinkedList<Percept> getFreshPercepts(String perceptName)
			throws PerceiveException, NoEnvironmentException {
		return getPerceptsFrom(perceptName, this.getFreshPercepts());
	}

	public String getStoredSingleParamPercept(String perceptName) {
		return getSingleParamPerceptFrom(perceptName, storedPercepts);
	}

	public String getFreshSingleParamPercept(String perceptName)
			throws PerceiveException, NoEnvironmentException {
		return getSingleParamPerceptFrom(perceptName, this.getFreshPercepts());
	}

}
