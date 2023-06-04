package envision.engine.settings;

public class UserProfile {
	
	private final String profileName;
	private final boolean isDev;
	
	public UserProfile(String nameIn) { this(nameIn, false); }
	public UserProfile(String nameIn, boolean devIn) {
		profileName = nameIn;
		isDev = devIn;
	}
	
	@Override
	public String toString() {
		return profileName;
	}
	
	public String getName() { return profileName; }
	public boolean isDev() { return isDev; }
	
	public static boolean isUserDev(UserProfile profile) {
		return profile != null && profile.isDev;
	}
	
}
