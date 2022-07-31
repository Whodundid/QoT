package world.dungeonBuilder;

import eutil.random.RandomUtil;
import world.GameWorld;
import world.worldTiles.WorldTile;

public class DungeonBuilder {
	
	public DungeonBuilderSettings settings;
	private DungeonRoomSquare startRoom;
	
	public DungeonBuilder() {}
	
	public GameWorld buildRandomDungeon() { return buildRandomDungeon(null); }
	public GameWorld buildRandomDungeon(DungeonBuilderSettings settingsIn) {
		settings = (settingsIn != null) ? settingsIn : new DungeonBuilderSettings();
		
		//determine map size from settings
		//int mapWidth, mapHeight;
		//mapWidth = settings.size.width;
		//mapHeight = settings.size.height;
		//System.out.println(mapWidth + " : " + mapHeight);
		
		//make a start room
		startRoom = DungeonRoomSquare.createRandom(true);
		int numRooms = settings.size.width * settings.size.height;
		numRooms /= 15;
		numRooms -= RandomUtil.getRoll(4, 12);
		DungeonMap theMap = new DungeonMap(settings.size, 15, numRooms);
		//System.out.println(theMap.w + " : " + theMap.h);
		//System.out.println(theMap.w / 2 + " : " + theMap.h / 2);
		
		int sX = theMap.w / 2;
		int sY = theMap.h / 2;
		theMap.forceSet(sX, sY, startRoom);
		
		BuilderBot a = new BuilderBot(0, theMap, sX + 1, sY); //W
		BuilderBot b = new BuilderBot(1, theMap, sX - 1, sY); //N
		BuilderBot c = new BuilderBot(2, theMap, sX, sY + 1); //S
		BuilderBot d = new BuilderBot(3, theMap, sX, sY - 1); //E
		
		a.start();
		b.start();
		c.start();
		d.start();
		
		try {
			a.join();
			b.join();
			c.join();
			d.join();
		}
		catch (Exception e) {
			
		}
		
		theMap.display();
		System.out.println();
		
		//int sX = RandomUtil.getRoll(0, (mapWidth - 1) - startRoom.w);
		//int sY = RandomUtil.getRoll(0, (mapHeight - 1) - startRoom.h);
		//addRoom(theWorld, startRoom, sX, sY);
		
		GameWorld theWorld = theMap.buildMap();
		theWorld.setPlayerSpawnPosition((sX * 15) + startRoom.w / 2, (sY * 15) + startRoom.h / 2);
		
		return theWorld;
	}
	
	private static void addRoom(GameWorld world, DungeonPiece room, int sX, int sY) {
		//System.out.println(sX + " : " + sY);
		WorldTile[][] startTiles = room.getTiles();
		for (int i = 0; i < room.getHeight(); i++) {
			for (int j = 0; j < room.getWidth(); j++) {
				world.setTileAt(sX + j, sY + i, startTiles[i][j]);
			}
		}
	}
	
}
