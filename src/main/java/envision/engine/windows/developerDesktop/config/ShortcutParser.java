package envision.engine.windows.developerDesktop.config;

import java.io.File;

import envision.engine.windows.developerDesktop.shortcuts.*;
import eutil.datatypes.boxes.Box2;
import eutil.datatypes.boxes.BoxList;
import eutil.datatypes.util.EList;
import eutil.strings.EStringBuilder;
import eutil.strings.EStringUtil;

public class ShortcutParser {
    
    private ShortcutParser() {}
    
    public static int parseShortcuts(DesktopConfig config, BoxList<Integer, String> lines, int startIndex) {
        final int size = lines.size();

        // parse out the 'SHORTCUTS:' line first
        int i = startIndex + 1;
        
        if (i >= size) return startIndex;
        
        for (; i < size; i++) {
            String line = lines.getB(i);
            if (!DesktopConfigParser.checkTab(line, 1)) break;
            
            line = line.trim();
            ShortcutType type = ShortcutType.parseShortcutType(line);
            
            switch (type) {
            case FILE:
                i = parseFileShortcut(config, lines, i);
                break;
            case WINDOW:
                i = parseWindowShortcut(config, lines, i);
                break;
            case SCRIPT:
                i = parseScriptShortcut(config, lines, i);
                break;
            case COMMAND:
                i = parseCommandShortcut(config, lines, i);
                break;
            case UNKNOWN:
                break;
            }
        }
        
        return i;
    }
    
    //==============================================================================================
    
    private static int parseFileShortcut(DesktopConfig config, BoxList<Integer, String> lines, int startIndex) {
        int lineNum = lines.getA(startIndex);
        String line = lines.getB(startIndex).trim();
        
        line = line.substring(ShortcutType.FILE.text.length()).trim();
        line = line.substring(1).trim();
        
        if (!DesktopConfigParser.isPopulated(line)) {
            throw DesktopConfigParser.error("File shortcut name is empty!", lineNum);
        }
        
        DesktopShortcut_File shortcut = new DesktopShortcut_File();
        
        final int size = lines.size();
        int i = startIndex + 1;
        for (; i < size; i++) {
            lineNum = lines.getA(i);
            line = lines.getB(i);
            if (!DesktopConfigParser.checkTab(line, 2)) { i--; break; }
            line = line.trim();
            
            var parsedProperty = parseProperty(line, lineNum);
            String name = parsedProperty.getA();
            String value = parsedProperty.getB();
            
            switch (name) {
            case "position":
                var position = parseDoubleList(parseList(value, lineNum));
                if (position.size() != 2) throw DesktopConfigParser.error("Invalid position list size!");
                shortcut.setPosition(position.get(0), position.get(1));
                break;
            case "target":
                shortcut.setFile(new File(value));
                break;
            default:
                shortcut.addAdditionalProperty(name, value);
            }
        }
        
        shortcut.init();
        config.addShortcut(shortcut);
        return i;
    }
    
    //==============================================================================================
    
    private static int parseWindowShortcut(DesktopConfig config, BoxList<Integer, String> lines, int startIndex) {
        int lineNum = lines.getA(startIndex);
        String line = lines.getB(startIndex).trim();
        
        line = line.substring(ShortcutType.WINDOW.text.length()).trim();
        line = line.substring(1).trim();
        
        if (!DesktopConfigParser.isPopulated(line)) {
            throw DesktopConfigParser.error("File shortcut name is empty!", lineNum);
        }
        
        DesktopShortcut_Window shortcut = new DesktopShortcut_Window(line);
        
        final int size = lines.size();
        int i = startIndex + 1;
        for (; i < size; i++) {
            lineNum = lines.getA(i);
            line = lines.getB(i);
            if (!DesktopConfigParser.checkTab(line, 2)) { i--; break; }
            line = line.trim();
            
            var parsedProperty = parseProperty(line, lineNum);
            String name = parsedProperty.getA();
            String value = parsedProperty.getB();
            
            switch (name) {
            case "position":
                var position = parseDoubleList(parseList(value, lineNum));
                if (position.size() != 2) throw DesktopConfigParser.error("Invalid position list size!");
                shortcut.setPosition(position.get(0), position.get(1));
                break;
            case "target":
                shortcut.parseWindowTarget(value);
                break;
            default:
                shortcut.addAdditionalProperty(name, value);
            }
        }
        
        shortcut.init();
        config.addShortcut(shortcut);
        return i;
    }
    
    //==============================================================================================
    
    private static int parseScriptShortcut(DesktopConfig config, BoxList<Integer, String> lines, int startIndex) {
        int lineNum = lines.getA(startIndex);
        String line = lines.getB(startIndex).trim();
        
        line = line.substring(ShortcutType.SCRIPT.text.length()).trim();
        line = line.substring(1).trim();
        
        if (!DesktopConfigParser.isPopulated(line)) {
            throw DesktopConfigParser.error("File shortcut name is empty!", lineNum);
        }
        
        return startIndex;
    }
    
