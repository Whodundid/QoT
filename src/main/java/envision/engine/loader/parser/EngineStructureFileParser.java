package envision.engine.loader.parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import envision.engine.loader.EnvisionGame;
import envision.engine.loader.LoadedGameDirectory;
import envision.engine.loader.ResourceType;
import envision.engine.loader.parser.objects.ParserObject;
import envision.engine.loader.parser.objects.ParserScript;
import envision.engine.loader.registry.GameResourceRegistry;
import eutil.datatypes.boxes.Box2;
import eutil.datatypes.boxes.BoxList;
import eutil.datatypes.util.EList;
import eutil.file.EFileUtil;
import eutil.file.LineReader;
import eutil.strings.EStringBuilder;
import eutil.strings.EStringUtil;

public class EngineStructureFileParser {
    
    //===============
    // Static Fields
    //===============
    
    /** The number of spaces in a 'tab' character. */
    protected static final int TAB_NUM_SPACES = 4;
    protected static final String TAB = " ".repeat(TAB_NUM_SPACES);
    
    private static final String VAR_REGEX = "\\$\\{([^{}]*)}";
    private static final Pattern VAR_PATTERN = Pattern.compile(VAR_REGEX);
    
    protected static final String INCOMPLETE_VAR_DEF = "Incomplete variable definition! Expected the following syntax: 'DEF_VAR: name = value'";
    protected static final String INCOMPLETE_LOCAL_VAR_DEF = "Incomplete local variable definition! Expected the following syntax: 'DEF_LOCAL: name = value'";
    protected static final String VAR_DEF_NULL_VALUE = "Invalid variable definition! Expected a non-null value!";
    protected static final String INCOMPLETE_PROPERTY = "Incomplete property definition! Expected the following syntax: 'name: value'";
    protected static final String PROPERTY_NULL_VALUE = "Invalid property definition! Expected a non-null value!";
    protected static final String INVALID_SCRIPT_DEF_LINE = "Invalid script definition line! Expected: 'SCRIPT: scriptName(arg1, arg2, ...):'";
    protected static final String INVALID_SCRIPT_CALL = "Invalid script execution call! Expected: EXECUTE_SCRIPT: scriptName(arg1, arg2, ...)";
    protected static final String INVALID_OBJECT_SCRIPT_CALL = "Invalid object script execution call! Expected: EXECUTE_OBJECT_SCRIPT: objectName : scriptName(arg1, arg2, ...)";
    protected static final String INCOMPLETE_SCRIPT_CALL = "Expected a set of script arguments to pass! Expected syntax: 'EXECUTE_SCRIPT: scriptName(arg1, arg2, ...)'";
    protected static final String INCOMPLETE_OBJECT_SCRIPT_CALL = "Expected a set of script arguments to pass! Expected syntax: 'EXECUTE_OBJECT_SCRIPT: objectName : scriptName(arg1, arg2, ...)'";
    
    protected static final String INCOMPLETE_SET_VAR = "Expected a global variable name and value to set!\n" +
                                                       "Example: 'SET_VAR: varName = value'";
    protected static final String INCOMPLETE_SET_LOCAL = "Expected a local variable name and value to set!\n" +
                                                         "Example: 'SET_LOCAL: varName = value'";
    protected static final String INCOMPLETE_SET_VAR_FROM_OBJ = "Expected a global variable name and object property to fetch!\n" +
                                                                "Example: 'SET_VAR_FROM_OBJECT_PROPERTY: varName = objName.propertyName'";
    protected static final String INCOMPLETE_SET_LOCAL_FROM_OBJ = "Expected a local variable name and object property to fetch!\n" +
                                                                  "Example: 'SET_LOCAL_FROM_OBJECT_PROPERTY: varName = objName.propertyName'";
    
    //========
    // Fields
    //========
    
    protected final Map<String, String> internalVariableValues;
    protected final Map<String, String> parsedLocalVariables = new HashMap<>();
    protected Map<String, String> parsedGlobalVariables;
    protected Map<String, ParserScript> parsedGlobalScripts;
    protected Map<String, ParserObject> parsedGlobalObjects;
    /** Keeps track of active import file depth along with line that import started at. */
    private final BoxList<File, Integer> fileStackTrace;
    
