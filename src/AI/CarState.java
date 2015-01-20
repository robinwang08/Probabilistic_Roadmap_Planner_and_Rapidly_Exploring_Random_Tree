package AI;

// this class declares the configuration of a car robot;
// standard set and get function;

public class CarState {
	protected double[] s;

	public CarState () {
		s = new double[3];
		s[0] = 0;
		s[1] = 0;
		s[2] = 0;
	}

	public CarState (double x, double y, double theta) {
		s = new double[3];
		s[0] = x;
		s[1] = y;
		//make sure the pi wraps
		double max = 2*Math.PI;
		if(theta < -max){
			theta = -(theta%(-max));
		}else if(theta < 0){
			theta += max;
				}
		s[2] = theta%max;
	}

	public void set(double x, double y, double theta) {
		s[0] = x;
		s[1] = y;
		// make sure the pi wraps
		double max = 2*Math.PI;
		if(theta < -max){
			theta = -(theta%(-max));
		}else if(theta < 0){
			theta += max;
				}
		s[2] = theta%max;
		
	}

	public double getX() {
		return s[0];
	}

	public double getY() {
		return s[1];
	}

	public double getTheta() {
		return s[2];
	}

	public double[] get() {
		return s;
	}
}
