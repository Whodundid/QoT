package envision.engine.terminal.terminalUtil;

import envision.engine.terminal.window.ETerminalWindow;

public class TermArgParsingException extends RuntimeException {
    
    private final String errorString;
    
    public TermArgParsingException(Exception cause) {
        this(cause, null);
    }
    
    public TermArgParsingException(Exception cause, String errorStringIn) {
        super(cause);
        errorString = errorStringIn;
    }
    
    public String getErrorString() {
        return errorString;
    }
    
    public void display(ETerminalWindow term) {
        var cause = getCause();
        term.javaError(cause);
        if (errorString != null) term.error(errorString);
    }
    
}