    //==============================================================================================
    
    private static int parseCommandShortcut(DesktopConfig config, BoxList<Integer, String> lines, int startIndex) {
        int lineNum = lines.getA(startIndex);
        String line = lines.getB(startIndex).trim();
        
        line = line.substring(ShortcutType.COMMAND.text.length()).trim();
        line = line.substring(1).trim();
        
        if (!DesktopConfigParser.isPopulated(line)) {
            throw DesktopConfigParser.error("File shortcut name is empty!", lineNum);
        }
        
        DesktopShortcut_Command shortcut = new DesktopShortcut_Command(line);
        
        final int size = lines.size();
        int i = startIndex + 1;
        for (; i < size; i++) {
            lineNum = lines.getA(i);
            line = lines.getB(i);
            if (!DesktopConfigParser.checkTab(line, 2)) { i--; break; }
            line = line.trim();
            
            var parsedProperty = parseProperty(line, lineNum);
            String name = parsedProperty.getA();
            String value = parsedProperty.getB();
            
            switch (name) {
            case "position":
                var position = parseDoubleList(parseList(value, lineNum));
                if (position.size() != 2) throw DesktopConfigParser.error("Invalid position list size!");
                shortcut.setPosition(position.get(0), position.get(1));
                break;
            case "command":
                shortcut.parseCommand(value);
                break;
            case "args":
                shortcut.setArguments(parseList(value, lineNum));
                break;
            case "openTerminal":
                shortcut.setOpenTerminal(Boolean.parseBoolean(value));
                break;
            default:
                shortcut.addAdditionalProperty(name, value);
            }
        }
        
        shortcut.init();
        config.addShortcut(shortcut);
        return i;
    }
    
    //=========================
    // Internal Helper Methods
    //=========================
    
    private static Box2<String, String> parseProperty(String line, int lineNum) {
        if (!DesktopConfigParser.isPopulated(line)) throw DesktopConfigParser.error("NULL shortcut property line!");
        if (!line.contains(":")) throw DesktopConfigParser.error("Invalid shortcut property line!");
        
        final int len = line.length();
        final var nameBuilder = new EStringBuilder();
        
        String name = null;
        String value = null;
        
        for (int i = 0; i < len; i++) {
            char c = line.charAt(i);
            if (c == ':') {
                name = nameBuilder.trim();
                if (line.length() > (i + 1)) {
                    value = line.substring(i + 1).trim();
                }
                else {
                    throw DesktopConfigParser.error("Incomplete shortcut property declaration!", lineNum);
                }
                break;
            }
            nameBuilder.a(c);
        }
        
        if (name == null) {
            throw DesktopConfigParser.error("Shortcut property declaration has a NULL name!", lineNum);
        }
        
        if (value == null) {
            throw DesktopConfigParser.error("Shortcut property declaration has a NULL value!", lineNum);
        }
        
        return new Box2<>(name, value);
    }
    
    private static EList<String> parseList(String line, int lineNum) {
        EList<String> list = EList.newList();
        
        if (!EStringUtil.startsAndEndsWith(line, "[", "]")) return null;
        line = line.substring(1, line.length() - 1).trim();
        
        final int length = line.length();
        var sb = new EStringBuilder();
        char stringStartChar = '\u0000';
        boolean inString = false;
        
        int i = 0;
        for (; i < length; i++) {
            char c = line.charAt(i);
            
            // if 'c' is a string char, check if currently in string
            if (c == '\'' || c == '"') {
                if (!inString) {
                    stringStartChar = c;
                    inString = true;
                }
                else if (c == stringStartChar) {
                    stringStartChar = '\u0000';
                    inString = false;
                }
            }
            // otherwise, if 'c' is a ',' and it's not currently a string
            // separate this value from the line and add it as an element
            else if (c == ',' && !inString) {
                String value = sb.trimClear();
                if (!DesktopConfigParser.isPopulated(value)) {
                    throw DesktopConfigParser.error("Empty list value at String index: '" + i + "'!", lineNum);
                }
                list.add(value);
                continue;
            }
            
            sb.a(c);
        }
        
        if (sb.isNotEmpty()) {
            String value = sb.trimClear();
            if (!DesktopConfigParser.isPopulated(value)) {
                throw DesktopConfigParser.error("Empty list value at String index: '" + i + "'!", lineNum);
            }
            list.add(value);
        }
        
        return list;
    }
    
    private static EList<Double> parseDoubleList(EList<String> list) {
        EList<Double> doubles = EList.newList();
        
        for (String s : list) {
            // if this breaks, I don't want it to be caught
            doubles.add(Double.parseDouble(s));
        }
        
        return doubles;
    }
    
    private static EList<Boolean> parseBooleanList(EList<String> list) {
        EList<Boolean> doubles = EList.newList();
        
        for (String s : list) {
            // if this breaks, I don't want it to be caught
            doubles.add(Boolean.parseBoolean(s.toLowerCase()));
        }
        
        return doubles;
    }
    
}
