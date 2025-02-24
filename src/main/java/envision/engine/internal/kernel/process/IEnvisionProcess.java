package envision.engine.internal.kernel.process;

import envision.engine.internal.kernel.user.UserProfile;

public interface IEnvisionProcess extends Runnable {
    
    /** @return the underlying thread for this process. */
    //Thread processThread();
    /** @return the user profile that launched the given process. */
    UserProfile launchingUser();
    /** @return this processes internal ID. */
    int pid();
    /** @return the command that was used to launch this process. */
    String launchCommand();
    /** @return the time this process was started. */
    long startTime();
    /** @return the active state of this process. */
    EnvisionProcessState state();
    /** Used to internally set the state of this process. */
    void setProcessState(EnvisionProcessState stateIn);
    
    /** Notifies this process that it is being destroyed right now. */
    void kill();
    /** Called by the kernel when this process has been started. */
    void onStart(UserProfile launchingUser, String launchCommand, int pid, long startTime);
    
    /** @return the minimum permission level requried to start this process. */
    default byte requiredPermissionLevel() { return 1; }
    /** @return the time in ms that this process has been alive for. */
    default long timeAlive() { return System.currentTimeMillis() - startTime(); }
    
}
