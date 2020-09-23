package gui;

import java.awt.Color;

/**
 * Provides a single point of access for layout parameters and colours. Access
 * methods allow parameters to be calculated on-the-fly or randomised if
 * necessary.
 */
public class LookAndFeel {
	// Some nice, useful colours
	private static Color paleBlue = new Color(234, 234, 255);
	private static Color midBlue = new Color(100, 139, 216);
	private static Color darkBlue = new Color(26, 0, 104);
	private static Color brightBlue = new Color(0, 204, 255);

	// General grid parameters
	private static Color gridBackground = paleBlue;
	private static Color gridLineColour = midBlue;
	private static int squareSize = 60;
	private static int gridLineWidth = 4;

	// VacBot appearance
	private static int vacBotClearance = 4;
	private static Color[] vacBotColours = { Color.RED, Color.YELLOW, brightBlue, Color.PINK, Color.GREEN, Color.ORANGE,
			Color.GRAY, Color.MAGENTA };
	private static int currentColour = 0;
	private static String[] vacBotNames = { "Henry", "Decco", "Lloyd", "Harry", "Keano", "Stevo", "Benjy", "Darth" };
	private static int currentName = 0;
	private static Color eyeColour = Color.WHITE;
	private static int eyeRadius = 6;
	private static int eyePosition = 15;
	private static int eyeSpacing = 20;
	private static Color pupilColour = Color.BLACK;
	private static int pupilRadius = 3;
	private static int pupilOffset = 3;
	private static Color noseColour = Color.BLACK;
	private static int noseRadius = 6;
	private static int nosePosition = 21;
	private static Color wheelColour = Color.BLACK;
	private static int wheelWidth = 7;
	private static int wheelRadius = 12;
	private static int wheelPosition = 24;
	private static Color lightOffColour = darkBlue;
	private static Color lightOnColour = brightBlue;
	private static int lightRadius = 9;

	// Dust parameters
	// Access method below multiplies size by a random factor 0.5 to 1.5
	private static int dustSize = 8;

	// General grid parameters
	public static Color getGridBackground() {
		return gridBackground;
	}

	public static Color getGridLineColour() {
		return gridLineColour;
	}

	public static int getSquareSize() {
		return squareSize;
	}

	public static int getGridLineWidth() {
		return gridLineWidth;
	}

	// VacBot appearance - called only once in each VacBot constructor, then stored
	public static Color getVacBotColour() {
		return vacBotColours[currentColour++ % vacBotColours.length];
	}

	public static String getVacBotName() {
		return vacBotNames[currentName++ % vacBotNames.length];
	}

	public static int getVacBotClearance() {
		return vacBotClearance;
	}

	public static Color getEyeColour() {
		return eyeColour;
	}

	public static int getEyeRadius() {
		return eyeRadius;
	}

	public static int getEyePosition() {
		return eyePosition;
	}

	public static int getEyeSpacing() {
		return eyeSpacing;
	}

	public static Color getPupilColour() {
		return pupilColour;
	}

	public static int getPupilRadius() {
		return pupilRadius;
	}

	public static int getPupilOffset() {
		return pupilOffset;
	}

	public static Color getNoseColour() {
		return noseColour;
	}

	public static int getNoseRadius() {
		return noseRadius;
	}

	public static int getNosePosition() {
		return nosePosition;
	}

	public static Color getWheelColour() {
		return wheelColour;
	}

	public static int getWheelWidth() {
		return wheelWidth;
	}

	public static int getWheelRadius() {
		return wheelRadius;
	}

	public static int getWheelPosition() {
		return wheelPosition;
	}

	public static Color getLightOffColour() {
		return lightOffColour;
	}

	public static Color getLightOnColour() {
		return lightOnColour;
	}

	public static int getLightRadius() {
		return lightRadius;
	}

	// Dust parameters - called only once for each piece, then stored, so can be
	// randomised
	public static Color getDustColour() {
		return new Color(getRandomByte(), getRandomByte(), getRandomByte());
	}

	private static int getRandomByte() {
		return (int) Math.round(Math.random() * 255.0);
	}

	public static int getDustSize() {
		return (int) Math.round((Math.random() + 0.5) * dustSize);
	}
}
