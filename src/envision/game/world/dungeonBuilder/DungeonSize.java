package envision.game.world.dungeonBuilder;

public enum DungeonSize {
	SMALL(50, 45),
	MEDIUM(100, 100),
	LARGE(200, 200),
	;
	
	public final int width;
	public final int height;
	
	private DungeonSize(int widthIn, int heightIn) {
		width = widthIn;
		height = heightIn;
	}
	
	public static DungeonSize fromString(String in) {
		switch (in.toLowerCase()) {
		case "small":
		case "sm": return SMALL;
		case "med":
		case "medium": return MEDIUM;
		case "lg":
		case "large": return LARGE;
		default: return SMALL;
		}
	}
	
}
