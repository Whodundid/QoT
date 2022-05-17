package game;

import eutil.random.RandomUtil;
import game.entities.Entity;
import game.entities.Goblin;
import game.entities.Thyrah;
import game.entities.TrollBoar;
import game.entities.Whodundid;

public enum EntityList {
	PLAYER(0),
	GOBLIN(1),
	WHODUNDID(2),
	TROLLBOAR(3),
	THYRAH(4),
	;
	
	public final int ID;
	
	private EntityList(int idIn) {
		ID = idIn;
	}
	
	public static EntityList randomType() {
		return values()[RandomUtil.getRoll(1, values().length - 1)];
	}
	
	public static Entity randomEntity() {
		return getEntity(values()[RandomUtil.getRoll(1, values().length - 1)]);
	}
	
	public static Entity getEntity(EntityList typeIn) {
		switch (typeIn) {
		//case PLAYER: return new Player(nameIn);
		case GOBLIN: return new Goblin();
		case WHODUNDID: return new Whodundid();
		case TROLLBOAR: return new TrollBoar();
		case THYRAH: return new Thyrah();
		default: return null;
		}
	}
	
	public static EntityList getType(int idIn) {
		if (idIn < 0) return null;
		int len = values().length;
		if (idIn >= len) return null;
		return values()[idIn];
	}
	
}
