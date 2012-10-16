package ca.chrislittle.noiseyapp.noise;

public class NoiseMap {
	
	private int width;
	private int height;

	private PerlinNoise source;
	private float[] noiseValues;
	// Minimum and maximum noise values stored in this noise array
	private float min;
	private float max;
	
	
	public NoiseMap(int width, int height, PerlinNoise noise_source) {
		this.width = width;
		this.height = height;
		
		min = 0.0f;
		max = 0.0f;
		
		source = noise_source;
		noiseValues = new float[width*height];
	}
	
	
	public int getWidth() { return width; }
	public int getHeight() { return height; }
	
	public float getNoise(int x, int y) {
		return noiseValues[y*width + x];
	}
	
	
	/**
	 * Generate the 2D noise array
	 */
	public void build() {
		float noise;
		float sample_x, sample_y;
		min = 0.0f;
		max = 0.0f;
		
		for (int y=0; y<this.height; ++y) {
			for (int x=0; x<this.width; ++x) {
				sample_x = (float)x/this.width;
				sample_y = (float)y/this.height;
				
				noise = source.noise(sample_x, sample_y, 0.5f);
				
				if (noise > 1.0f)
					noise = 1.0f;
				else if (noise < -1.0f)
					noise = -1.0f;
				
				// Keep track of highest and lowest noise values
				if (noise < min)
					min = noise;
				else if (noise > max)
					max = noise;
				
				
				noiseValues[y*width + x] = noise;
			}
		}
	}
	
	/**
	 * Remap minimum noise value to -1, and maximum value to 1
	 */
	public void remap() {
		float noise;
		float scale;
		
		// Avoid a divide by 0
		if (max - min == 0.0f)
			return;
		
		for (int y=0; y<this.height; ++y) {
			for (int x=0; x<this.width; ++x) {
				
				noise = noiseValues[y*width + x];
				
				// "Stretch" and remap noise distribution to more evenly fill [-1, 1] range
				scale = (noise - min) / (max - min);
				noise = -1.0f + 2.0f*scale;
				
				noiseValues[y*width + x] = noise;
			}
		}
		
		
		// Just in case, keep track of remapped min/max noise values
		if (min > -1.0f)
			min = -1.0f;
		
		if (max < 1.0f)
			max = 1.0f;
	}
}
