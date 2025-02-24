package envision.engine.internal.inputHandlers;

public interface IWindowResizeEventReceiver {
    
    void onWindowResized(long window, int newWidth, int newHeight);
    
}
