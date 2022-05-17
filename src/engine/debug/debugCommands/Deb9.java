package engine.debug.debugCommands;

import engine.terminal.window.ETerminal;
import game.EntityLevel;

@SuppressWarnings("unused")
public class Deb9 extends DebugCommand {

	@Override
	public void run(ETerminal termIn, Object... args) {
		EntityLevel level = new EntityLevel(500, 3);
		
		long total = 0;
		for (int i = 0; i < 10; i++) {
			long lvl = EntityLevel.getExpNeededForLevel(i);
			total += lvl;
			System.out.println(i + ": " + lvl + " -- total: " + total);
		}
		System.out.println();
		
		System.out.println(level.getXP() + " : " + EntityLevel.getTotalExpNeededForLevel(level.getLevel() + 1) + " : " + level.checkCanLevel());
	}

}