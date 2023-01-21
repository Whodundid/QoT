package envisionEngine.windowLib.windowTypes;

/** Overlay windows prevent the renderer from drawing the hud border. */
public class OverlayWindow<E> extends WindowParent<E> {
	
	@Override public boolean closesWithHud() { return true; }

}
