package envision.engine.terminal.commands;

import eutil.datatypes.util.EList;

public class CommandResult {
    
    private final TerminalCommand theCommand;
    private final EList<String> argumentsPassed = EList.newList();
    private final boolean wasCommandRunVisually;

    private final EList<String> outputLines = EList.newList();
    
    public CommandResult(TerminalCommand command, EList<String> passedArguments, boolean wasRunVisually) {
        theCommand = command;
        argumentsPassed.addAll(passedArguments);
        wasCommandRunVisually = wasRunVisually;
    }
    
    public void addNewLine() {
        outputLines.add("");
    }
    
    public void addLine(String line) {
        outputLines.add(line);
    }
    
    public TerminalCommand getExecutedCommand() { return theCommand; }
    public EList<String> getPassedArguments() { return argumentsPassed; }
    public boolean wasCommandRunVisually() { return wasCommandRunVisually; }
    public EList<String> getOutputLines() { return outputLines; }
    
}
