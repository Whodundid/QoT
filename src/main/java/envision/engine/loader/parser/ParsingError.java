package envision.engine.loader.parser;

class ParsingError extends RuntimeException {
    
    public ParsingError(String reason) {
        super(reason);
    }
    
    public ParsingError(Throwable error) {
        super(error);
    }
    
    public ParsingError(String reason, Throwable error) {
        super(reason, error);
    }
    
}
