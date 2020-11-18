package util.mathUtil;

//Author: Hunter Bragg

public enum Direction {
	N("N"),
	NE("NE"),
	E("E"),
	SE("SE"),
	S("S"),
	SW("SW"),
	W("W"),
	NW("NW"),
	OUT("Unknown");
	
	private String direction = "";
	
	Direction(String dir) {
		direction = dir;
	}
	
	public String getDir() { return direction; }
	
	public static Direction get(int ordinal) {
		return values()[ordinal];
	}
	
}
