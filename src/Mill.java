
public class Mill extends Equipment {
	
	public Mill() {
		super(0, "mill");
	}

	@Override
	Recipe getStandardRecipe(Crop crop) throws NoRecipeException {
		throw new NoRecipeException(this, crop);
	}

}
