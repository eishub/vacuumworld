package grid;

public abstract class GridIterator {
	protected int x;
	protected int y;
	protected boolean done;
	protected Square[][] array;

	public GridIterator(final Square[][] array) {
		this.array = array;
	}

	/**
	 * Allows an Iterator instance to be reused, GoF style. Sets the internal x and
	 * y coordinates to 0, and the 'done' flag to false.
	 */
	public void reset() {
		this.x = 0;
		this.y = 0;
		this.done = false;
	}

	protected void step() {
		this.x++;
		// We use array[x][y] and assume that the grid is rectangular
		if (this.x == this.array.length) {
			// Wrap around
			this.x = 0;
			this.y++;
			if (this.y == this.array[0].length) {
				// Reached end of grid
				this.done = true;
			}
		}
	}

	public void remove() {
		throw new UnsupportedOperationException();
	}
}
