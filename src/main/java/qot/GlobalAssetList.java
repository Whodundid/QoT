package qot;

import envision.game.GameObject;
import envision.game.world.worldEditor.editorUtil.PlayerSpawnPoint;
import eutil.datatypes.EArrayList;
import eutil.datatypes.util.EList;
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

public class GlobalAssetList {

	private static final EList<GameObject> assets = new EArrayList<>();
	
	static {
		assets.add(new Goblin());
		assets.add(new QoT_Player());
		assets.add(new Thyrah());
		assets.add(new TrollBoar());
		assets.add(new Whodundid());
		assets.add(new PineTree());
		assets.add(new PlayerSpawnPoint());
		assets.add(new WhodundidsBrother());
		
		assets.add(new Barrel());
		assets.add(new Chair());
		assets.add(new Crate());
		assets.add(new Stool());
		
		assets.add(new BirchTree());
		assets.add(new Bush0());
		assets.add(new StoneGroundClutter());
		assets.add(new PineTree2());
		
		assets.add(new BushyTree0());
		assets.add(new BushyTree1());
		assets.add(new BushyTree2());
		
		assets.add(new WeedsGroundClutter());
		assets.add(new LeavesGroundClutter());
		assets.add(new SticksGroundClutter());
		
		assets.add(new Archer());
		assets.add(new Spawner());
	}
	
	public static EList<GameObject> getAssets() {
		return new EArrayList<>(assets);
	}
	
}
