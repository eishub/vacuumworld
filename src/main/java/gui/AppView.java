package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import actions.Action;
import grid.Grid;
import grid.ModelListener;
import grid.ModelObject;
import vac.Clean;
import vac.VacWorld;
import vac.WorldListener;

/**
 * Top level GUI object. Constructor creates and shows the entire GUI.
 */
public class AppView implements ModelListener, WorldListener {
	private final String CLEAN_STOP = Clean.class.getName() + "." + Action.STOP_EVENT;
	private final JLabel dustCleanedLabel;
	private final JLabel elapsedTimeLabel;
	private final JFrame frame;
	private final VacWorld world; // for (un)subscribing

	/**
	 * Creates AppView but does not yet set it visible as that can be done only from
	 * the AWT thread.
	 *
	 * @param grid
	 */
	public AppView(final Grid grid, final VacWorld world) {
		this.world = world;
		// Register for updates on every GridObject
		grid.addListener(this);
		world.addListener(this);

		this.frame = new JFrame("Vacuum World");
		final GridView gridView = new GridView(grid);
		grid.addListener(gridView);
		this.frame.add(gridView);

		// Create a lower panel to show the current status
		final JPanel infoPanel = new JPanel();
		infoPanel.setLayout(new FlowLayout());
		this.dustCleanedLabel = new JLabel(getDustStatus());
		infoPanel.add(this.dustCleanedLabel);
		infoPanel.add(new JLabel("      "));
		this.elapsedTimeLabel = new JLabel("-");
		infoPanel.add(this.elapsedTimeLabel);
		this.frame.add(infoPanel, BorderLayout.SOUTH);

		this.frame.pack();
		this.frame.setLocationRelativeTo(null);
		this.frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		// Finally, show it, and start the timer
		setVisible(true);
	}

	private int dustCleaned = 0;

	private String getDustStatus() {
		return String.format("Dust cleaned: %02d", this.dustCleaned);
	}

	@Override
	public void eventFired(final String eventName, final ModelObject source) {
		if (eventName.equals(this.CLEAN_STOP)) {
			++this.dustCleaned;
			this.dustCleanedLabel.setText(getDustStatus());
		}
	}

	public void close() {
		this.world.removeListener(this);
		final JFrame theframe = this.frame;
		SwingUtilities.invokeLater(() -> theframe.dispose());
		setVisible(false);
	}

	public void setVisible(final boolean isVisible) {
		SwingUtilities.invokeLater(() -> AppView.this.frame.setVisible(isVisible));
	}

	@Override
	public void timeChanged(final String time) {
		this.elapsedTimeLabel.setText(time);
	}
}
