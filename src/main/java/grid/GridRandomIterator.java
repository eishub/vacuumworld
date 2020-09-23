package grid;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class GridRandomIterator extends GridIterator implements Iterator<Square> {
	private final List<Square> list = new LinkedList<>();

	public GridRandomIterator(final Square[][] array) {
		super(array);
		reset();
	}

	/**
	 * Allows an Iterator instance to be reused, GoF style.
	 */
	@Override
	public void reset() {
		// Call superclass method to reset x and y to 0, and 'done' to false
		super.reset();
		// Copy and 'flatten' the 2D array into a one-dimensional list
		this.list.clear();
		while (!this.done) {
			this.list.add(this.array[this.x][this.y]);
			step();
		}
	}

	@Override
	public boolean hasNext() {
		if (this.list.isEmpty()) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public Square next() {
		final int randomIndex = (int) Math.floor(Math.random() * this.list.size());
		final Square result = this.list.get(randomIndex);
		this.list.remove(randomIndex);
		return result;
	}
}
