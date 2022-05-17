package engine.scripting.builder.actions.entity;

import engine.scripting.builder.ScriptAction;
import eutil.misc.Direction;
import game.entities.Entity;

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
