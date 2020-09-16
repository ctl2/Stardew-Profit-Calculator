
public class Crop extends Product {

	private Boolean[] seasonBools;
	private int seedCost;
	private int growthTime;
	private int regrowthTime;
	private double averageYield;
	private int category;
	
	public Crop(int id, String name, Boolean[] seasonBools, int seedCost, int growthTime, int regrowthTime,
			double averageYield, int price, int category) {
		super(id, name, price);
		this.seasonBools = seasonBools;
		this.seedCost = seedCost;
		this.growthTime = growthTime;
		this.regrowthTime = regrowthTime;
		this.averageYield = averageYield;
		this.category = category;
	}

	public boolean canGrow(int seasonIndex) {
		return this.seasonBools[seasonIndex];
	}
	
	public int getTotalGrowthSeasons() {
		int totalGrowthSeasons = 0;
		for (boolean bool: this.seasonBools) {
			if (bool) totalGrowthSeasons++;
		}
		return totalGrowthSeasons;
	}

	public boolean isMultiSeason() {
		boolean foundOne = false;
		for (boolean bool: this.seasonBools) {
			if (bool) {
				if (foundOne) return true;
				foundOne = true;
			}
		}
		return false;
	}
	
	public boolean hasSeedMerchant() {
		return this.seedCost > 0;
	}

	public int getSeedCost() {
		return seedCost;
	}
	
	public int getGrowthTime() {
		return growthTime;
	}
	
	public boolean canRegrow() {
		return this.regrowthTime != -1;
	}

	public int getRegrowthTime() {
		return regrowthTime;
	}

	public double getAverageYield() {
		return averageYield;
	}
	
	public int getPrice() {
		return this.price;
	}
	
	public boolean isFruit() {
		return this.category == -79;
	}
	
	public boolean isVeg() {
		return this.category == -75;
	}

	@Override
	public boolean equals(Object cropName) {
		return this.name.toLowerCase().equals(((String) cropName).toLowerCase());
	}

}
