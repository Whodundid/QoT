package game.entities;

import envision.game.entity.Entity;
import eutil.random.RandomUtil;
import game.doodads.BirchTree;
import game.doodads.PineTree;
import game.entities.enemies.Goblin;
import game.entities.enemies.Thyrah;
import game.entities.enemies.TrollBoar;
import game.entities.enemies.Whodundid;
import game.entities.house.Barrel;
import game.entities.house.Chair;
import game.entities.house.Crate;
import game.entities.house.Stool;
import game.entities.neutral.WhodundidsBrother;
import game.entities.player.QoT_Player;

public enum EntityList {
	PLAYER(0),
	GOBLIN(1),
	WHODUNDID(2),
	TROLLBOAR(3),
	THYRAH(4),
	PINE_TREE(5),
	WHODUNDIDS_BROTHER(6),
	
	BARREL(7),
	CHAIR(8),
	CRATE(9),
	STOOL(10),
	
	BIRCH(11),
	BUSH0(12),
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
	
	public static Entity getEntity(int idIn) { return getEntity(getType(idIn)); }
	public static Entity getEntity(EntityList typeIn) {
		switch (typeIn) {
		case PLAYER: return new QoT_Player();
		case GOBLIN: return new Goblin();
		case WHODUNDID: return new Whodundid();
		case TROLLBOAR: return new TrollBoar();
		case THYRAH: return new Thyrah();
		case PINE_TREE: return new PineTree();
		case WHODUNDIDS_BROTHER: return new WhodundidsBrother();
		
		case BARREL: return new Barrel();
		case CHAIR: return new Chair();
		case CRATE: return new Crate();
		case STOOL: return new Stool();
		
		case BIRCH: return new BirchTree();
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
