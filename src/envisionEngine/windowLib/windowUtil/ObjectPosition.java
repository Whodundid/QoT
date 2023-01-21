package envisionEngine.windowLib.windowUtil;

//Author: Hunter Bragg

/**
 * An enum which specifies a relative location on the physical monitor
 * for how IWindowParents can be positioned at or around.
 */
public enum ObjectPosition {
	/** Center of window is positioned around the center of the screen. */
	SCREEN_CENTER,
	/** Bottom left corner of window is positioned at the bottom left corner of screen. */
	BOT_LEFT,
	/** Top left corner of window is positioned at the top left corner of screen. */
	TOP_LEFT,
	/** Bottom right corner of window is positioned at the bottom right corner of screen. */
	BOT_RIGHT,
	/** Top right corner of window is positioned at the top right corner of screen. */
	TOP_RIGHT,
	/** Center of window is centered around the cursor. */
	CURSOR_CENTER,
	/** Top left corner of window is positioned at the cursor's location. */
	CURSOR_CORNER,
	/** Centered around a specified object. */
	OBJECT_CENTER,
	/** Top left corner of window is positioned around specified object's top left corner. */
	OBJECT_CORNER,
	/** Top left corner of window is positioned around specified object's top left corner but slightly to the right and down. */
	OBJECT_INDENT,
	/** Top left corner of window is positioned around the first instance of the same type of window with indent. */
	EXISTING_OBJECT_INDENT,
	/** No special positioning, window is positioned at top left corner of screen by default. */
	NONE;
}
