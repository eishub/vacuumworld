package grid;

/**
 * Very simple class to indicate a specific grid square.
 */
public class GridPoint {
	public final int x, y;

	public GridPoint(final int x, final int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public String toString() {
		return "(" + this.x + "," + this.y + ")";
	}

	public boolean equalsGridPoint(final GridPoint other) {
		if (this.x == other.x && this.y == other.y) {
			return true;
		} else {
			return false;
		}
	}
}
