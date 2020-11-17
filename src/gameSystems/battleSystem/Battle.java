package gameSystems.battleSystem;

import entities.Entity;
import entities.enemy.types.Whodundid;
import entities.player.Player;
import java.util.Scanner;
import sound.Songs;

public class Battle {
	
	private static int failAmount = 0;
	
	public Battle(Player player1, Entity enemy1) {
		System.out.println("You encountered a " + enemy1.getName() + "! They are level " + enemy1.getLevel() + " with " + enemy1.getHealth() + " HP and " + enemy1.getMana() + "!");
		battle(player1, enemy1);
	}
	
	public static void battle(Player player1, Entity enemy1) {
		Songs.playSong((enemy1 instanceof Whodundid) ? Songs.darkCave : Songs.battleTheme);
		
		while (!player1.isDead() && !enemy1.isDead()) {
			int command = printActions(player1, enemy1);
			failAmount = 0;
			
			if (player1.getHealth() <= 0 || enemy1.getHealth() <= 0) {
				break;
			}
			
			if (!player1.isDead()) {
				switch (command) {
				case 0: enemy1.hurt(player1.getDamage()); break;
				case 1: player1.useFlame(enemy1); break;
				case 2:
					enemy1.hurt(player1.flameDamage);
					player1.useHeal();
					break;
				case 3:System.out.println("You chose to do nothing...");
				case 4: player1.utilizeInventory(null);
				default: System.out.println(":D");
				}
			}
			
			if (!enemy1.isDead()) { player1.hurt(enemy1.getDamage()); }
			System.out.println();
		}

		if (player1.isDead()) {
			System.out.println("You got rekt son.");
		}
		if (enemy1.isDead()) {
			System.out.println("Victory!");
		}
		
		Songs.stopSong((enemy1 instanceof Whodundid) ? Songs.darkCave : Songs.battleTheme);
		
	}
	
	private static int printActions(Player p, Entity e) {
		System.out.println("You have " + p.getHealth() + " HP and " + p.getMana() + " MP " + e.getName() + " has " + e.getHealth() + " HP.");
		System.out.println("Type 0 to attack the enemy.");
		System.out.println("Type 1 to use fire on the enemy");
		System.out.println("Type 2 to cast 'heal'");
		System.out.println("Type 3 to do absolutely nothing.");
		System.out.println("Type 4 to open your inventory.");
		
		int val = -1;
		while (true) {
			try {
				Scanner reader = new Scanner(System.in);
				val = reader.nextInt();
				break;
			}
			catch (Exception ex) {
				if (failAmount < 5) {
					System.out.println("Please enter one of the items on the list...");
				}
				else if (failAmount < 100) {
					System.out.println("Seriously.... TYPE 0 1 2 OR 3... IT'S NOT THAT HARD");
				}
				else if (failAmount >= 100) {
					System.out.println("you dont even deserve to play this anymore..");
					System.out.println("uninstall please ):");
					System.exit(0);
				}
				failAmount++;
				
			}
		}
		return val;
	}
	
	
}
