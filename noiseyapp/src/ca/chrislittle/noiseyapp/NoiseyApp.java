package ca.chrislittle.noiseyapp;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JPanel;

import ca.chrislittle.noiseyapp.noise.NoiseArray2D;
import ca.chrislittle.noiseyapp.noise.PerlinNoise;

public class NoiseyApp {

	public JFrame window;
	public TextureCanvas canvas;
	
	public Texture texture;
	
	
	public NoiseyApp() {
		// Setup window and texture drawing canvas
		window = new JFrame("Noisey");
		
		JPanel contentPanel = new JPanel(new BorderLayout());
		canvas = new TextureCanvas();
		
		contentPanel.add(canvas, BorderLayout.CENTER);
		window.setContentPane(contentPanel);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		window.setVisible(true);
		window.pack();
		window.setLocation(50, 50);
		
		
		// Create a texture
		texture = new Texture(768,768);
		//NoiseArray2D noiseArray = new NoiseArray2D(texture.width, texture.height, 10, 0.5f, 2.0f, true, true);
		//generatePlasmaTexture(texture, noiseArray);
		//generateColourStepTexture(texture, noiseArray);
		//generateCloudTexture(texture, noiseArray);
		//generateMarbleTexture(texture, noiseArray);
		
		NoiseArray2D noiseArray = new NoiseArray2D(texture.width, texture.height, 10, 0.5f, 4.0f, true, true);
		generateLickingFlameTexture(texture, noiseArray);
		//generateLightningTexture(texture,noiseArray);

		//generateWoodTexture(texture);
		
		
		// Send texture to display
		canvas.setImage(texture);
		canvas.repaint();
	}
	
	
	
	public void generatePlasmaTexture(Texture tex, NoiseArray2D noiseArray) {
		float noise;

		for (int y=0; y<tex.height; ++y) {
			for (int x=0; x<tex.width; ++x) {
				noise = noiseArray.getNoise(x, y);
				
				noise += 1.0f;
				noise *= 0.5f;
				
				// Apply the colour value
				tex.pixels[y*tex.width + x] = new Color(noise,noise,noise).getRGB();
			}
		}
	}
	
	public void generateColourStepTexture(Texture tex, NoiseArray2D noiseArray) {
		float noise;

		for (int y=0; y<tex.height; ++y) {
			for (int x=0; x<tex.width; ++x) {
				noise = noiseArray.getNoise(x, y);

				Color c = new Color(0xFFFFFF);
				if (noise > 0.8f)
					c = new Color(0x00FFFF);
				else if (noise > 0.4f)
					c = new Color(0x0000FF);
				else if (noise > 0.0f)
					c = new Color(0xFF00FF);
				else if (noise > -0.4f)
					c = new Color(0xFF0000);
				else if (noise > -0.8f)
					c = new Color(0xFFFF00);
				
				// Apply the colour value
				tex.pixels[y*tex.width + x] = c.getRGB();
			}
		}
	}
	
	public void generateCloudTexture(Texture tex, NoiseArray2D noiseArray) {
		float noise;
		float density = 0.6f; // Cloud density
		float sharpness = 0.01f; // Hardness of cloud edges (lower value -> sharper edges)
		
		ColorMap colorMap = new ColorMap();
		colorMap.addColor(0.0f, new Color(0x007FFF));
		colorMap.addColor(1.0f, new Color(0xFFFFFF));
		
		

		for (int y=0; y<tex.height; ++y) {
			for (int x=0; x<tex.width; ++x) {
				noise = noiseArray.getNoise(x, y);

				noise += 1.0f;
				noise *= 0.5f;
				
				// Apply exponential function (1 - sharpness^(noise - (1 - density)))
				float c = noise - (1.0f - density);
				if (c < 0.0f)
					c = 0.0f;
				
				c = (float)Math.pow((double)sharpness, (double)c);
				c = 1.0f - c;

				// Apply the colour value
				tex.pixels[y*tex.width + x] = colorMap.getColor(c).getRGB();
			}
		}
	}
	
