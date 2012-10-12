package ca.chrislittle.noiseyapp;

import java.awt.Color;

public class Texture {
	
	public int width;
	public int height;
	public int[] pixels;
	
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
}
