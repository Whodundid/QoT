package envision.engine.terminal.commands.categories.game;

import java.io.File;
import java.io.PrintWriter;

import org.apache.commons.io.FileUtils;

import envision.engine.terminal.commands.TerminalCommand;
import envision.engine.terminal.window.ETerminalWindow;
import eutil.colors.EColors;
import eutil.datatypes.util.EList;
import eutil.file.LineReader;
import eutil.math.ENumUtil;
import qot.settings.QoTSettings;

public class CMD_TransposeOld extends TerminalCommand {
	
	public CMD_TransposeOld() {
		setCategory("Game");
		expectedArgLength = 1;
	}

	@Override public String getName() { return "fixold"; }
	@Override public EList<String> getAliases() { return EList.of("fold"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Fixes an old world by transposing its tiles"; }
	@Override public String getUsage() { return "ex: fold 'name'"; }
	@Override public void handleTabComplete(ETerminalWindow conIn, EList<String> args) {
		this.basicTabComplete(conIn, args, QoTSettings.getEditorWorldsDir().list());
	}
	
	@Override
	public Object runCommand() throws Exception {
		expectExactly(1);
		
		var path = arg(0);
		
		File worldDir = new File(QoTSettings.getEditorWorldsDir(), path);
		File worldFile = new File(worldDir, path + ".twld");
		
		if (!worldFile.exists()) {
			error("Failed to find '" + worldFile + "' !");
			return null;
		}
		
		try (var reader = new LineReader(worldFile)) {
			String name = reader.nextLine();
			String[] dims = reader.nextLine().split(" ");
			
			int w = ENumUtil.parseInt(dims, 0, -1);
			int h = ENumUtil.parseInt(dims, 1, -1);
			int tw = ENumUtil.parseInt(dims, 2, -1);
			int th = ENumUtil.parseInt(dims, 3, -1);
			
			String[] spawn = reader.nextLine().split(" ");
			
			int spawnX = ENumUtil.parseInt(spawn, 0, -1);
			int spawnY = ENumUtil.parseInt(spawn, 1, -1);
			
			String underground = "0";
			String undergroundLine = reader.nextLine();
			boolean hasUnderground = undergroundLine.length() == 1;
			if (hasUnderground) underground = undergroundLine;
			
			String layers = "1";
			String layerLine = reader.nextLine();
			boolean hasLayers = layerLine.length() < 3 && !layerLine.contains(" ");
			if (hasLayers) layers = layerLine;
			
			String[][] tiles = new String[h][w];

			int readHeight = h;
			
			if (!hasUnderground) {
				readHeight--;
				String[] lineTiles = undergroundLine.split(" ");
				for (int i = 0; i < lineTiles.length; i++) {
					tiles[0][i] = lineTiles[i];
				}
			}
			
			if (!hasLayers) {
				readHeight--;
				String[] lineTiles = layerLine.split(" ");
				for (int i = 0; i < lineTiles.length; i++) {
					tiles[h - readHeight][i] = lineTiles[i];
				}
			}
			
			for (int i = (h - readHeight); i < readHeight; i++) {
				if (!reader.hasNextLine()) break;
				String[] lineTiles = reader.nextLine().split(" ");
				for (int j = 0; j < lineTiles.length; j++) {
					tiles[i][j] = lineTiles[j];
				}
			}
			
			EList<String> rest = EList.newList();
			while (reader.hasNextLine()) {
				rest.add(reader.nextLine());
			}
			
			// transpose world
			String[][] transposed = new String[w][h];
			
			for (int i = 0; i < tiles.length; i++) {
				for (int j = 0; j < tiles[0].length; j++) {
					var s = tiles[i][j];
					transposed[j][i] = s;
				}
			}
			
//			for (int i = 0; i < transposed.length; i++) {
//				String s = "";
//				for (int j = 0; j < transposed[0].length; j++) {
//					s += transposed[i][j] + " ";
//				}
//				System.out.println(s);
//			}
			
			FileUtils.copyFile(worldFile, new File(worldDir, worldFile.getName() + ".backup"));
			
			var writer = new PrintWriter(worldFile, "UTF-8");
			writer.println(name);
			writer.println(h + " " + w + " " + tw + " " + th);
			writer.println(spawnX + " " + spawnY);
			writer.println(underground);
			writer.println(layers);
			
			for (int i = 0; i < transposed.length; i++) {
				String line = "";
				for (int j = 0; j < transposed[0].length; j++) {
					line += transposed[i][j] + " ";
				}
				line = line.trim();
				writer.println(line);
			}
			
			for (String l : rest) {
				writer.println(l);
			}
			
			writer.close();
			writeln("Success!", EColors.green);
		}
		catch (Exception e) {
			throw e;
		}
		
		return null;
	}
	
}
