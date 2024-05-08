package qot.abilities.tier1;

import envision.game.abilities.Ability;
import envision.game.entities.Entity;
import qot.assets.textures.ability.AbilityTextures;
import qot.particles.ExplosionEffect;
import qot.particles.FireEffect;

public class Ability_RingOfFire extends Ability {
    
    public Ability_RingOfFire() {
        super("Ring of Fire", AbilityTextures.cast_heal);
        
        setNumTiers(2);
        
        tier(0).manaCost(0).castTime(200).cooldown(500).requiresLevel(0);
        tier(1).manaCost(0).castTime(200).cooldown(500).requiresLevel(10);
    }

    @Override
    public boolean use(Entity e, int tierIn) {
        
        double radius = 32;
        
        int num = 18;
        double angle = 360.0 / num;
        double rads = (angle * Math.PI / 180.0) - Math.PI;
        for (int i = 0; i < num; i++) {
            double r = rads * i;
            double dx = radius * Math.cos(r) - radius * Math.sin(r) + e.midX;
            double dy = radius * Math.sin(r) + radius * Math.cos(r) + e.midY;
            
            var fire = new FireEffect(dx - 16, dy - 50, 32, 64, 1500);
            var explode = new ExplosionEffect(dx - 16, dy, 32, 32, 1000);
            
            e.world.addEntity(explode);
            e.world.addEntity(fire);
        }
        
        return false;
    }

    @Override
    public boolean canEntityUse(Entity e, int tierIn) {
        System.out.println("Lol");
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
