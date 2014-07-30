package gui;

import java.awt.Graphics;

import grid.GridObject;
import grid.ModelListener;
import grid.ModelObject;

public class ObstacleView extends GridObjectView implements ModelListener {
	
	public ObstacleView(GridView parent, GridObject obstacle) {
		super(parent, obstacle);
		// Register for paint events
		obstacle.addListener(this);
	}

	@Override
	public void paint(Graphics g) {
		g.setColor(LookAndFeel.getGridLineColour());
		g.fillRect(getUpperLeft().x, getUpperLeft().y, LookAndFeel.getSquareSize(), LookAndFeel.getSquareSize());
	}

	public void eventFired(String eventName, ModelObject source) {
		// Only respond to paint events from Swing, forwarded by the GridView
		if (eventName.equals(GridObject.PAINT)) {
			paint(parent.getG2d());
		}
	}

}