    protected final File theFile;
    protected final File rootDir;
    protected final File parentFile;
    private int numLines;
    private int numLinesInFile;
    private int curLineIndex;
    private int curLineNumberIndex;
    private String curLine;
    private int curLineNumber;
    private int curTabAmount;
    private EList<String> lines;
    private EList<Integer> lineNumbers;
    
    private Map<String, String> varMap;
    
    // initial parsing variables
    protected int curParsingLine;
    protected int multilineCommentStartLine;
    protected boolean inMultilineComment;
    
    private GameResourceRegistry registry;
    private LoadedGameDirectory gameDir;
    private EnvisionGame gameBeingParsed;
    
    //==============
    // Constructors
    //==============
    
    /**
     * Creates a file parser for the given 'file'
     * @param file
     * @param parent
     * 
     * @throws Exception
     */
    public EngineStructureFileParser(File rootDir, File file) throws Exception {
        this(rootDir, file, null, new HashMap<>());
    }
    
    public EngineStructureFileParser(File rootDir, File file, File parentFile) throws Exception {
        this(rootDir, file, parentFile, new HashMap<>());
    }
    
    public EngineStructureFileParser(File rootDir, File file, Map<String, String> internalVariableValues) throws Exception {
        this(rootDir, file, null, internalVariableValues);
    }
    
    public EngineStructureFileParser(File rootDir, File file, File parentFile, Map<String, String> internalVariableValues) throws Exception {
        this(rootDir, file, parentFile, internalVariableValues, BoxList.newList());
        
        parsedGlobalVariables = new HashMap<>();
        parsedGlobalScripts = new HashMap<>();
        parsedGlobalObjects = new HashMap<>();
        fileStackTrace.push(theFile, 1);
    }
    
    //======================
    // Internal Constructor
    //======================
    
    protected EngineStructureFileParser(File rootDir, File file, File parentFile, Map<String, String> internalVariableValues, BoxList<File, Integer> fileStackTrace) throws Exception {
        this.theFile = file;
        this.parentFile = parentFile;
        this.rootDir = rootDir;
        this.internalVariableValues = internalVariableValues;
        this.fileStackTrace = fileStackTrace;
        
        this.lines = EList.newList();
        this.lineNumbers = EList.newList();
        this.varMap = new HashMap<>(internalVariableValues);
        
        try (var reader = new LineReader(file)) {
            String line = null;
            while (reader.hasNextLine()) {
                line = reader.nextLine();
                
                curParsingLine++;
                
                // comments can either be one line with '#' or
                // multiline starting with '#:' and ending with ':#'
                if (checkComment(line)) continue;
                if (EStringUtil.isNotPopulated(line)) continue;
                
                lines.add(line);
                lineNumbers.add(curParsingLine);
            }
        }
        
        if (inMultilineComment) {
            throw new SyntaxError("The file: '" + file +
                                  "' ended without closing multiline comment starting on line: " +
                                  multilineCommentStartLine);
        }
        
        if (lines.size() != lineNumbers.size()) {
            throw new IllegalStateException("The number of parsed lines is not equal to the number of parsed line numbers! This should be impossible!");
        }
        
        // prime the parser with the first line
        this.numLines = lines.size();
        this.numLinesInFile = curParsingLine;
        this.curLineIndex = -1;
        this.curLineNumberIndex = -1;
        advanceLine();
    }
    
    //=============
    // Parser Head
    //=============
    
