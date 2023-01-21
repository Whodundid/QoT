package miningGame;

import envisionEngine.Envision;
import envisionEngine.EnvisionGame;

public class MiningGame implements EnvisionGame {
	
	public MiningGame() {
		Envision.createGame(this, "Mining Game");
	}
	
}
