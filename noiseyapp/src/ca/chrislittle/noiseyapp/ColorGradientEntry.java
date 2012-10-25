package ca.chrislittle.noiseyapp;

import java.awt.Color;

/**
 * Container for an entry into the color map. Contains a color map
 * (gradient spectrum) position and associated colour.
 * 
 * @author Chris
 *
 */
public class ColorGradientEntry {
	
	public float lowerBound; // Starting position in gradient spectrum, between [0.0, 1.0]
	public Color color;
	
	public ColorGradientEntry(float lower_bound, Color color) {
		this.lowerBound = lower_bound;
		this.color = color;
	}
}
