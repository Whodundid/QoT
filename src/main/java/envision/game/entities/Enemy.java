package envision.game.entities;

import eutil.misc.Direction;

@Deprecated(since="any time --> remove this in favor of having everything be component/json based")
public abstract class Enemy extends BasicRenderedEntity {

	protected long randShort = 400l;
	protected long randLong = 2500l;
	protected long waitDelay = 0l;
	protected long moveTime = 0l;
	protected long waitTime = 0l;
	protected long lastMove = 0l;
	protected Direction lastDir = Direction.N;
	
	protected long timeSinceMoved = 0l;
	
	public Enemy(String nameIn) {
		super(nameIn);
		
		canBeMoved = false;
		canMoveEntities = true;
        this.canRegenHealth = true;
        this.canRegenMana = false;
	}

}
