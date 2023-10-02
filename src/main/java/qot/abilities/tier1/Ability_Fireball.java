package qot.abilities.tier1;

import envision.game.abilities.Ability;
import envision.game.abilities.AbilityTier;
import envision.game.entities.Entity;
import qot.assets.textures.ability.AbilityTextures;
import qot.entities.projectiles.Fireball;

public class Ability_Fireball extends Ability {
	public Ability_Fireball() {
		super("Fireball", AbilityTextures.cast_fireball);
		
		setNumTiers(3);
		
		tier(0).manaCost(4).castTime(3 * 60).cooldown(5 * 60).requiresLevel(1);
		tier(1).manaCost(15).castTime(3 * 60).cooldown(5 * 60).requiresLevel(15);
		tier(2).manaCost(25).castTime(3 * 60).cooldown(5 * 60).requiresLevel(25);
	}

	@Override
	public boolean use(Entity e, int tierIn) {
		if (!canEntityUse(e, tierIn)) return false;
		
		int level = e.getSpellbook().getAbilityLevel(this);
		AbilityTier tier = tier(level - 1);
		
		if (tier == null) {
			System.out.println(this + " does not have tier: '" + level + "' !");
			return false;
		}
		
		int manaCost = tier.manaCost();
		if (!e.manaCheck(manaCost)) { return false; }
		
		e.drainMana(manaCost);
		
		int maxHealth = e.getMaxHealth();
		
		int amount = switch (level) {
		case 1 -> (int) Math.ceil((double) maxHealth * 0.20);
		case 2 -> (int) Math.ceil((double) maxHealth * 0.33);
		case 3 -> (int) Math.ceil((double) maxHealth * 0.60);
		default -> maxHealth;
		};
		
		var fb = new Fireball();
		
		/*
		if (world.getDistance(this, p) <= 150) {
			if (System.currentTimeMillis() - timeSinceLastFireball >= fireballDelay) {
				timeSinceLastFireball = System.currentTimeMillis();
				var fb = new Fireball();
				fb.worldX = (left) ? worldX : (int) (worldX + width / world.getTileWidth());
				fb.worldY = worldY + 2;
				world.addEntity(fb);
			}
		}
		*/
		
		return true;
	}

	@Override
	public boolean canEntityUse(Entity e, int tierIn) {
		var sb = e.getSpellbook();
		
		if (!sb.knowsAbility(this)) { return false; }
		if (sb.getAbilityLevel(this) < tierIn) { return false; }
		
		return true;
	}

	@Override
	public boolean canEntityUpgrade(Entity e) {
		var sb = e.getSpellbook();
		
		if (!sb.knowsAbility(this)) { return false; }
		
		int tierLevel = sb.getAbilityLevel(this);
		
		if (tierLevel >= maxTiers) { return false; }
		
		var tier = tier(tierLevel + 1);
		
		if (tier.requiresLevel() > e.getLevel()) { return false; }
		
		return true;
	}
}
