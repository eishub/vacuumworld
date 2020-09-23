package grid;

import java.util.Iterator;

/**
 * Iterates through a grid in raster scan fashion, i.e. (0,0) -> (n,0) -> (0,1),
 * etc. Returns every possible Square in turn.
 */
public class GridSquareIterator extends GridIterator implements Iterator<Square> {
	public GridSquareIterator(final Square[][] array) {
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
	public Square next() {
		final Square result = this.array[this.x][this.y];
		step();
		return result;
	}
}
