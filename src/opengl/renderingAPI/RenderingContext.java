package opengl.renderingAPI;

public abstract class RenderingContext {
	
	private final RendererContextType contextType;
	
	protected RenderingContext(RendererContextType type) {
		contextType = type;
	}
	
	public abstract void init();
	
	public abstract void swapBuffers();
	
	public RendererContextType getContextType() { return contextType; }
	
}
