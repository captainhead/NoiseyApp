package ca.chrislittle.noiseyapp.noise;

public class ScaleInput extends NoiseSource {
	
	private NoiseSource sourceToScale;
	
	private float scaleX;
	private float scaleY;
	private float scaleZ;
	
	
	public ScaleInput(NoiseSource source){
		sourceToScale = source;
		
		scaleX = 1.0f;
		scaleY = 1.0f;
		scaleZ = 1.0f;
	}
	
	public void setScaleX(float scale) {
		if (scale >= 0.0f)
			scaleX = scale;
	}
	
	public void setScaleY(float scale) {
		if (scale >= 0.0f)
			scaleY = scale;
	}
	
	public void setScaleZ(float scale) {
		if (scale >= 0.0f)
			scaleZ = scale;
	}
	
	public void setScale(float x, float y, float z) {
		if (x >= 0.0f)
			scaleX = x;
		if (y >= 0.0f)
			scaleY = y;
		if (z >= 0.0f)
			scaleZ = z;
	}

	@Override
	public float noise(float sample_x, float sample_y, float sample_z) {
		return sourceToScale.noise(sample_x*scaleX, sample_y*scaleY, sample_z*scaleZ);
	}

}
