package ca.chrislittle.noiseyapp;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JPanel;

import ca.chrislittle.noiseyapp.noise.Absolute;
import ca.chrislittle.noiseyapp.noise.Add;
import ca.chrislittle.noiseyapp.noise.BrownianNoise;
import ca.chrislittle.noiseyapp.noise.Cylinders;
import ca.chrislittle.noiseyapp.noise.PerlinNoise;
import ca.chrislittle.noiseyapp.noise.Rotate;
import ca.chrislittle.noiseyapp.noise.ScaleBias;
import ca.chrislittle.noiseyapp.noise.ScaleInput;
import ca.chrislittle.noiseyapp.noise.Translate;
import ca.chrislittle.noiseyapp.noise.Turbulence;

public class NoiseyApp {
	
	public JFrame window;
	public TextureCanvas canvas;
	public ColorGradientEditorPanel gradientEditorPanel;
	
	public Texture texture;
	public ColorGradient colorGradient;
	
	
	public NoiseyApp() {
		// Setup window and texture drawing canvas
		window = new JFrame("Noisey");
		
		JPanel contentPanel = new JPanel(new BorderLayout());
		canvas = new TextureCanvas();
		gradientEditorPanel = new ColorGradientEditorPanel();
		
		contentPanel.add(canvas, BorderLayout.CENTER);
		contentPanel.add(gradientEditorPanel, BorderLayout.SOUTH);
		window.setContentPane(contentPanel);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		window.setVisible(true);
		window.setLocation(150, 150);
		window.pack();
		
		
		// Create a texture
		texture = new Texture(512,512);
		colorGradient = new ColorGradient();
		generatePlasmaTexture(texture);
//		generateCloudTexture(texture);
//		generateMarbleTexture(texture);
		
//		generateLickingFlameTexture(texture);
//		generateLightningTexture(texture);

//		generateWoodTexture(texture);
//		generateWood2Texture(texture);
//		generateWood3Texture(texture);
		
//		generateLandmass(texture);
		
		// Send texture to display
		canvas.setImage(texture);
		canvas.repaint();
		gradientEditorPanel.setColorGradient(colorGradient);
		gradientEditorPanel.repaint();
	}
	
	
	
	public void generatePlasmaTexture(Texture tex) {
		PerlinNoise noiseMaker = new PerlinNoise();
		
		BrownianNoise bNoiseMaker = new BrownianNoise(noiseMaker);
		bNoiseMaker.setOctaves(8);
		
		NoiseMap map = new NoiseMap(tex.width, tex.height, bNoiseMaker);
		map.setArea(1.2f, 3.2f, 1.2f, 3.2f);
		map.build();
		map.remap();
		
		colorGradient.addColor(0.0f, new Color(0x000000));
		colorGradient.addColor(1.0f, new Color(0xEE44FF));
//		colorGradient.addColor(0.0f,  new Color(0x000000));
//		colorGradient.addColor(0.143f, new Color(0x0000FF));
//		colorGradient.addColor(0.286f, new Color(0xFF00FF));
//		colorGradient.addColor(0.429f, new Color(0xFF0000));
//		colorGradient.addColor(0.571f, new Color(0xFFFF00));
//		colorGradient.addColor(0.714f, new Color(0x00FF00));
//		colorGradient.addColor(0.857f, new Color(0x00FFFF));
//		colorGradient.addColor(1.0f,  new Color(0xFFFFFF));
//		colorGradient.addColor(0.2f, new Color(0x000000));
//		colorGradient.addColor(0.3f, new Color(0x0000FF));
//		colorGradient.addColor(0.4f, new Color(0x00FFFF));
//		colorGradient.addColor(0.5f, new Color(0x00FF00));
//		colorGradient.addColor(0.6f, new Color(0xFFFF00));
//		colorGradient.addColor(0.7f, new Color(0xFF0000));
//		colorGradient.addColor(0.8f, new Color(0xFFFFFF));

		float noise;
		for (int y=0; y<tex.height; ++y) {
			for (int x=0; x<tex.width; ++x) {
				noise = map.getNoise(x, y);
				
				noise += 1.0f;
				noise *= 0.5f;
				
				// Apply the colour value
				tex.pixels[y*tex.width + x] = colorGradient.getColor(noise).getRGB();
			}
		}
	}
	
