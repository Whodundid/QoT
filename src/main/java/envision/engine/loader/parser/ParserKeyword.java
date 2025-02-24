package envision.engine.loader.parser;

import java.util.Objects;

import eutil.strings.EStringUtil;

enum ParserKeyword {
    
    //--------------------------
    // script stucture keywords
    //--------------------------
    
    DEF_VAR,
    DEF_LOCAL,
    DEF_OBJ,
    DEF_SCRIPT,
    SET_VAR,
    SET_LOCAL,
    SET_VAR_FROM_OBJECT_PROPERTY,
    SET_LOCAL_FROM_OBJECT_PROPERTY,
    IF_VAR_DEF("(", "):"),
    IF_LOCAL_DEF("(", "):"),
    IF_OBJ_DEF("(", "):"),
    IF_SCRIPT_DEF("(", "):"),
    EXECUTE_SCRIPT,
    EXECUTE_OBJECT_SCRIPT,
    IMPORT,
    
    //-----------------------------
    // engine json loader keywords
    //-----------------------------
    
    LOAD_JSON_GAME_DEF,
    LOAD_JSON_TEXTURES,
    LOAD_JSON_SOUND_EFFECTS,
    LOAD_JSON_SONGS,
    LOAD_JSON_SHADERS,
    LOAD_JSON_SCRIPTS,
    LOAD_JSON_SPRITES,
    LOAD_JSON_SPRITE_SHEETS,
    LOAD_JSON_WORLD_TILES,
    LOAD_JSON_TILE_SET,
    LOAD_JSON_GAME_RESOURCES,
    LOAD_JSON_EFFECTS,
    LOAD_JSON_PARTICLE_EFFECTS,
    LOAD_JSON_ITEMS,
    LOAD_JSON_ABILITIES,
    LOAD_JSON_FACTIONS,
    LOAD_JSON_SHOPS,
    LOAD_JSON_QUESTS,
    LOAD_JSON_ENTITIES,
    LOAD_JSON_MAPS,
    LOAD_JSON_LEVELS,
    LOAD_JSON_GAME_SCREENS,
    LOAD_JSON_CUSTOM_TYPE,
    
    ;
    
    //========
    // Fields
    //========
    
    public final String nextChars;
    public final String endChars;
    
    public final String keywordStart;
    
    //==============
    // Constructors
    //==============
    
    private ParserKeyword() {
        this(":", null);
    }
    
    private ParserKeyword(String nextChars) {
        this(nextChars, null);
    }
    
    private ParserKeyword(String nextChars, String endChars) {
        this.nextChars = nextChars;
        this.endChars = endChars;
        
        keywordStart = this.toString() + ((this.nextChars != null) ? this.nextChars : "");
    }
    
    //=========
    // Methods
    //=========
    
    public String extractKeywordFromLine(String line) {
        return extractKeywordFromLine(line, this);
    }
    
    //================
    // Static Methods
    //================
    
    public static ParserKeyword findKeywordAtLineStart(String line) {
        if (EStringUtil.isNotPopulated(line)) {
            return null;
        }
        
        String l = line.trim();
        
        for (ParserKeyword v : values()) {
            if (l.startsWith(v.keywordStart)) {
                // check if the keyword defines a required ending set of characters
                if (v.endChars != null) {
                    if (l.endsWith(v.endChars)) {
                        return v;
                    }
                    // we didn't match the end characters
                    else {
                        continue;
                    }
                }
                // we matched the keyword
                return v;
            }
        }
        
        return null;
    }
    
    public static String extractKeywordFromLine(String line, ParserKeyword keyword) {
        Objects.nonNull(line);
        Objects.nonNull(keyword);
        
        String l = line.trim();
        String kws = keyword.keywordStart;
        String end = keyword.endChars;
        
        // check if this keyword even matches
        if (!l.startsWith(kws)) {
            throw new IllegalArgumentException("The given line: '" + l + "' does not start with the given keyword: '" +
                                               keyword + "'!");
        }
        
        String extracted = l.substring(kws.length());
        
        // check if there are ending chars to also extract
        if (end != null) {
            if (extracted.length() >= end.length()) {
                throw new IllegalStateException("The given line: '" + l +
                                                "' does not actually match the given keyword's: '" + keyword +
                                                "' requirements! Must end with '" + end + "'");
            }
            extracted = extracted.substring(0, extracted.length() - end.length());
        }
        
        // trim off any fluff -- we don't care
        extracted = extracted.trim();
        
        return extracted;
    }
    
}