    public EnvisionGame parseRootFile() {
        try {
            gameBeingParsed = new EnvisionGame();
            registry = new GameResourceRegistry();
            gameBeingParsed.assignResourceRegistry(registry);
            
            parseFile();
            
            if (gameDir != null) {
                gameBeingParsed.assignFromLoadedGameDirectory(gameDir);
            }
            
            return gameBeingParsed;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    protected void parseFile() throws Exception {
        while (!atEnd()) {
            try {
                ParserKeyword nestedKeyword = parseKeyword(0);
                executeKeyword(nestedKeyword, 0);
            }
            // property errors are recoverable and we can just go past them
            catch (PropertyError e) {
                e.printStackTrace();
            }
            
            advanceLine();
        }
    }
    
    protected void executeNestedStructureScope(int expectedTab) throws Exception {
        while (curTabAmount >= expectedTab) {
            expectLineTabAmount(expectedTab);
            
            ParserKeyword nestedKeyword = parseKeyword(expectedTab);
            executeKeyword(nestedKeyword, expectedTab);
            
            advanceLine();
        }
    }
    
    protected void executeObjectStructureScope(ParserObject obj, int tabAmount) {
        ParserKeyword keyword = parseKeyword(tabAmount);
        String varInjectedLine = injectVariablesIntoLine(curLine);
        String defLine = ParserKeyword.extractKeywordFromLine(varInjectedLine, keyword);
        
        if (keyword == null) {
            var property = parseKeyValuePair(curLine, ":", INCOMPLETE_PROPERTY, PROPERTY_NULL_VALUE);
            String value = property.getB();
            obj.defineProperty(property.getA(), value);
        }
        else {
            switch (keyword) {
            case DEF_SCRIPT:
                handleDefScript(defLine, obj, curTabAmount);
                break;
            case EXECUTE_SCRIPT:
                handleExecuteScript(defLine, obj);
                break;
            default:
                throw createParsingError("Invalid parser keyword declaration location! The keyword '" + keyword +
                                         "' cannot be defined within Custom Objects!");
            }
        }
    }
    
    protected void executeKeyword(ParserKeyword keyword, int tabAmount) throws Exception {
        String varInjectedLine = injectVariablesIntoLine(curLine);
        String defLine = keyword.extractKeywordFromLine(varInjectedLine);
        
        switch (keyword) {
        
        //--------------------------
        // script stucture keywords
        //--------------------------
        
        case DEF_VAR:
            handleDefVar(defLine);
            break;
        case DEF_LOCAL:
            handleDefLocal(defLine);
            break;
        case DEF_OBJ:
            handleDefObject(defLine, tabAmount + 1);
            break;
        case DEF_SCRIPT:
            handleDefScript(defLine, null, tabAmount + 1);
            break;
        case SET_VAR:
            handleSetVar(defLine);
            break;
        case SET_LOCAL:
            handleSetLocal(defLine);
            break;
        case SET_VAR_FROM_OBJECT_PROPERTY:
            handleSetVarFromObjectProperty(defLine);
            break;
        case SET_LOCAL_FROM_OBJECT_PROPERTY:
            handleSetLocalFromObjectProperty(defLine);
            break;
        case IF_VAR_DEF:
            handleIfVarDef(defLine, tabAmount + 1);
            break;
        case IF_LOCAL_DEF:
            handleIfLocalDef(defLine, tabAmount + 1);
            break;
        case IF_OBJ_DEF:
            handleIfObjDef(defLine, tabAmount + 1);
            break;
        case IF_SCRIPT_DEF:
            handleIfScriptDef(defLine, tabAmount + 1);
            break;
        case EXECUTE_SCRIPT:
            handleExecuteScript(defLine, null);
            break;
        case EXECUTE_OBJECT_SCRIPT:
            handleExecuteObjectScript(defLine);
            break;
        case IMPORT:
            handleImport(defLine);
            break;
            
        //-----------------------------
        // engine json loader keywords
        //-----------------------------
            
        case LOAD_JSON_GAME_DEF:
            handleLoadJsonGameDef(defLine);
            break;
            
        case LOAD_JSON_TEXTURES:            handleLoadJson(defLine, ResourceType.TEXTURE);          break;
        case LOAD_JSON_SOUND_EFFECTS:       handleLoadJson(defLine, ResourceType.SOUND_EFFECT);     break;
        case LOAD_JSON_SONGS:               handleLoadJson(defLine, ResourceType.SONG);             break;
        case LOAD_JSON_SHADERS:             handleLoadJson(defLine, ResourceType.SHADER);           break;
        case LOAD_JSON_SCRIPTS:             handleLoadJson(defLine, ResourceType.SCRIPT);           break;
        case LOAD_JSON_SPRITES:             handleLoadJson(defLine, ResourceType.SPRITE);           break;
        case LOAD_JSON_SPRITE_SHEETS:       handleLoadJson(defLine, ResourceType.SPRITE_SHEET);     break;
        case LOAD_JSON_WORLD_TILES:         handleLoadJson(defLine, ResourceType.WORLD_TILE);       break;
        case LOAD_JSON_TILE_SET:            handleLoadJson(defLine, ResourceType.TILE_SET);         break;
        case LOAD_JSON_GAME_RESOURCES:      handleLoadJson(defLine, ResourceType.RESOURCE);         break;
        case LOAD_JSON_EFFECTS:             handleLoadJson(defLine, ResourceType.EFFECT);           break;
        case LOAD_JSON_PARTICLE_EFFECTS:    handleLoadJson(defLine, ResourceType.PARTICLE_EFFECT);  break;
        case LOAD_JSON_ITEMS:               handleLoadJson(defLine, ResourceType.ITEM);             break;
        case LOAD_JSON_ABILITIES:           handleLoadJson(defLine, ResourceType.ABILITY);          break;
        case LOAD_JSON_FACTIONS:            handleLoadJson(defLine, ResourceType.FACTION);          break;
        case LOAD_JSON_SHOPS:               handleLoadJson(defLine, ResourceType.SHOP);             break;
        case LOAD_JSON_QUESTS:              handleLoadJson(defLine, ResourceType.QUEST);            break;
        case LOAD_JSON_ENTITIES:            handleLoadJson(defLine, ResourceType.ENTITY);           break;
        case LOAD_JSON_MAPS:                handleLoadJson(defLine, ResourceType.MAP);              break;
        case LOAD_JSON_LEVELS:              handleLoadJson(defLine, ResourceType.LEVEL);            break;
        case LOAD_JSON_GAME_SCREENS:        handleLoadJson(defLine, ResourceType.SCREEN);           break;
        case LOAD_JSON_CUSTOM_TYPE:         handleLoadJson(defLine, ResourceType.CUSTOM_TYPE);      break;
        
        default:
            throw createParsingError("Unexpected script keyword: '" + keyword + "'!");
        }
    }
    
    //============================
    // Structure Keyword Handlers
    //============================
    
    /**
     * 
     * @param defLine
     */
    protected void handleDefVar(String defLine) {
        Box2<String, String> keyValue = parseKeyValuePair(defLine, "=", INCOMPLETE_VAR_DEF, VAR_DEF_NULL_VALUE);
        parsedGlobalVariables.put(keyValue.getA(), keyValue.getB());
        rebuildVarMap();
    }
    
    /**
     * 
     * @param defLine
     */
    protected void handleDefLocal(String defLine) {
        Box2<String, String> keyValue = parseKeyValuePair(defLine, "=", INCOMPLETE_LOCAL_VAR_DEF, VAR_DEF_NULL_VALUE);
        parsedLocalVariables.put(keyValue.getA(), keyValue.getB());
        rebuildVarMap();
    }
    
    /**
     * 
     * @param defLine
     */
    protected void handleDefObject(String defLine, int tabAmount) {
        String[] defLineParts = defLine.split(":");
        String objName = defLineParts[0].trim();
        String parentNamesString = defLineParts[1].trim();
        String[] parentNames = parentNamesString.split(",");
        
        for (int i = 0; i < parentNames.length; i++) {
            parentNames[i] = parentNames[i].trim();
        }
        
        // inhert from any defined parents
        ParserObject obj = new ParserObject(objName);
        for (String parent : parentNames) {
            ParserObject parentObj = parsedGlobalObjects.get(parent);
            obj.inheritFrom(parentObj);
        }
        
        while (curTabAmount >= tabAmount) {
            expectLineTabAmount(tabAmount);
            try {
                executeObjectStructureScope(obj, tabAmount);
            }
            catch (PropertyError e) {
                e.printStackTrace();
            }
            advanceLine();
        }
        
        // define this object globally
        parsedGlobalObjects.put(objName, obj);
    }
    
    /**
     * 
     * @param defLine
     * @param object
     */
    protected void handleDefScript(String defLine, ParserObject object, int tabAmount) {
        // if the def line doesn't contain either of these, then it isn't valid
        if (!defLine.contains("(") && !defLine.contains("):")) {
            throw createSyntaxError(INVALID_SCRIPT_DEF_LINE);
        }
        
        // extract 'name' and 'argument names' out of def line
        final int len = defLine.length();
        var nameBuilder = new EStringBuilder();
        String name = null;
        String argString = null;
        EList<String> argNames = EList.newList();
        // extract name from def line
        for (int i = 0; i < len; i++) {
            char c = defLine.charAt(i);
            if (c == '(') {
                name = nameBuilder.clear().trim();
                argString = defLine.substring(i).trim();
                break;
            }
            nameBuilder.a(c);
        }
        // format argString
        if (!argString.startsWith("(") && !argString.endsWith("):")) {
            throw createSyntaxError(INVALID_SCRIPT_DEF_LINE);
        }
        // consume the starting '(' and ending '):'
        argString = argString.substring(1, argString.length() - 2);
        // separate arguments by ','
        String[] args = argString.split(",");
        for (String a : args) {
            argNames.add(a.trim());
        }
        
        // parse script lines and assign argument names
        EList<String> lines = parseScriptLines((object != null) ? tabAmount + 1 : tabAmount);
        ParserScript script = new ParserScript(name, lines);
        script.setArgumentNames(argNames);
        
        // depending on caller, assign to either current object or globally
        if (object != null) {
            object.defineScript(name, script);
        }
        else {
            parsedGlobalScripts.put(name, script);
        }
    }
    
    /**
     * 
     * @param defLine
     */
    protected void handleSetVar(String defLine) {
        setVarValue(defLine, parsedGlobalVariables, INCOMPLETE_SET_VAR);
    }
    
    /**
     * 
     * @param defLine
     */
    protected void handleSetLocal(String defLine) {
        setVarValue(defLine, parsedLocalVariables, INCOMPLETE_SET_LOCAL);
    }
    
    /**
     * 
     * @param defLine
     */
    protected void handleSetVarFromObjectProperty(String defLine) {
        setVarValueFromObject(defLine, parsedGlobalVariables, INCOMPLETE_SET_VAR_FROM_OBJ);
    }
    
    /**
     * 
     * @param defLine
     */
    protected void handleSetLocalFromObjectProperty(String defLine) {
        setVarValueFromObject(defLine, parsedLocalVariables, INCOMPLETE_SET_LOCAL_FROM_OBJ);
    }
    
    /**
     * 
     * @param defLine
     * @throws Exception 
     */
    protected void handleIfVarDef(String defLine, int tabAmount) throws Exception {
        if (parsedGlobalVariables.containsKey(defLine)) {
            executeNestedStructureScope(tabAmount);
        }
    }
    
    /**
     * 
     * @param defLine
     * @throws Exception 
     */
    protected void handleIfLocalDef(String defLine, int tabAmount) throws Exception {
        if (parsedLocalVariables.containsKey(defLine)) {
            executeNestedStructureScope(tabAmount);
        }
    }
    
    /**
     * 
     * @param defLine
     * @throws Exception 
     */
    protected void handleIfObjDef(String defLine, int tabAmount) throws Exception {
        if (parsedGlobalObjects.containsKey(defLine)) {
            executeNestedStructureScope(tabAmount);
        }
    }
    
    /**
     * 
     * @param defLine
     * @throws Exception 
     */
    protected void handleIfScriptDef(String defLine, int tabAmount) throws Exception {
        if (parsedGlobalScripts.containsKey(defLine)) {
            executeNestedStructureScope(tabAmount);
        }
    }
    
    /**
     * 
     * @param defLine
     * @param object
     */
    protected void handleExecuteScript(String defLine, ParserObject object) {
        // execute can either reference an existing script somewhere or define one to execute directly
        int indent = (object != null) ? 2 : 1;
        
        ParserScript scriptToExecute = null;
        EList<String> argVals = EList.newList();
        
        // if there is anything on the def line still, then assume this is a call to an existing script
        final int len = defLine.length();
        if (len > 0) {
            var scriptNameBuilder = new EStringBuilder();
            String scriptName = null;
            String argString = null;
            for (int i = 0; i < len; i++) {
                char c = defLine.charAt(i);
                if (c == '(') {
                    scriptName = scriptNameBuilder.clear();
                    argString = defLine.substring(i);
                    break;
                }
            }
            if (argString == null) {
                throw createSyntaxError(INCOMPLETE_SCRIPT_CALL);
            }
            argString = argString.substring(1, argString.length() - 1);
            String[] argParts = argString.split(",");
            for (int i = 0; i < argParts.length; i++) {
                argVals.add(argParts[i].trim());
            }
            
            if (object != null && object.getScriptMap().containsKey(scriptName)) {
                scriptToExecute = object.getScriptMap().get(scriptName);
            }
        }
        // otherwise, parse an instance script
        else {
            EList<String> scriptLines = parseScriptLines(indent);
            scriptToExecute = new ParserScript("INSTANCE_SCRIPT", scriptLines);
        }
        
        // execute the script (the object is optional)
        try {
            scriptToExecute.executeOnObject(object, argVals);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 
     * @param defLine
     */
    protected void handleExecuteObjectScript(String defLine) {
        String[] parts = defLine.split(":");
        if (parts.length != 2) {
            
        }
        
        String objName = parts[0];
        String scriptName = null;
        EList<String> argVals = EList.newList();
        
        final int len = parts[1].length();
        var scriptNameBuilder = new EStringBuilder();
        String argString = null;
        for (int i = 0; i < len; i++) {
            char c = defLine.charAt(i);
            if (c == '(') {
                scriptName = scriptNameBuilder.clear();
                argString = defLine.substring(i);
                break;
            }
        }
        if (argString == null) {
            throw createSyntaxError(INCOMPLETE_SCRIPT_CALL);
        }
        argString = argString.substring(1, argString.length() - 1);
        String[] argParts = argString.split(",");
        for (int i = 0; i < argParts.length; i++) {
            argVals.add(argParts[i].trim());
        }
        
        ParserObject parserObject = parsedGlobalObjects.get(objName);
        if (parserObject == null) {
            throw createParsingError("Undefined object '" + objName + "'!");
        }
        
        ParserScript scriptToExecute = parserObject.getScriptMap().get(scriptName);
        if (scriptToExecute == null) {
            throw createParsingError("Undefined script '" + scriptName + "'!");
        } 
        
        // execute the script
        try {
            scriptToExecute.executeOnObject(parserObject, argVals);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Attmepts to import the script values from the given file path.
     * 
     * @param toImport A file name or path to import
     */
    protected void handleImport(final String toImport) throws Exception {
        String imp = toImport;
        String extension = EFileUtil.getFileExtension(imp);
        
        if (extension == null) {
            imp = imp + ".ini";
        }
        else if (!extension.equals("ini")) {
            throw createParsingError("Unexpected file format to parse: '" + imp + "'! Expected an '*.ini' file!");
        }
        
        File fileToImport;
        
        // attempt relative path lookup first
        fileToImport = new File(theFile.getParentFile(), imp);
        // try conjoined path string from root dir
        if (!fileToImport.exists()) fileToImport = new File(rootDir + imp);
        // otherwise, try exact file path
        if (!fileToImport.exists()) fileToImport = new File(imp);
        
        // if the path still doesn't exist, throw error
        if (!fileToImport.exists()) {
            throw new FileNotFoundException("Cannot find a file using the given import path of: '" + toImport + "'!");
        }
        
        // create a new nested FileParser using this file as the new parent file
        // -- also pass any passed global values to the nested parser
        var parser = new EngineStructureFileParser(rootDir, fileToImport, theFile.getParentFile(), internalVariableValues, fileStackTrace);
        parser.parsedGlobalVariables = parsedGlobalVariables;
        parser.parsedGlobalScripts = parsedGlobalScripts;
        parser.parsedGlobalObjects = parsedGlobalObjects;
        parser.gameBeingParsed = gameBeingParsed;
        parser.gameDir = gameDir;
        parser.registry = registry;
        fileStackTrace.push(theFile, curLineNumber);
        parser.parseFile();
        fileStackTrace.pop();
    }
    
    //=============================
    // Engine JSON Loader Handlers
    //=============================
    
    protected void handleLoadJsonGameDef(String defLine) {
        File gameFile = new File(rootDir, defLine);
        gameDir = new LoadedGameDirectory(rootDir, gameFile);
    }
    
    protected void handleLoadJson(String defLine, ResourceType type) {
        File jsonFile = new File(theFile.getParentFile(), defLine);
        if (!jsonFile.exists()) jsonFile = new File(defLine);
        
        var manager = GameResourceRegistry.createResourceManagerInstance(type, jsonFile);
        registry.addManager(manager, type);
    }
    
    //=========
    // Methods
    //=========
    
    protected String advanceLine() {
        if (atEnd()) return null;
        curLineIndex++;
        curLineNumberIndex++;
        if (atEnd()) return null;
        
        curLine = lines.get(curLineIndex);
        curLineNumber = lineNumbers.get(curLineNumberIndex);
        curTabAmount = getLineTabAmount(curLine);
        
        return curLine;
    }
    
    protected boolean atEnd() {
        return curLineIndex >= numLines || curLineNumberIndex >= numLines;
    }
    
    protected void expectLineTabAmount(int expectedTabAmount) {
        if (expectedTabAmount < 0) {
            throw new IllegalArgumentException("Expected a non-negative number of tabs!");
        }
        if (curLine == null) {
            throw new IllegalStateException("There is no current line in parser!");
        }
        
        assertLineTabAmount(expectedTabAmount, curTabAmount);
    }
    
    protected void expectLineTabAmount(String line, int expectedTabAmount) {
        if (expectedTabAmount < 0) {
            throw new IllegalArgumentException("Expected a non-negative number of tabs!");
        }
        if (line == null) {
            throw new IllegalStateException("There is no current line in parser!");
        }
        
        int actual = getLineTabAmount(line);
        
        assertLineTabAmount(expectedTabAmount, actual);
    }
    
    protected int getLineTabAmount(String line) {
        boolean expectSpaces = line.startsWith(" ");
        final int len = line.length();
        
        int tabAmount = 0;
        
        if (expectSpaces) {
            int spaceCount = 0;
            for (int i = 0; i < len; i++) {
                char c = line.charAt(i);
                if (c == ' ') {
                    spaceCount++;
                    if (spaceCount == TAB_NUM_SPACES) {
                        tabAmount++;
                        spaceCount = 0;
                    }
                }
                else if (c == '\t') {
                    throw createSyntaxError("Inconsistent line tab characters! Expected to only parse spaces but " +
                                            "parsed a tab '\\t' character instead!");
                }
                else {
                    break;
                }
            }
            if (spaceCount != 0) {
                return -1;
            }
        }
        else {
            for (int i = 0; i < len; i++) {
                char c = line.charAt(i);
                if (c == '\t') {
                    tabAmount++;
                }
                else if (c == ' ') {
                    throw createSyntaxError("Inconsistent line tab characters! Expected to only tabs but parsed a " +
                                            "space ' ' character instead!");
                }
                else {
                    break;
                }
            }
        }
        
        return tabAmount;
    }
    
    //=========================
    // Internal Helper Methods
    //=========================
    
    protected boolean checkComment(String line) {
        String lt = line.trim();
        String ltNoSpace = lt.replace(" ", "");
        
        // if we're in a multiline, check if we've reached the end of it
        if (inMultilineComment) {
            if (lt.isEmpty()) return true;
            boolean end = lt.endsWith(":#");
            inMultilineComment = !end;
            return true;
        }
        // check for 'multiline' comments that don't actually define any comment text
        else if (ltNoSpace.equals("#::#")) {
            return true;
        }
        // check for the start of a potential multiline comments
        else if (lt.startsWith("#:")) {
            // if the multiline comment contains comment text but doesn't end on the same line
            // -- then we are actually in a multiline comment
            if (!lt.endsWith(":#")) {
                multilineCommentStartLine = curParsingLine;
                inMultilineComment = true;
            }
            return true;
        }
        // otherwise, if the line just starts with a '#', then we're just in a single line comment
        return lt.startsWith("#");
    }
    
    /** Attempts to parse a keyword at the line's start. */
    protected ParserKeyword parseKeyword(int expectedTab) {
        expectLineTabAmount(expectedTab);
        return ParserKeyword.findKeywordAtLineStart(curLine);
    }
    
    protected void assertLineTabAmount(int expected, int actual) {
        if (actual != expected) {
            throw createSyntaxError("Unexpected tab amount at beginning of line! Expected '" + expected +
                                    "' but got '" + actual + "' instead!");
        }
    }
    
    protected Box2<String, String> parseKeyValuePair(String line, String separator, String incompleteError, String nullValueError) {
        String[] parts = line.split(separator);
        
        if (parts.length < 2 || parts.length > 2) {
            throw createPropertyError(incompleteError);
        }
        
        String name = parts[0].trim();
        String value = parts[1].trim();
        
        return new Box2<>(name, value);
    }
    
    protected EList<String> parseScriptLines(int expectedTab) {
        EList<String> scriptLines = EList.newList();
        
        while (curTabAmount >= expectedTab) {
            scriptLines.add(curLine);
            advanceLine();
        }
        
        return scriptLines;
    }
    
    protected void rebuildVarMap() {
        varMap.clear();
        // start with internal variable definitions
        varMap.putAll(internalVariableValues);
        // then override any internally defined variables with global definitions
        varMap.putAll(parsedGlobalVariables);
        // then finally override any globally defined variables with local definitions
        varMap.putAll(parsedLocalVariables);
    }
    
    protected String injectVariablesIntoLine(final String inputLine) {
        // go through the line and find '${VAR_NAME}' instances and recursively
        // replace variable names with their respective values
        String line = inputLine;
        Matcher m = VAR_PATTERN.matcher(inputLine);
        
        while (m.find()) {
            String foundVarRef = m.group(1);
            
            if (varMap.containsKey(foundVarRef)) {
                String value = varMap.get(foundVarRef);
                line = line.replaceFirst(VAR_REGEX, value != null ? value : m.group());
                m.reset(line);
            }
        }

        return line;
    }
    
    protected void setVarValue(String defLine, Map<String, String> variableMap, String error) {
        if (defLine.isEmpty()) {
            throw createSyntaxError(error);
        }
        
        String varName = null;
        String value = null;
        
        var equal = defLine.split("=");
        if (equal.length != 2) {
            throw createSyntaxError(error);
        }
        
        varName = equal[0].trim();
        value = equal[1].trim();
        
        variableMap.put(varName, value);
        rebuildVarMap();
    }
    
    protected void setVarValueFromObject(String defLine, Map<String, String> variableMap, String error) {
        if (defLine.isEmpty()) {
            throw createSyntaxError(error);
        }
        
        String varName = null;
        String objName = null;
        String objPropertyName = null;
        
        var equal = defLine.split("=");
        if (equal.length != 2) {
            throw createSyntaxError(error);
        }
        
        var dot = equal[1].split(".");
        if (dot.length != 2) {
            throw createSyntaxError(error);
        }
        
        varName = equal[0].trim();
        objName = dot[0].trim();
        objPropertyName = dot[1].trim();
        
        ParserObject obj = parsedGlobalObjects.get(objName);
        if (obj == null) {
            throw createParsingError("Undefined object '" + objName + "'!");
        }
        if (!obj.hasPropertyName(objPropertyName)) {
            throw createParsingError("Undefined property '" + objPropertyName + "' on object '" + objName + "'!");
        }
        
        String objValue = obj.getPropertyValue(objPropertyName);
        variableMap.put(varName, objValue);
        rebuildVarMap();
    }
    
    /**
     * If a property value is equal to:
     * <li>"NONE" then 'null' is returned.
     * <li>"\"NONE\"" then 'NONE' is returned.
     * <li>otherwise, the value itself is returned.
     * 
     * @param value The property value to get
     * @return The corresponding value for what the given 'value' string
     *             represents
     */
    protected String getPropertyValue(String value) {
        return switch (value) {
        case "NONE" -> null;
        case "\"NONE\"" -> "NONE";
        default -> value;
        };
    }
    
    //================
    // Error Creation
    //================
    
    /** A recoverable error when parsing a property or variable value. */
    protected PropertyError createPropertyError(String reason) {
        return new PropertyError(populateErrString(reason));
    }
    
    /** A unrecoverable error indicating a malformed input was parsed. */
    protected SyntaxError createSyntaxError(String reason) {
        return new SyntaxError(populateErrString(reason));
    }
    
    /** An unrecoverable error that usually relates to problems that go beyond the parser's scope. */
    protected ParsingError createParsingError(String reason) {
        return new ParsingError(populateErrString(reason));
    }
    
    protected String populateErrString(String reason) {
        var sb = new EStringBuilder(reason);
        sb.println();
        sb.println("At: " + theFile + ":" + curLineNumber);
        sb.println("Line: '" + curLine + "'");
        sb.println();
        if (fileStackTrace.size() > 1) {
            sb.println("File Stack Trace:");
            for (var b : fileStackTrace) {
                sb.println("  ", b.getA(), ":", b.getB());
            }
            sb.println();
        }
        
        return sb.toString();
    }
    
    //=========
    // Getters
    //=========
    
    /** The number of parsed lines with actual content on them. */
    protected int getNumberOfLines() { return numLines; }
    /** The total number of lines in the file. */
    protected int getTotalNumberOfLinesInFile() { return numLinesInFile; }
    /** The current line content. */
    protected String currentLine() { return curLine; }
    /** The line number of the current line. */
    protected int currentLineNumber() { return curLineNumber; }
    /** The current line tab amount. */
    protected int getCurrentLineTabAmount() { return curTabAmount; }
    
}
