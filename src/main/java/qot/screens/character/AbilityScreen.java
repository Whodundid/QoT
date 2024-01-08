package qot.screens.character;

import envision.Envision;
import envision.engine.resourceLoaders.Sprite;
import envision.engine.screens.GameScreen;
import envision.engine.windows.windowObjects.actionObjects.WindowButton;
import envision.engine.windows.windowObjects.advancedObjects.WindowScrollList;
import envision.engine.windows.windowObjects.basicObjects.WindowLabel;
import envision.game.entities.Entity;
import eutil.colors.EColors;
import qot.abilities.Abilities;
import qot.assets.textures.ability.AbilityTextures;

public class AbilityScreen extends GameScreen {
    
    //========
    // Fields
    //========
    
    Entity theEntity;
    WindowScrollList panel;
    WindowButton back;
    double bottomGap = 30;
    
    // this can be made better :)
    WindowButton heal;
    WindowButton fireball;
    WindowButton kick;
    WindowButton heavyAttack;
    WindowButton deepStrikes;
    WindowButton rapidBlows;
    WindowButton bloodLust;
    WindowButton evasion;
    WindowButton barder;
    
    //==============
    // Constructors
    //==============
    
    public AbilityScreen(Entity theEntityIn) {
        theEntity = theEntityIn;
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
        panel = new WindowScrollList(this, startX, startY, endX, endY - bottomGap - 5);
        var ld = panel.getListDimensions();
        
        double w = 100;
        double gap = 100;
        double sx = ld.startX + gap;
        double sy = ld.midY - 100;
        double bGap = gap + w;
        
        heal = new WindowButton(panel, sx + 0 * bGap, sy, w, 100);
        fireball = new WindowButton(panel, sx + 1 * bGap, sy, w, 100);
        kick = new WindowButton(panel, sx + 2 * bGap, sy, w, 100);
        heavyAttack = new WindowButton(panel, sx + 3 * bGap, sy, w, 100);
        deepStrikes = new WindowButton(panel, sx + 4 * bGap, sy, w, 100);
        
        WindowLabel healLbl = new WindowLabel(panel, heal.midX, heal.endY + 20);
        WindowLabel fireballLbl = new WindowLabel(panel, fireball.midX, fireball.endY + 20);
        WindowLabel kickLbl = new WindowLabel(panel, kick.midX, kick.endY + 20);
        WindowLabel heavyAttackLbl = new WindowLabel(panel, heavyAttack.midX, heavyAttack.endY + 20);
        WindowLabel deepStrikesLbl = new WindowLabel(panel, deepStrikes.midX, deepStrikes.endY + 20);
        
        healLbl.setDrawCentered(true);
        fireballLbl.setDrawCentered(true);
        kickLbl.setDrawCentered(true);
        heavyAttackLbl.setDrawCentered(true);
        deepStrikesLbl.setDrawCentered(true);
        
        final var sb = theEntity.spellbook;
        healLbl.setString("" + sb.getAbilityLevel(Abilities.selfHeal));
        fireballLbl.setString("" + sb.getAbilityLevel(Abilities.fireball));
        //kickLbl.setString("" + sb.getAbilityLevel(Abilities.selfHeal));
        //heavyAttackLbl.setString("" + sb.getAbilityLevel(Abilities.selfHeal));
        //deepStrikesLbl.setString("" + sb.getAbilityLevel(Abilities.selfHeal));
        
        heal.setEnabled(Abilities.selfHeal.canEntityUpgrade(theEntity));
        fireball.setEnabled(Abilities.fireball.canEntityUpgrade(theEntity));
        
        heal.setButtonTexture(new Sprite(AbilityTextures.cast_heal));
        fireball.setButtonTexture(new Sprite(AbilityTextures.cast_fireball));
        
        heal.setAction(() -> theEntity.spellbook.upgradeAbility(Abilities.selfHeal));
        fireball.setAction(() -> {
            System.out.println("LOL");
            theEntity.spellbook.upgradeAbility(Abilities.fireball);
        });
        
        // mouse hover doesn't register on list objects because the code to do checks only looks
        // for the object's children --> not a list's children
        panel.addObjectToList(heal, fireball, kick, heavyAttack, deepStrikes);
        panel.addObjectToList(healLbl, fireballLbl, kickLbl, heavyAttackLbl, deepStrikesLbl);
        panel.fitItemsInList();
        
        addObject(heal, fireball, kick, heavyAttack, deepStrikes);
        addObject(healLbl, fireballLbl, kickLbl, heavyAttackLbl, deepStrikesLbl);
        
        back = new WindowButton(this, midX - 100, endY - bottomGap, 200, bottomGap - 5, "Back");
        back.setAction(this::closeScreen);
        
        //addObject(panel);
        addObject(back);
    }
    
    @Override
    public void drawScreen(float dt, int mXIn, int mYIn) {
        drawRect(EColors.dgray);
        
    }
    
}
