package envision.engine.windows.windowUtil.windowEvents.eventUtil;

import eutil.misc.IEnumHelper;

//Author: Hunter Bragg

public enum FocusType implements IEnumHelper<FocusType> {
	
	GAINED,
	LOST,
	TRANSFER,
	MOUSE_PRESS,
	KEY_PRESS,
	DEFAULT_FOCUS_OBJECT,
	NONE;
	
}
