package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import grid.GridObject;
import grid.ModelListener;
import grid.ModelObject;
import vac.Dust;

public class DustView extends GridObjectView implements ModelListener {

	// numPieces not declared final because we may wish to decrement it during vacuuming
	private Dust dust;
	private final Color[] dustColours;
	private final Point[] dustLocations;
	private final int[] dustSizes;
	
	public DustView(GridView parent, Dust dust) {
		super(parent, dust);
		this.dust = dust;
		// Get the colour, size, and location for each piece of dust
		// Colour and size returned by LookAndFeel will be different each time, so store them
		int numPieces = dust.getSpeckCount();
		dustColours = new Color[numPieces];
		dustSizes = new int[numPieces];
		dustLocations = new Point[numPieces];
		for (int i = 0; i < numPieces; i++) {
			dustColours[i] = LookAndFeel.getDustColour();
			dustSizes[i] = LookAndFeel.getDustSize();
			// Generate a random location within the current square for each piece
			int s = LookAndFeel.getSquareSize();
			int xRandom = getUpperLeft().x + (int) Math.round(Math.random() * (s - dustSizes[i]));
			int yRandom = getUpperLeft().y + (int) Math.round(Math.random() * (s - dustSizes[i]));
			dustLocations[i] = new Point(xRandom, yRandom);
			
		}
		// Register for paint events
		dust.addListener(this);
	}

	public void eventFired(String eventName, ModelObject source) {
		// Only respond to paint events from Swing, forwarded by the GridView
		if (eventName.equals(GridObject.PAINT)) {
			paint(parent.getG2d());
		}
	}

	@Override
	public void paint(Graphics g) {
		for (int i = 0; i < dust.getSpeckCount(); i++) {
			g.setColor(dustColours[i]);
			g.fillOval(dustLocations[i].x, dustLocations[i].y, dustSizes[i], dustSizes[i]);
		}
	}
}
