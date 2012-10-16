package ca.chrislittle.noiseyapp.noise;

import java.util.Random;

public class PerlinNoise {
	private float baseFrequency;
	private float baseAmplitude; // TODO: Necessary? Or just multiply returned noise value?
	private float lacunarity; // Frequency multiplier for subsequent octaves
	private float persistence; // Amplitude multiplier for subsequent octaves
	private int octaves; // Number of fBM octaves to combine
	
	private int[] p; // Permutation table
	// Gradient vectors
	private float[] Gx;
	private float[] Gy;
	private float[] Gz;
	
	
	public PerlinNoise() {
		baseFrequency = 1.0f;
		baseAmplitude = 1.0f;
		lacunarity = 2.0f;
		persistence = 0.5f;
		octaves = 1;

		p = new int[256];
		Gx = new float[256];
		Gy = new float[256];
		Gz = new float[256];
		
		Random rand = new Random();
		
		// Init the permutation table and create random gradient vectors
		for (int i=0; i<256; ++i) {
			p[i] = i;
			
			Gx[i] = rand.nextFloat()*2.0f - 1.0f;
			Gy[i] = rand.nextFloat()*2.0f - 1.0f;
			Gz[i] = rand.nextFloat()*2.0f - 1.0f;
		}
		
		// Shuffle the permutation table
		int j;
		int swp;
		for (int i=0; i<256; ++i) {
			j = rand.nextInt(256);
			
			swp = p[i];
			p[i] = p[j];
			p[j] = swp;
		}
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
	
	
	/**
	 * Produce a noise value based on fractal Brownian motion (i.e. combine
	 * multiple octaves of Perlin noise).
	 *  
	 * @param sample_x Noise sample coordinates
	 * @param sample_y
	 * @param sample_z
	 * @return A noise value clamped to the range [-1, 1]
	 */
	public float noise(float sample_x, float sample_y, float sample_z) {
		float noise = 0.0f;
		float freq = baseFrequency;
		float amp = baseAmplitude;
		for (int i=0; i<octaves; ++i) {
			noise += amp * computeNoise(sample_x*freq, sample_y*freq, sample_z*freq);
			
			freq *= lacunarity;
			amp *= persistence;
		}
		
		if (noise > 1.0f)
			noise = 1.0f;
		if (noise < -1.0f)
			noise = -1.0f;
		
		return noise;
	}
	
	
	/**
	 * Generate a smoothed Perlin (gradient) noise value.
	 * 
	 * @param sample_x Noise sample coordinates
	 * @param sample_y
	 * @param sample_z
	 * @return A floating point noise value, usually in the range [-1, 1]
	 */
	public float computeNoise(float sample_x, float sample_y, float sample_z) {
		// Clamp sample point to integer unit cube coordinates
		int x0 = (int)sample_x;
		int x1 = x0 + 1;
		int y0 = (int)sample_y;
		int y1 = y0 + 1;
		int z0 = (int)sample_z;
		int z1 = z0 + 1;
		
		// Determine sample position within unit cube
		float px0 = sample_x - (float)x0;
		float px1 = px0 - 1.0f;
		float py0 = sample_y - (float)y0;
		float py1 = py0 - 1.0f;
		float pz0 = sample_z - (float)z0;
		float pz1 = pz0 - 1.0f;
		
		// For each unit cube vertex, compute dot product between gradient vector and vector to sample position
		int gIndex;
		gIndex = p[(x0 + p[(y0 + p[z0 % 256]) % 256]) % 256];
		float d000 = Gx[gIndex]*px0 + Gy[gIndex]*py0 + Gz[gIndex]*pz0;
		gIndex = p[(x1 + p[(y0 + p[z0 % 256]) % 256]) % 256];
		float d001 = Gx[gIndex]*px1 + Gy[gIndex]*py0 + Gz[gIndex]*pz0;

		gIndex = p[(x0 + p[(y1 + p[z0 % 256]) % 256]) % 256];
		float d010 = Gx[gIndex]*px0 + Gy[gIndex]*py1 + Gz[gIndex]*pz0;
		gIndex = p[(x1 + p[(y1 + p[z0 % 256]) % 256]) % 256];
		float d011 = Gx[gIndex]*px1 + Gy[gIndex]*py1 + Gz[gIndex]*pz0;

		gIndex = p[(x0 + p[(y0 + p[z1 % 256]) % 256]) % 256];
		float d100 = Gx[gIndex]*px0 + Gy[gIndex]*py0 + Gz[gIndex]*pz1;
		gIndex = p[(x1 + p[(y0 + p[z1 % 256]) % 256]) % 256];
		float d101 = Gx[gIndex]*px1 + Gy[gIndex]*py0 + Gz[gIndex]*pz1;

		gIndex = p[(x0 + p[(y1 + p[z1 % 256]) % 256]) % 256];
		float d110 = Gx[gIndex]*px0 + Gy[gIndex]*py1 + Gz[gIndex]*pz1;
		gIndex = p[(x1 + p[(y1 + p[z1 % 256]) % 256]) % 256];
		float d111 = Gx[gIndex]*px1 + Gy[gIndex]*py1 + Gz[gIndex]*pz1;
		
		// Interpolate dot product values at sample point using interpolation curve: 6x^5 - 15x^4 + 10x^3
		float wx = ((6.0f*px0 - 15.0f)*px0 + 10.0f)*px0*px0*px0;
		float wy = ((6.0f*py0 - 15.0f)*py0 + 10.0f)*py0*py0*py0;
		float wz = ((6.0f*pz0 - 15.0f)*pz0 + 10.0f)*pz0*pz0*pz0;
		
		float xa = d000 + wx*(d001 - d000);
		float xb = d010 + wx*(d011 - d010);
		float xc = d100 + wx*(d101 - d100);
		float xd = d110 + wx*(d111 - d110);
		float ya = xa + wy*(xb - xa);
		float yb = xc + wy*(xd - xc);
		float value = ya + wz*(yb - ya);
		
		return value;
	}
}
