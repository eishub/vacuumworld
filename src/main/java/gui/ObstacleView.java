package gui;

import java.awt.Graphics;

import grid.GridObject;
import grid.ModelListener;
import grid.ModelObject;

public class ObstacleView extends GridObjectView implements ModelListener {
	public ObstacleView(final GridView parent, final GridObject obstacle) {
		super(parent, obstacle);
		// Register for paint events
		obstacle.addListener(this);
	}

	@Override
	public void paint(final Graphics g) {
		g.setColor(LookAndFeel.getGridLineColour());
		g.fillRect(getUpperLeft().x, getUpperLeft().y, LookAndFeel.getSquareSize(), LookAndFeel.getSquareSize());
	}

	@Override
	public void eventFired(final String eventName, final ModelObject source) {
		// Only respond to paint events from Swing, forwarded by the GridView
		if (eventName.equals(GridObject.PAINT)) {
			paint(this.parent.getG2d());
		}
	}
}