	public void generateCloudTexture(Texture tex) {
		float density = 0.6f; // Cloud density
		float sharpness = 0.013f; // Hardness of cloud edges (lower value -> sharper edges)
		
		BrownianNoise bNoiseMaker = new BrownianNoise(new PerlinNoise());
		bNoiseMaker.setOctaves(10);
		bNoiseMaker.setBaseFrequency(2.0f);
		
		NoiseMap map = new NoiseMap(tex.width, tex.height, bNoiseMaker);
		map.setArea(1.2f, 3.2f, 1.2f, 3.2f);
		map.build();
		map.remap();
		
		colorGradient.addColor(0.0f, new Color(0x007FFF));
		colorGradient.addColor(1.0f, new Color(0xFFFFFF));
		
		

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
				tex.pixels[y*tex.width + x] = colorGradient.getColor(c).getRGB();
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
		
		NoiseMap map = new NoiseMap(tex.width, tex.height, bNoiseMaker);
		map.setArea(1.2f, 3.2f, 1.2f, 3.2f);
		map.build();
		map.remap();
		
		colorGradient.addColor(0.0f, new Color(0x225533));
		colorGradient.addColor(1.0f, new Color(0xF8FFF8));

		float noise;
		for (int y=0; y<tex.height; ++y) {
			for (int x=0; x<tex.width; ++x) {
				noise = map.getNoise(x, y);
				
				noise = (float)Math.sin(range*bands*((float)x/(float)tex.width + scale*noise));
				
				noise += 1.0f;
				noise *= 0.5f;
				
				// Apply the colour value
				tex.pixels[y*tex.width + x] = colorGradient.getColor(noise).getRGB();
			}
		}
	}
	
	public void generateLickingFlameTexture(Texture tex) {
		PerlinNoise noiseMaker = new PerlinNoise();
		
		Absolute absNoiseMaker = new Absolute(noiseMaker);

		BrownianNoise bNoiseMaker = new BrownianNoise(absNoiseMaker);
		bNoiseMaker.setOctaves(16);
		
		NoiseMap map = new NoiseMap(tex.width, tex.height, bNoiseMaker);
		map.setArea(1.2f, 3.2f, 1.2f, 3.2f);
		map.build();
		map.remap();
		
		colorGradient.addColor(0.0f, new Color(0xAA0000));
		colorGradient.addColor(0.2f, new Color(0xFF0000));
		colorGradient.addColor(0.8f, new Color(0xFFFF00));
		colorGradient.addColor(1.0f, new Color(0xFFFFFF));
		
		float noise;
		for (int y=0; y<tex.height; ++y) {
			for (int x=0; x<tex.width; ++x) {
				noise = map.getNoise(x,y);
				
				noise = Math.abs(noise);
				
				// Apply the colour value
				tex.pixels[y*tex.width + x] = colorGradient.getColor(noise).getRGB();
			}
		}
	}
	
