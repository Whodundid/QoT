package envision.engine.rendering.renderingAPI.error;

public enum ErrorReportingLevel {
	HIGH(0),
	MEDIUM(1),
	LOW(2),
	NOTIFICATION(3);
	
	public final int level;
	
	private ErrorReportingLevel(int levelIn) {
		level = levelIn;
	}
	
	public static ErrorReportingLevel from(String in) {
		return ErrorReportingLevel.valueOf(in);
	}
}