	public void generateMarbleTexture(Texture tex, NoiseArray2D noiseArray) {
		float noise;
		float bands = 4.0f; // Frequency of the base sin-wave pattern
		float scale = 0.4f; // Scale the effect of noise on the sin-wave pattern
		
		float range = 2.0f*(float)Math.PI;
		
		ColorMap colorMap = new ColorMap();
		colorMap.addColor(0.0f, new Color(0x374912));
		colorMap.addColor(1.0f, new Color(0xD6E6CC));

		for (int y=0; y<tex.height; ++y) {
			for (int x=0; x<tex.width; ++x) {
				noise = noiseArray.getNoise(x, y);
				
				noise = (float)Math.sin(range*bands*((float)x/(float)tex.width + scale*noise));
				
				noise += 1.0f;
				noise *= 0.5f;
				
				// Apply the colour value
				tex.pixels[y*tex.width + x] = colorMap.getColor(noise).getRGB();
			}
		}
	}
	
	public void generateLickingFlameTexture(Texture tex, NoiseArray2D noiseArray) {
		float noise;
		
		ColorMap colorMap = new ColorMap();
		colorMap.addColor(0.0f, new Color(0x880000));
		colorMap.addColor(0.2f, new Color(0xFF0000));
		colorMap.addColor(0.6f, new Color(0xFFFF00));
		colorMap.addColor(1.0f, new Color(0xFFFFFF));

		for (int y=0; y<tex.height; ++y) {
			for (int x=0; x<tex.width; ++x) {
				noise = noiseArray.getNoise(x, y);
				
				noise = Math.abs(noise);
				
				// Apply the colour value
				tex.pixels[y*tex.width + x] = colorMap.getColor(noise).getRGB();
			}
		}
	}
	
	public void generateLightningTexture(Texture tex, NoiseArray2D noiseArray) {
		float noise;
		
		ColorMap colorMap = new ColorMap();
		colorMap.addColor(0.5f, new Color(0x000000));
		colorMap.addColor(0.8f, new Color(0x0000FF));
		colorMap.addColor(1.0f, new Color(0xFFFFFF));

		for (int y=0; y<tex.height; ++y) {
			for (int x=0; x<tex.width; ++x) {
				noise = noiseArray.getNoise(x, y);
				
				noise = 1.0f - Math.abs(noise);
				
				// Apply the colour value
				tex.pixels[y*tex.width + x] = colorMap.getColor(noise).getRGB();
			}
		}
	}
	
	
	public void generateWoodTexture(Texture tex) {
		float noise;
		float grainNoise;
		float sample_x;
		float sample_y;
		float freq;
		float amp;
		
		float majorFrequency = 2.0f;
		int majorOctaves = 1;
		float majorPersistence = 0.5f;
		float majorBands = 32.0f; // "Density" of major wood bands (i.e. frequency of discontinuities)
		
		float grainFrequency = 32.0f; // Control noise frequency
		float grainAmplitude = 0.3f; // Controls intensity of grain colour change
		int grainOctaves = 2;
		float grainPersistence = 0.5f;
		float grainStretch = 16.0f; // Control noise sample position stretch factor
		
		PerlinNoise noiseMaker = new PerlinNoise();
		
		ColorMap colorMap = new ColorMap();
		colorMap.addColor(0.0f, new Color(0x472207)); // Dark
		colorMap.addColor(1.0f, new Color(0xA55015)); // Light

		for (int y=0; y<tex.height; ++y) {
			for (int x=0; x<tex.width; ++x) {
				sample_x = (float)x/tex.width;
				sample_y = (float)y/tex.height;
				
				noise = 0.0f;
				
				// Major wood pattern
				freq = majorFrequency;
				amp = 1.0f;
				for (int i=0; i<majorOctaves; ++i) {
					noise += amp * noiseMaker.noise(sample_x*freq, sample_y*freq, 0.5f);
					
					freq *= 2.0f;
					amp *= majorPersistence;
				}
				
				if (noise > 1.0f)
					noise = 1.0f;
				if (noise < -1.0f)
					noise = -1.0f;

				noise += 1.0f;
				noise *= 0.5f;
				
				noise *= majorBands;
				noise = noise - (float)Math.floor(noise);
				
				// Fine wood grain detail
				grainNoise = 0.0f;
				freq = grainFrequency;
				amp = grainAmplitude;
				for (int i=0; i<grainOctaves; ++i) {
					grainNoise += amp * noiseMaker.noise(sample_x*freq*grainStretch, sample_y*freq, 0.5f);
					
					freq *= 2.0f;
					amp *= grainPersistence;
				}
				
				noise += grainNoise;
				
				if (noise > 1.0f)
					noise = 1.0f;
				if (noise < 0.0f)
					noise = 0.0f;

				// Apply the colour value
				Color result = colorMap.getColor(noise);
				tex.pixels[y*tex.width + x] = result.getRGB();
			}
		}
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		NoiseyApp app = new NoiseyApp();
	}

}
