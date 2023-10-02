package envision.engine.windows.developerDesktop.config;

import eutil.strings.EStringBuilder;

public enum DesktopConfigKeyword {
    
    CONFIG_NAME("CONFIG_NAME"),
    CONFIG_VERSION("CONFIG_VERSION"),
    PARSER_VERSION("PARSER_VERSION"),
    DEF_VAR("DEF_VAR"),
    SHORTCUTS("SHORTCUTS"),
    
    UNKNOWN("")
    ;
    
    public final String text;
    
    private DesktopConfigKeyword(String textIn) {
        text = textIn;
    }
    
    public static DesktopConfigKeyword parseKeyword(String lineIn) {
        if (lineIn == null || lineIn.isBlank() || lineIn.isEmpty()) return UNKNOWN;
        
        // only care if the string actually contains a ':'
        if (!lineIn.contains(":")) return UNKNOWN;
        
        final String workingLine = lineIn.trim();
        final var sb = new EStringBuilder();
        String keywordString = null;
        
        // find ':' within the line
        for (int i = 0; i < workingLine.length(); i++) {
            char c = workingLine.charAt(i);
            if (c == ':') {
                keywordString = sb.toString();
                break;
            }
            sb.a(c);
        }
        
        // not sure if this is even possible, but sanity checking just in case
        if (keywordString == null) return UNKNOWN;
        
        // search for matching keyword string
        for (DesktopConfigKeyword k : values()) {
            if (keywordString.equals(k.text)) return k;
        }
        
        return UNKNOWN;
    }
    
}
