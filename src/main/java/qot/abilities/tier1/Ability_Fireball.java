package qot.abilities.tier1;

import org.joml.Vector3f;

import envision.Envision;
import envision.game.abilities.Ability;
import envision.game.abilities.AbilityTier;
import envision.game.entities.Entity;
import eutil.math.ENumUtil;
import eutil.random.ERandomUtil;
import qot.assets.textures.ability.AbilityTextures;
import qot.entities.projectiles.FireballProjectile;

public class Ability_Fireball extends Ability {
	public Ability_Fireball() {
		super("Fireball", AbilityTextures.cast_fireball);
		
		setNumTiers(3);
		
		tier(0).manaCost(4).castTime(3 * 60).cooldown(5 * 60).requiresLevel(5);
		tier(1).manaCost(10).castTime(3 * 60).cooldown(5 * 60).requiresLevel(10);
		tier(2).manaCost(0).castTime(1).cooldown(1).requiresLevel(20);
	}

	@Override
	public boolean use(Entity e, int tierIn) {
		if (!canEntityUse(e, tierIn)) return false;
		
		int level = e.getSpellbook().getAbilityLevel(this);
		AbilityTier tier = tier(level);
		
		if (tier == null) {
			System.out.println(this + " does not have tier: '" + level + "' !");
			return false;
		}
		
		int manaCost = tier.manaCost();
		if (!e.manaCheck(manaCost)) return false;
		
		e.drainMana(manaCost);
		
		final var cam = Envision.levelManager.getCamera();
        if (cam == null) return false;
        final double mpx = cam.getMousePixelX();
        final double mpy = cam.getMousePixelY();
        
        Vector3f dir = new Vector3f((float) (e.midX - mpx), (float) (e.midY - mpy), 0.0f);
        dir.normalize();
        dir.mul(-1.0f);
        
        if (level == 0) {
            spawnFireball(e, dir, level);
        }
        else if (level == 1) {
            spawnFireball(e, dir.rotateZ((float) (-25 * Math.PI / 180), new Vector3f()), level);
            spawnFireball(e, dir, level);
            spawnFireball(e, dir.rotateZ((float) (25 * Math.PI / 180), new Vector3f()), level);
        }
        else if (level == 2) {
            //spawnFireball(e, dir, level);
            int num = 20;
            double angle = 360.0 / num;
            for (int i = 0; i < num; i++) {
                spawnFireball(e, dir.rotateZ((float) (i * angle * Math.PI / 180), new Vector3f()), level);
            }
//            spawnFireball(e, dir.rotateZ((float) (-45 * Math.PI / 180), new Vector3f()), level);
//            spawnFireball(e, dir.rotateZ((float) (-25 * Math.PI / 180), new Vector3f()), level);
//            spawnFireball(e, dir.rotateZ((float) (-10 * Math.PI / 180), new Vector3f()), level);
//            spawnFireball(e, dir, level);
//            spawnFireball(e, dir.rotateZ((float) (10 * Math.PI / 180), new Vector3f()), level);
//            spawnFireball(e, dir.rotateZ((float) (25 * Math.PI / 180), new Vector3f()), level);
//            spawnFireball(e, dir.rotateZ((float) (45 * Math.PI / 180), new Vector3f()), level);
        }
        else {
            //spawnFireball(e, dir, level);
            int num = 2;
            int angle = 360 / num;
            for (int i = 0; i < num; i++) {
                spawnFireball(e, dir.rotateZ((float) (i * angle * Math.PI / 180), new Vector3f()), level);
            }
        }
		
		return true;
	}
	
	protected void spawnFireball(Entity e, Vector3f direction, int level) {
	    int minDamage = switch (level) {
        case 0 -> 5;
        case 1 -> 10;
        case 2 -> 15;
        default -> 20;
        };
        
        int maxDamage = switch (level) {
        case 0 -> 10;
        case 1 -> 15;
        case 2 -> 20;
        default -> 25;
        };
        
        int maxLifeSpan = switch (level) {
        case 0 -> 400;
        case 1 -> 500;
        case 2 -> 750;
        default -> 1000;
        };
        
        int maxDamageCount = switch (level) {
        case 0 -> 1;
        case 1 -> 1;
        case 2 -> 2;
        default -> 3;
        };
        
        int damage = e.getMagicLevel();
        if (e.activeEffectsTracker.containsKey("MAGIC_MODIFIER")) {
            damage += e.activeEffectsTracker.get("MAGIC_MODIFIER");
        }
        damage = minDamage + ERandomUtil.getRoll(0, damage);
        damage = ENumUtil.clamp(damage, minDamage, maxDamage);
        //System.out.println(damage);
        
        var fb = new FireballProjectile();
        fb.setBaseMeleeDamage(damage);
        fb.setMaxLifeSpan(maxLifeSpan);
        fb.setMaxDamageCount(maxDamageCount);
        
        fb.startX = e.midX - fb.width * 0.5;
        fb.startY = e.midY - fb.height * 0.5;
        fb.setFiredDirection(direction);
        fb.setFiringEntity(e);
        
        e.world.addEntity(fb);
	}

	@Override
	public boolean canEntityUse(Entity e, int tierIn) {
		var sb = e.getSpellbook();
		
		if (!sb.knowsAbility(this)) return false;
		if (sb.getAbilityLevel(this) < tierIn) return false;
		
		return true;
	}

	@Override
	public boolean canEntityUpgrade(Entity e) {
		var sb = e.getSpellbook();
		if (!sb.knowsAbility(this)) return false;
		
		int tierLevel = sb.getAbilityLevel(this);
		if (tierLevel >= maxTiers) return false;
		
		var tier = tier(tierLevel + 1);
		if (tier.requiresLevel() > e.getMagicLevel()) return false;
		
		return true;
	}
}
