package main;

import java.util.Scanner;
import shop.Shopkeeper;
import sound.Songs;
import entities.enemy.Enemy;
import entities.enemy.types.Goblin;
import entities.enemy.types.Whodundid;
import entities.player.Player;
import gameSystems.battleSystem.Battle;
import items.types.LesserHealing;

public class Main implements Runnable {
	
	public static Main m = null;
	
	public static void main(String[] args) {
		//m = new Main();
		Game.getGame().runGame();
	}
	
	@Override
	public void run() {
		Scanner reader = new Scanner(System.in);
		
		Songs.playSong(Songs.theme);
		
		System.out.println("Welcome to Quest of Thyrah. There is an evil tyranical dragon named Thyrah who absorbs the soulds of those he consumes...");
		String pressingEnter = reader.nextLine();
		System.out.println("In a world like this, there is an individual who is reincarnated every time there is evil in the world.");
		pressingEnter = reader.nextLine();
		System.out.println("This person is called 'The Elemental Master'");
		pressingEnter = reader.nextLine();
		System.out.println("Society has also established an organization called 'The Gauntra.' This is a group of people who are sworn to destroy all dragons in the world.");
		pressingEnter = reader.nextLine();
		System.out.println("You awake in the middle of a forest. The only memories you have are the ones in the description above. Strangely enough you don't even remember your name.");
		pressingEnter = reader.nextLine();
		System.out.println("What is your name?");
		String name = reader.nextLine();
		Player mainCharacter = new Player(name, 1, 200, 200, 50, 50, 0, 25, 0);
		
		Enemy goblin = new Goblin();
		System.out.println(goblin.getName() + " has " + goblin.getHealth() + " HP.");
		
		
		System.out.println("Your character's name is: " + mainCharacter.getName() + ", and he is level " + mainCharacter.getLevel() + " with " + mainCharacter.getHealth() +
				 " HP and " + mainCharacter.getMana() + " MP.");
		
		mainCharacter.getInventory().storeItems(items.Items.woodSword);
		mainCharacter.getInventory().storeItems(items.Items.lesserHealing);
		
		System.out.println("Your character has: ");
		mainCharacter.displayInventory();
		//System.out.println(Swords.Items.woodSword.getWeight());
		
		//System.out.println(mainCharacter.getTrueDamage());
		System.out.println("Damage: " + mainCharacter.getTrueDamage());
		mainCharacter.equipWeapon(items.Items.woodSword);
		//System.out.println(mainCharacter.getTrueDamage());
		System.out.println("Damage: " + mainCharacter.getTrueDamage());
		
		Songs.stopSong(Songs.theme);
		Battle battle1 = new Battle(mainCharacter, goblin);
		Battle theOne = new Battle(mainCharacter, new Whodundid());
		
		Shopkeeper shopKeeper = new Shopkeeper("Whodundid's Brother", 350);
		shopKeeper.getShop().addItem(new LesserHealing());
		System.out.println("------------");
		shopKeeper.displayShop();
		
		Battle theRealOne = new Battle(mainCharacter, shopKeeper);
	}
	
}
