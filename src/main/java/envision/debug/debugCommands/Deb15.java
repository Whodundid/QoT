package envision.debug.debugCommands;

import java.io.File;

import com.fasterxml.jackson.databind.ObjectMapper;

import envision.engine.loader.dtos.WorldTileDTO;
import envision.engine.loader.dtos.WorldTileListDTO;
import envision.engine.registry.registries.WorldTileRegistry;
import envision.engine.terminal.window.ETerminalWindow;
import envision.game.world.worldTiles.WorldTile;
import eutil.datatypes.util.EList;
import qot.settings.QoTSettings;

@SuppressWarnings("unused")
public class Deb15 extends DebugCommand {
    
    @Override
    public void run(ETerminalWindow termIn, Object... args) throws Exception {
        EList<WorldTile> tiles = EList.newList();
        
        File tileListFile = new File(QoTSettings.getLocalGameDir(), "tileList.json");
        ObjectMapper mapper = new ObjectMapper();
        WorldTileListDTO tileListDTO = mapper.readValue(tileListFile, WorldTileListDTO.class);
        
        for (WorldTileDTO dto : tileListDTO.worldTileList()) {
            WorldTile t = WorldTile.fromDTO(dto);
            tiles.add(t);
        }
        
        WorldTileRegistry.registerAllTiles(tiles);
        
        for (WorldTile t : tiles) {
            WorldTile tileFromName = WorldTileRegistry.getTileByResourceName(t.tileName);
            Integer idFromName = WorldTileRegistry.getTileResourceID(t.tileName);
            Integer idFromTile = WorldTileRegistry.getTileResourceID(t);
            
            WorldTile tileFromID = null;
            if (idFromTile != null) {
                tileFromID = WorldTileRegistry.getTileFromID(idFromTile.intValue());
            }
            
            System.out.println(t + "= [" + tileFromName + ", " + idFromName + ", " + idFromTile + ", " + tileFromID + "]");
        }
//        System.out.println(WorldTileRegistry.getRegisteredTiles());
//        System.out.println();
    }
    
}
