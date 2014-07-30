package grid;

/**
 * Very simple class to encapsulate conversion from relative to absolute Direction.
 */
public class RelativeDirection {
	
	public static final String FORWARD = "forward";
	public static final String RIGHT = "right";
	public static final String BACK = "back";
	public static final String LEFT = "left";
	public static final String FORWARD_RIGHT = "forwardRight";
	public static final String FORWARD_LEFT = "forwardLeft";
	public static final String HERE = "here";

	public static final RelativeDirection forward = new RelativeDirection(FORWARD, 0);
	public static final RelativeDirection right = new RelativeDirection(RIGHT, Math.PI * 0.5);
	public static final RelativeDirection back = new RelativeDirection(BACK, Math.PI);
	public static final RelativeDirection left = new RelativeDirection(LEFT, Math.PI * 1.5);
	public static final RelativeDirection forwardRight = new RelativeDirection(FORWARD_RIGHT, Math.PI * 0.25);
	public static final RelativeDirection forwardLeft = new RelativeDirection(FORWARD_LEFT, Math.PI * 1.75);
	// Experimental special value - might want to remove this!
	public static final RelativeDirection here = new RelativeDirection(HERE, 0);
	
	private String name;
	private double value;
	
	private RelativeDirection(String name, double value) {
		this.name = name;
		this.value = value;
	}
	
	public Direction toAbsolute(Direction currentDirection) {
		return new Direction(currentDirection.getRadians() + value);
	}
	
	public String getName() {
		return this.name;
	}
}
