package envision.game.scripts.scriptBuilder.actions.entity;

import envision.game.entity.Entity;
import envision.game.scripts.scriptBuilder.ScriptAction;
import eutil.misc.Direction;

public class Action_Entity_Move extends ScriptAction {
	
	Entity ent;
	Direction dir;

	public Action_Entity_Move(Entity entIn, Direction dirIn) {
		ent = entIn;
		dir = dirIn;
	}

	@Override
	public void runAction(Object... args) throws Exception {
		if (ent != null && ent.exists()) {
			ent.move(dir);
		}
	}
	
}
