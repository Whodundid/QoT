package game;

import eutil.datatypes.EArrayList;
import game.doodads.PineTree;
import game.doodads.PlayerSpawnPosition;
import game.entities.Goblin;
import game.entities.Player;
import game.entities.Thyrah;
import game.entities.TrollBoar;
import game.entities.Whodundid;

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
	}
	
	public static EArrayList<GameObject> getAssets() {
		return new EArrayList(assets);
	}
	
}
