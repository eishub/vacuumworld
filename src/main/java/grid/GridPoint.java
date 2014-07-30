package grid;

/**
 * Very simple class to indicate a specific grid square.
 */
public class GridPoint {

	public final int x, y;
	
	public GridPoint(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public String toString() {
		return "(" + x + "," + y + ")";
	}
	
	public boolean equalsGridPoint(GridPoint other) {
		if (this.x == other.x && this.y == other.y) return true;
		else return false;
	}
}
