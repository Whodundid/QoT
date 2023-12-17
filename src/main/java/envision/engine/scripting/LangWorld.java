package envision.engine.scripting;

import envision.game.world.GameWorld;
import envision_lang.lang.classes.ClassInstance;
import envision_lang.lang.classes.EnvisionClass;
import envision_lang.lang.functions.IPrototypeHandler;

public class LangWorld extends EnvisionClass {

    public static final LangWorld WORLD_CLASS = new LangWorld();
    
    private static final IPrototypeHandler WORLD_PROTOTYPES = new IPrototypeHandler();
    
    static {
        
    }
    
    public LangWorld() {
        super("GameWorld");
    }
    
    public LangWorld(GameWorld world) {
        super("GameWorld");
    }
    
    @Override
    protected void defineScopeMembers(ClassInstance inst) {
        // define super object's members
        super.defineScopeMembers(inst);
        // define scope members
        WORLD_PROTOTYPES.defineOn(inst);
    }
    
}
