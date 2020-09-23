package grid;

import java.util.Iterator;

/**
 * Iterates through a grid in raster scan fashion, i.e. (0,0) -> (n,0) -> (0,1),
 * etc. Returns every possible GridPoint in turn.
 */
public class GridPointIterator extends GridIterator implements Iterator<GridPoint> {
	public GridPointIterator(final Square[][] array) {
		super(array);
		reset();
	}

	@Override
	public boolean hasNext() {
		if (this.done) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public GridPoint next() {
		final GridPoint result = new GridPoint(this.x, this.y);
		step();
		return result;
	}
}
