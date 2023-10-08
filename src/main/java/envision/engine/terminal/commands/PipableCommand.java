package envision.engine.terminal.commands;

public interface PipableCommand {
    
    String getPipedResult();
    
    default int getPipedArgumentTargetIndex() { return 0; }
    
}
