package log;

import java.io.PrintStream;

import grid.GridObject;
import grid.ModelObject;

public class StreamEventLogger extends EventLogger {

	public StreamEventLogger(GridObject gridObject, PrintStream stream) {
		super(gridObject);
		this.stream = stream;
	}

	@Override
	protected String formatEventSource(ModelObject source) {
		StringBuffer sb = new StringBuffer();
		sb.append(source.toString());
		sb.append(": ");
		return sb.toString();
	}

}
