package ca.chrislittle.noiseyapp.noise;

/**
 * Generates noise based on 'fractional/fractal Brownian motion' by combining multiple
 * noise octaves from another NoiseSource.
 * 
 * @author Chris
 *
 */
public class BrownianNoise extends NoiseSource {
	
	private NoiseSource source;
	
	private float baseFrequency; // TODO: Necessary? Or specify "area" when building 2D arrays (i.e. textures), etc.?
	private float baseAmplitude; // TODO: Necessary? Or just multiply returned noise value?
	private float lacunarity; // Frequency multiplier for subsequent octaves
	private float persistence; // Amplitude multiplier for subsequent octaves
	private int octaves; // Number of fBM octaves to combine
	
	
	public BrownianNoise(NoiseSource s) {
		source = s;
		
		baseFrequency = 1.0f;
		baseAmplitude = 1.0f;
		lacunarity = 2.0f;
		persistence = 0.5f;
		octaves = 6;
	}
	
	public void setBaseFrequency(float f) {
		// Ensure a positive frequency value
		baseFrequency = Math.abs(f);
	}
	
	public void setBaseAmplitude(float a) {
		baseAmplitude = a;
	}
	
	public void setLacunarity(float l) {
		// Ensure frequency isn't multiplied by a negative value
		lacunarity = Math.abs(l);
	}
	
	public void setPersistence(float p) {
		persistence = p;
	}
	
	public void setOctaves(int o){
		// Ensure number of octaves is 1 or greater
		if (o <= 0) return;
		octaves = o;
	}

	
	@Override
	public float noise(float sample_x, float sample_y, float sample_z) {
		float noise = 0.0f;
		float freq = baseFrequency;
		float amp = baseAmplitude;
		for (int i=0; i<octaves; ++i) {
			noise += amp * source.noise(sample_x*freq, sample_y*freq, sample_z*freq);
			
			freq *= lacunarity;
			amp *= persistence;
		}
		
		return noise;
	}

}
