package ca.chrislittle.noiseyapp;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class TextureCanvas extends Canvas {

	private static final long serialVersionUID = 1L;
	
	public BufferedImage image;
	
	
	public TextureCanvas() {
		// Default to 512x512 texture display
		Dimension defaultSize = new Dimension(768,768);
		setSize(defaultSize);
		setPreferredSize(defaultSize);
		setMaximumSize(defaultSize);
		setMinimumSize(defaultSize);
		
		image = new BufferedImage(defaultSize.width, defaultSize.height, BufferedImage.TYPE_INT_RGB);
	}
	
	
	public void setImage(Texture tex) {
		image = new BufferedImage(tex.width, tex.height, BufferedImage.TYPE_INT_RGB);
		
		int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
		for (int i=0; i<tex.width*tex.height; ++i) {
			pixels[i] = tex.pixels[i];
		}
	}
	
	@Override
	public void paint(Graphics g) {
		g.clearRect(0, 0, getWidth(), getHeight());
		g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
	}
}
