package game;

import eutil.datatypes.EArrayList;
import eutil.datatypes.EList;
import game.doodads.nature.BirchTree;
import game.doodads.nature.PineTree;
import game.entities.enemies.Goblin;
import game.entities.enemies.Thyrah;
import game.entities.enemies.TrollBoar;
import game.entities.enemies.Whodundid;
import game.entities.house.Barrel;
import game.entities.house.Chair;
import game.entities.house.Crate;
import game.entities.house.Stool;
import game.entities.neutral.WhodundidsBrother;
import game.entities.player.Player;
import world.mapEditor.editorUtil.PlayerSpawnPosition;

public class GlobalAssetList {

	private static final EList<GameObject> assets = new EArrayList<>();
	
	static {
		assets.add(new Goblin());
		assets.add(new Player());
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
	}
	
	public static EList<GameObject> getAssets() {
		return new EArrayList<>(assets);
	}
	
}
