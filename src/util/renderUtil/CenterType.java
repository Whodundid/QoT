package util.renderUtil;

//Author: Hunter Bragg

/** An enum which specifies a relative location on the physical monitor for how IWindowParents can be positioned at or around. */
public enum CenterType {
	
	screen, //center of window is positioned around the center of the screen
	botLeftScreen, //bottom left corner of window is positioned at the bottom left corner of screen
	topLeftScreen, //top left corner of window is positioned at the top left corner of screen
	botRightScreen, //bottom right corner of window is positioned at the bottom right corner of screen
	topRightScreen, //top right corner of window is positioned at the top right corner of screen
	cursor, //center of window is centered around the cursor
	cursorCorner, //top left corner of window is positioned at the cursor's location
	object, //centered around a specified object
	objectCorner, //top left corner of window is positioned around specified object's top left corner
	objectIndent, //top left corner of window is positioned around specified object's top left corner but slightly to the right and down
	existingObjectIndent, //top left corner of window is positioned around the first instance of the same type of window with indent
	none; //no special positioning, window is positioned at top left corner of screen by default
	
}
