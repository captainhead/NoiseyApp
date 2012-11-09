package ca.chrislittle.noiseyapp.noise;

/**
 * A noise source which scales (multiplies) and biases (adds/subjects) a
 * noise value produced by another noise source.
 * 
 * Note the noise value is modified in the following order:
 * 1) scale
 * 2) bias
 * For example, given a scale of 0.5 and a bias of 2.0, a noise value of
 * 0.2 would become 2.1 (i.e. 0.5*0.2 + 2.0).
 * 
 * A common use case would be to transform noise values in range [-1, 1] to
 * the range [0, 1], using scale=0.5 and bias=0.5.
 * 
 * @author Chris
 *
 */
public class ScaleBias extends NoiseSource {
	
	private NoiseSource source;
	
	private float scale;
	private float bias;
	
	public ScaleBias(NoiseSource s) {
		source = s;
		
		scale = 1.0f;
		bias = 0.0f;
	}
	
	public void setScale(float s) {
		scale = s;
	}
	
	public void setBias(float b) {
		bias = b;
	}

	@Override
	public float noise(float sample_x, float sample_y, float sample_z) {
		return source.noise(sample_x, sample_y, sample_z)*scale + bias;
	}

}
