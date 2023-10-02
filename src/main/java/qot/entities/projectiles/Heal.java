package qot.entities.projectiles;

import envision.game.GameObject;
import envision.game.entities.BasicRenderedEntity;

public class Heal extends BasicRenderedEntity {
	private boolean spawned = false;
	private long timeToLive = 3000;
	private long timeSpawned;
	
	public Heal() { this("Heal", null, 0, 0); }
	public Heal(GameObject spawningObject) { this("Heal", spawningObject, 0, 0); }
	
	public Heal(String nameIn, GameObject spawningObject, int x, int y) {
		super(nameIn);
	}

	@Override
	public int getInternalSaveID() {
		// TODO Auto-generated method stub
		return 0;
	}

}
