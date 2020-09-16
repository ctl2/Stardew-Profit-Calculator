
public class SubPlan {

	private int days = 14;
	private int seasons = 1;
	private Crop crop;
	private int speedIncrease;
	private boolean wateredDayOne;
	private boolean seedMaker;
	private Equipment processor;

	public SubPlan(int days, Crop crop, int speedIncrease, boolean wateredDayOne, boolean seedMaker, Equipment processor) {
		this.days = days;
		this.crop = crop;
		this.speedIncrease = speedIncrease;
		this.wateredDayOne = wateredDayOne;
		this.seedMaker = seedMaker;
		this.processor = processor;
	}
	
	public void setCrop(Crop crop) {
		this.crop = crop;
		this.seasons = 1;
	}
	
	public boolean isWateredDayOne() {
		return this.wateredDayOne;
	}
	
	public void setWateredDayOne(boolean watered) {
		this.wateredDayOne = true;
	}

	public void addSeason() {
		this.seasons++;
	}

	Report getReport() throws NoRecipeException {
		// Calculate seed cost and crop yield
		int totalDays = this.days + (this.seasons - 1) * 28;
		int remainingDays = this.seasons * this.days;
		if (!this.wateredDayOne) remainingDays--;
		int harvests = 0;
		double yield = 0;
		int seeds = 1;
		int nextGrowthTime = crop.getGrowthTime() * (100 - this.speedIncrease) / 100;
		while ((remainingDays -= nextGrowthTime) >= 0) { // Consume growth time
			// Simulate harvest
			harvests++;
			yield += crop.getAverageYield();
			if (crop.canRegrow()) {
				// Simulate regrowth
				nextGrowthTime = crop.getRegrowthTime();
			} else {
				// Simulate replanting
				if (crop.getId() == 431) { // Sunflower
					seeds += 0.175; // An average of 0.825 seeds are regained upon harvesting
				} else {
					seeds++;
				}
			}
		}
		if (!crop.canRegrow()) {
			seeds--; // Last harvest failed so no point in buying the seeds
		}
		if (this.seedMaker || !this.crop.hasSeedMerchant()) {
			yield -= seeds / SeedMaker.getCropSeedRatio(this.crop); // Reduced yield from seed maker usage
			seeds = 0; // No seed cost
		}
		Recipe recipe = null;
		recipe = this.processor.getRecipe(this.crop);
		return new Report(totalDays, crop, harvests, yield, seeds, recipe, recipe.getRequiredQuant(yield, totalDays), remainingDays + nextGrowthTime);
	}
	
	public Crop getCrop() {
		return this.crop;
	}

}
