package grid;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Iterates through a grid in raster scan fashion, i.e. (0,0) -> (n,0) -> (0,1), etc.
 * Returns only the squares that are not empty.
 */
public class GridScanIterator extends GridIterator implements Iterator<Square> {

	public GridScanIterator(Square[][] array) {
		super(array);
		reset();
	}
	
	public boolean hasNext() {
		// Advance the pointer to the next non-null, non-empty square
		walk();
		if (this.done) return false;
		else return true;
	}

	public Square next() {
		// Advance the pointer 0 or more steps
		walk();
		if (this.done) throw new NoSuchElementException();
		// x, y should now be pointing at a non-null, non-empty square
		Square result = array[this.x][this.y];
		// Step forward one, so subsequent next() call does not return the same square
		step();
		return result;
	}
	
	// Advance the pointer 0 or more steps, until a non-null, non-empty square is found
	private void walk() {
		while (!this.done && (array[this.x][this.y] == null || array[this.x][this.y].getCount() == 0)) {
			step();
		}
	}

}
