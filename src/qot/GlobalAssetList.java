package qot;

import envision.game.objects.GameObject;
import envision.game.world.worldEditor.editorUtil.PlayerSpawnPoint;
import eutil.datatypes.EArrayList;
import qot.doodads.BirchTree;
import qot.doodads.Bush0;
import qot.doodads.PineTree;
import qot.doodads.PineTree2;
import qot.doodads.StoneGroundClutter;
import qot.entities.enemies.Goblin;
import qot.entities.enemies.Thyrah;
import qot.entities.enemies.TrollBoar;
import qot.entities.enemies.Whodundid;
import qot.entities.house.Barrel;
import qot.entities.house.Chair;
import qot.entities.house.Crate;
import qot.entities.house.Stool;
import qot.entities.neutral.WhodundidsBrother;
import qot.entities.player.QoT_Player;

public class GlobalAssetList {

	private static final EArrayList<GameObject> assets = new EArrayList();
	
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
	}
	
	public static EArrayList<GameObject> getAssets() {
		return new EArrayList(assets);
	}
	
}
