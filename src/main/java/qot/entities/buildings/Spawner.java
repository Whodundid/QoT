package qot.entities.buildings;

import envision.Envision;
import envision.game.component.EntityComponent;
import envision.game.component.types.RandomTimeEventComponent;
import envision.game.entities.Entity;
import envision.game.entities.EntitySpawn;
import eutil.datatypes.util.EList;
import eutil.random.ERandomUtil;
import qot.assets.textures.doodads.house.HouseTextures;
import qot.entities.EntityList;
import qot.entities.enemies.dragon.Fireball;

public class Spawner extends Building {

	protected EntitySpawn entityToSpawn;
	protected int maxEntitiesCanSpawn = 5;
	protected EList<Entity> trackedEntities = EList.newList();
	
	private RandomTimeEventComponent spawnerComponent;
	private RandomTimeEventComponent defenderComponent;
	
	public Spawner() { this(0, 0, new EntitySpawn(0, 0, EntityList.randomEntity())); }
	public Spawner(int posX, int posY, EntitySpawn entityToSpawn) {
		this.entityToSpawn = entityToSpawn;
		this.setTexture(HouseTextures.anvil);
		
		init(posX, posY, 80, 80);
		
		setMaxHealth(30);
		setHealth(30);
		setBaseMeleeDamage(1);
		setExperienceRewardedOnKill(300);
		
		addComponent(spawnerComponent = new RandomTimeEventComponent(this, "spawn_update", 200, 1000));
		/*addComponent(*/defenderComponent = new RandomTimeEventComponent(this, "defense", 100, 500)/*)*/;
		
		this.setHeadText(EntityList.getEntity(entityToSpawn.getType()).getName());
	}
	
	@Override
	public void onComponentEvent(EntityComponent theComponent, String id, Object... args) {
		if ("spawn_update".equals(id)) spawnEntity();
		else if ("defense".equals(id)) spawnFireBall();
	}
	
	@Override
	public void onLivingUpdate(float dt) {
		var dist = world.getDistance(this, Envision.thePlayer);
		var inRange = dist <= 300;
		defenderComponent.setPaused(!inRange);
	}
	
	@Override
	public Entity setWorldPos(int x, int y) {
		super.setWorldPos(x, y);
		entityToSpawn.setPosition(x, y + 1);
		return this;
	}
	
	protected void spawnFireBall() {
		var fb = new Fireball(this);
		fb.worldX = worldX + 1;
		fb.worldY = worldY + 1;
		fb.setSpeed(700);
		world.addEntity(fb);
	}
	
	protected void spawnEntity() {
		if (trackedEntities.size() >= maxEntitiesCanSpawn) {
			updateTrackedEntities();
		}
		else {
			Entity e = entityToSpawn.spawnEntity(world);
			e.worldX += ERandomUtil.getRoll(-2, 2);
			e.worldY += ERandomUtil.getRoll(-2, 2);
			trackedEntities.add(e);
		}
	}
	
	protected void updateTrackedEntities() {
		for (int i = 0; i < trackedEntities.size(); i++) {
			var e = trackedEntities.get(i);
			if (!world.getEntitiesInWorld().contains(e)) {
				trackedEntities.remove(i);
				i--;
			}
		}
	}
	
	@Override
	public int getInternalSaveID() {
		return EntityList.Spawner.ID;
	}
	
}
