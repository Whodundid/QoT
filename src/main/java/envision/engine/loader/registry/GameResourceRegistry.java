package envision.engine.loader.registry;

import java.io.File;
import java.util.function.Consumer;

import envision.engine.loader.ResourceType;
import envision.engine.loader.built.game.Ability;
import envision.engine.loader.built.game.CustomObjectType;
import envision.engine.loader.built.game.Effect;
import envision.engine.loader.built.game.Entity;
import envision.engine.loader.built.game.EnvisionScript;
import envision.engine.loader.built.game.Faction;
import envision.engine.loader.built.game.GameLevel;
import envision.engine.loader.built.game.GameResource;
import envision.engine.loader.built.game.GameScreen;
import envision.engine.loader.built.game.GameTexture;
import envision.engine.loader.built.game.Item;
import envision.engine.loader.built.game.ParticleEffect;
import envision.engine.loader.built.game.Quest;
import envision.engine.loader.built.game.ShaderProgram;
import envision.engine.loader.built.game.Shop;
import envision.engine.loader.built.game.SongResource;
import envision.engine.loader.built.game.SoundEffectResource;
import envision.engine.loader.built.game.Sprite;
import envision.engine.loader.built.game.SpriteSheet;
import envision.engine.loader.built.game.TileSet;
import envision.engine.loader.registry.types.AbilityResourceManager;
import envision.engine.loader.registry.types.CustomObjectTypeResourceManager;
import envision.engine.loader.registry.types.EffectResourceManager;
import envision.engine.loader.registry.types.FactionResourceManager;
import envision.engine.loader.registry.types.GameLevelResourceManager;
import envision.engine.loader.registry.types.GameResourceResourceManager;
import envision.engine.loader.registry.types.GameWorldResourceManager;
import envision.engine.loader.registry.types.ItemResourceManager;
import envision.engine.loader.registry.types.ParticleEffectResourceManager;
import envision.engine.loader.registry.types.QuestResourceManager;
import envision.engine.loader.registry.types.ScreenResourceManager;
import envision.engine.loader.registry.types.ScriptResourceManager;
import envision.engine.loader.registry.types.ShaderResourceManager;
import envision.engine.loader.registry.types.ShopResourceManager;
import envision.engine.loader.registry.types.SongResourceManager;
import envision.engine.loader.registry.types.SoundEffectResourceManager;
import envision.engine.loader.registry.types.SpriteResourceManager;
import envision.engine.loader.registry.types.SpriteSheetResourceManager;
import envision.engine.loader.registry.types.TextureResourceManager;
import envision.engine.loader.registry.types.TileSetResourceManager;
import envision.engine.loader.registry.types.WorldTileResourceManager;
import envision.game.world.GameWorld;
import envision.game.world.worldTiles.WorldTile;
import eutil.datatypes.util.EList;

public class GameResourceRegistry {
    
    /*
     * manager load order:
     * 
     * 1. textures
     * 2. sounds/songs
     * 3. shaders
     * 4. scripts
     * 5. sprites
     * 6. sprite sheets
     * 7. tiles
     * 8. tile sets
     * 9. game resources
     * 10. effects
     * 11. particle effects
     * 12. items
     * 13. abilities
     * 14. factions
     * 15. shops
     * 16. quests
     * 17. entities
     * 18. maps
     * 19. levels
     * 20. screens
     * 21. custom types
     */
    
    //========
    // Fields
    //========
    
