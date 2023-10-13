package envision.debug.debugCommands;

import java.util.PriorityQueue;

import envision.engine.terminal.window.ETerminalWindow;
import qot.entities.Cell;

@SuppressWarnings("unused")
public class Deb18 extends DebugCommand {

	@Override
	public void run(ETerminalWindow termIn, Object... args) {
		var queue = new PriorityQueue<Cell>((a, b) -> (int) (a.f - b.f));
		
		var c1 = new Cell();
		var c2 = new Cell();
		var c3 = new Cell();
		var c4 = new Cell();
		var c5 = new Cell();
		var c6 = new Cell();
		var c7 = new Cell();
		
		c1.f = 0;
		c2.f = 32;
		c3.f = 76;
		c4.f = 45;
		c5.f = 96;
		c6.f = 923;
		c7.f = 1;
		
		queue.add(c1);
		queue.add(c2);
		queue.add(c3);
		queue.add(c4);
		queue.add(c5);
		queue.add(c6);
		queue.add(c7);
		
		while (!queue.isEmpty()) {
		    termIn.writeln(queue.poll());
		}
	}

}