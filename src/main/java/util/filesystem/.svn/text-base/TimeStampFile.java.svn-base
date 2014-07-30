/**
 * 
 */
package util.filesystem;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class TimeStampFile extends File {

	private static final long serialVersionUID = 5071372643007574140L;
	private final String originalName;
	
	public TimeStampFile(String fileName) {
		super(fileName);
		this.originalName = fileName;
	}
	
	/**
	 * Outputs the current computer time, in milliseconds, as a file.
	 * @throws IOException if an error occurs while writing to file.
	 */
	public void create() throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(this));
		writer.write("" + System.currentTimeMillis());
		writer.close();
	}

	/**
	 * Get the stored timestamp value from this file.
	 * @return the computer time, in milliseconds, when this timestamp was created, or 0 if an error occured.
	 */
	public long getValue() {
		BufferedReader reader;
		long timeStamp = 0;
		try {
			reader = new BufferedReader(new FileReader(this));
			timeStamp = Long.parseLong(reader.readLine());
			reader.close();
			return timeStamp;
		} catch (FileNotFoundException e) {
			System.out.println("Could not find timestamp file \"" + originalName + "\".");
			e.printStackTrace();
		} catch (NumberFormatException e) {
			System.out.println("Contents of timestamp file \"" + originalName + "\" corrupted!");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("IOException while reading timestamp file \"" + originalName + "\".");
			e.printStackTrace();
		}
		return timeStamp;
	}
}