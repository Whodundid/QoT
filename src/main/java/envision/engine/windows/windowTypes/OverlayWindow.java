package envision.engine.windows.windowTypes;

/** Overlay windows prevent the renderer from drawing the hud border. */
public class OverlayWindow extends WindowParent {
	
	@Override public boolean closesWithHud() { return true; }

}
