
public class Keg extends Equipment {
	
	public Keg() {
		super(1, "keg");
	}

	@Override
	Recipe getStandardRecipe(Crop crop) throws NoRecipeException {
		if (crop.isFruit()) {
			return new Recipe(
					crop,
					new Product(348, "wine", crop.getPrice() * 3), 
					1, 1, 7);
		} else if (crop.isVeg()) {
			return new Recipe(
					crop,
					new Product(348, "juice", (int) (crop.getPrice() * 2.25)), 
					1, 1, 4);
		} else {
			throw new NoRecipeException(this, crop);
		}
	}

}