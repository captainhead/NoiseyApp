package ca.chrislittle.noiseyapp;

import java.awt.Color;

public class Texture {
	
	public int width;
	public int height;
	public int[] pixels;
	
	/**
	 * Generate a blank 2D image given a raster width and height.
	 * 
	 * @param textureWidth
	 * @param textureHeight
	 */
	public Texture(int textureWidth, int textureHeight) {
		width = textureWidth;
		height = textureHeight;
		
		pixels = new int[width*height];
		
		// Init all pixels to red
		int red = Color.RED.getRGB();
		for (int y=0; y<height; ++y) {
			for (int x=0; x<width; ++x) {
				pixels[y*width + x] = red;
			}
		}
	}
	
	/**
	 * Generate a 2D image from a noise map containing a 2D array of noise
	 * values (in the range between 0 and 1) and a colour gradient (which
	 * maps a value in the range 0 to 1 to a colour value).
	 * 
	 * @param noiseMap2D The NoiseMap that contains noise values within the range 0 and 1.
	 * @param cg A ColorGradient object which transforms the noise values to colours.
	 */
	public Texture(NoiseMap noiseMap2D, ColorGradient cg) {
		width = noiseMap2D.getWidth();
		height = noiseMap2D.getHeight();
		
		pixels = new int[width*height];
		
		
		// Skip generating colour values if noise values are outside the range 0 and 1
		if (noiseMap2D.getMin() < 0.0f || noiseMap2D.getMax() > 1.0f)
			return;
		
		
		// Compute and store colour value for each pixel.
		for (int y=0; y<height; ++y) {
			for (int x=0; x<width; ++x) {
				
				// Retrieve gradient colour value.
				pixels[y*width + x] = cg.getColor(noiseMap2D.getNoise(x, y)).getRGB();
			}
		}
	}
}
