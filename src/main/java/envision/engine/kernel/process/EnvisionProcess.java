package envision.engine.kernel.process;

import envision.engine.kernel.user.UserProfile;

public abstract class EnvisionProcess implements IEnvisionProcess {
    
    //========
    // Fields
    //========
    
    private String processName;
    
    //private Thread processThread;
    private UserProfile launchingUser;
    private int pid;
    private String launchCommand;
    private long startTime;
    
    private EnvisionProcessState state = EnvisionProcessState.NOT_RUNNING;
    
    //==============
    // Constructors
    //==============
    
    public EnvisionProcess() {}
    public EnvisionProcess(String processName) {
        this.processName = processName;
    }
    
    //===========
    // Overrides
    //===========
    
   //@Override public final Thread processThread() { return processThread; }
    @Override public final UserProfile launchingUser() { return launchingUser; }
    @Override public final int pid() { return pid; }
    @Override public final String launchCommand() { return launchCommand; }
    @Override public final long startTime() { return startTime; }
    @Override public final EnvisionProcessState state() { return state; }
    @Override public final void setProcessState(EnvisionProcessState state) { this.state = state; }
    
    @Override
    public final void kill() {
        state = EnvisionProcessState.DYING;

//        if (processThread == null) return;
//        processThread.interrupt();
    }
    
    @Override
    public final void onStart(UserProfile launchingUser, String launchCommand, int pid, long startTime) {
        this.launchingUser = launchingUser;
        this.launchCommand = launchCommand;
        //this.processThread = processThread;
        this.pid = pid;
        this.startTime = startTime;
        if (processName == null) processName = getClass().getSimpleName();
        run();
    }
    
    //=========
    // Getters
    //=========
    
    public String processName() { return processName; }
    
}
