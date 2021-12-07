package grid;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Holds the contents and location of a single square in the grid.
 */
public class Square {
	// Maintain a thread-safe stack of GridObjects
	private final List<GridObject> gridObjects = new LinkedList<>();
	public final GridPoint location;

	/**
	 * Simple constructor to make each SquareContents instance aware of its location
	 *
	 * @param location This square's location on the grid
	 */
	public Square(final GridPoint location) {
		this.location = location;
	}

	public synchronized void add(final GridObject gridObject) {
		this.gridObjects.add(gridObject);
	}

	/**
	 * Get the topmost (i.e. most recently added) GridObject that matches the given
	 * type. Returns null if there are no matching instances of GridObject in this
	 * square.
	 *
	 * @return The most recently added matching GridObject, or null if none exists.
	 */
	public synchronized GridObject get(final Class<?> c) {
		GridObject lastFound = null;
		final Iterator<GridObject> i = this.gridObjects.iterator();
		while (i.hasNext()) {
			final GridObject gridObject = i.next();
			if (gridObject.getClass() == c) {
				lastFound = gridObject;
			}
		}
		return lastFound;
	}

	public boolean has(final Class<?> c) {
		if (get(c) == null) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Get the topmost (i.e. most recently added) GridObject that is a subclass of
	 * the given type. Returns null if there are no matching instances of GridObject
	 * in this square.
	 *
	 * @return The most recently added matching GridObject, or null if none exists.
	 */
	public synchronized GridObject getInstanceOf(final Class<?> c) {
		GridObject lastFound = null;
		final Iterator<GridObject> i = this.gridObjects.iterator();
		while (i.hasNext()) {
			final GridObject gridObject = i.next();
			if (c.isAssignableFrom(gridObject.getClass())) {
				lastFound = gridObject;
			}
		}
		return lastFound;
	}

	public boolean hasInstanceOf(final Class<?> c) {
		if (getInstanceOf(c) == null) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Delete the given GridObject, if it is in this square. If the object is not
	 * found, does nothing.
	 *
	 * @param gridObject A GridObject in this square, to be deleted.
	 */
	public synchronized void remove(final GridObject gridObject) {
		this.gridObjects.remove(gridObject);
	}

	/**
	 * Returns a count of the GridObjects currently occupying this square.
	 *
	 * @return the total number of objects in this square
	 */
	public synchronized int getCount() {
		return this.gridObjects.size();
	}

	/**
	 * Get an iterator to iterate through the contents in drawing order.
	 *
	 * @return an Iterator<GridObject> in bottom-up order.
	 */
	public Iterator<GridObject> iterator() {
		return this.gridObjects.iterator();
	}
}
