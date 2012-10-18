package ca.chrislittle.noiseyapp.noise;

public abstract class NoiseSource {
	/**
	 * Retrieve a noise value at the given coordinates
	 *  
	 * @param sample_x Noise sample coordinates
	 * @param sample_y
	 * @param sample_z
	 * @return A noise value clamped to the range [-1, 1]
	 */
	public abstract float noise(float sample_x, float sample_y, float sample_z);
}
