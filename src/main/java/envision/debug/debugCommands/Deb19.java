package envision.debug.debugCommands;

import envision.Envision;
import envision.engine.creation.EnvisionCodeBlock;
import envision.engine.creation.block.blockTypes.AndBlock;
import envision.engine.creation.block.blockTypes.BufferBlock;
import envision.engine.creation.block.blockTypes.ConstBooleanBlock;
import envision.engine.creation.block.blockTypes.ConstNumberBlock;
import envision.engine.creation.block.blockTypes.ConstStringBlock;
import envision.engine.creation.block.blockTypes.DisplayBlock;
import envision.engine.creation.block.blockTypes.EqualityBlock;
import envision.engine.creation.block.blockTypes.InverterBlock;
import envision.engine.creation.block.blockTypes.OrBlock;
import envision.engine.creation.block.blockTypes.XorBlock;
import envision.engine.terminal.window.ETerminalWindow;
import eutil.math.ENumUtil;

@SuppressWarnings("unused")
public class Deb19 extends DebugCommand {

	@Override
	public void run(ETerminalWindow termIn, Object... args) {
	    if (args.length == 0 || args.length > 1) {
	        termIn.writeln("Creates a block of type (arg)");
	        termIn.writeln("  0. buffer");
	        termIn.writeln("  1. const bool");
	        termIn.writeln("  2. const number");
	        termIn.writeln("  3. const string");
	        termIn.writeln("  4. equality");
	        termIn.writeln("  5. display");
	        termIn.writeln("  6. inverter");
	        termIn.writeln("  7. and");
	        termIn.writeln("  8. or");
	        termIn.writeln("  9. xor");
	        termIn.writeln(" 10. envision");
	        return;
	    }
	    
		Object arg = args[0];
		int value = -1;
		if (arg instanceof String s) value = ENumUtil.parseInt(s, -1);
		else if (arg instanceof Number n) value = n.intValue();
		var top = Envision.getActiveTopParent();
		
		switch (value) {
		case 0: top.displayWindow(new BufferBlock()); break;
		case 1: top.displayWindow(new ConstBooleanBlock()); break;
		case 2: top.displayWindow(new ConstNumberBlock()); break;
		case 3: top.displayWindow(new ConstStringBlock()); break;
		case 4: top.displayWindow(new EqualityBlock()); break;
		case 5: top.displayWindow(new DisplayBlock()); break;
		case 6: top.displayWindow(new InverterBlock()); break;
		case 7: top.displayWindow(new AndBlock()); break;
		case 8: top.displayWindow(new OrBlock()); break;
		case 9: top.displayWindow(new XorBlock()); break;
		case 10: top.displayWindow(new EnvisionCodeBlock()); break;
		default:
		    termIn.writeln("INVALID TYPE!");
		}
	}

}