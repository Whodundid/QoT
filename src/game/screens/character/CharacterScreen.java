package game.screens.character;

import assets.textures.WindowTextures;
import engine.inputHandlers.Keyboard;
import engine.renderEngine.textureSystem.GameTexture;
import engine.screenEngine.GameScreen;
import engine.windowLib.windowObjects.actionObjects.WindowButton;
import engine.windowLib.windowTypes.interfaces.IActionObject;
import eutil.colors.EColors;
import game.entities.Entity;
import main.QoT;

public class CharacterScreen extends GameScreen {
	
	private Entity theEntity;
	
	//character texture draw dimensions
	private GameTexture texture;
	private double dX, dY;
	private double dW, dH;
	//character frame dimensions
	private double fX, fY;
	private double fW, fH;
	
	//upgrade buttons
	private WindowButton back;
	private WindowButton upHealth, upStrength, upMana;
	
	//private CharacterInventory inventory;
	
	//--------------
	// Constructors
	//--------------
	
	public CharacterScreen(Entity entIn) {
		theEntity = entIn;
		texture = theEntity.getTexture();
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
	
	@Override
	public void initScreen() {
		QoT.pause();
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
		
		addChild(upHealth, upStrength, upMana);
	}
	
	@Override
	public void onScreenClosed() {
		QoT.unpause();
	}
	
	@Override
	public void drawScreen(int mXIn, int mYIn) {
		drawRect(EColors.pdgray);
		drawChar();
		drawStats();
		drawInventory();
		drawAbilities();
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
		drawTexture(texture, dX, dY, dW, dH);
		
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
		double bEY = midY;
		drawRect(bX, bY, bEX, bEY, EColors.black);
		drawRect(bX + 1, bY + 1, bEX - 1, bEY- 1, EColors.pdgray.brightness(0x88));
		
		//Level
		drawString("Level", tX, tY, EColors.lorange);
		drawString(theEntity.getLevel(), tX, tY + (cY));
		
		//Experience
		drawString("Experience", tX, tY + (cY * 3), EColors.purple);
		drawString(theEntity.getExperience() +  " / " + theEntity.getXPNeeded(), tX, tY + (cY * 4));
		
		//Health
		drawString("Health", tX, tY + (cY * 6), EColors.mc_darkred);
		drawString(theEntity.getHealth() +  " / " + theEntity.getMaxHealth(), tX, tY + (cY * 7));
		
		//Mana
		drawString("Mana", tX, tY + (cY * 9), EColors.skyblue);
		drawString(theEntity.getMana() +  " / " + theEntity.getMaxMana(), tX, tY + (cY * 10));
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
	public void onWindowResized() {
		setDimensions(0, 0, QoT.getWidth(), QoT.getHeight());
		updateDrawDimensions();
		reInitChildren();
	}
	
	private void levelHP() {}
	private void levelStrength() {}
	private void levelMana() {}
	
}
