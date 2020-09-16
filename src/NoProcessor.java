
public class NoProcessor extends Equipment {

	public NoProcessor() {
		super(0, "raw");
	}

	@Override
	Recipe getStandardRecipe(Crop crop) throws NoRecipeException {
		return new Recipe(crop, crop, 1, 1, 0);
	}

}
