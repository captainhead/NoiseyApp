package ca.chrislittle.noiseyapp.noise;

import java.util.LinkedList;

/**
 * A noise source which sums the noise values of two or more noise sources.
 * 
 * @author Chris Little
 *
 */
public class Add extends NoiseSource {
	
	private LinkedList<NoiseSource> sources;
	
	
	/**
	 * Create an additive noise object given two noise sources.
	 * 
	 * @param s1 Two NoiseSource objects that will be summed to produce an additive noise value.
	 * @param s2
	 */
	public Add(NoiseSource s1, NoiseSource s2) {
		sources = new LinkedList<NoiseSource>();
		sources.add(s1);
		sources.add(s2);
	}
	
	/**
	 * Append another NoiseSource. 
	 * 
	 * @param source NoiseSource object to append.
	 */
	public void addSource(NoiseSource source) {
		sources.add(source);
	}

	
	@Override
	public float noise(float sample_x, float sample_y, float sample_z) {
		float noise = 0.0f;
		
		for (NoiseSource noiseMaker : sources) {
			noise += noiseMaker.noise(sample_x, sample_y, sample_z);
		}
		
		return noise;
	}

}
