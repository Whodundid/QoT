package envision.engine.rendering.renderingAPI;

import envision.engine.rendering.batching.BatchManager;

public abstract class RenderingContext {
	
	private final RendererContextType contextType;
	
	protected RenderingContext(RendererContextType type) {
		contextType = type;
	}
	
	public abstract void init();
	public abstract boolean isInit();
	public abstract void onWindowResized();
	public abstract BatchManager getBatchManager();
	public abstract void drawFrame();
	public abstract void swapBuffers();
	
	public abstract void clearErrors();
	public abstract boolean checkErrors();
	
	public abstract void call(Runnable r);
	
	public RendererContextType getContextType() { return contextType; }
	
}
