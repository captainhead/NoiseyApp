package ca.chrislittle.noiseyapp;

import java.awt.Color;

/**
 * Container for an entry into the color map. Contains a gradient point
 * position and its associated colour.
 * 
 * @author Chris
 *
 */
public class ColorGradientEntry {
	
	private float gradientPoint; // Starting position in gradient spectrum, between [0.0, 1.0]
	private Color color;
	
	/**
	 * Create a gradient point entry given a gradient point position (expected
	 * to be a value between 0 and 1) and a Color object.
	 * 
	 * @param gradient_point Gradient point position.
	 * @param color Color object describing the desired colour.
	 */
	public ColorGradientEntry(float gradient_point, Color color) {
		this.gradientPoint = gradient_point;
		if (gradientPoint < 0.0f)
			gradientPoint = 0.0f;
		else if (gradientPoint > 1.0f)
			gradientPoint = 1.0f;
		
		this.color = color;
	}
	
	
	/**
	 * Return the gradient point position in range 0.0 to 1.0.
	 * 
	 * @return Gradient point position.
	 */
	public float getGradPoint() { return gradientPoint; }
	
	/**
	 * Return the colour for the gradient entry.
	 * 
	 * @return Color object for this entry.
	 */
	public Color getColor() { return color; }
}
