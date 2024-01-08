package envision.engine.registry.types;

import envision.game.effects.sounds.Audio;
import eutil.datatypes.util.EList;

public class SoundRegistry {
    
    private final EList<Audio> sounds = EList.newList();
    private final EList<Audio> songs = EList.newList();
    
    public EList<Audio> getRegisteredSounds() { return sounds; }
    public EList<Audio> getRegisteredSongs() { return songs; }
    
}
