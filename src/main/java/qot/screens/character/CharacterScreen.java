package qot.screens.character;

import envision.Envision;
import envision.engine.inputHandlers.Keyboard;
import envision.engine.rendering.fontRenderer.FontRenderer;
import envision.engine.resourceLoaders.Sprite;
import envision.engine.screens.GameScreen;
import envision.engine.windows.windowObjects.actionObjects.WindowButton;
import envision.engine.windows.windowTypes.interfaces.IActionObject;
import envision.engine.windows.windowTypes.interfaces.IWindowObject;
import envision.game.entities.Entity;
import envision.game.entities.util.EntityLevel;
import eutil.EUtil;
import eutil.colors.EColors;
import qot.assets.textures.window.WindowTextures;

public class CharacterScreen extends GameScreen {
	
    //========
    // Fields
    //========
    
	private Entity theEntity;
	
	//character texture draw dimensions
	private Sprite texture;
	private double dX, dY;
	private double dW, dH;
	//character frame dimensions
	private double fX, fY;
	private double fW, fH;
	
	//upgrade buttons
	private WindowButton back;
	private WindowButton upHealth, upStrength, upMana;
	private WindowButton abilities;
	
	private InventoryRenderer inventory;
	
	//==============
    // Constructors
    //==============
	
	public CharacterScreen(Entity entIn) {
		theEntity = entIn;
		texture = theEntity.getSprite();
		updateDrawDimensions();
	}
	
	private void updateDrawDimensions() {
		double scaleX = 4;
		double scaleY = 2;
		
		//The texture should be sized to fit exactly 1/4 of the screen's size
		//To do this, take the scaleX and scaleY using ratio math of the screen's
		//dimensions. Then, use the smaller of the two to form a square texture
		dW = (width / scaleX);
		dH = (height / scaleY);
		if (dW > dH) dW = dH;
		else dH = dW;
		
		dX = midX - (dW / 2);
		dY = midY - (dH / 2) - (height / 10);
		
		//calculate frame dimensions
		fW = dW + dW;
		fH = dH + (dH / 2);
		fX = midX - (fW / 2);
		fY = midY - (fH / 2) - (height / 10);
	}
	
	//===========
    // Overrides
    //===========
	
	@Override
	public void initScreen() {
		Envision.pause();
	}
	
	@Override
	public void initChildren() {
		back = new WindowButton(this, 5, endY - 60, 100, 95, "Back");
		
		double bH = height / 20;
		double bW = bH;
		
		upHealth = new WindowButton(this, 50, midY - 100, bW, bH);
		upStrength = new WindowButton(this, 50, upHealth.endY + 10, bW, bH);
		upMana = new WindowButton(this, 50, upStrength.endY + 10, bW, bH);
		
		WindowButton.setTextures(WindowTextures.plus, WindowTextures.plus_sel, upHealth, upStrength, upMana);
		
		inventory = new InventoryRenderer(theEntity);
		inventory.setDrawItemName(true);
		
		double inventoryWidth = inventory.width;
		double inventoryHeight = inventory.height;
		double inventoryX = midX - inventoryWidth / 2;
		double inventoryY = endY - (midY / 4) - inventoryHeight / 2;
		inventory.setPosition(inventoryX, inventoryY);
		
		inventory.setItemTextPosition(inventory.midX, inventory.startY - 30);
		
		abilities = new WindowButton(this, 5, endY - 40, 200, 35, "Abilities");
		abilities.setAction(() -> Envision.displayScreen(new AbilityScreen(theEntity), this));
		
		addObject(inventory);
		addObject(upHealth, upStrength, upMana);
		addObject(abilities);
	}
	
	@Override
	public void onScreenClosed() {
		Envision.unpause();
	}
	
	@Override
	public void drawScreen(float dt, int mXIn, int mYIn) {
		drawRect(EColors.pdgray);
		drawChar();
		drawStats();
		drawInventory();
		drawAbilities();
		
		if (EUtil.notNull(upHealth, upStrength, upMana)) {
			if (EntityLevel.checkLevelUp(theEntity.getLevel(), theEntity.getExperience())) {
				IWindowObject.setVisible(true, upHealth, upStrength, upMana);
			}
			else {
				IWindowObject.setVisible(false, upHealth, upStrength, upMana);
			}
		}
		
		drawString("Gold: " + EColors.mc_gold + theEntity.getGold(), 50, midY + dH / 2 - 35);
		drawString("Damage per hit: " + EColors.lred + theEntity.getBaseMeleeDamage(), 50, midY + dH / 2);
		
		var effects = theEntity.activeEffectsTracker.keySet();
		int i = 0;
		for (var effect : effects) {
		    String name = effect;
		    String value = String.valueOf(theEntity.activeEffectsTracker.get(effect));
		    drawString(name + ": " + value, fX + fW + 20, midY - 200 + i * 30);
		    i++;
		}
	}
	
