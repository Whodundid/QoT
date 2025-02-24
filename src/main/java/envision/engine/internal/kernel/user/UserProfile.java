package envision.engine.internal.kernel.user;

import eutil.math.ENumUtil;

public class UserProfile {
    
    //========
    // Fields
    //========
    
    /**
     * The user's profile name.
     */
    private final String profileName;
    
    /** Valid permission range: [0, 2].
     * 
     *  0 = no permissions : User is literally not allowed to use the
     *                       terminal or access the kernel.
     *                       
     *  1 = regular permissions : User is allowed to view/run regular
     *                            commands and start/stop their own
     *                            processes on the kernel.
     *                            
     *  2 = full permissions : No restrictions on what this user is
     *                         allowed to view/access/run/modify whether
     *                         it be something they created or not.
     */
    private byte permissionLevel = 0;    
    //==============
    // Constructors
    //==============
    
    public UserProfile(String nameIn) { this(nameIn, (byte) 1); }
    public UserProfile(String nameIn, int permissionLevelIn) { this(nameIn, (byte) permissionLevelIn); }
    public UserProfile(String nameIn, byte permissionLevelIn) {
        profileName = nameIn;
        permissionLevel = ENumUtil.clamp(permissionLevelIn, (byte) 0, (byte) 2);
    }    
    //===========
    // Overrides
    //===========
    
    @Override
    public String toString() {
        return profileName;
    }    
    //=========
    // Getters
    //=========
    
    public String getName() { return profileName; }
    public byte getPermissionLevel() { return permissionLevel; }    
    //=========
    // Setters
    //=========
    
    public void setPermissionLevel(byte level) {
        permissionLevel = level;
    }    
    //================
    // Static Methods
    //================
    
    public static boolean isUserDev(UserProfile profile) {
        return profile != null && profile.permissionLevel == 2;
    }
    
}
