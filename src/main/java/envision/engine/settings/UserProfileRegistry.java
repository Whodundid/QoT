package envision.engine.settings;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import eutil.datatypes.boxes.Box2;

/**
 * Keeps track of active user profiles.
 * 
 * @author Hunter Bragg
 */
public class UserProfileRegistry {
	
	//========
	// Fields
	//========
	
	/** The internal user profile repository map. */
	private final ConcurrentMap<String, UserProfile> profiles = new ConcurrentHashMap<>();
	
	/** The current user profile. */
	private UserProfile currentUser = null;
	
	//=========
	// Methods
	//=========
	
	/** Attempts to register a new user profile. */
	public boolean registerProfile(UserProfile profile) {
		if (profile == null) return false;
		
		final String name = profile.getName();
		if (name == null || name.isBlank() || name.isEmpty()) return false;
		if (profiles.get(name) != null) return false;
		
		profiles.put(name, profile);
		return true;
	}
	
	/** Attempts to unregister the given, already registered, profile. */
	public boolean unregisterProfile(UserProfile profile) {
		if (profile == null) return false;
		
		final String name = profile.getName();
		if (name == null || name.isBlank() || name.isEmpty()) return false;
		if (!profiles.containsKey(name)) return false;
		
		profiles.remove(name);
		return true;
	}
	
	/** Returns a registered profile under the given username. */
	public UserProfile getProfile(String name) {
		if (name == null) return null;
		
		return profiles.get(name);
	}
	
	/** Returns true if the given username is already taken by a registered profile. */
	public boolean isRegistered(String name) {
		return (name != null) ? profiles.containsKey(name) : false;
	}
	
	/** Returns true if the given profile is already registered. */
	public boolean isRegistered(UserProfile profile) {
		if (profile == null) return false;
		
		final String name = profile.getName();
		if (name == null || name.isBlank() || name.isEmpty()) return false;
		
		return profiles.containsKey(name);
	}
	
	//=========
	// Getters
	//=========
	
	public UserProfile getCurrentUser() { return currentUser; }
	
	public Box2<Boolean, String> setCurrentUser(UserProfile user) {
		if (user == null) {
			return new Box2<>(false, "Cannot set a NULL user!");
		}
		
		if (currentUser == user) {
			return new Box2<>(false, "Already " + user + "!");
		}
		
		if (!isRegistered(user)) {
			return new Box2<>(false, user + ": is not registered! Cannot switch to!");
		}
		
		currentUser = user;
		return new Box2<>(true, "Successfully switched to: " + user);
	}
	
	public List<String> getRegisteredProfileNames() { return profiles.values().stream().map(p -> p.getName()).toList(); }
	public List<UserProfile> getRegisteredProfiles() { return profiles.values().stream().toList(); }
	
}
