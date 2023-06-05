package envision.engine.rendering.renderingAPI.error;

public class RendererErrorReporter {
	
	//--------------
	// Constructors
	//--------------
	
	private RendererErrorReporter() {}
	
	//--------
	// Static
	//--------
	
	private static ErrorReportingLevel curLevel = ErrorReportingLevel.HIGH;
	private static IRendererErrorReceiver receiver = null;
	private static boolean defaultToSysout = true;
	
	public static void setReportingLevel(ErrorReportingLevel levelIn) {
		curLevel = levelIn;
	}
	
	public static void setReceiver(IRendererErrorReceiver receiverIn) {
		receiver = receiverIn;
	}
	
	/**
	 * Specifies whether or not an error will be logged to Java's System.out
	 * stream in the event that no IRenderingErrorReceiver has been set.
	 * 
	 * @param val
	 */
	public static void setDefaultToSysout(boolean val) {
		defaultToSysout = val;
	}
	
	//----------------
	// Handler Method
	//----------------
	
	public static void onEvent(String msg, ErrorReportingLevel reportingLevel) {
		if (reportingLevel.level <= curLevel.level) distribute(msg, reportingLevel);
	}
	
	private static void distribute(String msg, ErrorReportingLevel reportingLevel) {
		if (receiver == null) {
			if (defaultToSysout) System.out.println("[Renderer Error SYSOUT] " + msg);
		}
		else {
			receiver.onRenderErrorReporterMessage(msg, reportingLevel);
		}
	}
	
}
