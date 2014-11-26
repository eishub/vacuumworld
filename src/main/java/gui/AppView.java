package gui;

import grid.Grid;
import grid.ModelListener;
import grid.ModelObject;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import vac.Clean;
import vac.VacWorld;
import vac.WorldListener;
import actions.Action;

/**
 * Top level GUI object. Constructor creates and shows the entire GUI.
 */
public class AppView implements ModelListener, WorldListener {

	private final String CLEAN_STOP = Clean.class.getName() + "."
			+ Action.STOP_EVENT;
	private final String TIME_CHANGED = VacWorld.class.getName() + "."
			+ Action.TIME_EVENT;

	private final JLabel dustCleanedLabel;
	private final JLabel elapsedTimeLabel;
	private final JFrame frame;
	private VacWorld world; // for (un)subscribing

	/**
	 * Creates AppView but does not yet set it visible as that can be done only
	 * from the AWT thread.
	 * 
	 * @param grid
	 */
	public AppView(Grid grid, VacWorld world) {
		this.world = world;
		// Register for updates on every GridObject
		grid.addListener(this);
		world.addListener(this);

		frame = new JFrame("Vacuum World");
		GridView gridView = new GridView(grid);
		grid.addListener(gridView);
		frame.add(gridView);

		// Create a lower panel to show the current status
		JPanel infoPanel = new JPanel();
		infoPanel.setLayout(new FlowLayout());
		dustCleanedLabel = new JLabel(getDustStatus());
		infoPanel.add(dustCleanedLabel);
		infoPanel.add(new JLabel("      "));
		elapsedTimeLabel = new JLabel("-");
		infoPanel.add(elapsedTimeLabel);
		frame.add(infoPanel, BorderLayout.SOUTH);

		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		// Finally, show it, and start the timer
		setVisible(true);
	}

	private int dustCleaned = 0;

	private String getDustStatus() {
		return String.format("Dust cleaned: %02d", dustCleaned);
	}

	public void eventFired(String eventName, ModelObject source) {
		if (eventName.equals(CLEAN_STOP)) {
			++dustCleaned;
			dustCleanedLabel.setText(getDustStatus());
		}
	}

	public void close() {
		world.removeListener(this);
		frame.dispose();
		setVisible(false);
	}

	public void setVisible(final boolean isVisible) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				frame.setVisible(isVisible);
			}
		});
	}

	@Override
	public void timeChanged(String time) {
		elapsedTimeLabel.setText(time);
	}

}
