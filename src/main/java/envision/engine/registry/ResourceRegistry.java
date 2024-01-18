package envision.engine.registry;

import envision.engine.registry.registries.AbilityRegistry;
import envision.engine.registry.registries.EntityTypeRegistry;
import envision.engine.registry.registries.ItemRegistry;
import envision.engine.registry.registries.QuestRegistry;
import envision.engine.registry.registries.SoundRegistry;
import envision.engine.registry.registries.TextureRegistry;
import envision.engine.registry.registries.WorldTileRegistry;

public class ResourceRegistry {
    
    private ResourceRegistry() {}
    
    private static final AbilityRegistry abilityRegistry = new AbilityRegistry();
    private static final EntityTypeRegistry entityRegistry = new EntityTypeRegistry();
    private static final ItemRegistry itemRegistry = new ItemRegistry();
    private static final QuestRegistry questRegistry = new QuestRegistry();
    private static final SoundRegistry soundRegistry = new SoundRegistry();
    private static final TextureRegistry textureRegistry = new TextureRegistry();
    private static final WorldTileRegistry worldTileRegistry = new WorldTileRegistry();
    
    public static AbilityRegistry getAbilityRegistry() { return abilityRegistry; }
    public static EntityTypeRegistry getEntityRegistry() { return entityRegistry; }
    public static ItemRegistry getItemRegistry() { return itemRegistry; }
    public static QuestRegistry getQuestRegistry() { return questRegistry; }
    public static SoundRegistry getSoundRegistry() { return soundRegistry; }
    public static TextureRegistry getTextureRegistry() { return textureRegistry; }
    public static WorldTileRegistry getWorldTileRegistry() { return worldTileRegistry; }
    
}
