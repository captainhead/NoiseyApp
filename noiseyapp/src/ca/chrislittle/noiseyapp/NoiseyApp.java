package ca.chrislittle.noiseyapp;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JPanel;

import ca.chrislittle.noiseyapp.noise.Absolute;
import ca.chrislittle.noiseyapp.noise.Add;
import ca.chrislittle.noiseyapp.noise.BrownianNoise;
import ca.chrislittle.noiseyapp.noise.Cylinders;
import ca.chrislittle.noiseyapp.noise.NoiseMap;
import ca.chrislittle.noiseyapp.noise.PerlinNoise;
import ca.chrislittle.noiseyapp.noise.Rotate;
import ca.chrislittle.noiseyapp.noise.ScaleBias;
import ca.chrislittle.noiseyapp.noise.ScaleInput;
import ca.chrislittle.noiseyapp.noise.Translate;
import ca.chrislittle.noiseyapp.noise.Turbulence;

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
		window.pack();
		window.setResizable(false);
		window.setVisible(true);
		window.setLocation(100, 10);
		
		
		// Create a texture
		texture = new Texture(512,512);
		//generatePlasmaTexture(texture);
		//generateCloudTexture(texture);
		//generateMarbleTexture(texture);
		
		//generateLickingFlameTexture(texture);
		//generateLightningTexture(texture);

		//generateWoodTexture(texture);
		//generateWood2Texture(texture);
		generateWood3Texture(texture);
		
		
		// Send texture to display
		canvas.setImage(texture);
		canvas.repaint();
	}
	
	
	
	public void generatePlasmaTexture(Texture tex) {
		PerlinNoise noiseMaker = new PerlinNoise();
		
		BrownianNoise bNoiseMaker = new BrownianNoise(noiseMaker);
		bNoiseMaker.setOctaves(10);
		bNoiseMaker.setBaseFrequency(2.0f);
		
		NoiseMap map = new NoiseMap(tex.width, tex.height, bNoiseMaker);
		map.build();
		map.remap();
		
		ColorMap colorMap = new ColorMap();
		colorMap.addColor(0.0f, new Color(0x000000));
		colorMap.addColor(1.0f, new Color(0xEE44FF));
//		colorMap.addColor(0.0f,  new Color(0x000000));
//		colorMap.addColor(0.143f, new Color(0x0000FF));
//		colorMap.addColor(0.286f, new Color(0xFF00FF));
//		colorMap.addColor(0.429f, new Color(0xFF0000));
//		colorMap.addColor(0.571f, new Color(0xFFFF00));
//		colorMap.addColor(0.714f, new Color(0x00FF00));
//		colorMap.addColor(0.857f, new Color(0x00FFFF));
//		colorMap.addColor(1.0f,  new Color(0xFFFFFF));
//		colorMap.addColor(0.2f, new Color(0x000000));
//		colorMap.addColor(0.3f, new Color(0x0000FF));
//		colorMap.addColor(0.4f, new Color(0x00FFFF));
//		colorMap.addColor(0.5f, new Color(0x00FF00));
//		colorMap.addColor(0.6f, new Color(0xFFFF00));
//		colorMap.addColor(0.7f, new Color(0xFF0000));
//		colorMap.addColor(0.8f, new Color(0xFFFFFF));

		float noise;
		for (int y=0; y<tex.height; ++y) {
			for (int x=0; x<tex.width; ++x) {
				noise = map.getNoise(x, y);
				
				noise += 1.0f;
				noise *= 0.5f;
				
				// Apply the colour value
				tex.pixels[y*tex.width + x] = colorMap.getColor(noise).getRGB();
			}
		}
	}
	
	public void generateCloudTexture(Texture tex) {
		float density = 0.6f; // Cloud density
		float sharpness = 0.013f; // Hardness of cloud edges (lower value -> sharper edges)

		PerlinNoise noiseMaker = new PerlinNoise();
		
		BrownianNoise bNoiseMaker = new BrownianNoise(noiseMaker);
		bNoiseMaker.setOctaves(10);
		bNoiseMaker.setBaseFrequency(2.0f);
		
		NoiseMap map = new NoiseMap(tex.width, tex.height, bNoiseMaker);
		map.build();
		map.remap();
		
		ColorMap colorMap = new ColorMap();
		colorMap.addColor(0.0f, new Color(0x007FFF));
		colorMap.addColor(1.0f, new Color(0xFFFFFF));
		
		

		float noise;
		for (int y=0; y<tex.height; ++y) {
			for (int x=0; x<tex.width; ++x) {
				noise = map.getNoise(x, y); //noiseArray.getNoise(x, y);

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
	
	public void generateMarbleTexture(Texture tex) {
		float bands = 4.0f; // Frequency of the base sin-wave pattern
		float scale = 0.4f; // Scale the effect of noise on the sin-wave pattern
		float range = 2.0f*(float)Math.PI;

		PerlinNoise noiseMaker = new PerlinNoise();

		BrownianNoise bNoiseMaker = new BrownianNoise(noiseMaker);
		bNoiseMaker.setOctaves(10);
		bNoiseMaker.setBaseFrequency(2.0f);
		
		NoiseMap map = new NoiseMap(tex.width, tex.height, bNoiseMaker);
		map.build();
		map.remap();
		
		ColorMap colorMap = new ColorMap();
		colorMap.addColor(0.0f, new Color(0x225533));
		colorMap.addColor(1.0f, new Color(0xF8FFF8));

		float noise;
		for (int y=0; y<tex.height; ++y) {
			for (int x=0; x<tex.width; ++x) {
				noise = map.getNoise(x, y);
				
				noise = (float)Math.sin(range*bands*((float)x/(float)tex.width + scale*noise));
				
				noise += 1.0f;
				noise *= 0.5f;
				
				// Apply the colour value
				tex.pixels[y*tex.width + x] = colorMap.getColor(noise).getRGB();
			}
		}
	}
	
	public void generateLickingFlameTexture(Texture tex) {
		PerlinNoise noiseMaker = new PerlinNoise();
		
		Absolute absNoiseMaker = new Absolute(noiseMaker);

		BrownianNoise bNoiseMaker = new BrownianNoise(absNoiseMaker);
		bNoiseMaker.setOctaves(4);
		bNoiseMaker.setBaseFrequency(2.0f);
		
		NoiseMap map = new NoiseMap(tex.width, tex.height, bNoiseMaker);
		map.build();
		map.remap();
		
		ColorMap colorMap = new ColorMap();
		colorMap.addColor(0.0f, new Color(0x990000));
		colorMap.addColor(0.2f, new Color(0xFF0000));
		colorMap.addColor(0.6f, new Color(0xFFFF00));
		colorMap.addColor(1.0f, new Color(0xFFFFFF));
		
		float noise;
		for (int y=0; y<tex.height; ++y) {
			for (int x=0; x<tex.width; ++x) {
				noise = map.getNoise(x,y);
				
				noise = Math.abs(noise);
				
				// Apply the colour value
				tex.pixels[y*tex.width + x] = colorMap.getColor(noise).getRGB();
			}
		}
	}
	
	public void generateLightningTexture(Texture tex) {
		PerlinNoise noiseMaker = new PerlinNoise();

		BrownianNoise bNoiseMaker = new BrownianNoise(noiseMaker);
		bNoiseMaker.setOctaves(8);
		bNoiseMaker.setBaseFrequency(4.0f);
		
		NoiseMap map = new NoiseMap(tex.width, tex.height, bNoiseMaker);
		map.build();
		map.remap();
		
		ColorMap colorMap = new ColorMap();
		colorMap.addColor(0.5f, new Color(0x000000));
		colorMap.addColor(0.94f, new Color(0x0000FF));
		colorMap.addColor(1.0f, new Color(0xFFFFFF));

		float noise;
		for (int y=0; y<tex.height; ++y) {
			for (int x=0; x<tex.width; ++x) {
				noise = map.getNoise(x, y);
				
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
		
		float majorFrequency = 1.0f;
		int majorOctaves = 1;
		float majorPersistence = 0.5f;
		float majorBands = 32.0f; // "Density" of major wood bands (i.e. frequency of discontinuities)
		
		float grainFrequency = 32.0f; // Control noise frequency
		float grainAmplitude = 0.2f; // Controls intensity of grain colour change
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
				
				
				// Major wood pattern
				noise = 0.0f;
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
	
	public void generateWood2Texture(Texture tex) {
		// Major wood grain pattern
		PerlinNoise grainNoise = new PerlinNoise();

		BrownianNoise bGrainNoise = new BrownianNoise(grainNoise);
		bGrainNoise.setOctaves(10);
		bGrainNoise.setBaseFrequency(1.0f);
		
		ScaleInput scaleGrainNoise = new ScaleInput(bGrainNoise);
		scaleGrainNoise.setScaleX(6.0f);
		
		// Finer bumps/detail pattern
		PerlinNoise bumpNoise = new PerlinNoise();

		BrownianNoise bBumpNoise = new BrownianNoise(bumpNoise);
		bBumpNoise.setOctaves(4);
		bBumpNoise.setBaseFrequency(1.0f);
		
		ScaleInput stretchedBumpNoise = new ScaleInput(bBumpNoise);
		stretchedBumpNoise.setScaleX(50.0f);
		
		ScaleBias scaledBumpNoise = new ScaleBias(stretchedBumpNoise);
		scaledBumpNoise.setScale(0.1f);
		
		// Combine patterns
		Add noiseSum = new Add(scaleGrainNoise, scaledBumpNoise);
		
		NoiseMap map = new NoiseMap(tex.width, tex.height, noiseSum);
		map.build();
		map.remap();
		
		
		ColorMap colorMap = new ColorMap();
//		colorMap.addColor(0.0f, new Color(0x000000));
//		colorMap.addColor(1.0f, new Color(0xFFFFFF));
		
//		colorMap.addColor(0.0f, new Color(0x472207)); // Dark
//		colorMap.addColor(1.0f, new Color(0xA55015)); // Light
		
		colorMap.addColor(0.0f, new Color(0x3A1D0D)); // Dark (top)
		colorMap.addColor(0.17f, new Color(0x532913));
		colorMap.addColor(0.3f, new Color(0x75462A));
		colorMap.addColor(0.375f, new Color(0x7B4C30));
		colorMap.addColor(0.5f, new Color(0x7B4C30));
		colorMap.addColor(0.6f, new Color(0x200900));
		colorMap.addColor(0.625f, new Color(0x79462B));
		colorMap.addColor(0.7f, new Color(0x79462B));
		colorMap.addColor(0.81f, new Color(0x4A260E));
		colorMap.addColor(1.0f, new Color(0x6C3E26)); // Light (bottom)

//		colorMap.addColor(0.0f, new Color(0x3A1D0D)); // Dark (top)
//		colorMap.addColor(0.33f, new Color(0x532913));
//		colorMap.addColor(0.4f, new Color(0x75462A));
//		colorMap.addColor(0.4375f, new Color(0x7B4C30));
//		colorMap.addColor(0.5f, new Color(0x7B4C30));
//		colorMap.addColor(0.55f, new Color(0x200900));
//		colorMap.addColor(0.5625f, new Color(0x79462B));
//		colorMap.addColor(0.6f, new Color(0x79462B));
//		colorMap.addColor(0.66f, new Color(0x4A260E));
//		colorMap.addColor(1.0f, new Color(0x6C3E26)); // Light (bottom)
		
//		colorMap.addColor(0.05f, new Color(0x6D3F27));
//		colorMap.addColor(0.2f, new Color(0x4A260E));
//		colorMap.addColor(0.3f, new Color(0x864F30));
//		colorMap.addColor(0.4f, new Color(0x864F30));
//		colorMap.addColor(0.5f, new Color(0x200C03));
//		colorMap.addColor(0.6f, new Color(0x7D4A2D));
//		colorMap.addColor(0.75f, new Color(0x7D4A2D));
//		colorMap.addColor(0.9f, new Color(0x482014));

		
		float noise;
		for (int y=0; y<tex.height; ++y) {
			for (int x=0; x<tex.width; ++x) {
				noise = map.getNoise(x, y);
				
				noise += 1.0f;
				noise *= 0.5f;
				
				// Apply the colour value
				tex.pixels[y*tex.width + x] = colorMap.getColor(noise).getRGB();
			}
		}
	}
	

	public void generateWood3Texture(Texture tex) {
		// The "rings" of the wood log
		Cylinders baseWood = new Cylinders();
		baseWood.setFrequency(12.0f);

		// Perturb the ring pattern
		Turbulence perturbedWood = new Turbulence(baseWood);
		perturbedWood.setBaseAmplitude(0.03f);
		perturbedWood.setBaseFrequency(2.0f);
		perturbedWood.setOctaves(4);

		// Fractal noise for finer details
		BrownianNoise woodGrain = new BrownianNoise(new PerlinNoise());
		woodGrain.setOctaves(2);
		woodGrain.setBaseFrequency(32.0f);
		
		// Vertically stretch finer noise pattern
		ScaleInput stretchedWoodGrain = new ScaleInput(woodGrain);
		stretchedWoodGrain.setScaleX(16.0f);
		
		// Scale down the effect of the finer grains
		ScaleBias scaledWoodGrain = new ScaleBias(stretchedWoodGrain);
		scaledWoodGrain.setScale(0.5f);
		
		// Combine rings and fine grain detail
		Add combinedWood = new Add(perturbedWood, scaledWoodGrain);
		
		// Translate slightly outward from the centre of the log
		Translate translatedWood = new Translate(combinedWood);
		translatedWood.translateZ(-0.1f);
		
		// Sample the log at an angle
		Rotate rotatedWood = new Rotate(translatedWood);
		rotatedWood.setAngles(7.0f, 0.0f, 0.0f);
		
		// One last bit of turbulence
		Turbulence finalWood = new Turbulence(rotatedWood);
		finalWood.setBaseFrequency(2.0f);
		finalWood.setBaseAmplitude(0.015f);
		finalWood.setOctaves(4);
		
		
		NoiseMap map = new NoiseMap(tex.width, tex.height, finalWood);
		map.build();
		map.remap();
		
		
		ColorMap colorMap = new ColorMap();
		colorMap.addColor(0.15f, new Color(0x281409)); // Dark
		colorMap.addColor(1.0f, new Color(0x672C08)); // Light
		
		
		float noise;
		for (int y=0; y<tex.height; ++y) {
			for (int x=0; x<tex.width; ++x) {
				noise = map.getNoise(x, y);
				
				noise += 1.0f;
				noise *= 0.5f;
				
				// Apply the colour value
				tex.pixels[y*tex.width + x] = colorMap.getColor(noise).getRGB();
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
