package ca.chrislittle.noiseyapp.noise;

/**
 * Translate input coordinates to another noise source.
 * 
 * @author Chris
 *
 */
public class Translate extends NoiseSource {
	
	private NoiseSource source;
	
	private float x;
	private float y;
	private float z;
	
	public Translate(NoiseSource s) {
		source = s;
		
		x = 0.0f;
		y = 0.0f;
		z = 0.0f;
	}
	
	public void translateX(float x) {
		this.x = x;
	}
	public void translateY(float y) {
		this.y = y;
	}
	public void translateZ(float z) {
		this.z = z;
	}
	public void translate(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public float noise(float sample_x, float sample_y, float sample_z) {
		return source.noise(sample_x + x, sample_y + y, sample_z + z);
	}

}
