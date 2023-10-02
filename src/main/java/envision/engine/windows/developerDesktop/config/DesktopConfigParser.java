package envision.engine.windows.developerDesktop.config;

import java.io.IOException;
import java.nio.file.Files;

import envision.engine.windows.developerDesktop.DeveloperDesktop;
import eutil.datatypes.boxes.BoxList;
import eutil.datatypes.util.EList;
import eutil.file.LineReader;
import eutil.strings.EStringBuilder;
import eutil.strings.EStringUtil;

/**
 * A wrapper for a file that keeps track of all shortcuts/programs/items on
 * the desktop along with their positions.
 * <p>
 * When any action modifies the desktop, the corresponding content file
 * should be updated to reflect these changes across engine runs.
 * 
 * @author Hunter Bragg
 */
public class DesktopConfigParser {
    
    public static final String CONFIG_DEFAULT_NAME = "DESKTOP";
    public static final String CONFIG_PARSER_VERSION = "0.1";
    
    static {
        if (!DeveloperDesktop.DESKTOP_CONFIG_FILE.exists()) {
            try {
                createDefaultConfigFile();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    public static void loadConfigFile() {
        try {
            if (!DeveloperDesktop.DESKTOP_CONFIG_FILE.exists()) {
                createDefaultConfigFile();
            }
            
            var config = DeveloperDesktop.getDesktopConfig();
            config.setParsed(false);
            parseConfigFile(config);
            config.setParsed(true);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void saveConfigFile() {
        try {
            String saveString = DeveloperDesktop.getDesktopConfig().toSaveString();
            byte[] bytes = saveString.getBytes();
            writeToFile(bytes);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private static void writeToFile(byte[] toWrite) throws IOException {
        Files.write(DeveloperDesktop.DESKTOP_CONFIG_FILE.toPath(), toWrite);
    }
    
    private static void createDefaultConfigFile() throws IOException {
        var sb = buildConfigHeader();
        sb.println(DesktopConfigKeyword.CONFIG_NAME.text, ": ", CONFIG_DEFAULT_NAME);
        sb.println(DesktopConfigKeyword.CONFIG_VERSION.text, ": 1.0");
        writeToFile(sb.getBytes());
    }
    
    static EStringBuilder buildConfigHeader() {
        EStringBuilder sb = new EStringBuilder();
        String top = DesktopConfigKeyword.PARSER_VERSION.text + ": " + CONFIG_PARSER_VERSION;
        String divider = "#" + "=".repeat(top.length());
        sb.println(divider);
        sb.println(top);
        sb.println(divider);
        sb.println();
        return sb;
    }
    
    //=========
    // Parsers
    //=========
    
    private static synchronized void parseConfigFile(DesktopConfig config) throws IOException {
        config.clear();
        
        BoxList<Integer, String> parsedLines = parseVariables(config);
        
        final int size = parsedLines.size();
        int i = 0;
        
        for (; i < size; i++) {
            var lineBox = parsedLines.get(i);
            int lineNum = lineBox.getA();
            String line = lineBox.getB();
            
            DesktopConfigKeyword keyword = DesktopConfigKeyword.parseKeyword(line);
            
            switch (keyword) {
            case PARSER_VERSION:
                i = parseParserVersion(parsedLines, i);
                break;
            case CONFIG_NAME:
                i = parseConfigName(config, parsedLines, i);
                break;
            case CONFIG_VERSION:
                i = parseConfigVersion(config, parsedLines, i);
                break;
            case SHORTCUTS:
                i = ShortcutParser.parseShortcuts(config, parsedLines, i);
                break;
            case UNKNOWN:
            default:
                System.out.println("UNKNOWN Desktop Config file line: [" + lineNum + "] '" + line + "'");
            }
        }
    }
    
    private static BoxList<Integer, String> parseVariables(DesktopConfig config) throws IOException {
        BoxList<Integer, String> parsedLines = new BoxList<>();
        
        int lineNum = 0;
        try (var reader = new LineReader(DeveloperDesktop.DESKTOP_CONFIG_FILE)) {
            while (reader.hasNextLine()) {
                try {
                    lineNum++;
                    String line = reader.nextLine();
                    if (line.isEmpty() || line.isBlank() || line.trim().startsWith("#")) continue;
                    parseSingleVariable(config, parsedLines, lineNum, line);
                }
                catch (DesktopConfigParsingError e) {
                    e.printStackTrace();
                }
            }
        }
        
        return parsedLines;
    }
    
    private static void parseSingleVariable(DesktopConfig config, BoxList<Integer, String> parsedLines, int lineNum, String line) {
        // ignore any and all empty lines
        if (line.isBlank() || line.isEmpty()) return;
        
        // only care if the line isn't indented at all
        if (!checkTab(line, 0)) {
            parsedLines.add(lineNum, line);
            return;
        }
        
        // check for the 'DEF_VAR' keyword
        DesktopConfigKeyword keyword = DesktopConfigKeyword.parseKeyword(line);
        // if it's not a 'DEF_VAR', add the line and move on
        if (keyword != DesktopConfigKeyword.DEF_VAR) {
            parsedLines.add(lineNum, line);
            return;
        }
        
        // remove the 'DEF_VAR'
        line = line.substring(DesktopConfigKeyword.DEF_VAR.text.length()).trim();
        // remove the ':'
        line = line.substring(1).trim();
        
        if (line.isEmpty()) {
            throw error("Incomplete variable declaration!", lineNum);
        }
        
        var sb = new EStringBuilder();
        String name = null;
        String value = null;
        
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '=') {
                name = sb.toString().trim();
                if (line.length() > (i + 1)) {
                    value = line.substring(i + 1).trim();
                }
                else {
                    throw error("Incomplete variable declaration!", lineNum);
                }
                break;
            }
            sb.a(c);
        }
        
        // don't allow empty variable names to pass
        if (name == null || name.isBlank() || name.isEmpty()) {
            throw error("Incomplete variable declaration!", lineNum);
        }
        
        // don't allow empty variable values to pass
        if (value == null || value.isBlank() || value.isEmpty()) {
            throw error("Incomplete variable declaration!", lineNum);
        }
        
        config.addVariable(name, value);
    }
    
    private static int parseParserVersion(BoxList<Integer, String> lines, int startIndex) {
        String line = lines.getB(startIndex);
        line = line.substring(DesktopConfigKeyword.PARSER_VERSION.text.length()).trim();
        line = line.substring(1).trim();
        
        return startIndex;
    }
    
    private static int parseConfigName(DesktopConfig config, BoxList<Integer, String> lines, int startIndex) {
        String line = lines.getB(startIndex);
        line = line.substring(DesktopConfigKeyword.CONFIG_NAME.text.length()).trim();
        line = line.substring(1).trim();
        
        if (line.isEmpty()) throw error("Empty config name!", lines.getA(startIndex));
        config.setName(line);
        
        return startIndex;
    }
    
    private static int parseConfigVersion(DesktopConfig config, BoxList<Integer, String> lines, int startIndex) {
        String line = lines.getB(startIndex);
        line = line.substring(DesktopConfigKeyword.CONFIG_VERSION.text.length()).trim();
        line = line.substring(1).trim();
        
        if (line.isEmpty()) throw error("Empty config version!", lines.getA(startIndex));
        config.setVersion(line);
        
        return startIndex;
    }
    
    //=========================
    // Internal Helper Methods
    //=========================
    
    static DesktopConfigParsingError error(String reason) {
        return new DesktopConfigParsingError(reason);
    }
    
    static DesktopConfigParsingError error(String reason, int lineNum) {
        return new DesktopConfigParsingError(reason, lineNum);
    }
    
    static int parseShortcut(DesktopConfig config, EList<String> lines, int startIndex) {
        return startIndex;
    }
    
    static boolean checkTab(String line, int tabAmount) {
        if (tabAmount == 0 && !line.isEmpty()) return !Character.isWhitespace(line.charAt(0));
        return EStringUtil.startsWithAny(line, "    ".repeat(tabAmount), "\t".repeat(tabAmount));
    }
    
    static boolean isPopulated(String value) {
        return value != null && !value.isBlank() && !value.isEmpty();
    }
    
}
