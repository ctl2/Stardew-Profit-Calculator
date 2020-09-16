
public class OilMaker extends Equipment {
	
	public OilMaker() {
		super(1, "oil maker");
	}

	@Override
	Recipe getStandardRecipe(Crop crop) throws NoRecipeException {
		throw new NoRecipeException(this, crop);
	}

}
