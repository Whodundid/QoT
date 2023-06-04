package envision.engine.terminal.commands.categories.game;

import envision.Envision;
import envision.engine.terminal.commands.TerminalCommand;
import eutil.colors.EColors;
import eutil.datatypes.util.EList;
import eutil.math.ENumUtil;
import qot.items.Items;

public class CMD_GiveItem extends TerminalCommand {
	
	public CMD_GiveItem() {
		setCategory("Game");
		expectedArgLength = 1;
	}

	@Override public String getName() { return "giveitem"; }
	@Override public EList<String> getAliases() { return EList.of("gi"); }
	@Override public String getHelpInfo(boolean runVisually) { return "Give the entitiy the item"; }
	@Override public String getUsage() { return "ex: giveitem player 0"; }
	
	@Override
	public void runCommand() {
		if (Envision.thePlayer == null) {
			error("There is no player!");
			return;
		}
		
		var items = Items.getAllItems();
		
		if (args().isEmpty()) {
			int i = 0;
			for (var item : items) {
				term.writeln(EColors.skyblue + "" + i + EColors.yellow + " : " + EColors.green + item.getName());
				i++;
			}
			return;
		}
		
		expectAtLeast(1);
		var target = Envision.thePlayer;
		
		var input = ENumUtil.parseInt(arg(0), -1);
		
		if (input < 0 || input >= items.size()) {
			error("Item index out of range!");
			return;
		}
		
		//toggle
		var item = items.get(input);
		target.giveItem(item);
		term.writeln(EColors.green + "Giving item: '" + EColors.mc_gold + item.getName() +
					 EColors.green + "' to '" + EColors.mc_gold + target.getName() + EColors.green + "'");
	}
	
}
