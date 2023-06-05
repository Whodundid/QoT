package envision.game.abilities;

import envision.engine.rendering.RenderingManager;
import envision.engine.rendering.fontRenderer.FontRenderer;
import envision.engine.rendering.textureSystem.GameTexture;
import envision.engine.screens.GameScreen;
import envision.game.entities.Entity;
import eutil.colors.EColors;
import eutil.datatypes.boxes.Box2;
import eutil.datatypes.boxes.BoxList;

/**
 * Keeps track of the abilities/spells that an entity has unlocked along
 * with their respective level for each.
 * 
 * @author Hunter Bragg
 */
public class EntitySpellbook {
	
	//========
	// Fields
	//========
	
	private Entity theEntity;
	
	/** [Ability, Level] */
	private BoxList<Ability, Integer> unlockedAbilities = new BoxList<>();
	
	//==============
	// Constructors
	//==============
	
	public EntitySpellbook(Entity entIn) {
		theEntity = entIn;
	}
	
	//=================
	// Drawing Methods
	//=================
	
	public void drawAbilities(GameScreen screen) {
		final var dims = screen.getDimensions();
		
		final double midX = dims.midX;
		final double width = 64;
		final double height = 64;
		final double gap = 10;
		
		final int size = unlockedAbilities.size();
		final double startX = midX - (size * width) - ((size - 1) * gap);
		
		for (int i = 0; i < size; i++) {
			var knownAbility = unlockedAbilities.get(i);
			
			Ability ability = knownAbility.getA();
			int level = knownAbility.getB();
			
			var tracker = theEntity.getAbilityTracker();
			var cooldown = tracker.getCooldownForAbility(ability);
			
			GameTexture tex = ability.getTexture();
			
			double sx = startX + (i * width) + ((i - 1) * gap);
			double sy = dims.endY - height;
			double sw = FontRenderer.strWidth("" + level) * 0.5;
			
			var white = EColors.white;
			var full = white.brightness(255);
			var low = white.brightness(127);
			int color = full;
			color = (theEntity.getMana() >= knownAbility.getA().getManaCostForTier(level - 1)) ? color : low;
			RenderingManager.drawTexture(tex, sx, sy, width, height, color);
			
			if (cooldown.getA()) {
				int amount = cooldown.getB();
				int max = ability.getCooldownForTier(level - 1);
				
				double ratio = (double) amount / (double) max;
//				System.out.println(amount + " : " + max + " : " + ratio + " : " + (sy + height - (height * ratio)));
				double drawY = sy + height - (height * ratio);
				
				RenderingManager.drawRect(sx, drawY, sx + width, sy + height, EColors.vdgray.opacity(200));
			}

			RenderingManager.drawString(level, sx + width * 0.5 - sw, sy + height * 0.75, EColors.green);
		}
	}
	
	//=========
	// Methods
	//=========
	
	public boolean learnAbility(Ability abilityIn) {
		if (unlockedAbilities.containsA(abilityIn)) return false;
		unlockedAbilities.add(abilityIn, 1);
		return true;
	}
	
	public boolean unlearnAbility(Ability abilityIn) {
		if (unlockedAbilities.containsA(abilityIn)) {
			unlockedAbilities.removeBoxesContainingA(abilityIn);
			return true;
		}
		return false;
	}
	
	public boolean knowsAbility(Ability abilityIn) {
		return unlockedAbilities.containsA(abilityIn);
	}
	
	public int getAbilityLevel(Ability abilityIn) {
		var box = unlockedAbilities.getBoxWithA(abilityIn);
		if (box == null) return -1;
		return box.getB();
	}
	
	public Box2<Ability, Integer> getKnownAbility(Ability abilityIn) {
		return unlockedAbilities.getBoxWithA(abilityIn);
	}
	
	public Box2<Boolean, String> upgradeAbility(Ability abilityIn) {
		if (abilityIn == null) return ret(false, "Null ability given!");
		var box = getKnownAbility(abilityIn);
		if (box == null) return ret(false, theEntity + " doesn't know the ability '" + abilityIn.getAbilityName() + "'");
		
		Ability ability = box.getA();
		boolean canUpgrade = ability.canEntityUpgrade(theEntity);
		
		if (canUpgrade) {
			box.setB(box.getB() + 1);
			ret(true);
		}
		
		return ret(false, ability.getAbilityName() + " Can't be upgraded for some reason!");
	}
	
	private static Box2<Boolean, String> ret(boolean val) { return ret(val, ""); }
	private static Box2<Boolean, String> ret(boolean val, String reason) { return new Box2<>(val, reason); }
	
}
