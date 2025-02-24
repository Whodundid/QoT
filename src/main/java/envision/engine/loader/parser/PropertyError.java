package envision.engine.loader.parser;

class PropertyError extends ParsingError {
    
    public PropertyError(String reason) {
        super("Property Error: " + reason);
    }
    
    public PropertyError(Throwable error) {
        super("Property Error: " + error);
    }
    
    public PropertyError(String reason, Throwable error) {
        super("Property Error: " + reason, error);
    }
    
}
