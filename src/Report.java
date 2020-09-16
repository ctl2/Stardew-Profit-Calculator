import java.util.ArrayList;
import java.util.List;

public class Report {
	
	int days;
	Crop crop;
	int harvests;
	double yield;
	double seedPurchases;
	Recipe process;
	double processorCount;
	int wastedDays;
	
	public Report(int days, Crop crop, int harvests, double yield, double seedPurchases, Recipe process, double processorCount, int wastedDays) {
		this.days = days;
		this.crop = crop;
		this.harvests = harvests;
		this.yield = yield;
		this.seedPurchases = seedPurchases;
		this.process = process;
		this.processorCount = processorCount;
		this.wastedDays = wastedDays;
	}
	
	public long getProfit() {
		return Math.round((this.yield * this.process.getConversionRate() * this.process.getOutput().getPrice() // Produce price
				- this.seedPurchases * this.crop.getSeedCost()) // Minus seed cost
				/ days); // Profit per day
	}
	
	@Override
	public String toString() {
		List<String> report = new ArrayList<String>();
		report.add(this.crop.getName().toUpperCase());
		// Input
		report.add("Input");
		report.add("\tSeeds: " + this.seedPurchases);
		report.add("\t" + (this.seedPurchases * this.crop.getSeedCost()) + "G");
		report.add("\tProcessors: " + this.processorCount);
		// Output
		report.add("Output");
		report.add("\tHarvests: " + this.harvests);
		report.add("\tCrops: " + this.yield);
		report.add("\tProducts: " + (this.yield * this.process.getConversionRate()));
		// Overall
		report.add("Crop Total");
		report.add("\tProfit: " + getProfit());
		report.add("\tWasted days: " + this.wastedDays);
		return report.stream()
				.reduce("", (reportString, line) -> reportString + "\n" + line);
	}
	
}
