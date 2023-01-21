package envisionEngine.testing.renderingAPI.error;

@FunctionalInterface
public interface IRendererErrorReceiver {
	
	void onRenderErrorReporterMessage(String msg, ErrorReportingLevel reportingLevel);
	
}
