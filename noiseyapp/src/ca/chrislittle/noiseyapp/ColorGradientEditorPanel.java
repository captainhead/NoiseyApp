package ca.chrislittle.noiseyapp;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;

public class ColorGradientEditorPanel extends JPanel {
	private static final long serialVersionUID = 4542584126816930581L;

	private static final int width = 512;
	private static final int height = 40;
	
	ColorGradient gradient;
	
	
	public ColorGradientEditorPanel() {
		gradient = null;
	}
	
	/**
	 * Set the color gradient to be displayed.
	 * 
	 * @param cg ColorGradient object to be used.
	 */
	public void setColorGradient(ColorGradient cg) {
		gradient = cg;
	}

	@Override
	public void paintComponent(Graphics g) {
		if (gradient == null)
			return;
		
		Graphics2D g2d = (Graphics2D)g;
		g2d.setPaint(Color.black);
		g2d.fillRect(0, 0, width, 24);
		
		ColorGradientEntry[] gradArray = gradient.getGradientPoints();
		
		ColorGradientEntry e1, e2;
		float p1, p2;
		float start = 0.0f;
		for (int i=0; i<gradArray.length; ++i) {
			
			e1 = gradArray[i];
			if (i == gradArray.length-1) {
				e2 = gradArray[i];
				p1 = e1.lowerBound*width;
				p2 = width;
			}
			else {
				e2 = gradArray[i+1];
				p1 = e1.lowerBound*width;
				p2 = e2.lowerBound*width;
			}
			
			GradientPaint gradientPaint = new GradientPaint(p1,0,e1.color, p2,0,e2.color);
			g2d.setPaint(gradientPaint);
			g2d.fill(new Rectangle2D.Double(start, 0, p2-start, 24));
			
			start = p2;
		}
		
//		GradientPaint gradientPaint = new GradientPaint(0, 0, new Color(0xFF0000), width, 0, new Color(0x0000FF));
//		g2d.setPaint(gradientPaint);
//		g2d.fill(new Rectangle2D.Double(0, 0, width, 24));
	}
}
