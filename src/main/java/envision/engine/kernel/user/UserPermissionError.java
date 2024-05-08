package envision.engine.kernel.user;

/**
 * An exception thrown when a user attempts to perform an action that they
 * do not have permission for.
 * 
 * @author Hunter Bragg
 */
public class UserPermissionError extends RuntimeException {
    
    public UserPermissionError() {
        super();
    }
    
    public UserPermissionError(String reason) {
        super(reason);
    }
    
}
