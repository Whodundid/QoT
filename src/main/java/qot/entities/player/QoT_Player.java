package qot.entities.player;

import envision.Envision;
import envision.engine.inputHandlers.Keyboard;
import envision.game.entities.Entity;
import envision.game.entities.player.Player;
import envision.game.entities.player.PlayerStats;
import envision.game.world.GameWorld;
import eutil.datatypes.EArrayList;
import eutil.datatypes.util.EList;
import eutil.random.ERandomUtil;
import qot.assets.textures.entity.EntityTextures;
import qot.entities.enemies.dragon.Thyrah;
import qot.items.Items;

public class QoT_Player extends Player {
	
	private PlayerStats stats;
	
	public long oldTime, curTime;
	public boolean start;
	float dt;
	
	public QoT_Player() { this("Player", 0, 0); }
	public QoT_Player(String nameIn) { this(nameIn, 0, 0); }
	public QoT_Player(String nameIn, int posX, int posY) {
		super(nameIn);
		
		//init(posX, posY, 32, 32);
		init(posX, posY, 40, 40);
		
		setMaxHealth(10);
		setHealth(10);
		setBaseMeleeDamage(1);
		
		baseInventorySize = 20;
		inventory.setSize(baseInventorySize);
		
		inventory.setItem(0, Items.lesserHealing);
		inventory.setItem(1, Items.lesserMana);
		
		setCollisionBox(midX - 8, endY - 10, midX + 8, endY);
		tex = EntityTextures.player;
		timeUntilNextAttack = 175l;

		addComponent(new PlayerRenderer(this));
	}
	
	//----------------
	// Player Getters
	//----------------
	
	@Override
	public void onMousePress(int mXIn, int mYIn, int button) {
		if (!attacking && button == 0) {
			attacking = true;
			recentlyAttacked = true;
			attackDrawStart = System.currentTimeMillis();
			attackStart = System.currentTimeMillis();
			
			EList<Entity> inRange = new EArrayList<>();
			for (var e : Envision.theWorld.getEntitiesInWorld()) {
				if (e == this) continue;
				if (e.isInvincible()) continue;
				if (((GameWorld) Envision.theWorld).getDistance(e, this) < 50) inRange.add((Entity) e);
			}
			
			for (var e : inRange) {
				var damage = getBaseMeleeDamage();
				e.drainHealth(damage);
				//addObject(new DamageSplash(e.startX + e.midX, e.startY + e.midY, damage));
				if (e.isDead()) {
					if (e instanceof Thyrah) giveItem(Items.random());
					else if (ERandomUtil.roll(5, 0, 10)) giveItem(Items.random());
					
					getStats().addKilled(1);
					Envision.theWorld.removeEntity(e);
					addXP(e.getExperienceRewardedOnKill());
				}
			}
		}
	}
	
	@Override
	public void onKeyPress(char typedChar, int keyCode) {
		if (keyCode >= Keyboard.KEY_0 && keyCode <= Keyboard.KEY_9) {
			int ability = keyCode - Keyboard.KEY_1;
			abilityTracker.useAbility(ability);
		}
	}
	
	@Override
	public void onLivingUpdate(float dt) {
		abilityTracker.onGameTick(dt);
	}
	
	@Override
	public int getInternalSaveID() {
		return 0;
	}
	
}
