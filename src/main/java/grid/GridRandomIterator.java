package grid;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class GridRandomIterator extends GridIterator implements Iterator<Square> {

	private List<Square> list = new LinkedList<Square>();
	
	public GridRandomIterator(Square[][] array) {
		super(array);
		reset();
	}

	/**
	 * Allows an Iterator instance to be reused, GoF style.
	 */
	public void reset() {
		// Call superclass method to reset x and y to 0, and 'done' to false
		super.reset();
		// Copy and 'flatten' the 2D array into a one-dimensional list
		list.clear();
		while(!this.done) {
			list.add(array[x][y]);
			this.step();
		}
	}
	
	public boolean hasNext() {
		if (list.isEmpty()) return false;
		else return true;
	}

	public Square next() {
		int randomIndex = (int)Math.floor(Math.random() * list.size());
		Square result = list.get(randomIndex);
		list.remove(randomIndex);
		return result;
	}

}
