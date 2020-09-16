
public class NoRecipeException extends Exception {
	
	NoRecipeException(Equipment equipment, Crop crop) {
		super(equipment + " is unable to process " + crop + ".");
	}

}
