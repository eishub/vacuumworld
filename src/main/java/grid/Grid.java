package grid;

import java.util.Iterator;

public class Grid extends ModelObject implements ModelListener {

	public final int sizeX;
	public final int sizeY;
	private final Square[][] contents;

	public Grid(int sizeX, int sizeY) {
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		contents = new Square[sizeX][sizeY];
		Iterator<GridPoint> iterator = new GridPointIterator(contents);
		while (iterator.hasNext()) {
			GridPoint point = iterator.next();
			contents[point.x][point.y] = new Square(point);
		}
	}

	private boolean inRangeX(int x) {
		return !(x < 0 || x >= sizeX);
	}

	private boolean inRangeY(int y) {
		return !(y < 0 || y >= sizeY);
	}

	/**
	 * Check to see if the given square actually exists in this grid.
	 * 
	 * @param location
	 *            coordinates of the square to check
	 * @return true if the square exists, false if not
	 */
	public boolean exists(GridPoint location) {
		return (inRangeX(location.x) && inRangeY(location.y));
	}

	public synchronized void add(GridObject gridObject, GridPoint location) {
		getSquareAt(location).add(gridObject);
		gridObject.addListener(this);
	}

	public Square getSquareAt(GridPoint location) {
		// Check the requested square is actually in the grid
		if (!inRangeX(location.x))
			throw new IllegalArgumentException("x co-ordinate (" + location.x
					+ ") must be in range 0 <= x < " + sizeX);
		if (!inRangeY(location.y))
			throw new IllegalArgumentException("y co-ordinate (" + location.y
					+ ") must be in range 0 <= y < " + sizeY);
		return contents[location.x][location.y];
	}

	/**
	 * get iterator to check all squares in the grid.
	 * 
	 * @return
	 */
	public Iterator<Square> squareIterator() {
		return new GridSquareIterator(contents);
	}

	public Iterator<Square> scanIterator() {
		return new GridScanIterator(contents);
	}

	public Iterator<Square> randomIterator() {
		return new GridRandomIterator(contents);
	}

	/**
	 * Simply forward on all events received.
	 */
	public void eventFired(String eventName, ModelObject source) {
		fireEvent(eventName, source);
	}

}