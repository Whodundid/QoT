package envision.engine.inputHandlers;

import org.lwjgl.glfw.GLFWWindowSizeCallback;

public class WindowResizeListener extends GLFWWindowSizeCallback {

	private static WindowResizeListener instance;
	private static int width;
	private static int height;
	private IWindowResizeEventReceiver receiver;
	
	//==================
	// Static Singleton
	//==================
	
	public static WindowResizeListener create() { return create((IWindowResizeEventReceiver) null); }
	public static WindowResizeListener create(IWindowResizeEventReceiver receiverIn) {
		return instance = new WindowResizeListener(receiverIn);
	}
	
	public static WindowResizeListener getInstance() {
		return instance = (instance != null) ? instance : new WindowResizeListener(null);
	}
	
	private WindowResizeListener(IWindowResizeEventReceiver receiverIn) {
		super();
		receiver = receiverIn;
	}
	
	//===========
	// Overrides
	//===========
	
	@Override
	public void invoke(long window, int widthIn, int heightIn) {
		width = widthIn;
		height = heightIn;
		
		if (receiver != null) {
			receiver.onWindowResized(window, widthIn, heightIn);
		}
	}
	
	//=========
	// Methods
	//=========
	
	public WindowResizeListener setReceiver(IWindowResizeEventReceiver receiverIn) {
		receiver = receiverIn;
		return this;
	}
	
	//================
	// Static Getters
	//================
	
	public static int getWidth() { return width; }
	public static int getHeight() { return height; }
	
}