    private final EList<IResourceManager<GameTexture>> textureRegistry = EList.newList();
    private final EList<IResourceManager<SoundEffectResource>> soundEffectRegistry = EList.newList();
    private final EList<IResourceManager<SongResource>> songRegistry = EList.newList();
    private final EList<IResourceManager<ShaderProgram>> shaderRegistry = EList.newList();
    private final EList<IResourceManager<EnvisionScript>> scriptRegistry = EList.newList();
    private final EList<IResourceManager<Sprite>> spriteRegistry = EList.newList();
    private final EList<IResourceManager<SpriteSheet>> spriteSheetRegistry = EList.newList();
    private final EList<IResourceManager<WorldTile>> worldTileRegistry = EList.newList();
    private final EList<IResourceManager<TileSet>> tileSetRegistry = EList.newList();
    private final EList<IResourceManager<GameResource>> gameResourceRegistry = EList.newList();
    private final EList<IResourceManager<Effect>> effectRegistry = EList.newList();
    private final EList<IResourceManager<ParticleEffect>> particleEffectRegistry = EList.newList();
    private final EList<IResourceManager<Item>> itemRegistry = EList.newList();
    private final EList<IResourceManager<Ability>> abilityRegistry = EList.newList();
    private final EList<IResourceManager<Faction>> factionRegistry = EList.newList();
    private final EList<IResourceManager<Shop>> shopRegistry = EList.newList();
    private final EList<IResourceManager<Quest>> questRegistry = EList.newList();
    private final EList<IResourceManager<Entity>> entityRegistry = EList.newList();
    private final EList<IResourceManager<GameWorld>> gameWorldRegistry = EList.newList();
    private final EList<IResourceManager<GameLevel>> gameLevelRegistry = EList.newList();
    private final EList<IResourceManager<GameScreen>> screenRegistry = EList.newList();
    private final EList<IResourceManager<CustomObjectType>> customObjectTypeRegistry = EList.newList();
    
    /** Holds all other registry lists for more efficient code execution. */
    private final EList<EList<? extends IResourceManager<?>>> allManagers = EList.newList();
    
    //==============
    // Constructors
    //==============
    
    public GameResourceRegistry() {
        allManagers.add(textureRegistry);
        allManagers.add(soundEffectRegistry);
        allManagers.add(songRegistry);
        allManagers.add(shaderRegistry);
        allManagers.add(scriptRegistry);
        allManagers.add(spriteRegistry);
        allManagers.add(spriteSheetRegistry);
        allManagers.add(worldTileRegistry);
        allManagers.add(tileSetRegistry);
        allManagers.add(gameResourceRegistry);
        allManagers.add(effectRegistry);
        allManagers.add(particleEffectRegistry);
        allManagers.add(itemRegistry);
        allManagers.add(abilityRegistry);
        allManagers.add(factionRegistry);
        allManagers.add(shopRegistry);
        allManagers.add(questRegistry);
        allManagers.add(entityRegistry);
        allManagers.add(gameWorldRegistry);
        allManagers.add(gameLevelRegistry);
        allManagers.add(screenRegistry);
        allManagers.add(customObjectTypeRegistry);
    }
    
    //======
    // Load
    //======
    
    public void loadManagerDtos() {
        assertManagerState(ResourceManagerState.INITIALIZED);
        for (var managers : allManagers) {
            managers.forEach(LOAD_DTOS);
        }
    }
    
    public void buildManagerResources() {
        assertManagerState(ResourceManagerState.DTOS_LOADED);
        for (var managers : allManagers) {
            managers.forEach(BUILD_RESOURCES);
        }
    }
    
    public void reloadManagerResources() {
        assertManagerState(ResourceManagerState.RESOURCES_BUILT);
        for (var managers : allManagers) {
            managers.forEach(RELOAD_RESOURCES);
        }
    }
    
    public void unloadManagerResources(EList registry) {
        assertManagerState(ResourceManagerState.RESOURCES_BUILT);
        for (var managers : allManagers) {
            managers.forEach(UNLOAD_RESOURCES);
        }
    }
    
    public void saveManagerResourceDtos(EList registry) {
        assertManagerState(ResourceManagerState.RESOURCES_BUILT);
        for (var managers : allManagers) {
            managers.forEach(SAVE_RESOURCE_DTOS);
        }
    }
    
    //=========================
    // Internal Helper Methods
    //=========================
    
    private void assertManagerState(ResourceManagerState state) {
        for (var managers : allManagers) {
            assertManagerState(managers, state);
        }
    }
    
    private void assertManagerState(EList<? extends IResourceManager<?>> managers, ResourceManagerState state) {
        for (var manager : managers) {
            if (manager.getCurrentManagerState() != state) {
                throw new IllegalStateException("Resouce Manager: '" + manager + "' is not expected state: '" + state +
                                                "' but rather: '" + manager.getCurrentManagerState() + "'!");
            }
        }
    }
    
    //=========
    // Methods
    //=========
    
