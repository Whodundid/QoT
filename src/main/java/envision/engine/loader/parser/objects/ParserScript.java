package envision.engine.loader.parser.objects;

import java.util.HashMap;
import java.util.Map;

import envision_lang._launch.EnvisionProgram;
import envision_lang._launch.EnvisionProgramRunner;
import eutil.datatypes.util.EList;
import eutil.debug.Optional;

/**
 * A script that is defined and executed within the bounds of a parsed engine
 * structure ini file.
 * 
 * @author Hunter Bragg
 */
public class ParserScript {
    
    //========
    // Fields
    //========
    
    private final String scriptName;
    private final EList<String> scriptLines;
    private final EList<String> argumentNames = EList.newList();
    
    private EnvisionProgramRunner runner;
    private EnvisionProgram script;
    
    //==============
    // Constructors
    //==============
    
    public ParserScript(String scriptName, EList<String> scriptLines) {
        this.scriptName = scriptName;
        this.scriptLines = scriptLines;
    }
    
    //=========
    // Methods
    //=========
    
    public void setArgumentNames(EList<String> argumentNames) {
        this.argumentNames.addAll(argumentNames);
    }
    
    public void execute() { executeOnObject(null, EList.newList()); }
    public void execute(@Optional EList<String> args) { executeOnObject(null, args); }
    public void executeOnObject(@Optional ParserObject object) { executeOnObject(object, EList.newList()); }
    public void executeOnObject(@Optional ParserObject object, @Optional EList<String> args) {
        // make sure this isn't null
        if (args == null) args = EList.newList();
        // ensure we have the same number of arguments passed that the script expects
        if (args.size() != argumentNames.size()) {
            throw new IllegalStateException("Incorrect number of script arguments passed! Expected '" +
                                            argumentNames.size() + "' but got '" + args.size() + "' instead!");
        }
        
        // inject variables into script scope
        EList<String> processedLines = processScriptLines(object, args);
        
        // start the script
        try {
            script = new EnvisionProgram(scriptName, processedLines);
            runner = new EnvisionProgramRunner(script);
            runner.start();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    //=========================
    // Internal Helper Methods
    //=========================
    
    protected EList<String> processScriptLines(ParserObject object, EList<String> args) {
        Map<String, String> mappedArgs = new HashMap<>();
        // if there is an object to execute against, define its property values upfront
        if (object != null) {
            for (var p : object.getPropertyMap().entrySet()) {
                mappedArgs.put(p.getKey(), p.getValue());
            }
        }
        // define any passed arguments on top of potential object properties
        for (int i = 0; i < args.size(); i++) {
            String name = argumentNames.get(i);
            String value = args.get(i);
            mappedArgs.put(name, value);
        }
        
        final int len = scriptLines.size();
        EList<String> processedLines = EList.newList();
        
        for (int i = 0; i < len; i++) {
            String line = scriptLines.get(i);
            
            for (var v : mappedArgs.entrySet()) {
                if (line.contains("${" + v.getKey() + "}")) {
                    line.replace("${" + v.getKey() + "}", v.getValue());
                }
            }
            
            processedLines.add(line);
        }
        
        return processedLines;
    }
    
}
