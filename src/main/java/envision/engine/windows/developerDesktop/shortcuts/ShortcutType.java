package envision.engine.windows.developerDesktop.shortcuts;

public enum ShortcutType {
    
	FILE("FILE"),
	COMMAND("COMMAND"),
	SCRIPT("SCRIPT"),
	WINDOW("WINDOW"),
	
	UNKNOWN("");
	
	public final String text;
    
    private ShortcutType(String textIn) {
        this.text = textIn;
    }
    
    public static ShortcutType parseShortcutType(String textIn) {
        if (textIn == null || textIn.isBlank() || textIn.isEmpty()) return UNKNOWN;
        
        for (var type : ShortcutType.values()) {
            String text = type.text;
            if (textIn.startsWith(text)) return type;
        }
        
        return UNKNOWN;
    }
    
}
