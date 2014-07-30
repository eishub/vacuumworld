package grid;

public abstract class GridIterator {

	protected int x;
	protected int y;
	protected boolean done;
	protected Square[][] array;

	public GridIterator(Square[][] array) {
		this.array = array;
	}
	
	/**
	 * Allows an Iterator instance to be reused, GoF style.
	 * Sets the internal x and y coordinates to 0, and the 'done' flag to false.
	 */
	public void reset() {
		x = 0;
		y = 0;
		done = false;
	}

	protected void step() {
		x++;
		// We use array[x][y] and assume that the grid is rectangular 
		if (x == array.length) {
			// Wrap around
			x = 0;
			y++;
			if (y == array[0].length) {
				// Reached end of grid
				done = true;
			}
		}
	}
	
	public void remove() {
		throw new UnsupportedOperationException();
	}

}
