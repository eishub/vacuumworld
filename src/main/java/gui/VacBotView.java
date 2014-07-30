package gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.RadialGradientPaint;
import java.awt.Stroke;

import grid.Direction;
import grid.ModelListener;
import grid.ModelObject;
import vac.VacBot;

public class VacBotView extends GridObjectView implements ModelListener {

	private VacBot vacBot;
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
	
	public VacBotView(GridView parent, VacBot vacBot) {
		super(parent, vacBot);
		this.vacBot = vacBot;
		// Register the view to receive updates from the model
		vacBot.addListener(this);
		// Some LookAndFeel methods may return different values each time, so store them
		// If VacBot does not specify its own colour, choose a default
		if (vacBot.getColour() == null) vacBotColour = LookAndFeel.getVacBotColour();
		else vacBotColour = vacBot.getColour();
		eyeColour = LookAndFeel.getEyeColour();
		pupilColour = LookAndFeel.getPupilColour();
		noseColour = LookAndFeel.getNoseColour();
		lightOffColour = LookAndFeel.getLightOffColour();
		lightOnColour = LookAndFeel.getLightOnColour();
		lightWeights = new float[] {(float)0.0, (float)0.4, (float)1.0};
		lightColours = new Color[] {lightOnColour, lightOnColour, lightOffColour};
		wheelColour = LookAndFeel.getWheelColour();
		squareSize = LookAndFeel.getSquareSize();
		halfSquareSize = squareSize / 2;
		clearance = LookAndFeel.getVacBotClearance();
		doubleClearance = clearance * 2;
		eyeRadius = LookAndFeel.getEyeRadius();
		eyeDiameter = 2 * eyeRadius;
		eyePosition = LookAndFeel.getEyePosition();
		eyeHalfSpacing = LookAndFeel.getEyeSpacing() / 2;
		pupilRadius = LookAndFeel.getPupilRadius();
		pupilDiameter = 2 * pupilRadius;
		pupilOffset = LookAndFeel.getPupilOffset();
		pupilPosition = eyePosition + pupilOffset;
		noseRadius = LookAndFeel.getNoseRadius();
		nosePosition = LookAndFeel.getNosePosition();
		wheelWidth = LookAndFeel.getWheelWidth();
		wheelStroke = new BasicStroke(wheelWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
		wheelRadius = LookAndFeel.getWheelRadius();
		wheelPosition = LookAndFeel.getWheelPosition();
		lightRadius = LookAndFeel.getLightRadius();
		lightDiameter = 2 * lightRadius;
	}
	
	public void eventFired(String eventName, ModelObject source) {
		if (eventName.equals(VacBot.PAINT)) {
			// Paint events will always be triggered by Swing itself, so call paint directly
			paint(parent.getG2d());
		} else if (eventName.equals(VacBot.LIGHT_ON)
				|| eventName.equals(VacBot.LIGHT_OFF)) {
			// Calculate the repaint region for the light
			int xCentre = getUpperLeft().x + halfSquareSize;
			int yCentre = getUpperLeft().y + halfSquareSize;
			getParent().repaint(xCentre - lightRadius, yCentre - lightRadius,
					lightDiameter, lightDiameter);
		} else if (eventName.endsWith(".step") || eventName.endsWith(".stop")) {
			// Repaint the whole VacBot and its square
			getParent().repaint(getUpperLeft().x, getUpperLeft().y, squareSize, squareSize);
		}
	}
	
	public void paint(Graphics g) {
		// Cast to Graphics2D to enable gradient fill and other features
		Graphics2D g2d = (Graphics2D)g;
		
		// Set up some local variables, in an attempt to make this more readable
		int xTopLeft = getUpperLeft().x;
		int yTopLeft = getUpperLeft().y;
		int xCentre = xTopLeft + halfSquareSize;
		int yCentre = yTopLeft + halfSquareSize;
		Direction direction = vacBot.getDirection();
		// x and y components of the current direction
		double xDirection = direction.getXComponent();
		double yDirection = direction.getYComponent();
	
		// Draw the wheels
		Stroke oldStroke = g2d.getStroke();
		g2d.setStroke(wheelStroke);
		g2d.setColor(wheelColour);
		g2d.drawLine(xCentre - (int)Math.round(wheelRadius * xDirection)
				- (int)Math.round(wheelPosition * yDirection),
				yCentre - (int)Math.round(wheelRadius * yDirection)
				+ (int)Math.round(wheelPosition * xDirection),
				xCentre + (int)Math.round(wheelRadius * xDirection)
				- (int)Math.round(wheelPosition * yDirection),
				yCentre + (int)Math.round(wheelRadius * yDirection)
				+ (int)Math.round(wheelPosition * xDirection));
		g2d.drawLine(xCentre + (int)Math.round(wheelRadius * xDirection)
				+ (int)Math.round(wheelPosition * yDirection),
				yCentre + (int)Math.round(wheelRadius * yDirection)
				- (int)Math.round(wheelPosition * xDirection),
				xCentre - (int)Math.round(wheelRadius * xDirection)
				+ (int)Math.round(wheelPosition * yDirection),
				yCentre - (int)Math.round(wheelRadius * yDirection)
				- (int)Math.round(wheelPosition * xDirection));
		g2d.setStroke(oldStroke);
		
		// Draw the VacBot's main shape
		g2d.setColor(vacBotColour);
		g2d.fillOval(xTopLeft + clearance, yTopLeft + clearance,
				squareSize - doubleClearance, squareSize - doubleClearance);
		
		// Indicate which way we are pointing by drawing a face and some wheels
		// Draw the eyeballs
		g2d.setColor(eyeColour);
		g2d.fillOval(xCentre + (int)Math.round(eyePosition * xDirection)
				+ (int)Math.round(eyeHalfSpacing * yDirection) - eyeRadius,
				yCentre + (int)Math.round(eyePosition * yDirection)
				- (int)Math.round(eyeHalfSpacing * xDirection) - eyeRadius,
				eyeDiameter, eyeDiameter);
		g2d.fillOval(xCentre + (int)Math.round(eyePosition * xDirection)
				- (int)Math.round(eyeHalfSpacing * yDirection) - eyeRadius,
				yCentre + (int)Math.round(eyePosition * yDirection)
				+ (int)Math.round(eyeHalfSpacing * xDirection) - eyeRadius,
				eyeDiameter, eyeDiameter);
		// Draw the pupils
		g2d.setColor(pupilColour);
		g2d.fillOval(xCentre + (int)Math.round(pupilPosition * xDirection)
				+ (int)Math.round(eyeHalfSpacing * yDirection) - pupilRadius,
				yCentre + (int)Math.round(pupilPosition * yDirection) 
				- (int)Math.round(eyeHalfSpacing * xDirection) - pupilRadius,
				pupilDiameter, pupilDiameter);
		g2d.fillOval(xCentre + (int)Math.round(pupilPosition * xDirection) 
				- (int)Math.round(eyeHalfSpacing * yDirection) - pupilRadius,
				yCentre + (int)Math.round(pupilPosition * yDirection) 
				+ (int)Math.round(eyeHalfSpacing * xDirection) - pupilRadius,
				pupilDiameter, pupilDiameter);
		// Draw the nose
		g2d.setColor(noseColour);
		g2d.fillOval(xCentre - noseRadius + (int)Math.round(nosePosition * xDirection),
				yCentre - noseRadius + (int)Math.round(nosePosition * yDirection),
				noseRadius * 2, noseRadius * 2);
		
		// Draw the light
		if (vacBot.isLightOn()) {
			// Save the current paint style so we can restore it later
			Paint oldPaint = g2d.getPaint();
			// Wide centre in lightOnColour, with the edges fading to lightOffColour
			g2d.setPaint(new RadialGradientPaint(xCentre, yCentre, lightRadius,	lightWeights, lightColours));
			g2d.fillOval(xCentre - lightRadius, yCentre - lightRadius, lightDiameter, lightDiameter);
			// Restore the original paint style
			g2d.setPaint(oldPaint);
		} else {
			g2d.setColor(lightOffColour);
			g2d.fillOval(xCentre - lightRadius, yCentre - lightRadius, lightDiameter, lightDiameter);
		}
	}

}
