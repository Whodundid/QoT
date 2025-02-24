package envision.engine.internal.rendering.renderingAPI.error;

@FunctionalInterface
public interface IRendererErrorReceiver {
    
    void onRenderErrorReporterMessage(String msg, ErrorReportingLevel reportingLevel);
    
}
