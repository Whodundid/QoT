package envision.engine.loader.parser;

class SyntaxError extends ParsingError {
    
    public SyntaxError(String reason) {
        super("Syntax Error: " + reason);
    }
    
    public SyntaxError(Throwable error) {
        super("Syntax Error: " + error);
    }
    
    public SyntaxError(String reason, Throwable error) {
        super("Syntax Error: " + reason, error);
    }
    
}
