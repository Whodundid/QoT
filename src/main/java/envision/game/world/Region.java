package envision.game.world;

import java.io.File;
import java.util.Scanner;

import envision.Envision;
import envision.engine.events.eventTypes.entity.EntityEnteredRegionEvent;
import envision.engine.events.eventTypes.entity.EntityExitedRegionEvent;
import envision.game.GameObject;
import envision.game.entities.Entity;
import envision_lang._launch.EnvisionProgram;
import eutil.colors.EColors;
import eutil.datatypes.EArrayList;
import eutil.datatypes.util.EList;
import eutil.math.dimensions.Dimension_d;
import eutil.math.dimensions.Dimension_i;
import qot.settings.QoTSettings;

public class Region extends GameObject {
	
	protected IGameWorld world;
	protected String name;
	public int startX, startY, endX, endY;
	public int midX, midY;
	public int width, height;
	protected int regionColor;
	protected boolean onlyRenderInEditor = true;
	protected EList<Entity> entitiesInside = new EArrayList<>();
	protected EnvisionProgram regionScript;
	
	public Region(IGameWorld worldIn, String nameIn) { this(worldIn, nameIn, 0, 0, 0, 0, 0xff55ff55); }
	public Region(IGameWorld worldIn, String nameIn, EColors colorIn) { this(worldIn, nameIn, 0, 0, 0, 0, colorIn.intVal); }
	public Region(IGameWorld worldIn, String nameIn, int colorIn) { this(worldIn, nameIn, 0, 0, 0, 0, colorIn); }
	public Region(IGameWorld worldIn, String nameIn, int sX, int sY, int eX, int eY) { this(worldIn, nameIn, sX, sY, eX, eY, 0xff55ff55); }
	public Region(IGameWorld worldIn, String nameIn, int sX, int sY, int eX, int eY, EColors colorIn) { this(worldIn, nameIn, sX, sY, eX, eY, colorIn.intVal); }
	public Region(IGameWorld worldIn, String nameIn, int sX, int sY, int eX, int eY, int colorIn) {
		world = worldIn;
		name = nameIn;
		setDimensions(sX, sY, eX, eY);
		regionColor = colorIn;
	}
	
	@Override public String toString() { return toSaveString(); }
	
	public void updateRegion(float dt) {
		//check to see if the entities that are said to be inside are still actually inside
		var it = entitiesInside.iterator();
		while (it.hasNext()) {
			var ent = it.next();
			if (ent == null) it.remove();
			
			Dimension_d entDims = ent.getCollisionDims();
			if (!getRegionDimensions().partiallyContains(entDims)) {
				onEntityExited(ent, ent.startX, ent.startY);
				it.remove();
			}
		}
	}
	
	public void setDimensions(int widthIn, int heightIn) { setDimensions(startX, startY, startX + widthIn, startY + heightIn); }
	public void setDimensions(int sX, int sY, int eX, int eY) {
		startX = sX;
		startY = sY;
		endX = eX;
		endY = eY;
		width = endX - startX;
		height = endY - startY;
		midX = startX + width / 2;
		midY = startY + height / 2;
	}
	
	public Dimension_i getRegionDimensions() {
		return new Dimension_i(startX, startY, endX, endY);
	}
	
	public void moveRegion(int x, int y) {
		startX += x;
		startY += y;
		endX -= x;
		endY -= y;
		setDimensions(startX, startY, endX, endY);
	}
	
	public synchronized EList<Entity> getEntitiesInside() {
		return EList.of(entitiesInside);
	}
	
	protected synchronized void onEntityEntered(Entity in, double xPos, double yPos) {
		Envision.getEventHandler().postEvent(new EntityEnteredRegionEvent(world, in, this, xPos, yPos));
		
		//TODO: Remove this
		if (in == Envision.thePlayer) {
			//if (world.getWorldName().equals("new")) {
				if (name.equals("Cave")) {
					Envision.loadWorld(new GameWorld(new File(QoTSettings.getEditorWorldsDir(), "cave/cave.twld")));
					Envision.theWorld.setUnderground(true);
				}
				else if (name.equals("MountainPass")) {
					Envision.loadWorld(new GameWorld(new File(QoTSettings.getEditorWorldsDir(), "mountains/mountains.twld")));
				}
				else if (name.equals("Mine")) {
					Envision.loadWorld(new GameWorld(new File(QoTSettings.getEditorWorldsDir(), "mountain_dung/mountain_dung.twld")));
				}
			//}
			else if (name.equals("exitCave")) {
				Envision.loadWorld(new GameWorld(new File(QoTSettings.getEditorWorldsDir(), "new/new.twld")));
			}
			else if (name.equals("Valley")) {
				Envision.loadWorld(new GameWorld(new File(QoTSettings.getEditorWorldsDir(), "new/new.twld")));
			}
			else if (name.equals("cave_entrance")) {
				Envision.loadWorld(new GameWorld(new File(QoTSettings.getEditorWorldsDir(), "mountains/mountains.twld")));
			}
		}
	}
	protected synchronized void onEntityExited(Entity in, double xPos, double yPos) {
		Envision.getEventHandler().postEvent(new EntityExitedRegionEvent(world, in, this, xPos, yPos));
	}
	
	public synchronized void addEntity(Entity in) {
		if (in == null || entitiesInside.contains(in)) return;
		
		entitiesInside.add(in);
		onEntityEntered(in, in.startX, in.startY);
	}
	
	public synchronized void removeEntity(Entity in) {
		if (in == null || entitiesInside.notContains(in)) return;
		
		entitiesInside.remove(in);
		onEntityExited(in, in.startX, in.startY);
	}
	
	public boolean containsEntity(GameObject object) {
		return entitiesInside.contains(object);
	}
	
	public String getName() { return name; }
	public IGameWorld getWorld() { return world; }
	public int getColor() { return regionColor; }
	public boolean onlyRenderInEditor() { return onlyRenderInEditor; }
	public Region setOnlyRenderInEditor(boolean val) { onlyRenderInEditor = val; return this; }
	
	public Region setName(String nameIn) { name = nameIn; return this; }
	public Region setColor(EColors colorIn) { return setColor(colorIn.intVal); }
	public Region setColor(int colorIn) { regionColor = colorIn; return this; }
	
	public static Region parseRegion(GameWorld world, String line) {
		try {
			Scanner lineReader = new Scanner(line.substring(2));
			//int i = (parts.length == 7) ? 1 : 0;
			int sX = Integer.valueOf(lineReader.next());
			int sY = Integer.valueOf(lineReader.next());
			int eX = Integer.valueOf(lineReader.next());
			int eY = Integer.valueOf(lineReader.next());
			int color = Integer.valueOf(lineReader.next());
			String name = lineReader.nextLine().trim();
			lineReader.close();
			return new Region(world, name, sX, sY, eX, eY, color);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String toSaveString() {
		return "r " + startX + " " + startY + " " + endX + " " + endY + " " + getColor() + " " + getName();
	}
	
	/** Don't know for regions yet... */
	@Override public int getInternalSaveID() { return 0; }
	
}