	public void generateLightningTexture(Texture tex) {
		BrownianNoise bNoiseMaker = new BrownianNoise(new PerlinNoise());
		bNoiseMaker.setOctaves(8);
		
		NoiseMap map = new NoiseMap(tex.width, tex.height, bNoiseMaker);
		map.setArea(1.2f, 5.2f, 1.2f, 5.2f);
		map.build();
		map.remap();
		
		colorGradient.addColor(0.5f, new Color(0x000000));
		colorGradient.addColor(0.94f, new Color(0x0000FF));
		colorGradient.addColor(1.0f, new Color(0xFFFFFF));

		float noise;
		for (int y=0; y<tex.height; ++y) {
			for (int x=0; x<tex.width; ++x) {
				noise = map.getNoise(x, y);
				
				noise = 1.0f - Math.abs(noise);
				
				// Apply the colour value
				tex.pixels[y*tex.width + x] = colorGradient.getColor(noise).getRGB();
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
		
		colorGradient.addColor(0.0f, new Color(0x472207)); // Dark
		colorGradient.addColor(1.0f, new Color(0xA55015)); // Light

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
				Color result = colorGradient.getColor(noise);
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
		
		
//		colorGradient.addColor(0.0f, new Color(0x000000));
//		colorGradient.addColor(1.0f, new Color(0xFFFFFF));
//		
//		colorGradient.addColor(0.0f, new Color(0x472207)); // Dark
//		colorGradient.addColor(1.0f, new Color(0xA55015)); // Light
		
		colorGradient.addColor(0.0f, new Color(0x3A1D0D)); // Dark (top)
		colorGradient.addColor(0.17f, new Color(0x532913));
		colorGradient.addColor(0.3f, new Color(0x75462A));
		colorGradient.addColor(0.375f, new Color(0x7B4C30));
		colorGradient.addColor(0.5f, new Color(0x7B4C30));
		colorGradient.addColor(0.6f, new Color(0x200900));
		colorGradient.addColor(0.625f, new Color(0x79462B));
		colorGradient.addColor(0.7f, new Color(0x79462B));
		colorGradient.addColor(0.81f, new Color(0x4A260E));
		colorGradient.addColor(1.0f, new Color(0x6C3E26)); // Light (bottom)

//		colorGradient.addColor(0.0f, new Color(0x3A1D0D)); // Dark (top)
//		colorGradient.addColor(0.33f, new Color(0x532913));
//		colorGradient.addColor(0.4f, new Color(0x75462A));
//		colorGradient.addColor(0.4375f, new Color(0x7B4C30));
//		colorGradient.addColor(0.5f, new Color(0x7B4C30));
//		colorGradient.addColor(0.55f, new Color(0x200900));
//		colorGradient.addColor(0.5625f, new Color(0x79462B));
//		colorGradient.addColor(0.6f, new Color(0x79462B));
//		colorGradient.addColor(0.66f, new Color(0x4A260E));
//		colorGradient.addColor(1.0f, new Color(0x6C3E26)); // Light (bottom)
//		
//		colorGradient.addColor(0.05f, new Color(0x6D3F27));
//		colorGradient.addColor(0.2f, new Color(0x4A260E));
//		colorGradient.addColor(0.3f, new Color(0x864F30));
//		colorGradient.addColor(0.4f, new Color(0x864F30));
//		colorGradient.addColor(0.5f, new Color(0x200C03));
//		colorGradient.addColor(0.6f, new Color(0x7D4A2D));
//		colorGradient.addColor(0.75f, new Color(0x7D4A2D));
//		colorGradient.addColor(0.9f, new Color(0x482014));

		
		float noise;
		for (int y=0; y<tex.height; ++y) {
			for (int x=0; x<tex.width; ++x) {
				noise = map.getNoise(x, y);
				
				noise += 1.0f;
				noise *= 0.5f;
				
				// Apply the colour value
				tex.pixels[y*tex.width + x] = colorGradient.getColor(noise).getRGB();
			}
		}
	}
	

	public void generateWood3Texture(Texture tex) {
		// The "rings" of the wood log
		Cylinders baseWood = new Cylinders();
		baseWood.setFrequency(10.0f);

		// Perturb the ring pattern
		Turbulence perturbedWood = new Turbulence(baseWood);
		perturbedWood.setBaseAmplitude(0.03f);
		perturbedWood.setBaseFrequency(2.0f);
		perturbedWood.setOctaves(4);

		// Fractal noise for finer details
		BrownianNoise woodGrain = new BrownianNoise(new PerlinNoise());
		woodGrain.setOctaves(2);
		woodGrain.setBaseFrequency(16.0f);
		
		// Vertically stretch finer noise pattern
		ScaleInput stretchedWoodGrain = new ScaleInput(woodGrain);
		stretchedWoodGrain.setScaleX(16.0f);
		
		// Scale down the effect of the finer grains
		ScaleBias scaledWoodGrain = new ScaleBias(stretchedWoodGrain);
		scaledWoodGrain.setScale(0.5f);
		
		// Combine rings and fine grain detail
		Add combinedWood = new Add(perturbedWood, scaledWoodGrain);

		// Translate further away from the 0,0,0 origin		
		Translate translatedWood = new Translate(combinedWood);
		translatedWood.translateY(15.2f);
		// Translate slightly outward from the centre of the log
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
		map.setArea(-0.5f, 0.5f, -0.5f, 0.5f);
		map.build();
		map.remap();
		
		
		colorGradient.addColor(0.15f, new Color(0x281409)); // Dark
		colorGradient.addColor(1.0f, new Color(0x672C08)); // Light
		
		
		float noise;
		for (int y=0; y<tex.height; ++y) {
			for (int x=0; x<tex.width; ++x) {
				noise = map.getNoise(x, y);
				
				noise += 1.0f;
				noise *= 0.5f;
				
				// Apply the colour value
				tex.pixels[y*tex.width + x] = colorGradient.getColor(noise).getRGB();
			}
		}
	}
	

	public void generateLandmass(Texture tex) {
		// Generate overall landmass pattern
		BrownianNoise landmassNoise = new BrownianNoise(new PerlinNoise());
		landmassNoise.setOctaves(8);
		
		NoiseMap map = new NoiseMap(tex.width, tex.height, landmassNoise);
		map.setArea(1.2f, 5.2f, 1.2f, 5.2f);
		map.build();
		map.remap();
		
		
		// Generate landmass colour variance pattern
		BrownianNoise varianceNoise = new BrownianNoise(new PerlinNoise());
		varianceNoise.setOctaves(16);
		
		ScaleBias scaledVarianceNoise = new ScaleBias(varianceNoise);
		scaledVarianceNoise.setScale(0.085f);
		
//		BrownianNoise fineVarianceNoise = new BrownianNoise(new PerlinNoise());
//		fineVarianceNoise.setBaseFrequency(64.0f);
//		fineVarianceNoise.setOctaves(8);
//		
//		ScaleBias fineScaledVarianceNoise = new ScaleBias(fineVarianceNoise);
//		fineScaledVarianceNoise.setScale(0.05f);
		
		NoiseMap varianceMap = new NoiseMap(tex.width, tex.height, scaledVarianceNoise);
		varianceMap.setArea(1.2f, 33.2f, 1.2f, 33.2f);
		varianceMap.build();
		
		
		colorGradient.addColor(0.08f, new Color(0x0C49FF)); // Deep water
		colorGradient.addColor(0.27f, new Color(0x42C6FF)); // Shallow water
		colorGradient.addColor(0.37f, new Color(0xFFD07F)); // Sand
		colorGradient.addColor(0.48f, new Color(0x00AD00)); // Grass
		colorGradient.addColor(0.7f, new Color(0x00AD00)); // Grass
		colorGradient.addColor(0.83f, new Color(0x888888)); // Rock
		colorGradient.addColor(0.9f, new Color(0x888888)); // Rock
		colorGradient.addColor(0.95f, new Color(0xEEEEEE)); // Snow
		
		
		float noise;
		float variance;
		Color mappedColor;
		float[] colorComponents;
		Color finalColor;
		for (int y=0; y<tex.height; ++y) {
			for (int x=0; x<tex.width; ++x) {
				noise = map.getNoise(x, y);
				variance = 1.0f + varianceMap.getNoise(x, y);
				
				noise += 1.0f;
				noise *= 0.5f;
				
				// Lighten/darken the colour based on the variance noise map
				mappedColor = colorGradient.getColor(noise);
				colorComponents = mappedColor.getComponents(null);
				for (int i=0; i<colorComponents.length; ++i) {
					colorComponents[i] *= variance;
					if (colorComponents[i] > 1.0f)
						colorComponents[i] = 1.0f;
					if (colorComponents[i] < 0.0f)
						colorComponents[i] = 0.0f;
				}
				finalColor = new Color(colorComponents[0],colorComponents[1],colorComponents[2]);
				
				
				// Apply the colour value
				tex.pixels[y*tex.width + x] = finalColor.getRGB();
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
