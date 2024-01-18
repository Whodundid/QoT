package envision.debug.debugCommands;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

import envision.engine.loader.dtos.WorldTileListDTO;
import envision.engine.terminal.window.ETerminalWindow;
import envision.game.world.worldTiles.WorldTile;
import qot.settings.QoTSettings;
import qot.world_tiles.GlobalTileList;

@SuppressWarnings("unused")
public class Deb16 extends DebugCommand {

	@Override
	public void run(ETerminalWindow termIn, Object... args) {
		
	    File tileListFile = new File(QoTSettings.getLocalGameDir(), "tileList.json");
	    
	    var tiles = GlobalTileList.getTiles().map(WorldTile::toDTO);
	    WorldTileListDTO tilesDTO = new WorldTileListDTO(tiles);
	    
	    ObjectMapper mapper = new ObjectMapper();
	    
	    try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(tileListFile, tilesDTO);
            termIn.writeln("Tiles written to: " + tileListFile);
        }
        catch (StreamWriteException e) {
            e.printStackTrace();
        }
        catch (DatabindException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
	    
	}

}