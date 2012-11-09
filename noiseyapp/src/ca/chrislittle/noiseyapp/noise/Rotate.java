package ca.chrislittle.noiseyapp.noise;

/**
 * Rotate input coordinates around x, y, and z axes.
 * 
 * @author Chris
 *
 */
public class Rotate extends NoiseSource {
	
	private NoiseSource source;
	
	private double x1;
	private double x2;
	private double x3;
	private double y1;
	private double y2;
	private double y3;
	private double z1;
	private double z2;
	private double z3;
	
//	private double xAngle;
//	private double yAngle;
//	private double zAngle;
	
	
	public Rotate(NoiseSource s) {
		source = s;
		
		setAngles(0.0f, 0.0f, 0.0f);
	}
	
	/**
	 * Set rotation angles in degrees.
	 * 
	 * @param angle_x Rotation around x-axis
	 * @param angle_y Rotation around y-axis
	 * @param angle_z Rotation around z-axis
	 */
	public void setAngles(float angle_x, float angle_y, float angle_z) {
		double degToRad = Math.PI/180.0;
		
		double xCos, yCos, zCos, xSin, ySin, zSin;
		xCos = Math.cos(angle_x*degToRad);
		yCos = Math.cos(angle_y*degToRad);
		zCos = Math.cos(angle_z*degToRad);
		xSin = Math.sin(angle_x*degToRad);
		ySin = Math.sin(angle_y*degToRad);
		zSin = Math.sin(angle_z*degToRad);
		
		x1 = ySin * xSin * zSin + yCos * zCos;
		y1 = xCos * zSin;
		z1 = ySin * zCos - yCos * xSin * zSin;
		x2 = ySin * xSin * zCos - yCos * zSin;
		y2 = xCos * zCos;
		z2 = -yCos * xSin * zCos - ySin * zSin;
		x3 = -ySin * xCos;
		y3 = xSin;
		z3 = yCos * xCos;
		
//		xAngle = angle_x;
//		yAngle = angle_y;
//		zAngle = angle_z;
	}
	
	@Override
	public float noise(float sample_x, float sample_y, float sample_z) {
		float nx = (float)(x1*sample_x + y1*sample_y + z1*sample_z);
		float ny = (float)(x2*sample_x + y2*sample_y + z2*sample_z);
		float nz = (float)(x3*sample_x + y3*sample_y + z3*sample_z);
		return source.noise(nx, ny, nz);
	}

}
