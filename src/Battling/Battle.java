package Battling;

import java.util.Scanner;
import Entities.Enemy;
import Entities.Player;
import Entities.Whodundid;
import Sound.Songs;

public class Battle {
	
	private static int failAmount = 0;
	
	public Battle(Player player1, Enemy enemy1) {
		System.out.println("You encountered a " + enemy1.getName() + "! They are level " + enemy1.getLevel() + " with " + enemy1.getHealth() + " HP and " + enemy1.getMana() + "!");
		battle(player1, enemy1);
	}
	
	public static void battle(Player player1, Enemy enemy1) {
		Songs.playSong((enemy1 instanceof Whodundid) ? Songs.darkCave : Songs.nightfall);
		
		while (!player1.isDead && !enemy1.isDead) {
			int command = printActions(player1, enemy1);
			failAmount = 0;
			
			if (player1.health <= 0 || enemy1.health <= 0) {
				break;
			}
			
			if (!player1.isDead) {
				switch (command) {
				case 0: enemy1.hurt(player1.getDamage()); break;
				case 1: player1.useFlame(enemy1); break;
				case 2:
					enemy1.hurt(player1.flameDamage);
					player1.useHeal();
					break;
				case 3:
				default: System.out.println("You chose to do nothing...");
				}
			}
			
			if (!enemy1.isDead) { player1.hurt(enemy1.getDamage()); }
			System.out.println();
		}

		if (player1.isDead) {
			System.out.println("You got rekt son.");
		}
		else if (enemy1.isDead) {
			System.out.println("Victory!");
		}
		
		Songs.stopSong((enemy1 instanceof Whodundid) ? Songs.darkCave : Songs.nightfall);
		
	}
	
	private static int printActions(Player p, Enemy e) {
		System.out.println("You have " + p.health + " HP and " + p.mana + " MP " + e.name + " has " + e.health + " HP.");
		System.out.println("Type 0 to attack the enemy.");
		System.out.println("Type 1 to use fire on the enemy");
		System.out.println("Type 2 to cast 'heal'");
		System.out.println("Type 3 to do absolutely nothing.");
		
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
