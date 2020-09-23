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

public class GridView extends JPanel implements ModelListener {
	private static final long serialVersionUID = 1L;
	private final String DUST_CREATED = Dust.class.getName() + "." + GridObject.CREATE;
	private final int worldSizeX;
	private final int worldSizeY;
	private final int squareSize;
	private final int gridLineWidth;
	private final int xDimension;
	private final int yDimension;
	private final Color gridLineColour;

	private final Grid grid;
	private volatile Graphics2D g2d;

	public GridView(final Grid grid) {
		this.grid = grid;
		setDoubleBuffered(true);
		this.worldSizeX = grid.sizeX;
		this.worldSizeY = grid.sizeY;
		this.squareSize = LookAndFeel.getSquareSize();
		this.gridLineWidth = LookAndFeel.getGridLineWidth();
		this.gridLineColour = LookAndFeel.getGridLineColour();
		// Work out the on-screen size of the grid
		this.xDimension = this.worldSizeX * this.squareSize + (this.worldSizeX - 1) * this.gridLineWidth;
		this.yDimension = this.worldSizeY * this.squareSize + (this.worldSizeY - 1) * this.gridLineWidth;
		setPreferredSize(new Dimension(this.xDimension, this.yDimension));
		setBackground(LookAndFeel.getGridBackground());
		// Get all the objects in the grid and create their visual equivalents
		final Iterator<Square> iterator = grid.scanIterator();
		while (iterator.hasNext()) {
			final Square sc = iterator.next();
			// Get contents in drawing order
			final Iterator<GridObject> si = sc.iterator();
			while (si.hasNext()) {
				// Using instanceof here is not ideal, but the alternative - each model object
				// returns its appropriate view object - creates a circular dependency.
				final GridObject go = si.next();
				if (go instanceof VacBot) {
					new VacBotView(this, (VacBot) go);
				} else if (go instanceof Dust) {
					// If regenerating dust is enabled, future DustView instances will be
					// constructed
					// on CREATE events. However, we can't use this mechanism here because the model
					// is already built, and the CREATE events fired before a listener was
					// registered.
					new DustView(this, (Dust) go);
				} else {
					new ObstacleView(this, go);
				}
			}
		}
	}

	// Override the paint method to draw the grid and its contents
	@Override
	public void paint(final Graphics g) {
		super.paint(g);
		// Cast to Graphics2D to access advanced features (anti-aliasing, gradient
		// fills)
		this.g2d = (Graphics2D) g;
		// Turn on graphical anti aliasing
		this.g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		// Draw the grid lines
		this.g2d.setColor(this.gridLineColour);
		// Draw the vertical lines
		int xPos = this.squareSize;
		for (int i = 1; i < this.worldSizeX; i++) {
			this.g2d.fillRect(xPos, 0, this.gridLineWidth, this.yDimension);
			xPos += this.gridLineWidth + this.squareSize;
		}
		// Draw the horizontal lines
		int yPos = this.squareSize;
		for (int i = 1; i < this.worldSizeY; i++) {
			this.g2d.fillRect(0, yPos, this.xDimension, this.gridLineWidth);
			yPos += this.gridLineWidth + this.squareSize;
		}
		// Tell each object in the grid to paint
		final Iterator<Square> gridIterator = this.grid.scanIterator();
		while (gridIterator.hasNext()) {
			final Square square = gridIterator.next();
			// Get contents in drawing order, preventing concurrent modification to the
			// square
			synchronized (square) {
				final Iterator<GridObject> squareIterator = square.iterator();
				while (squareIterator.hasNext()) {
					// Fire a 'paint' event to each grid object
					squareIterator.next().fireEvent(GridObject.PAINT, null);
				}
			}
		}
	}

	/**
	 * Grid objects views that need to draw themselves can use this to get a
	 * graphics context.
	 *
	 * @return The graphics context that was passed in the most recent call to
	 *         paint(Graphics).
	 */
	Graphics2D getG2d() {
		return this.g2d;
	}

	@Override
	public void eventFired(final String eventName, final ModelObject source) {
		if (eventName.equals(this.DUST_CREATED)) {
			new DustView(this, (Dust) source);
			this.repaint();
		}
	}
}
