package mainT;

import java.util.Scanner;
import shopT.Shopkeeper;
import soundT.Songs;
import battleSystem.Battle;
import entitiesT.enemy.Enemy;
import entitiesT.enemy.types.Goblin;
import entitiesT.enemy.types.Whodundid;
import entitiesT.player.Player;
import itemsT.types.LesserHealing;

public class Main implements Runnable {
	
	public static Main m = null;
	
	public static void main(String[] args) {
		m = new Main();
		Game.getInstance().runGame();
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
		Player mainCharacter = new Player(name, 1, 40, 40, 15, 15, 0, 25, 0);
		
		Enemy goblin = new Goblin();
		System.out.println(goblin.getName() + " has " + goblin.getHealth() + " HP.");
		
		
		System.out.println("Your character's name is: " + mainCharacter.getName() + ", and he is level " + mainCharacter.getLevel() + " with " + mainCharacter.getHealth() +
				 " HP and " + mainCharacter.getMana() + " MP.");

		mainCharacter.getInventory().storeItems(itemsT.Items.woodSword);
		
		System.out.println("Your character has: ");
		mainCharacter.displayInventory();
		//System.out.println(Swords.Items.woodSword.getWeight());
		
		//System.out.println(mainCharacter.getTrueDamage());
		mainCharacter.equipWeapon(itemsT.Items.woodSword);
		//System.out.println(mainCharacter.getTrueDamage());
		
		Songs.stopSong(Songs.theme);
		Battle battle1 = new Battle(mainCharacter, goblin);
		Battle theOne = new Battle(mainCharacter, new Whodundid());
		
		Songs.playSong(Songs.lithinburg);
		
		Shopkeeper shopKeeper = new Shopkeeper("Whodundid's Brother", 350);
		shopKeeper.getShop().addItem(new LesserHealing());
		System.out.println("------------");
		shopKeeper.displayShop();
	}
	
	public static Game getWindow() { return Game.getInstance(); }
	
}
