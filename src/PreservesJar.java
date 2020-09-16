
public class PreservesJar extends Equipment {
	
	public PreservesJar() {
		super(1, "preserves jar");
	}

	@Override
	Recipe getStandardRecipe(Crop crop) throws NoRecipeException {
		if (crop.isFruit()) {
			return new Recipe(
					crop,
					new Product(344, "jelly", crop.getPrice() * 2 + 50), 
					1, 1, 3);
		} else if (crop.isVeg()) {
			return new Recipe(
					crop,
					new Product(342, "pickles", (int) (crop.getPrice() * 2 + 50)), 
					1, 1, 3);
		} else {
			throw new NoRecipeException(this, crop);
		}
	}

}