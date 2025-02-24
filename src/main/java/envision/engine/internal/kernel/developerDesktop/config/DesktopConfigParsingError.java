package envision.engine.internal.kernel.developerDesktop.config;

public class DesktopConfigParsingError extends RuntimeException {
    
    public DesktopConfigParsingError(String reason) {
        super(reason);
    }
    
    public DesktopConfigParsingError(String reason, int lineNum) {
        super("Desktop Config Parsing Error: " + reason + " Line: '" + lineNum + "'");
    }
    
}
