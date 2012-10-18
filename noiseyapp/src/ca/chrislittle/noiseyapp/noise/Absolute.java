package ca.chrislittle.noiseyapp.noise;

/**
 * Simply retrieves a noise value from a NoiseSource and returns its absolute value.
 * 
 * @author Chris
 *
 */
public class Absolute extends NoiseSource {
	
	private NoiseSource source;
	
	
	public Absolute(NoiseSource s) {
		source = s;
	}

	@Override
	public float noise(float sample_x, float sample_y, float sample_z) {
		return Math.abs(source.noise(sample_x, sample_y, sample_z));
	}

}
