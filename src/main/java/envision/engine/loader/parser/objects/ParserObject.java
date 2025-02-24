package envision.engine.loader.parser.objects;

import java.util.HashMap;
import java.util.Map;

import eutil.datatypes.util.EList;

/**
 * An object that can be created and referenced from within a parsed engine
 * structure ini file.
 * 
 * @author Hunter Bragg
 */
public class ParserObject {
    
    //========
    // Fields
    //========
    
    private final String name;
    private final EList<ParserObject> parents = EList.newList();
    private final Map<String, String> properties = new HashMap<>();
    private final Map<String, ParserScript> scripts = new HashMap<>();
    
    //==============
    // Constructors
    //==============
    
    public ParserObject(String name) {
        this.name = name;
    }
    
    //===========
    // Overrides
    //===========
    
    @Override
    public String toString() {
        return name;
    }
    
    //=========
    // Methods
    //=========
    
    public void defineProperty(String name, String value) {
        properties.put(name, value);
    }
    
    public void defineScript(String scriptName, ParserScript script) {
        scripts.put(scriptName, script);
    }
    
    public void inheritFrom(ParserObject parent) {
        if (parents.contains(parent)) {
            throw new IllegalStateException("Object: '" + name + "' already defines '" + parent + "' as a parent!");
        }
        
        parents.add(parent);
        
        // if this object already defines a property, skip it to maintain inheritance definition order
        for (var p : parent.properties.entrySet()) {
            if (properties.containsKey(p.getKey())) continue;
            properties.put(p.getKey(), p.getValue());
        }
        // same with scripts
        for (var s : parent.scripts.entrySet()) {
            if (scripts.containsKey(s.getKey())) continue;
            scripts.put(s.getKey(), s.getValue());
        }
    }
    
    //=========
    // Getters
    //=========
    
    public boolean hasPropertyName(String name) {
        return properties.containsKey(name);
    }
    
    public String getPropertyValue(String name) {
        return properties.get(name);
    }
    
    public Map<String, String> getPropertyMap() {
        return properties;
    }
    
    public Map<String, ParserScript> getScriptMap() {
        return scripts;
    }
    
    public EList<ParserObject> getParentHierarchy() {
        return parents;
    }
    
}
