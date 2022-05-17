package world.dungeonBuilder;

import eutil.misc.Direction;
import eutil.random.RandomUtil;

class BuilderBot implements Runnable {

	private int id;
	private DungeonMap map;
	private Thread thread;
	private int startX, startY;
	
	public BuilderBot(int idIn, DungeonMap mapIn, int startX, int startY) {
		id = idIn;
		map = mapIn;
		this.startX = startX;
		this.startY = startY;
	}
	
	public void start() {
		thread = new Thread(this);
		thread.start();
	}
	
	@Override
	public void run() {
		while (map.anyRemaining() && map.inMap(startX, startY)) {
			//branch in all directions
			branch(startX, startY);
		}
	}
	
	private void branch(int x, int y) {
		//System.out.println(id + " : " + map.anyRemaining() + " : " + map.inMap(x, y) + " : (" + x + "," + y + ")");
		
		if (!map.inMap(x, y) || !map.checkSpot(x, y)) {
			map.reduceRemaining();
		}
		else if (map.anyRemaining()) {
			map.claimSpot(x, y, id);
			
			DungeonPiece p = DungeonRoomSquare.createRandom(false);
			map.setSpot(x, y, id, p);
			
			if (map.anyRemaining()) {
				if (RandomUtil.roll(2, 0, 2)) branch(x + 1, y);
				if (RandomUtil.roll(2, 0, 2)) branch(x - 1, y);
				if (RandomUtil.roll(2, 0, 2)) branch(x, y + 1);
				if (RandomUtil.roll(2, 0, 2)) branch(x, y - 1);
			}
		}
	}
	
	private Direction nextDir(Direction lastDir) {
		switch (lastDir) {
		case N: return Direction.E;
		case E: return Direction.S;
		case S: return Direction.W;
		case W: return Direction.N;
		default: return Direction.N;
		}
	}
	
	public void join() throws InterruptedException {
		thread.join();
	}
	
	public int getID() { return id; }
	public Thread getThread() { return thread; }
	
}
