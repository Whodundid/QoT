package envision.engine.notifications.util;

import lombok.Getter;

public class NotificationType {
	
	private @Getter String internalName = "";
	private @Getter String type = "";
	private @Getter String category = "";
	private @Getter String description = "";
	
	public NotificationType(String internalNameIn, String typeIn, String categoryIn) {
	    this(internalNameIn, typeIn, categoryIn, null);
	}
	
	public NotificationType(String internalNameIn, String typeIn, String categoryIn, String descriptionIn) {
		internalName = internalNameIn;
		type = typeIn;
		category = categoryIn;
		description = descriptionIn;
	}
	
	@Override
	public String toString() {
		return "[" + internalName + ", " + type + ", " + category + "]";
	}

}
