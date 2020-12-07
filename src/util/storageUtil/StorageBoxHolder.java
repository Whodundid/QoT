package util.storageUtil;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import util.EUtil;

//Author: Hunter Bragg

/** Essentially an ArrayList implementation that holds StorageBoxes. */
public class StorageBoxHolder<A, B> implements Iterable<StorageBox<A, B>> {
	
	EArrayList<StorageBox<A, B>> createdList = new EArrayList();
	public boolean allowDuplicates = false;
	
	/** Creates a new StorageBoxHolder that does allow duplicate object entries. */
	public StorageBoxHolder() { this(true); }
	
	/** Creates a new StorageBoxHolder in which duplicates can be allowed or not. */
	public StorageBoxHolder(boolean allowDuplicatesIn) {
		allowDuplicates = allowDuplicatesIn;
	}
	
	public StorageBoxHolder(A obj, B val) {
		this(true);
		add(obj, val);
	}
	
	public StorageBoxHolder(boolean allowDuplicatesIn, A obj, B val) {
		this(allowDuplicatesIn);
		add(obj, val);
	}
	
	public StorageBoxHolder(StorageBoxHolder<A, B> holderIn) {
		this(true);
		holderIn.forEach(s -> add(s.getA(), s.getB()));
	}
	
	/** Creates a new StorageBox with the given A and B values and then adds it to the specified position of this holder. */
	public void add(int pos, A obj, B value) {
		if (allowDuplicates || !contains(obj)) { createdList.add(pos, new StorageBox<A, B>(obj, value)); }
	}
	
	public boolean add() { return add(null, null); }
	
	/** Creates a new StorageBox with the given A and B values and then adds it to the end of this holder. */
	public boolean add(A obj, B value) {
		return (allowDuplicates || !contains(obj)) ? createdList.add(new StorageBox<A, B>(obj, value)) : false;
	}
	
	/** Adds the specified box if it is not null to this StorageBoxHolder. */
	public boolean add(StorageBox<A, B> boxIn) {
		return (boxIn != null && (allowDuplicates || !contains(boxIn))) ? createdList.add(boxIn) : false;
	}
	
	public boolean addIf(boolean condition, A obj, B value) { if (condition) { return add(obj, value); } return false; }
	
	public boolean addIf(boolean condition, StorageBox<A, B> boxIn) { if (condition) { return add(boxIn); } return false; }
	
	/** Adds all the boxes from one StorageBoxHolder into this one. */
	public StorageBoxHolder<A, B> addAll(StorageBoxHolder<A, B> in) {
		if (in != null) { in.forEach(b -> add(b)); }
		return this;
	}
	
	/** Adds all of the boxes from a list into this StorageBoxHolder. */
	public StorageBoxHolder<A, B> addAll(List<StorageBox<A, B>> listIn) {
		if (listIn != null) { listIn.forEach(b -> add(b)); }
		return this;
	}
	
	/** Adds if the object does not already exist, or updates the B value of the existing box with the given A value. */
	public void put(A obj, B value) {
		StorageBox<A, B> box = this.getBoxWithA(obj);
		if (box != null) { box.setB(value); }
		else { add(obj, value); }
	}
	
	/** Removes every box that contains the given A value. */
	public List<StorageBox<A, B>> removeBoxesContainingA(A obj) {
		List<StorageBox<A, B>> returnList = new EArrayList();
		Iterator<StorageBox<A, B>> i = createdList.iterator();
		while (i.hasNext()) {
			StorageBox<A, B> getBox = i.next();
			if (getBox.contains(obj)) { returnList.add(getBox); i.remove(); }
		}
		return returnList;
	}
	
	/** Removes every box that has the exact A and B values. */
	public List<StorageBox<A, B>> removeBoxesWithSaidValues(A obj1, B obj2) {
		List<StorageBox<A, B>> returnList = new EArrayList();
		Iterator<StorageBox<A, B>> i = createdList.iterator();
		while (i.hasNext()) {
			StorageBox<A, B> getBox = i.next();
			if (getBox.compare(obj1, obj2)) { returnList.add(getBox); i.remove(); }
		}
		return returnList;
	}
	
	/** Retrieves the first box that contains the specified A value. */
	public StorageBox<A, B> getBoxWithA(A objIn) {
		for (StorageBox<A, B> getBox : createdList) {
			if (getBox.containsA(objIn)) { return getBox; }
		}
		return null;
	}
	
	/** Retrieves all boxes that contain the specified A value. */
	public List<StorageBox<A, B>> getAllBoxesWithA(A obj) {
		return createdList.stream().filter(b -> b.getA().equals(obj)).collect(Collectors.toList());
	}
	
	/** Replaces an in a box with the given A value with the specified new A value.
	 *  If the box does not exist, nothing is added and nothing is modified. */
	public StorageBoxHolder<A, B> setAInBox(A obj, A newObj) {
		StorageBox<A, B> box = getBoxWithA(obj);
		if (box != null) { box.setA(newObj); }
		return this;
	}
	
	/** Replaces a value in a box with the given A value with the specified new B value.
	 *  If the box does not exist, nothing is added and nothing is modified. */
	public StorageBoxHolder<A, B> setBInBox(A obj, B newVal) {
		StorageBox<A, B> box = getBoxWithA(obj);
		if (box != null) { box.setB(newVal); }
		return this;
	}
	
	/** Returns true if this holder has any box with the specified A value. */
	public boolean contains(A obj) {
		for (StorageBox<A, B> getBox : createdList) {
			if (getBox.contains(obj)) { return true; }
		}
		return false;
	}
	
	public boolean contains(StorageBox<A, B> boxIn) {
		for (StorageBox<A, B> b : createdList) {
			if (b.compare(boxIn)) { return true; }
		}
		return false;
	}
	
