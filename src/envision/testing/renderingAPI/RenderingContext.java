package envision.testing.renderingAPI;

public abstract class RenderingContext {
	
	private final RendererContextType contextType;
	
	protected RenderingContext(RendererContextType type) {
		contextType = type;
	}
	
	public abstract void init();
	public abstract boolean isInit();
	public abstract void onWindowResized();
	public abstract void swapBuffers();
	
	public RendererContextType getContextType() { return contextType; }
	
	
}
