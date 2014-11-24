package gui;

import grid.Grid;
import grid.ModelListener;
import grid.ModelObject;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import vac.Clean;
import actions.Action;

/**
 * Top level GUI object. Constructor creates and shows the entire GUI.
 */
public class AppView implements ModelListener {

	private final String CLEAN_STOP = Clean.class.getName() + "."
			+ Action.STOP_EVENT;
	private final JLabel dustCleanedLabel;
	private final JLabel elapsedTimeLabel;
	private final JFrame frame;
	private final Timer timer;

	/**
	 * Creates AppView but does not yet set it visible as that can be done only
	 * from the AWT thread.
	 * 
	 * @param grid
	 */
	public AppView(Grid grid) {
		// Register for updates on every GridObject
		grid.addListener(this);

		// Create a one-second repeat timer
		timer = new Timer(1000, new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				timerUpdate();
			}
		});

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
		elapsedTimeLabel = new JLabel(getTimeStatus());
		infoPanel.add(elapsedTimeLabel);
		frame.add(infoPanel, BorderLayout.SOUTH);

		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		// Finally, show it, and start the timer
		setVisible(true);
		timer.start();
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

	private int elapsedSeconds = 0;
	private int elapsedMinutes = 0;
	private int elapsedHours = 0;

	private String getTimeStatus() {
		return String.format("Elapsed time: %02d:%02d:%02d", elapsedHours,
				elapsedMinutes, elapsedSeconds);
	}

	public void timerUpdate() {
		++elapsedSeconds;
		if (elapsedSeconds >= 60) {
			elapsedSeconds = 0;
			++elapsedMinutes;
			if (elapsedMinutes >= 60) {
				elapsedMinutes = 0;
				++elapsedHours;
			}
		}
		elapsedTimeLabel.setText(getTimeStatus());
	}

	public void close() {
		timer.stop();
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

}
