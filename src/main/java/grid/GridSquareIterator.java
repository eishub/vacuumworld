package grid;

import java.util.Iterator;

/**
 * Iterates through a grid in raster scan fashion, i.e. (0,0) -> (n,0) -> (0,1),
 * etc. Returns every possible Square in turn.
 */
public class GridSquareIterator extends GridIterator implements
		Iterator<Square> {

	public GridSquareIterator(Square[][] array) {
		super(array);
		this.reset();
	}

	public boolean hasNext() {
		if (this.done)
			return false;
		else
			return true;
	}

	public Square next() {
		Square result = array[this.x][this.y];
		this.step();
		return result;
	}

}