	/** Returns true if this holder has any box with both the specified A and B pair. */
	public boolean containsABoxWithBoth(A obj1, B obj2) {
		for (StorageBox<A, B> getBox : createdList) {
			if (getBox.compare(obj1, obj2)) { return true; }
		}
		return false;
	}
	
	/** Returns a list of every A value in each box. */
	public EArrayList<A> getAVals() {
		return createdList.stream().map(StorageBox::getA).collect(EArrayList.toEArrayList());
	}
	
	/** Returns a list of every B value in each box. */
	public EArrayList<B> getBVals() {
		return createdList.stream().map(StorageBox::getB).collect(EArrayList.toEArrayList());
	}
	
	/** Returns a list containing every box in this holder. */
	public EArrayList<StorageBox<A, B>> getBoxes() {
		return new EArrayList<StorageBox<A, B>>(createdList);
	}
	
	/** Returns the boxes of this holder within an array of StorageBox objects with the corresponding parameters. */
	public StorageBox<A, B>[] getBoxesAsArray() {
		return (StorageBox<A, B>[]) EUtil.asArray(getBoxes().toArray(new StorageBox[0]));
	}
	
	/** Returns the B value of a box with the specified object. */
	public B getB(A objIn) {
		StorageBox<A, B> b = getBoxWithA(objIn);
		return b != null ? b.getB() : null;
	}
	
	//static methods
	
	/** Static method used to create a new StorageBoxHolder parametized with the given object and value types for each list.
	 *  If values are to be passed, they must be passed in list objects, and each list must have the same size. If both lists
	 *  are null, an empty parametized holder with be returned. If one list is null and the other is not, nothing is returned. */
	public static <thing1, thing2> StorageBoxHolder<thing1, thing2> createBox(List<thing1> objectsIn, List<thing2> valuesIn) {
		if (objectsIn != null && valuesIn != null) {
			if (objectsIn.size() == valuesIn.size()) {
				StorageBoxHolder<thing1, thing2> newHolder = new StorageBoxHolder();
				for (int i = 0; i < objectsIn.size(); i++) { newHolder.add(objectsIn.get(i), valuesIn.get(i)); }
				return newHolder;
			}
		}
		else if (objectsIn == null && valuesIn == null) {
			return new StorageBoxHolder<thing1, thing2>();
		}
		return null;
	}
	
	/** Static method used to create a new StorageBoxHolder parametized with the given object and value types.
	 *  A variable sized list of argument is passed to initialize the values of this holder. For every argument
	 *  passed, a corresponding value must also be passed along with it so that is adheres to the <Object, Value>
	 *  relationship. */
	public static <T, V> StorageBoxHolder<T, V> of(Class<T> tType, Class<V> vType, Object... dataIn) {
		if (dataIn.length % 2 == 0) {
			StorageBoxHolder<T, V> newHolder = new StorageBoxHolder();
			for (int i = 0; i < dataIn.length; i += 2) { newHolder.add((T) dataIn[i], (V) dataIn[i + 1]); }
			return newHolder;
		}
		return null;
	}
	
	/** Sets this box to not have duplicates and procedes to purge any and all duplicates from this holder. */
	public StorageBoxHolder<A, B> noDuplicates() { allowDuplicates = false; purgeDuplicates(this); return this; }
	
	/** Sets this box to have duplicates or not. If no, all duplicates are purged from this holder. */
	public StorageBoxHolder<A, B> setAllowDuplicates(boolean val) { allowDuplicates = val; if (!allowDuplicates) { purgeDuplicates(this); } return this; }
	
	/** Internal function used to remove duplicates from a specified holder. */
	private static void purgeDuplicates(StorageBoxHolder holderIn) {
		if (holderIn != null) {
			EArrayList<StorageBox> noDups = new EArrayList();
			
			Iterator<StorageBox> it = holderIn.iterator();
			while (it.hasNext()) {
				StorageBox box = it.next();
				if (box != null) {
					boolean contains = false;
					for (StorageBox b : noDups) { if (StorageBox.compare(box, b)) { contains = true; break; } }
					if (!contains) { noDups.add(box); }
				}
				it.remove();
			}
			
			holderIn.addAll(noDups);
		}
	}
	
	/** Returns the total number of boxes in this holder. */
	public int size() { return createdList.size(); }
	
	/** Same as size() but just adds a call to unify language across Strings, Arrays, and now lists. */
	public int length() { return createdList.size(); }
	
	/** Returns the box at the specified point number. */
	public StorageBox<A, B> get(int pointNumber) { return createdList.get(pointNumber); }
	
	/** Returns true if this holder does not contain any boxes. */
	public boolean isEmpty() { return createdList.isEmpty(); }
	
	/** Returns true if this holder does contain boxes. */
	public boolean isNotEmpty() { return !createdList.isEmpty(); }
	
	/** Removes every box, along with the contents of each box, from this holder. */
	public void clear() { this.createdList.clear(); }
	
	/** Removes the box at the specified point number. */
	public boolean remove(int pointNumber) { return createdList.remove(pointNumber) != null; }
	
	/** Returns the object from the box at the specified point number. */
	public A getObject(int pointNumber) { return createdList.get(pointNumber).getA(); }
	
	/** Returns the value from the box at the specified point number. */
	public B getValue(int pointNumber) { return createdList.get(pointNumber).getB(); }
	
	//object overrides
	
	@Override public Iterator<StorageBox<A, B>> iterator() { return createdList.iterator(); }
	
	@Override
	public String toString() {
		String returnString = "[";
		for (int i = 0; i < createdList.size(); i++) {
			returnString += ("(" + getObject(i) + ", " + getValue(i) + (i == createdList.size() - 1 ? ")" : "), "));
		}
		returnString += "]";
		return returnString;
	}
	
}
