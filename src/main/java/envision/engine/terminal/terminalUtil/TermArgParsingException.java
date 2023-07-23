package envision.engine.terminal.terminalUtil;

import envision.engine.terminal.window.ETerminalWindow;

public class TermArgParsingException extends RuntimeException {
    
    private String errorString;
    
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
        term.javaError(getCause());
        if (errorString != null) term.error(errorString);
    }
    
}
