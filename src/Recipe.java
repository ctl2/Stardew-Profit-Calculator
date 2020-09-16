
public class Recipe {

	Crop input;
	Product output;
	int inputQuant;
	int outputQuant;
	double processingDays;
	
	public Recipe(Crop input, Product output, int inputQuant, int outputQuant, double processingDays) {
		this.input = input;
		this.output = output;
		this.inputQuant = inputQuant;
		this.outputQuant = outputQuant;
		this.processingDays = processingDays;
	}
	
	public double getRequiredQuant(double yield, int totalDays) {
		return yield / inputQuant * processingDays / totalDays; // Maybe wrong
	}
	
	public double getConversionRate() {
		return (double) this.outputQuant / this.inputQuant;
	}

	public Crop getInput() {
		return input;
	}

	public Product getOutput() {
		return output;
	}

	public double getProcessingDays() {
		return processingDays;
	}
	
	

}
