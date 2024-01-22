package qot.abilities.tier1;

import org.joml.Vector3f;

import envision.Envision;
import envision.game.abilities.Ability;
import envision.game.entities.Entity;
import envision.game.entities.combat.DodgeRoll;
import qot.assets.textures.ability.AbilityTextures;

public class Ability_DodgeRoll extends Ability {
    
    //========
    // Fields
    //========
    
    public final DodgeRoll dodgeRoller = new DodgeRoll();
    
    //==============
    // Constructors
    //==============
    
    public Ability_DodgeRoll() {
        super("Dodge Roll", AbilityTextures.cast_kick);
        
        setNumTiers(1);
        
        tier(0).staminaCost(25).cooldown(50);
    }
    
    //===========
    // Overrides
    //===========
    
    @Override
    public boolean use(Entity e, int tierIn) {
        if (!canEntityUse(e, tierIn)) return false;
        
        int staminaCost = this.getStaminaCostForTier(tierIn);
        if (!e.staminaCheck(staminaCost)) return false;
        e.drainStamina(staminaCost);
        
        final var cam = Envision.levelManager.getCamera();
        if (cam == null) return false;
        final double mpx = cam.getMxPixel();
        final double mpy = cam.getMyPixel();
        
        Vector3f dir = new Vector3f((float) (e.midX - mpx), (float) (e.midY - mpy), 0.0f);
        dir.normalize();
        dir.mul(-1.0f);
        
        dodgeRoller.performDodgeRoll(e, dir, 420, 0.30);
        
        return true;
    }
    
    @Override
    public void updateAbility(long dt) {
        dodgeRoller.update(dt);
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
