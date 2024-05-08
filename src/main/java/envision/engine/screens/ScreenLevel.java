package envision.engine.screens;

public enum ScreenLevel {
	/** The top renderer (always present). */
	TOP,
	/** The currently active screen (if any). */
	SCREEN,
	/** The current screen layer, TOP if the desktop is open, or SCREEN if there is an active screen. */
	ACTIVE;
}