	private void drawChar() {
		//draw texture frame
		drawRect(fX, startY, fX + fW, fY + fH, EColors.black);
		drawRect(fX + 2, startY, fX + fW - 2, fY + fH - 2, EColors.vdgray);
		
		double ratioLightX = (fW / 3.2);
		double ratioLightY = (fY + fH - 2) / 2.3;
		double lightInc = (fW / 64);
		int lightColor = EColors.dgray.opacity(0x06);
		for (int i = 0; i < 10; i++) {
			double rX = ratioLightX - (i * lightInc);
			double rY = ratioLightY - (i * lightInc) / 4;
			drawFilledEllipse(midX, midY - (midY / 6), rX, rY, 16, lightColor);
		}
		
		//draw texture
		drawSprite(texture, dX, dY, dW, dH);
		
		//draw character name above char texture
		drawStringC(theEntity.getName(), midX, 40);
	}
	
	private void drawStats() {
		double tX = 70;
		double tY = 70;
		double cY = 25; // 'change in Y'
		
		double bX = (tX / 2);
		double bY = (tY / 2);
		double bEX = fX - (bX);
		double bEY = midY + 120;
		drawRect(bX, bY, bEX, bEY, EColors.black);
		drawRect(bX + 1, bY + 1, bEX - 1, bEY- 1, EColors.pdgray.brightness(0x88));
		
		//Level
		drawString("Level", tX, tY, EColors.lorange);
		drawString(theEntity.getLevel(), tX, tY + (cY));
		
		//Experience
		drawString("Experience", tX, tY + (cY * 2.5), EColors.purple);
		drawString(theEntity.getExperience() +  " / " + theEntity.getXPNeeded(), tX, tY + (cY * 3.5));
		
		//Health
		drawString("Health", tX, tY + (cY * 5), EColors.mc_darkred);
		drawString(theEntity.getHealth() +  " / " + theEntity.getMaxHealth(), tX, tY + (cY * 6));
		
		//Mana
		drawString("Mana", tX, tY + (cY * 7.5), EColors.skyblue);
		drawString(theEntity.getMana() +  " / " + theEntity.getMaxMana(), tX, tY + (cY * 8.5));
		
		//draw skills
		drawStringS("Hitpoints " + EColors.white + theEntity.getHitpointsLevel(), upHealth.endX + 20, upHealth.midY - FontRenderer.HALF_FH, EColors.red);
		drawStringS("Strength " + EColors.white + theEntity.getStrengthLevel(), upStrength.endX + 20, upStrength.midY - FontRenderer.HALF_FH, EColors.lgreen);
		drawStringS("Magic " + EColors.white + theEntity.getMagicLevel(), upMana.endX + 20, upMana.midY - FontRenderer.HALF_FH, EColors.blue);
	}
	
	private void drawInventory() {
		
	}
	
	private void drawAbilities() {
		
	}
	
	@Override
	public void keyPressed(char typedChar, int keyCode) {
		if (keyCode == Keyboard.KEY_TAB) closeScreen();
		
		super.keyPressed(typedChar, keyCode);
	}
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		if (object == back) closeScreen();
		if (object == upHealth) levelHP();
		if (object == upStrength) levelStrength();
		if (object == upMana) levelMana();
	}
	
	@Override
	public void onScreenResized() {
		setDimensions(0, 0, Envision.getWidth(), Envision.getHeight());
		updateDrawDimensions();
		reInitChildren();
	}
	
	private void levelHP() {
		theEntity.setHitpointsLevel(theEntity.getHitpointsLevel() + 1);
		theEntity.levelUp();
	}
	private void levelStrength() {
		theEntity.setStrengthLevel(theEntity.getStrengthLevel() + 1);
		theEntity.levelUp();
	}
	private void levelMana() {
		theEntity.setMagicLevel(theEntity.getMagicLevel() + 1);
		theEntity.levelUp();
	}
	
}
