package game;

import eutil.datatypes.EArrayList;
import game.doodads.nature.PineTree;
import game.entities.enemies.Goblin;
import game.entities.enemies.Thyrah;
import game.entities.enemies.TrollBoar;
import game.entities.enemies.Whodundid;
import game.entities.neutral.WhodundidsBrother;
import game.entities.player.Player;
import world.mapEditor.editorUtil.PlayerSpawnPosition;

public class GlobalAssetList {

	private static final EArrayList<GameObject> assets = new EArrayList();
	
	static {
		assets.add(new Goblin());
		assets.add(new Player());
		assets.add(new Thyrah());
		assets.add(new TrollBoar());
		assets.add(new Whodundid());
		assets.add(new PineTree());
		assets.add(new PlayerSpawnPosition());
		assets.add(new WhodundidsBrother());
	}
	
	public static EArrayList<GameObject> getAssets() {
		return new EArrayList(assets);
	}
	
}
