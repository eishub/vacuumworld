package grid;

/**
 * Very simple class to encapsulate the concept of the four compass points.
 */
public class Direction {
	
	public static final String NORTH = "north";
	public static final String EAST = "east";
	public static final String SOUTH = "south";
	public static final String WEST = "west";
	
	public static final Direction north = new Direction(0, false);
	public static final Direction east = new Direction(Math.PI * 0.5, false);
	public static final Direction south = new Direction(Math.PI, false);
	public static final Direction west = new Direction(Math.PI * 1.5, false);
	
	private static final Direction[] directions = {north, east, south, west};
	private double radians;
	private final boolean mutable;
	
	/**
	 * Construct a mutable Direction with the given initial angle, measured clockwise from north
	 * @param radians angle in radians
	 */
	public Direction(double radians) {
		this.mutable = true;
		this.radians = normaliseAngle(radians);
	}
	
	private Direction(double radians, boolean mutable) {
		this.mutable = mutable;
		this.radians = normaliseAngle(radians);
	}
	
	public double getRadians() {
		return radians;
	}
	
	public void setValue(double value) {
		if (!mutable) throw new IllegalArgumentException("Cannot set the value of a constant direction!");
		this.radians = normaliseAngle(value);
	}
	
	private double normaliseAngle(double value) {
		// Converts value to an angle between 0 and 2 pi
		value %= (2.0 * Math.PI);
		if (value < 0) value += (2.0 * Math.PI);
		return value;
	}
	
	public boolean equalsDirection(Direction other) {
		double difference = Math.abs(other.getRadians() - radians);
		// Now make sure difference is less than the required accuracy
		if (difference < 0.01) return true;
		else return false;
	}
	
	public double getXComponent() {
		return Math.sin(radians);
	}
	
	/**
	 * Get the y component of this direction.
	 * Note that we use 'computer' (as opposed to mathematical) y coordinates; y increases south.
	 * @return a double representing the y component of this direction, from -1 to 1.
	 */
	public double getYComponent() {
		return -Math.cos(radians);
	}

	/**
	 * Returns a human-readable representation of this direction.
	 */
	public String toString() {
		// Only name the four compass points
		if (this.equalsDirection(north)) return "North";
		else if (this.equalsDirection(east)) return "East";
		else if (this.equalsDirection(south)) return "South";
		else if (this.equalsDirection(west)) return "West";
		else return "Unknown";
	}
	
	/**
	 * Returns a machine-readable all-lower-case representation of this direction.
	 */
	public String toName() {
		// Only name the four compass points
		if (this.equalsDirection(north)) return NORTH;
		else if (this.equalsDirection(east)) return EAST;
		else if (this.equalsDirection(south)) return SOUTH;
		else if (this.equalsDirection(west)) return WEST;
		else return "unknown";
	}
	
	/**
	 * Rounds this direction to a non-mutable direction north, south, east, or west.
	 * @return The closest of the public static final compass point Directions.
	 */
	public Direction round() {
		int n = (int)(Math.round(this.radians / (Math.PI * 0.5)) % 4);
		switch (n) {
		case 0: return north;
		case 1: return east;
		case 2: return south;
		case 3: return west;
		default: throw new RuntimeException("Failed to round " + radians + " rad to NSEW!");
		}
	}
	
	/**
	 * Get a random choice of north, south, east, and west.
	 * @return a static final direction representing one of the four points of the compass.
	 */
	public static Direction random() {
		return directions[(int)Math.floor(Math.random() * 4.0)];
	}
}
