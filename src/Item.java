
public abstract class Item {

	int id;
	String name;
	
	public Item(int id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return this.name;
	}
	
}
