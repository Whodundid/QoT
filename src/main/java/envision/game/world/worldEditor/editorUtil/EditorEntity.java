package envision.game.world.worldEditor.editorUtil;

import envision.engine.internal.windows.windowTypes.WindowObject;
import envision.engine.loader.built.game.Entity;

public class EditorEntity extends WindowObject {
    
    private Entity entity;
    
    public EditorEntity(Entity entIn) {
        entity = entIn;
    }
    
}
