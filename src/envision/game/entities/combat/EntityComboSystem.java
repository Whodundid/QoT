package envision.game.entities.combat;

import envision.Envision;

public class EntityComboSystem {
	
	private boolean inCombo;
	private int comboPhase = 0;
	private long tickOfLastInput;
	private long comboStartTick;
	
	public void onGameTick(float dt) {
		if (!inCombo) return;
		
		long cur = Envision.getRunningTicks();
		
		if (cur - tickOfLastInput > 1) {
			
		}
	}
	
	public void onCombotAttack() {
		if (!inCombo) {
			inCombo = true;
			comboStartTick = Envision.getRunningTicks();
		}
	}
	
}
