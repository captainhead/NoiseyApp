package ca.chrislittle.noiseyapp.noise;

/**
 * Adds a random (Perlin noise) turbulence to the input values of a noise
 * source.
 * 
 * @author Chris
 *
 */
public class Turbulence extends NoiseSource {
	
	private NoiseSource source;
	
	private BrownianNoise xTurb;
	private BrownianNoise yTurb;
	private BrownianNoise zTurb;
	
	
	public Turbulence(NoiseSource s) {
		source = s;
		
		xTurb = new BrownianNoise(new PerlinNoise());
		yTurb = new BrownianNoise(new PerlinNoise());
		zTurb = new BrownianNoise(new PerlinNoise());
		
		setOctaves(3);
	}
	
	public void setOctaves(int o) {
		xTurb.setOctaves(o);
		yTurb.setOctaves(o);
		zTurb.setOctaves(o);
	}
	
	/**
	 * Setting a higher base frequency increases the "roughness" of the noise
	 * source values.
	 * 
	 * @param f Frequency value.
	 */
	public void setBaseFrequency(float f) {
		xTurb.setBaseFrequency(f);
		yTurb.setBaseFrequency(f);
		zTurb.setBaseFrequency(f);
	}
	
	/**
	 * Setting a higher base amplitude increases the "power" or intensity of
	 * the noise source values.
	 * 
	 * @param a Amplitude value.
	 */
	public void setBaseAmplitude(float a) {
		xTurb.setBaseAmplitude(a);
		yTurb.setBaseAmplitude(a);
		zTurb.setBaseAmplitude(a);
	}

	@Override
	public float noise(float sample_x, float sample_y, float sample_z) {
		float xDistort = sample_x + xTurb.noise(sample_x, sample_y, sample_z);
		float yDistort = sample_y + yTurb.noise(sample_x, sample_y, sample_z);
		float zDistort = sample_z + zTurb.noise(sample_x, sample_y, sample_z);
		return source.noise(xDistort, yDistort, zDistort);
	}

}
