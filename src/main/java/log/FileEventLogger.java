package log;

import grid.GridObject;
import grid.ModelObject;

public class FileEventLogger extends EventLogger {

	public FileEventLogger(GridObject gridObject, String fileName) {
		super(gridObject);
		// TODO: open the file and initialise stream
	}

	@Override
	protected String formatEventSource(ModelObject source) {
		// No need to show the source, this would be the same on each line
		return null;
	}

}
