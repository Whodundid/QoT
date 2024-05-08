package envision.engine.kernel;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

import envision.engine.kernel.process.EnvisionProcessState;
import envision.engine.kernel.process.IEnvisionProcess;
import envision.engine.kernel.user.UserPermissionError;
import envision.engine.kernel.user.UserProfile;
import envision.engine.kernel.user.UserProfileRegistry;
import eutil.datatypes.util.EList;

public final class EnvisionKernel {
    
    //==================
    // Static Singleton
    //==================
    
    /** The singleton kernel instance. */
    private static EnvisionKernel instance;
    
    /** Gets (or creates) the single kernel instance for Envision. */
    public static EnvisionKernel getInstance() {
        if (instance != null) return instance;
        return instance = new EnvisionKernel();
    }
    
    private static final AtomicInteger pidCounter = new AtomicInteger();
    
    //========
    // Fields
    //========
    
    private final ConcurrentMap<Integer, IEnvisionProcess> activeProcesses = new ConcurrentHashMap<>();
    
    private final UserProfileRegistry profileRegistry = new UserProfileRegistry();
    
    //==============
    // Constructors
    //==============
    
    private EnvisionKernel() {
        registerNatives();
        
        var player = new UserProfile("player", 0);
        var user = new UserProfile("standard_user", 1);
        var dev = new UserProfile("dev", 2);
        
        profileRegistry.registerProfile(player);
        profileRegistry.registerProfile(user);
        profileRegistry.registerProfile(dev);
        
        profileRegistry.setCurrentUser(dev);
    }
    
    //==================
    // Internal Methods
    //==================
    
    private void registerNatives() {
        // nothing for now
    }
    
    //=========
    // Methods
    //=========
    
    public void update() {
        // check if there are any processes that are dead and should be cleaned up
        var it = activeProcesses.entrySet().iterator();
        while (it.hasNext()) {
            var processEntry = it.next();
            var process = processEntry.getValue();
            
            var state = process.state();
            if (state == EnvisionProcessState.DYING || state == EnvisionProcessState.DEAD) {
                onProcessEnded(process);
                it.remove();
            }
        }
    }
    
    /**
     * Launches the given process and registers it internally.
     * 
     * @param user
     * @param process
     */
    public synchronized void startProcess(UserProfile user, String launchCommand, IEnvisionProcess process) {
        if (user == null) return;
        if (process == null) return;
        
        // ensure that the user actually has permission to run the given process
        if (user.getPermissionLevel() < process.requiredPermissionLevel()) {
            throw new UserPermissionError("Permission Denied");
        }
        
        int pid = pidCounter.getAndIncrement();
        long startTime = System.currentTimeMillis();
        
        process.setProcessState(EnvisionProcessState.SPAWNING);
        activeProcesses.put(pid, process);
        
        //Thread processThread = Thread.startVirtualThread(() -> {
            process.setProcessState(EnvisionProcessState.RUNNING);
            process.onStart(user, launchCommand, pid, startTime);
            process.setProcessState(EnvisionProcessState.DYING);
        //});
        
        //return processThread;
    }
    
    public void killProcess(UserProfile user, IEnvisionProcess process) {
        killProcess(user, process.pid());
    }
    
    public void killProcess(UserProfile user, int pid) {
        if (user == null || pid < 0) return;
        
        IEnvisionProcess process = getProcessByPid(pid);
        if (process == null) return;
        
        byte uLevel = user.getPermissionLevel();
        byte pLevel = process.requiredPermissionLevel();
        
        // ensure that the user actually has permission to stop the given process
        //
        // if the user's permission level is lower than the process's permission level,
        // check to see if the given user is potentially an admin.
        if ((uLevel < pLevel) || (uLevel < 2)) {
            throw new UserPermissionError("Permission Denied");
        }
        
        process.kill();
    }
    
    public void onProcessEnded(IEnvisionProcess process) {
        if (process == null) return;
        
        process.setProcessState(EnvisionProcessState.DEAD);
    }
    
    public IEnvisionProcess getProcessByPid(int pid) {
        IEnvisionProcess p = activeProcesses.get(pid);
        
        return p;
    }
    
    //=========
    // Getters
    //=========
    
    public EList<IEnvisionProcess> getActiveProcesses() {
        return EList.unmodifiableList(activeProcesses.values());
    }
    
    /** Returns the current user. */
    public UserProfile getCurrentUser() { return profileRegistry.getCurrentUser(); }
    /** Returns the user profile registry. */
    public UserProfileRegistry getProfileRegistry() { return profileRegistry; }
    
    /** Returns the active user of the kernel instance. */
    public static UserProfile getActiveUser() { return getInstance().getCurrentUser(); }
    
}
