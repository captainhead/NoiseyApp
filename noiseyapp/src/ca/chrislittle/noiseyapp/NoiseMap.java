package ca.chrislittle.noiseyapp;

import ca.chrislittle.noiseyapp.noise.NoiseSource;

/**
 * NoiseMap generates and contains a 2D array of noise values.
 * 
 * @author Chris
 *
 */
public class NoiseMap {
	
	private int width;
	private int height;
	
	private float left_bound, right_bound;
	private float bottom_bound, top_bound;

	private NoiseSource source;
	private float[] noiseValues;
	// Minimum and maximum noise values stored in this noise array
	private float min;
	private float max;
	
	
	/**
	 * Initialize the 2D noise map parameters
	 * 
	 * @param width Dimensions of the 2D array
	 * @param height
	 * @param noise_source NoiseSource object from which noise values are generated
	 */
	public NoiseMap(int width, int height, NoiseSource noise_source) {
		this.width = width;
		this.height = height;
		
		left_bound = 1.0f;
		right_bound = 2.0f;
		bottom_bound = 1.0f;
		top_bound = 2.0f;
		
		min = 0.0f;
		max = 0.0f;
		
		source = noise_source;
		noiseValues = new float[width*height];
	}
	
	
	public int getWidth() { return width; }
	public int getHeight() { return height; }
	
	public float getMin() { return min; }
	public float getMax() { return max; }
	
	/**
	 * Retrieve the noise value for position x,y in the noise array.
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public float getNoise(int x, int y) {
		return noiseValues[y*width + x];
	}
	
	
	/**
	 * Set sampling area bounds. Specifying a larger sampling area is like
	 * "zooming out" from an atlas/terrain map generated by the NoiseSource. 
	 * 
	 * @param left Minimum and maximum bounds along x axis.
	 * @param right
	 * @param bottom Minimum and maximum bounds along y axis.
	 * @param top
	 */
	public void setArea(float left, float right, float bottom, float top) {
		left_bound = left;
		right_bound = right;
		bottom_bound = bottom;
		top_bound = top;
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
				sample_x = left_bound + (right_bound-left_bound)*(float)x/this.width;
				sample_y = bottom_bound + (top_bound-bottom_bound)*(float)y/this.height;
				
				noise = source.noise(sample_x, sample_y, 0.0f);
				
				// Keep track of minimum and maximum noise values
				if (noise < min)
					min = noise;
				else if (noise > max)
					max = noise;
				
				
				noiseValues[y*width + x] = noise;
			}
		}
	}
	
	
	/**
	 * Remap the previously generated noise values (within the range between
	 * min and max) to the new range specified by minRange and maxRange. If
	 * maxRange is less than or equal to minRange, the stored noise values
	 * will not be modified.
	 * 
	 * @param minRange New minimum value that noise values will be mapped to.
	 * @param maxRange New maximum value that noise values will be mapped to.
	 */
	public void remapToRange(float minRange, float maxRange) {
		// Avoid a divide by 0.
		float maxSubMin = max - min;
		if (maxSubMin <= 0.0f)
			return;
		// Avoid an ill-formed new range.
		float newRangeSize = maxRange - minRange;
		if (newRangeSize <= 0.0f)
			return;
		
		float noise;
		float scale;
		float inverseMaxSubMin = 1.0f / maxSubMin;
		
		for (int y=0; y<this.height; ++y) {
			for (int x=0; x<this.width; ++x) {
				
				noise = noiseValues[y*width + x];
				
				// "Stretch" and remap noise distribution to more evenly fill [-1, 1] range
				scale = (noise - min) * inverseMaxSubMin;
				noise = minRange + newRangeSize*scale;
				
				// Clamp the noise value to ensure rounding error, etc. does push the value outide -1 to 1 range.
				if (noise > maxRange)
					noise = maxRange;
				if (noise < minRange)
					noise = minRange;
				
				noiseValues[y*width + x] = noise;
			}
		}
		
		
		// Just in case, set remapped min/max values to -1 and 1 respectively.
		min = minRange;
		max = maxRange;
	}
}
