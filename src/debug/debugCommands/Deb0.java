package debug.debugCommands;

import assets.worldTiles.WorldTile;
import gameSystems.mapSystem.GameWorld;
import java.awt.image.BufferedImage;
import java.io.File;
import main.Game;
import tempUtil.FloodFill;
import terminal.window.ETerminal;

@SuppressWarnings("unused")
public class Deb0 extends DebugCommand {
	
	@Override
	public void run(ETerminal termIn, Object... args) {
		
		File f = new File(Game.settings.getEditorWorldsDir(), "fillTest.twld");
		GameWorld world = new GameWorld(f);
		
		int[][] intWorld = new int[world.getWidth()][world.getHeight()];
		
		for (int i = 0; i < world.getWidth(); i++) {
			for (int j = 0; j < world.getHeight(); j++) {
				WorldTile t = world.getTileAt(i, j);
				if (t != null) {
					intWorld[i][j] = t.getID();
				}
			}
		}
		
		for (int i = 0; i < world.getWidth(); i++) {
			for (int j = 0; j < world.getHeight(); j++) {
				System.out.print(intWorld[i][j] + " ");
			}
			System.out.println();
		}
		
        // Function Call
        FloodFill.replace(intWorld, 0, 0, 3);
        
        System.out.println();
        
        for (int i = 0; i < world.getWidth(); i++) {
			for (int j = 0; j < world.getHeight(); j++) {
				System.out.print(intWorld[i][j] + " ");
			}
			System.out.println();
		}
		
	}

}
