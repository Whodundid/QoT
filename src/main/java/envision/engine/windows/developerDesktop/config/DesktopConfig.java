package envision.engine.windows.developerDesktop.config;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import envision.engine.windows.developerDesktop.shortcuts.DesktopShortcut;
import eutil.datatypes.util.EList;
import eutil.strings.EStringBuilder;

public class DesktopConfig {
    
    //========
    // Fields
    //========
    
    private String configName;
    private String version;
    private final ConcurrentMap<String, String> variables = new ConcurrentHashMap<>();
    private final EList<DesktopShortcut> shortcuts = EList.newList();
    private final EList<DesktopConfig> childConfigs = EList.newList();
    
    /** A flag for whether or not this config file is parsed. */
    private volatile boolean isParsed = false;
    
    //==============
    // Constructors
    //==============
    
    public DesktopConfig() {
        // does nothing by default
    }
    
    public DesktopConfig(String configName) {
        this.configName = configName;
    }
    
    //===========
    // Overrides
    //===========
    
    @Override
    public String toString() {
        var sb = new EStringBuilder();
        sb.println("Config Name: ", configName, " : ", version);
        sb.incrementTabCount();
        sb.println("Parent Configs: ", childConfigs.map(c -> c.getConfigName()));
        sb.println("Variables: ", variables);
        sb.println("Shortcuts: ", shortcuts);
        return sb.toString();
    }
    
    //========
    // Saving
    //========
    
    public String toSaveString() {
        var sb = DesktopConfigParser.buildConfigHeader();
        
        sb.println(DesktopConfigKeyword.CONFIG_NAME.text, ": ", configName);
        sb.println(DesktopConfigKeyword.CONFIG_VERSION.text, ": ", version);
        sb.println();
        
        // write variables
        if (!variables.isEmpty()) {
            for (var v : variables.entrySet()) {
                sb.println(DesktopConfigKeyword.DEF_VAR, ": ", v.getKey(), " = ", v.getValue());
            }
        }
        
        // write shortcuts
        if (shortcuts.isNotEmpty()) {
            sb.println(DesktopConfigKeyword.SHORTCUTS, ":");
            sb.incrementTabCount();
            for (var s : shortcuts) {
                s.generateSaveString(sb);
                sb.println();
            }
            sb.decrementTabCount();
        }
        
        return sb.toString();
    }
    
    //=========
    // Methods
    //=========
    
    public void setParsed(boolean val) {
        isParsed = val;
    }
    
    public boolean isParsed() {
        return isParsed;
    }
    
    public void clear() {
        configName = null;
        version = null;
        variables.clear();
        shortcuts.clear();
        childConfigs.clear();
    }
    
    public void addVariable(String name, String value) {
        variables.put(name, value);
    }
    
    public void addShortcut(DesktopShortcut shortcut) {
        shortcuts.add(shortcut);
    }
    
    public void removeShortcut(DesktopShortcut shortcut) {
        shortcuts.remove(shortcut);
    }
    
    public void addChildConfig(DesktopConfig configIn) {
        childConfigs.add(configIn);
    }
    
    //=========
    // Getters
    //=========
    
    public String getVariableValue(String variableName) {
        return variables.get(variableName);
    }
    
    public String getConfigName() { return configName; }
    public String getVersion() { return version; }
    public Map<String, String> getParsedVariables() { return new HashMap<>(variables); }
    public EList<DesktopShortcut> getShortcuts() { return shortcuts; }
    public EList<DesktopConfig> getChildConfigs() { return childConfigs; }
    
    //=========
    // Setters
    //=========
    
    public void setName(String nameIn) { configName = nameIn; }
    public void setVersion(String versionIn) { version = versionIn; }
    
}
