package envisionEngine.gameEngine.gameObjects.entity;

import envisionEngine.gameEngine.gameObjects.items.Item;
import envisionEngine.gameEngine.world.gameWorld.GameWorld;
import eutil.datatypes.EArrayList;
import eutil.misc.Rotation;
import qot.entities.EntityList;

public class EntitySpawn {
	
	//--------
	// Fields
	//--------
	
	private int x, y;
	private int type;
	private EArrayList<Item> spawnItems = new EArrayList();
	private int initHealth = -1;
	private int initMana = -1;
	private Rotation initFacing = Rotation.LEFT;
	
	//--------------
	// Constructors
	//--------------
	
	private EntitySpawn() {}
	public EntitySpawn(int xIn, int yIn, Entity entIn) { this(xIn, yIn, entIn.getInternalSaveID()); }
	public EntitySpawn(int xIn, int yIn, int typeIn) {
		x = xIn;
		y = yIn;
		type = typeIn;
	}
	
	public EntitySpawn(EntitySpawn spawnIn) {
		x = spawnIn.x;
		y = spawnIn.y;
		type = spawnIn.type;
		//others not really supported (or used) yet
	}
	
	public static EntitySpawn parse(String in) {
		EntitySpawn spawn = new EntitySpawn();
		in = in.substring(4);
		
		String[] tokens = in.split(" ");
		spawn.x = Integer.parseInt(tokens[0]);
		spawn.y = Integer.parseInt(tokens[1]);
		spawn.type = Integer.parseInt(tokens[2]);
		
		if (tokens.length > 3) {
			for (int i = 3; i < tokens.length; i++) {
				System.out.print(tokens[i] + " ");
			}
			System.out.println();
		}
		
		return spawn;
	}
	
	public void spawnEntity(GameWorld world) {
		Entity ent = getEntity(world);
		
		if (initHealth != -1) ent.setHealth(initHealth);
		if (initMana != -1) ent.setMana(initMana);
		
		world.addEntity(ent);
	}
	
	public Entity getEntity(GameWorld world) {
		var ent = EntityList.getEntity(type);
		ent.world = world;
		ent.setWorldPos(x, y);
		return ent;
	}
	
	@Override
	public String toString() {
		return toSaveString();
	}
	
	public int getX() { return x; }
	public int getY() { return y; }
	public int getType() { return type; }
	public EArrayList<Item> getItems() { return spawnItems; }
	public int getHealth() { return initHealth; }
	public int getMana() { return initMana; }
	public Rotation getFacing() { return initFacing; }
	
	public void setInitHealth(int val) { initHealth = val; }
	public void setInitMana(int val) { initMana = val; }
	public void setInitFacing(Rotation dir) { initFacing = dir; }
	public void setItems(EArrayList<Item> itemsIn) { spawnItems.addAll(itemsIn); spawnItems.purgeNulls(); }
	public void setPosition(int x, int y) { this.x = x; this.y = y; }
	
	public String toSaveString() {
		String out = "ent ";
		out += x + " " + y;
		out += " " + type;
		out += (initHealth > 0) ? " h " + initHealth : "";
		out += (initMana > 0) ? " m " + initMana : "";
		out += (initFacing != Rotation.LEFT) ? " d " + initFacing : "";
		
		if (spawnItems.isNotEmpty()) {
			out += " i[";
			for (int i = 0; i < spawnItems.size(); i++) {
				out += spawnItems.get(i).getID();
				out += (i < spawnItems.size() - 1) ? "," : "";
			}
			out += "]";
		}
		
		return out;
	}
	
}
