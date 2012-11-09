package ca.chrislittle.noiseyapp.noise;

/**
 * Generates values based on a series of concentric cylinders aligned along
 * the y-axis. Radius of each subsequent cylinder are a distance of 1.0
 * units apart. This can be changed by setting a higher frequency, e.g. set
 * frequency to 4.0 to generate cylinders 0.25 units apart
 * 
 * @author Chris
 *
 */
public class Cylinders extends NoiseSource {
	
	private float frequency;
	
	
	public Cylinders() {
		this.frequency = 1.0f;
	}
	
	public void setFrequency(float f) {
		frequency = f;
	}

	@Override
	public float noise(float sample_x, float sample_y, float sample_z) {
		float x = sample_x*frequency;
		float z = sample_z*frequency;
		
		double dist = Math.sqrt(x*x + z*z);
		double distSmall = dist - Math.floor(dist);
		double distLarge = 1.0 - distSmall;
		double near = Math.min(distSmall, distLarge);
		
		return 1.0f - (float)(near*4.0);
	}

}
