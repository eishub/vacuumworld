package gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.RadialGradientPaint;
import java.awt.Stroke;

import grid.Direction;
import grid.GridObject;
import grid.ModelListener;
import grid.ModelObject;
import vac.VacBot;

public class VacBotView extends GridObjectView implements ModelListener {
	private final VacBot vacBot;
	private final Color vacBotColour;
	private final Color eyeColour;
	private final Color pupilColour;
	private final Color noseColour;
	private final Color lightOffColour;
	private final Color lightOnColour;
	private final float[] lightWeights;
	private final Color[] lightColours;
	private final Color wheelColour;
	private final int squareSize;
	private final int halfSquareSize;
	private final int clearance;
	private final int doubleClearance;
	private final int eyeRadius;
	private final int eyeDiameter;
	private final int eyePosition;
	private final int eyeHalfSpacing;
	private final int pupilRadius;
	private final int pupilDiameter;
	private final int pupilOffset;
	private final int pupilPosition;
	private final int noseRadius;
	private final int nosePosition;
	private final Stroke wheelStroke;
	private final int wheelWidth;
	private final int wheelRadius;
	private final int wheelPosition;
	private final int lightRadius;
	private final int lightDiameter;

	public VacBotView(final GridView parent, final VacBot vacBot) {
		super(parent, vacBot);
		this.vacBot = vacBot;
		// Register the view to receive updates from the model
		vacBot.addListener(this);
		// Some LookAndFeel methods may return different values each time, so store them
		// If VacBot does not specify its own colour, choose a default
		if (vacBot.getColour() == null) {
			this.vacBotColour = LookAndFeel.getVacBotColour();
		} else {
			this.vacBotColour = vacBot.getColour();
		}
		this.eyeColour = LookAndFeel.getEyeColour();
		this.pupilColour = LookAndFeel.getPupilColour();
		this.noseColour = LookAndFeel.getNoseColour();
		this.lightOffColour = LookAndFeel.getLightOffColour();
		this.lightOnColour = LookAndFeel.getLightOnColour();
		this.lightWeights = new float[] { (float) 0.0, (float) 0.4, (float) 1.0 };
		this.lightColours = new Color[] { this.lightOnColour, this.lightOnColour, this.lightOffColour };
		this.wheelColour = LookAndFeel.getWheelColour();
		this.squareSize = LookAndFeel.getSquareSize();
		this.halfSquareSize = this.squareSize / 2;
		this.clearance = LookAndFeel.getVacBotClearance();
		this.doubleClearance = this.clearance * 2;
		this.eyeRadius = LookAndFeel.getEyeRadius();
		this.eyeDiameter = 2 * this.eyeRadius;
		this.eyePosition = LookAndFeel.getEyePosition();
		this.eyeHalfSpacing = LookAndFeel.getEyeSpacing() / 2;
		this.pupilRadius = LookAndFeel.getPupilRadius();
		this.pupilDiameter = 2 * this.pupilRadius;
		this.pupilOffset = LookAndFeel.getPupilOffset();
		this.pupilPosition = this.eyePosition + this.pupilOffset;
		this.noseRadius = LookAndFeel.getNoseRadius();
		this.nosePosition = LookAndFeel.getNosePosition();
		this.wheelWidth = LookAndFeel.getWheelWidth();
		this.wheelStroke = new BasicStroke(this.wheelWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
		this.wheelRadius = LookAndFeel.getWheelRadius();
		this.wheelPosition = LookAndFeel.getWheelPosition();
		this.lightRadius = LookAndFeel.getLightRadius();
		this.lightDiameter = 2 * this.lightRadius;
	}

	@Override
	public void eventFired(final String eventName, final ModelObject source) {
		if (eventName.equals(GridObject.PAINT)) {
			// Paint events will always be triggered by Swing itself, so call paint directly
			paint(this.parent.getG2d());
		} else if (eventName.equals(VacBot.LIGHT_ON) || eventName.equals(VacBot.LIGHT_OFF)) {
			// Calculate the repaint region for the light
			final int xCentre = getUpperLeft().x + this.halfSquareSize;
			final int yCentre = getUpperLeft().y + this.halfSquareSize;
			getParent().repaint(xCentre - this.lightRadius, yCentre - this.lightRadius, this.lightDiameter,
					this.lightDiameter);
		} else if (eventName.endsWith(".step") || eventName.endsWith(".stop")) {
			// Repaint the whole VacBot and its square
			getParent().repaint(getUpperLeft().x, getUpperLeft().y, this.squareSize, this.squareSize);
		}
	}

	@Override
	public void paint(final Graphics g) {
		// Cast to Graphics2D to enable gradient fill and other features
		final Graphics2D g2d = (Graphics2D) g;

		// Set up some local variables, in an attempt to make this more readable
		final int xTopLeft = getUpperLeft().x;
		final int yTopLeft = getUpperLeft().y;
		final int xCentre = xTopLeft + this.halfSquareSize;
		final int yCentre = yTopLeft + this.halfSquareSize;
		final Direction direction = this.vacBot.getDirection();
		// x and y components of the current direction
		final double xDirection = direction.getXComponent();
		final double yDirection = direction.getYComponent();

		// Draw the wheels
		final Stroke oldStroke = g2d.getStroke();
		g2d.setStroke(this.wheelStroke);
		g2d.setColor(this.wheelColour);
		g2d.drawLine(
				xCentre - (int) Math.round(this.wheelRadius * xDirection)
						- (int) Math.round(this.wheelPosition * yDirection),
				yCentre - (int) Math.round(this.wheelRadius * yDirection)
						+ (int) Math.round(this.wheelPosition * xDirection),
				xCentre + (int) Math.round(this.wheelRadius * xDirection)
						- (int) Math.round(this.wheelPosition * yDirection),
				yCentre + (int) Math.round(this.wheelRadius * yDirection)
						+ (int) Math.round(this.wheelPosition * xDirection));
		g2d.drawLine(
				xCentre + (int) Math.round(this.wheelRadius * xDirection)
						+ (int) Math.round(this.wheelPosition * yDirection),
				yCentre + (int) Math.round(this.wheelRadius * yDirection)
						- (int) Math.round(this.wheelPosition * xDirection),
				xCentre - (int) Math.round(this.wheelRadius * xDirection)
						+ (int) Math.round(this.wheelPosition * yDirection),
				yCentre - (int) Math.round(this.wheelRadius * yDirection)
						- (int) Math.round(this.wheelPosition * xDirection));
		g2d.setStroke(oldStroke);

		// Draw the VacBot's main shape
		g2d.setColor(this.vacBotColour);
		g2d.fillOval(xTopLeft + this.clearance, yTopLeft + this.clearance, this.squareSize - this.doubleClearance,
				this.squareSize - this.doubleClearance);

		// Indicate which way we are pointing by drawing a face and some wheels
		// Draw the eyeballs
		g2d.setColor(this.eyeColour);
		g2d.fillOval(
				xCentre + (int) Math.round(this.eyePosition * xDirection)
						+ (int) Math.round(this.eyeHalfSpacing * yDirection) - this.eyeRadius,
				yCentre + (int) Math.round(this.eyePosition * yDirection)
						- (int) Math.round(this.eyeHalfSpacing * xDirection) - this.eyeRadius,
				this.eyeDiameter, this.eyeDiameter);
		g2d.fillOval(
				xCentre + (int) Math.round(this.eyePosition * xDirection)
						- (int) Math.round(this.eyeHalfSpacing * yDirection) - this.eyeRadius,
				yCentre + (int) Math.round(this.eyePosition * yDirection)
						+ (int) Math.round(this.eyeHalfSpacing * xDirection) - this.eyeRadius,
				this.eyeDiameter, this.eyeDiameter);
		// Draw the pupils
		g2d.setColor(this.pupilColour);
		g2d.fillOval(
				xCentre + (int) Math.round(this.pupilPosition * xDirection)
						+ (int) Math.round(this.eyeHalfSpacing * yDirection) - this.pupilRadius,
				yCentre + (int) Math.round(this.pupilPosition * yDirection)
						- (int) Math.round(this.eyeHalfSpacing * xDirection) - this.pupilRadius,
				this.pupilDiameter, this.pupilDiameter);
		g2d.fillOval(
				xCentre + (int) Math.round(this.pupilPosition * xDirection)
						- (int) Math.round(this.eyeHalfSpacing * yDirection) - this.pupilRadius,
				yCentre + (int) Math.round(this.pupilPosition * yDirection)
						+ (int) Math.round(this.eyeHalfSpacing * xDirection) - this.pupilRadius,
				this.pupilDiameter, this.pupilDiameter);
		// Draw the nose
		g2d.setColor(this.noseColour);
		g2d.fillOval(xCentre - this.noseRadius + (int) Math.round(this.nosePosition * xDirection),
				yCentre - this.noseRadius + (int) Math.round(this.nosePosition * yDirection), this.noseRadius * 2,
				this.noseRadius * 2);

		// Draw the light
		if (this.vacBot.isLightOn()) {
			// Save the current paint style so we can restore it later
			final Paint oldPaint = g2d.getPaint();
			// Wide centre in lightOnColour, with the edges fading to lightOffColour
			g2d.setPaint(
					new RadialGradientPaint(xCentre, yCentre, this.lightRadius, this.lightWeights, this.lightColours));
			g2d.fillOval(xCentre - this.lightRadius, yCentre - this.lightRadius, this.lightDiameter,
					this.lightDiameter);
			// Restore the original paint style
			g2d.setPaint(oldPaint);
		} else {
			g2d.setColor(this.lightOffColour);
			g2d.fillOval(xCentre - this.lightRadius, yCentre - this.lightRadius, this.lightDiameter,
					this.lightDiameter);
		}
	}
}