    public void addManager(IResourceManager manager, ResourceType type) {
        switch (type) {
        case TEXTURE:           textureRegistry.add(manager);           break;
        case SOUND_EFFECT:      soundEffectRegistry.add(manager);       break;
        case SONG:              songRegistry.add(manager);              break;
        case SHADER:            shaderRegistry.add(manager);            break;
        case SCRIPT:            scriptRegistry.add(manager);            break;
        case SPRITE:            spriteRegistry.add(manager);            break;
        case SPRITE_SHEET:      spriteSheetRegistry.add(manager);       break;
        case WORLD_TILE:        worldTileRegistry.add(manager);         break;
        case TILE_SET:          tileSetRegistry.add(manager);           break;
        case RESOURCE:          gameResourceRegistry.add(manager);      break;
        case EFFECT:            effectRegistry.add(manager);            break;
        case PARTICLE_EFFECT:   particleEffectRegistry.add(manager);    break;
        case ITEM:              itemRegistry.add(manager);              break;
        case ABILITY:           abilityRegistry.add(manager);           break;
        case FACTION:           factionRegistry.add(manager);           break;
        case SHOP:              shopRegistry.add(manager);              break;
        case QUEST:             questRegistry.add(manager);             break;
        case ENTITY:            entityRegistry.add(manager);            break;
        case MAP:               gameWorldRegistry.add(manager);         break;
        case LEVEL:             gameLevelRegistry.add(manager);         break;
        case SCREEN:            screenRegistry.add(manager);            break;
        case CUSTOM_TYPE:       customObjectTypeRegistry.add(manager);  break;
        
        default: throw new IllegalArgumentException("Expected a valid ResourceType!");
        }
    }
    
    //=======================
    // Static Helper Methods
    //=======================
    
    public static IResourceManager<?> createResourceManagerInstance(ResourceType type, File resourceFile) {
        return switch (type) {
        case TEXTURE            ->  new TextureResourceManager(resourceFile);
        case SOUND_EFFECT       ->  new SoundEffectResourceManager(resourceFile);
        case SONG               ->  new SongResourceManager(resourceFile);
        case SHADER             ->  new ShaderResourceManager(resourceFile);
        case SCRIPT             ->  new ScriptResourceManager(resourceFile);
        case SPRITE             ->  new SpriteResourceManager(resourceFile);
        case SPRITE_SHEET       ->  new SpriteSheetResourceManager(resourceFile);
        case WORLD_TILE         ->  new WorldTileResourceManager(resourceFile);
        case TILE_SET           ->  new TileSetResourceManager(resourceFile);
        case RESOURCE           ->  new GameResourceResourceManager(resourceFile);
        case EFFECT             ->  new EffectResourceManager(resourceFile);
        case PARTICLE_EFFECT    ->  new ParticleEffectResourceManager(resourceFile);
        case ITEM               ->  new ItemResourceManager(resourceFile);
        case ABILITY            ->  new AbilityResourceManager(resourceFile);
        case FACTION            ->  new FactionResourceManager(resourceFile);
        case SHOP               ->  new ShopResourceManager(resourceFile);
        case QUEST              ->  new QuestResourceManager(resourceFile);
        case MAP                ->  new GameWorldResourceManager(resourceFile);
        case LEVEL              ->  new GameLevelResourceManager(resourceFile);
        case SCREEN             ->  new ScreenResourceManager(resourceFile);
        case CUSTOM_TYPE        ->  new CustomObjectTypeResourceManager(resourceFile);
            
        default -> throw new IllegalArgumentException("Expected a valid ResourceType!");
        };
    }
    
    //==================
    // Internal Classes
    //==================
    
    private static final Consumer<? super IResourceManager<?>> LOAD_DTOS = m -> {
        try {
            m.loadDtos();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    };
    
    private static final Consumer<? super IResourceManager<?>> BUILD_RESOURCES = m -> {
        try {
            m.buildResources();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    };
    
    private static final Consumer<? super IResourceManager<?>> RELOAD_RESOURCES = m -> {
        try {
            m.reload();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    };
    
    private static final Consumer<? super IResourceManager<?>> UNLOAD_RESOURCES = m -> {
        try {
            m.unloadResources();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    };
    
    private static final Consumer<? super IResourceManager<?>> SAVE_RESOURCE_DTOS = m -> {
        try {
            m.saveDtos();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    };
    
}
