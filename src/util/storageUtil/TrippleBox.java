package util.storageUtil;

import util.EUtil;

public class TrippleBox<Obj1, Obj2, Obj3> {

	public Obj1 o1;
	public Obj2 o2;
	public Obj3 o3;
	
	public TrippleBox() { this(null, null, null); }
	public TrippleBox(Obj1 one, Obj2 two, Obj3 three) { set(one, two, three); }
	public TrippleBox(TrippleBox<Obj1, Obj2, Obj3> box) { EUtil.nullDo(box, b -> set(b.o1, b.o2, b.o3)); }
	
	public boolean compare(TrippleBox<Obj1, Obj2, Obj3> box) {
		if (box != null) {
			return EUtil.isEqual(box.o1, o1) && EUtil.isEqual(box.o2, o2) && EUtil.isEqual(box.o3, o3);
		}
		return false;
	}
	
	public TrippleBox set(Obj1 one, Obj2 two, Obj3 three) {
		o1 = one;
		o2 = two;
		o3 = three;
		return this;
	}
	
	@Override public String toString() { return "[" + o1 + ", " + o2 + ", " + o3 + "]"; }
	
}
