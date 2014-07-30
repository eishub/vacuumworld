package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.Iterator;

import javax.swing.JPanel;

import grid.Grid;
import grid.GridObject;
import grid.ModelListener;
import grid.ModelObject;
import grid.Square;
import vac.Dust;
import vac.VacBot;

@SuppressWarnings("serial")
public class GridView extends JPanel implements ModelListener {

	private final String DUST_CREATED = Dust.class.getName() + "." + GridObject.CREATE;
	private final int worldSizeX;
	private final int worldSizeY;
	private final int squareSize;
	private final int gridLineWidth;
	private final int xDimension;
	private final int yDimension;
	private final Color gridLineColour;

	private Grid grid;
	private volatile Graphics2D g2d;

	public GridView(Grid grid) {
		this.grid = grid;
		this.setDoubleBuffered(true);
		worldSizeX = grid.sizeX;
		worldSizeY = grid.sizeY;
		squareSize = LookAndFeel.getSquareSize();
		gridLineWidth = LookAndFeel.getGridLineWidth();
		gridLineColour = LookAndFeel.getGridLineColour();
		// Work out the on-screen size of the grid
		xDimension = worldSizeX * squareSize + (worldSizeX - 1) * gridLineWidth;
		yDimension = worldSizeY * squareSize + (worldSizeY - 1) * gridLineWidth;
		this.setPreferredSize(new Dimension(xDimension, yDimension));
		this.setBackground(LookAndFeel.getGridBackground());
		// Get all the objects in the grid and create their visual equivalents
		Iterator<Square> iterator = grid.scanIterator();
		while (iterator.hasNext()) {
			Square sc = iterator.next();
			// Get contents in drawing order
			Iterator<GridObject> si = sc.iterator();
			while (si.hasNext()) {
				// Using instanceof here is not ideal, but the alternative - each model object
				// returns its appropriate view object - creates a circular dependency.
				GridObject go = si.next();
				if (go instanceof VacBot) {
					new VacBotView(this, (VacBot)go);
				} else if (go instanceof Dust) {
					// If regenerating dust is enabled, future DustView instances will be constructed 
					// on CREATE events. However, we can't use this mechanism here because the model
					// is already built, and the CREATE events fired before a listener was registered.
					new DustView(this, (Dust)go);
				} else {
					new ObstacleView(this, (GridObject)go);
				}
			}
		}
	}
	
	// Override the paint method to draw the grid and its contents
	public void paint(Graphics g) {
		super.paint(g);
		// Cast to Graphics2D to access advanced features (anti-aliasing, gradient fills)
		g2d = (Graphics2D)g;
		// Turn on graphical anti aliasing
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		// Draw the grid lines
		g2d.setColor(gridLineColour);
		// Draw the vertical lines
		int xPos = squareSize;
		for (int i = 1; i < worldSizeX; i++) {
			g2d.fillRect(xPos, 0, gridLineWidth, yDimension);
			xPos += gridLineWidth + squareSize;
		}
		// Draw the horizontal lines
		int yPos = squareSize;
		for (int i = 1; i < worldSizeY; i++) {
			g2d.fillRect(0, yPos, xDimension, gridLineWidth);
			yPos += gridLineWidth + squareSize;
		}
		// Tell each object in the grid to paint
		Iterator<Square> gridIterator = grid.scanIterator();
		while (gridIterator.hasNext()) {
			Square square = gridIterator.next();
			// Get contents in drawing order, preventing concurrent modification to the square
			synchronized (square) {
				Iterator<GridObject> squareIterator = square.iterator();
				while (squareIterator.hasNext()) {
					// Fire a 'paint' event to each grid object
					squareIterator.next().fireEvent(GridObject.PAINT, null);
				}
			}
		}
	}
	
	/**
	 * Grid objects views that need to draw themselves can use this to get a graphics context.
	 * @return The graphics context that was passed in the most recent call to paint(Graphics).
	 */
	Graphics2D getG2d() {
		return g2d;
	}

	public void eventFired(String eventName, ModelObject source) {
		if (eventName.equals(DUST_CREATED)) {
			new DustView(this, (Dust)source);
			this.repaint();
		}
	}
}
