package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import grid.GridObject;
import grid.ModelListener;
import grid.ModelObject;
import vac.Dust;

public class DustView extends GridObjectView implements ModelListener {
	// numPieces not declared final because we may wish to decrement it during
	// vacuuming
	private final Dust dust;
	private final Color[] dustColours;
	private final Point[] dustLocations;
	private final int[] dustSizes;

	public DustView(final GridView parent, final Dust dust) {
		super(parent, dust);
		this.dust = dust;
		// Get the colour, size, and location for each piece of dust
		// Colour and size returned by LookAndFeel will be different each time, so store
		// them
		final int numPieces = dust.getSpeckCount();
		this.dustColours = new Color[numPieces];
		this.dustSizes = new int[numPieces];
		this.dustLocations = new Point[numPieces];
		for (int i = 0; i < numPieces; i++) {
			this.dustColours[i] = LookAndFeel.getDustColour();
			this.dustSizes[i] = LookAndFeel.getDustSize();
			// Generate a random location within the current square for each piece
			final int s = LookAndFeel.getSquareSize();
			final int xRandom = getUpperLeft().x + (int) Math.round(Math.random() * (s - this.dustSizes[i]));
			final int yRandom = getUpperLeft().y + (int) Math.round(Math.random() * (s - this.dustSizes[i]));
			this.dustLocations[i] = new Point(xRandom, yRandom);

		}
		// Register for paint events
		dust.addListener(this);
	}

	@Override
	public void eventFired(final String eventName, final ModelObject source) {
		// Only respond to paint events from Swing, forwarded by the GridView
		if (eventName.equals(GridObject.PAINT)) {
			paint(this.parent.getG2d());
		}
	}

	@Override
	public void paint(final Graphics g) {
		for (int i = 0; i < this.dust.getSpeckCount(); i++) {
			g.setColor(this.dustColours[i]);
			g.fillOval(this.dustLocations[i].x, this.dustLocations[i].y, this.dustSizes[i], this.dustSizes[i]);
		}
	}
}
