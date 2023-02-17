package envision.game.objects.entities;

import eutil.misc.Direction;

public abstract class Enemy extends Entity {

	protected long randShort = 400l;
	protected long randLong = 2500l;
	protected long waitDelay = 0l;
	protected long moveTime = 0l;
	protected long waitTime = 0l;
	protected long lastMove = 0l;
	protected Direction lastDir = Direction.N;
	
	public Enemy(String nameIn) {
		super(nameIn);
	}

}
