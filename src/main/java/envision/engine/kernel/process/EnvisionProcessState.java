package envision.engine.kernel.process;

public enum EnvisionProcessState {
    /** The process has not even been registered yet. */
    NOT_RUNNING,
    /** The process is actively being registered and started. */
    SPAWNING,
    /** The process is currently running. */
    RUNNING,
    /** The process is scheduled to be terminated. */
    DYING,
    /** The process completed execution. */
    DEAD,
}
