package envision.engine.scripting.scriptBuilder.actions.entity;

import envision.engine.scripting.scriptBuilder.ScriptAction;
import envision.game.objects.entities.Entity;
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
