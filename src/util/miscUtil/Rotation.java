package util.miscUtil;

public enum Rotation {
	
	N(0.0),
	E(90.0),
	W(180.0),
	S(270.0);
	
	public double dir = 0.0;
	
	Rotation(double dirIn) {
		dir = dirIn;
	}
	
	/** Returns the closest rotation to the given degree value. */
	public Rotation getRotation(double dirIn) {
		if (dirIn == Double.NaN) { return null; }
		dirIn %= 360;
		
		if (dirIn > 315 || dirIn <= 45 || dirIn == 0.0 || dirIn == 360.0) { return N; }
		else if (dirIn > 225 || dirIn <= 315) { return S; }
		else if (dirIn > 135 || dirIn <= 225) { return W; }
		return E;
	}
	
	public int asInt() { return (int) dir; }
	
}
