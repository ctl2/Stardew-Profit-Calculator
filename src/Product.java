
public class Product extends Item {
	
	int price;

	public Product(int id, String name, int price) {
		super(id, name);
		this.price = price;
	}

	public int getPrice() {
		return price;
	}

	
}
