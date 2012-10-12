package ca.chrislittle.noiseyapp.noise;


public class NoiseArray2D {
	
	public int width;
	public int height;

	private float[] noiseValues;
	// Minimum and maximum noise values stored in this noise array
	private float min;
	private float max;
	// Indicates whether noise values have been clamped to [-1, 1]
	private boolean clamped;
	
	
	/**
	 * Generate an array of floating point noise values
	 * 
	 * @param width
	 * @param height
	 * @param octaves Number of fractal levels
	 * @param persistence Influence of subsequent octaves (usually 0.5 works well)
	 * @param base_frequency Higher base frequency makes "noisier" noise (usually 1.0 works well)
	 * @param clamp Clamp noise values to ensure they fall into range [-1, 1]
	 * @param remap Stretch/move noise values to be more "evenly distributed" within [-1, 1] range. This has the effect of more stark contrast when creating "plasma" textures.
	 */
	public NoiseArray2D(int width, int height, int octaves, float persistence, float base_frequency, boolean clamp, boolean remap) {
		this.width = width;
		this.height = height;
		
		noiseValues = new float[width*height];
		
		clamped = false;
		
		
		PerlinNoise noiseMaker = new PerlinNoise();
		
		
		float noise;
		float freq;
		float amp;
		float sample_x, sample_y;
		min = 0.0f;
		max = 0.0f;
		
		// Generate noise values
		for (int y=0; y<this.height; ++y) {
			for (int x=0; x<this.width; ++x) {
				sample_x = (float)x/this.width;
				sample_y = (float)y/this.height;
				
				// Sum fractal noise for the requested number of octaves
				noise = 0.0f;
				freq = base_frequency;
				amp = 1.0f;
				for (int i=0; i<octaves; ++i) {
					noise += amp * noiseMaker.noise(sample_x*freq, sample_y*freq, 0.5f);
					
					freq *= 2.0f;
					amp *= persistence;
				}
				
				// Track highest and lowest noise values
				if (noise < min)
					min = noise;
				else if (noise > max)
					max = noise;
				
				
				noiseValues[y*width + x] = noise;
			}
		}
		

		if (clamp || remap) {
			float scale;
			for (int y=0; y<this.height; ++y) {
				for (int x=0; x<this.width; ++x) {
					
					noise = noiseValues[y*width + x];
					
					if (remap) {
						// "Stretch" and remap noise distribution to more evenly fill [-1, 1] range
						scale = (noise - min) / (max - min);
						noise = -1.0f + 2.0f*scale;
					}
	
					if (clamp) {
						// Clamp noise to [-1, 1]
						if (noise > 1.0f)
							noise = 1.0f;
						if (noise < -1.0f)
							noise = -1.0f;
						
						clamped = true;
					}
					
					noiseValues[y*width + x] = noise;
				}
			}
			
			
			if (remap) {
				// Just in case, keep track of remapped min/max noise values
				if (min > -1.0f)
					min = -1.0f;
				
				if (max < 1.0f)
					max = 1.0f;
			}
		}
	}
	
	
	
	public float getNoise(int x, int y) {
		
		if (x >= width || y >= height) return 0.0f;
		
		return noiseValues[y*width + x];
	}
	
	public boolean isClamped() { return clamped; }
}
