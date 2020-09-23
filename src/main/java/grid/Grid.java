package grid;

import java.util.Iterator;

public class Grid extends ModelObject implements ModelListener {
	public final int sizeX;
	public final int sizeY;
	private final Square[][] contents;

	public Grid(final int sizeX, final int sizeY) {
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.contents = new Square[sizeX][sizeY];
		final Iterator<GridPoint> iterator = new GridPointIterator(this.contents);
		while (iterator.hasNext()) {
			final GridPoint point = iterator.next();
			this.contents[point.x][point.y] = new Square(point);
		}
	}

	private boolean inRangeX(final int x) {
		return !(x < 0 || x >= this.sizeX);
	}

	private boolean inRangeY(final int y) {
		return !(y < 0 || y >= this.sizeY);
	}

	/**
	 * Check to see if the given square actually exists in this grid.
	 *
	 * @param location coordinates of the square to check
	 * @return true if the square exists, false if not
	 */
	public boolean exists(final GridPoint location) {
		return (inRangeX(location.x) && inRangeY(location.y));
	}

	public synchronized void add(final GridObject gridObject, final GridPoint location) {
		getSquareAt(location).add(gridObject);
		gridObject.addListener(this);
	}

	public Square getSquareAt(final GridPoint location) {
		// Check the requested square is actually in the grid
		if (!inRangeX(location.x)) {
			throw new IllegalArgumentException(
					"x co-ordinate (" + location.x + ") must be in range 0 <= x < " + this.sizeX);
		}
		if (!inRangeY(location.y)) {
			throw new IllegalArgumentException(
					"y co-ordinate (" + location.y + ") must be in range 0 <= y < " + this.sizeY);
		}
		return this.contents[location.x][location.y];
	}

	/**
	 * get iterator to check all squares in the grid.
	 *
	 * @return
	 */
	public Iterator<Square> squareIterator() {
		return new GridSquareIterator(this.contents);
	}

	public Iterator<Square> scanIterator() {
		return new GridScanIterator(this.contents);
	}

	public Iterator<Square> randomIterator() {
		return new GridRandomIterator(this.contents);
	}

	/**
	 * Simply forward on all events received.
	 */
	@Override
	public void eventFired(final String eventName, final ModelObject source) {
		fireEvent(eventName, source);
	}
}