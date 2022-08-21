package game;

import envision.game.GameObject;
import envision.game.world.mapEditor.editorUtil.PlayerSpawnPosition;
import eutil.datatypes.EArrayList;
import game.doodads.BirchTree;
import game.doodads.Bush0;
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

public class GlobalAssetList {

	private static final EArrayList<GameObject> assets = new EArrayList();
	
	static {
		assets.add(new Goblin());
		assets.add(new QoT_Player());
		assets.add(new Thyrah());
		assets.add(new TrollBoar());
		assets.add(new Whodundid());
		assets.add(new PineTree());
		assets.add(new PlayerSpawnPosition());
		assets.add(new WhodundidsBrother());
		
		assets.add(new Barrel());
		assets.add(new Chair());
		assets.add(new Crate());
		assets.add(new Stool());
		
		assets.add(new BirchTree());
		assets.add(new Bush0());
	}
	
	public static EArrayList<GameObject> getAssets() {
		return new EArrayList(assets);
	}
	
}
