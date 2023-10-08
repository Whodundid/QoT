package qot.entities;

import envision.game.entities.Entity;
import eutil.datatypes.util.EList;
import eutil.random.ERandomUtil;
import qot.doodads.BirchTree;
import qot.doodads.Bush0;
import qot.doodads.BushyTree0;
import qot.doodads.BushyTree1;
import qot.doodads.BushyTree2;
import qot.doodads.LeavesGroundClutter;
import qot.doodads.PineTree;
import qot.doodads.PineTree2;
import qot.doodads.SticksGroundClutter;
import qot.doodads.StoneGroundClutter;
import qot.doodads.WeedsGroundClutter;
import qot.entities.buildings.Spawner;
import qot.entities.enemies.Goblin;
import qot.entities.enemies.PathfindingTestEntity;
import qot.entities.enemies.TrollBoar;
import qot.entities.enemies.Whodundid;
import qot.entities.enemies.WhodundidsBrother;
import qot.entities.enemies.archer.Archer;
import qot.entities.enemies.dragon.Thyrah;
import qot.entities.house.Barrel;
import qot.entities.house.Chair;
import qot.entities.house.Crate;
import qot.entities.house.Stool;
import qot.entities.player.QoT_Player;
import qot.entities.shopkeepers.ShopGuy;

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
	STONE_GROUND_CLUTTER(13),
	PINE_TREE_2(14),
	
	BUSHY_TREE_0(15),
	BUSHY_TREE_1(16),
	BUSHY_TREE_2(17),
	
	WEEDS(18),
	LEAVES(19),
	STICKS(20),
	
	ARCHER(21),
	
	SPAWNER(22),
	SHOPKEEPER(23), // please let this be the last damn one of these..
	PATHFINDER_TEST(24),
	;
	
	public final int ID;
	
	private EntityList(int idIn) {
		ID = idIn;
	}
	
	public static EntityList randomType() {
		return values()[ERandomUtil.getRoll(1, values().length - 1)];
	}
	
	public static Entity randomEntity() {
		return getEntity(values()[ERandomUtil.getRoll(1, values().length - 1)]);
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
		case BUSH0: return new Bush0();
		case STONE_GROUND_CLUTTER: return new StoneGroundClutter();
		case PINE_TREE_2: return new PineTree2();
		
		case BUSHY_TREE_0: return new BushyTree0();
		case BUSHY_TREE_1: return new BushyTree1();
		case BUSHY_TREE_2: return new BushyTree2();
		
		case WEEDS: return new WeedsGroundClutter();
		case LEAVES: return new LeavesGroundClutter();
		case STICKS: return new SticksGroundClutter();
		
		case ARCHER: return new Archer();
		case SPAWNER: return new Spawner();
		case SHOPKEEPER: return new ShopGuy();
		case PATHFINDER_TEST: return new PathfindingTestEntity();
		default: return null;
		}
	}
	
	public static EntityList getType(int idIn) {
		if (idIn < 0) return null;
		int len = values().length;
		if (idIn >= len) return null;
		return values()[idIn];
	}
	
	public static EList<EntityList> spawnable() {
	    EList<EntityList> list = EList.newList();
	    list.add(ARCHER);
	    list.add(SPAWNER);
	    list.add(GOBLIN);
	    list.add(WHODUNDID);
	    list.add(TROLLBOAR);
	    list.add(THYRAH);
	    list.add(WHODUNDIDS_BROTHER);
	    return list;
	}
	
}
