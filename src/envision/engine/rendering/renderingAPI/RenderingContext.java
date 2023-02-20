package envision.engine.rendering.renderingAPI;

import envision.engine.rendering.OpenGLBatchManager;
import envision.engine.rendering.shaders.ShaderProgram;

public abstract class RenderingContext {
	
	private final RendererContextType contextType;
	
	protected RenderingContext(RendererContextType type) {
		contextType = type;
	}
	
	public abstract void init();
	public abstract boolean isInit();
	public abstract void onWindowResized();
	public abstract OpenGLBatchManager getPrimaryBatch();
	public abstract void useShader(ShaderProgram programIn);
	public abstract void drawFrame();
	public abstract void swapBuffers();
	
	public abstract void clearErrors();
	public abstract boolean checkErrors();
	
	public abstract void call(Runnable r);
	
	public RendererContextType getContextType() { return contextType; }
	
}
