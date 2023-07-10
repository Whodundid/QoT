package envision.game.component.types;

import envision.game.component.ComponentBasedObject;
import envision.game.component.ComponentType;
import envision.game.component.EntityComponent;

public class OnClickComponent extends EntityComponent {

    protected OnClickComponent(ComponentBasedObject theEntityIn) {
        super(theEntityIn, ComponentType.ON_CLICK);
    }
    
    public void onClick(int mX, int mY, int button) {
        this.onEvent(mX, mY, button);
    }
    
}
