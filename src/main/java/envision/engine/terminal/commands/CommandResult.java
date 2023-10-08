package envision.engine.terminal.commands;

import eutil.datatypes.util.EList;

public class CommandResult {
    
    private final EList<String> outputLines = EList.newList();
    
    public void addLine(String line) {
        outputLines.add(line);
    }
    
}
