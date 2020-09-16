import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public abstract class Equipment implements JsonReader {
	
	int stackSize;
	String name;
	List<Recipe> specialRecipes = new ArrayList<Recipe>();
	
	public Equipment(int stackSize, String name) {
		this.stackSize = stackSize;
		this.name = name;
	}
	
	boolean isProcessible(Crop crop) {
		try {
			getRecipe(crop);
			return true;
		} catch (NoRecipeException e) {
			return false;
		}
	}
	
	Recipe getRecipe(Crop crop) throws NoRecipeException {
		try {
			return getSpecialRecipe(crop);
		} catch (NoRecipeException e) {
			return getStandardRecipe(crop);
		}
	}
	
	abstract Recipe getStandardRecipe(Crop crop) throws NoRecipeException;
	
	Recipe getSpecialRecipe(Crop crop) throws NoRecipeException {
		List<Entry<String, JsonElement>> recipes = getJson("Artisan") // Get a list of valid recipes for the input crop
				.getAsJsonObject(this.name)
				.entrySet()
				.stream()
				.filter(entry -> entry.getKey().equals("" + crop.getId()))
				.collect(Collectors.toList());
		if (recipes.size() == 0) {
			throw new NoRecipeException(this, crop); // If no valid recipes are found, throw an error
		}
		String[] recipe = recipes.get(0).getValue().getAsString().split("/"); // Isolate recipe
		int outputProductId = Integer.parseInt(recipe[0]); // Record output id as an int for readability
		double processingTime;
		if (this.stackSize == 0) {
			processingTime = 0;
		} else {
			processingTime = Double.parseDouble(recipe[1]);
		}
		return new Recipe(
				crop,
				new Product(
						outputProductId, 
						getName(outputProductId), 
						Integer.parseInt(getJson("ObjectInformation").get(recipe[0])
							.getAsString()
							.split("/")
							[1])),
				Integer.parseInt(recipe[2]),
				Integer.parseInt(recipe[3]),
				processingTime);
	}
	
	@Override
	public String toString() {
		return this.name;
	}

}
