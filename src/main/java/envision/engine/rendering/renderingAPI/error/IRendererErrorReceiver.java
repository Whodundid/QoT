package envision.engine.rendering.renderingAPI.error;

@FunctionalInterface
public interface IRendererErrorReceiver {
	
	void onRenderErrorReporterMessage(String msg, ErrorReportingLevel reportingLevel);
	
}
