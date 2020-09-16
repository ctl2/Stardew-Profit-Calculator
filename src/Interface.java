import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.google.gson.*;

// To do:
// * Add BeeHouse as an Equipment type
// * Make a greenhouse option (only regrowth time)

public class Interface implements JsonReader, InputReader {

	static final String ESC = "Q";
	private List<Crop> crops;

	public Interface() {
		this.crops = getCrops();
		System.out.println("Enter command 'year', 'season', 'custom', 'greenhouse' or 'all'");
		while(true) {
			Plan plan;
			switch (getInput()) {
				case "year":
					plan = getYearPlan();
					break;
				case "season":
					plan = getSeasonPlan();
					break;
				case "custom":
					plan = getCustomPlan();
					break;
				case "greenhouse":
					plan = getGreehousePlan();
					break;
				case "all":
					printAllProfitsToFile();
					continue;
				default:
					continue;
			}
			try {
				if (plan == null) System.out.println("Functionality not yet implemented.");
				plan.printReport();
			} catch (NoRecipeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private boolean getBooleanInput(String question) {
		boolean bool;
		while (true) {
			System.out.println(question + " Enter y for yes or n for no.");
			switch (getInput()) {
				case "n":
					bool = false;
					break;
				case "y":
					bool = true;
					break;
				default:
					System.err.println("Unrecognised response");
					continue;
			}
			break;
		}
		return bool;
	}

	private void printAllProfitsToFile() {
		int days = getDays();
		List<String> output = new ArrayList<String>();
		String[] seasons = {"spring", "summer", "fall"};
		SubPlan cropPlan = getSubPlan(days, null);
		for (int i = 0; i < 3; i++) {
			output.add(seasons[i].toUpperCase() + "\n");
			// Get crops that only grow in this season
			final int iFinal = i;
			List<Crop> seasonalCrops = crops.stream()
					.filter(crop -> crop.canGrow(iFinal) && !crop.isMultiSeason())
					.collect(Collectors.toList());
			// Get multi-season crops that grow in this season
			List<Crop> multiSeasonCrops = crops.stream()
					.filter(crop -> crop.canGrow(iFinal) && crop.isMultiSeason())
					.collect(Collectors.toList());
			// Print profits
			for (Crop seasonalCrop: seasonalCrops) {
				try {
					cropPlan.setCrop(seasonalCrop);
					output.add(seasonalCrop.getName() + "\t" + cropPlan.getReport().getProfit());
				} catch (NoRecipeException e) {} // Unreachable
			}
			for (Crop multiSeasonCrop: multiSeasonCrops) {
				try {
					cropPlan.setCrop(multiSeasonCrop);
					int growthSeasons = 1;
					for (int j = i - 1; j >= 0; j--) {
						if (multiSeasonCrop.canGrow(j)) {
							cropPlan.addSeason();
							growthSeasons++;
						}
					}
					output.add(multiSeasonCrop.getName() + " (" + growthSeasons + " seasons)\t" + cropPlan.getReport().getProfit());
				} catch (NoRecipeException e) {
					 // Unreachable
					throw new RuntimeException(e);
				}
			}
			output.add("\n");
			// Provide option to water crops day 1 after spring
			if (!cropPlan.isWateredDayOne()) {
				if (i == 0) {
					if (getBooleanInput("Would you like to have crops watered day one after Spring?")) {
						cropPlan.setWateredDayOne(true);
					}
				} else if (i == 1) {
					if (getBooleanInput("Would you like to have crops watered day one after Summer?")) {
						cropPlan.setWateredDayOne(true);
					}
				}
			}
		}
		output.forEach(string -> System.out.println(string));
	}

	private int getDays() {
		int days;
		while(true) {
			System.out.println("Choose a number of days.");
			days = Integer.parseInt(getInput());
			if (days < 0) {
				System.err.println("Please choose a positive number of days.");
				continue;
			}
			break;
		}
		return days;
	}

	private Plan getCustomPlan() {
		int days = getDays();
		// Select a crop to grow
		Crop crop = null;
		while(true) {
			System.out.println("What crop will you grow?");
			String cropName = getInput();
			for (Crop validCrop: this.crops) {
				if (validCrop.equals(cropName)) {
					crop = validCrop;
					break;
				}
			}
			if (crop == null) {
				System.err.println("Unrecognised crop name");
				continue;
			}
			break;
		}
		Plan plan = new Plan();
		plan.addCrop(getSubPlan(days, crop));
		return plan;
	}

	private Plan getGreehousePlan() {
		// TODO Auto-generated method stub
		return null;
	}

	private Plan getSeasonPlan() {
		int seasonIndex;
		while(true) {
			System.out.println("Choose a season index (0, 1 or 2).");
			seasonIndex = Integer.parseInt(getInput());
			if (seasonIndex < 0 || seasonIndex > 2) {
				System.err.println("Invalid season index.");
				continue;
			}
			break;
		}
		Plan plan = new Plan();
		addCropToPlan(plan, seasonIndex);
		return plan;
	}

	private void addCropToPlan(Plan plan, int seasonIndex) {
		String season;
		switch (seasonIndex) {
			case 0:
				season = "spring";
				break;
			case 1:
				season = "summer";
				break;
			default:
				season = "fall";
		}
		Crop crop = null;
		// Select a crop to grow
		while (true) {
			System.out.println("What crop will you grow in " + season + "? Leave blank to continue the previous season's crop.");
			String cropName = getInput();
			if (cropName.equals("")) {
				plan.extendCrop();
				break;
			}
			for (Crop validCrop: this.crops) {
				if (validCrop.equals(cropName)) {
					crop = validCrop;
					break;
				}
			}
			if (crop == null) {
				System.err.println("Unrecognised crop name");
			} else {
				SubPlan subPlan = getSubPlan(28, crop);
				// Confirm plan
				plan.addCrop(subPlan);
				// Advance season loop
				break;
			}
		}
	}

	private SubPlan getSubPlan(int days, Crop crop) {
		// Select a growth speed increase
		int speedIncrease = 0;
		while(true) {
			System.out.println("Enter a percentage growth speed increase.");
			try {
				speedIncrease = Integer.parseInt(getInput());
				if (speedIncrease < 0) {
					System.err.println("Please enter a positive number");
				} else break;
			} catch (NumberFormatException e) {
				System.err.println("Please enter a number");
			}
		}
		// Choose whether watered on day 1
		boolean wateredDayOne = getBooleanInput("Will the crops be watered on day 1?");
		// Choose whether seeds are purchased or produced via seed maker
		boolean seedMaker = true;
		if (crop != null && crop.hasSeedMerchant()) {
			while (true) {
				System.out.println("Will you buy seeds or use seed makers? Enter b for buy or m for seed maker.");
				switch (getInput()) {
					case "b":
						seedMaker = false;
						break;
					case "m":
						seedMaker = true;
						break;
					default:
						System.err.println("Unrecognised response");
						continue;
				}
				break;
			}
		}
		// Choose a processing method
		Equipment processor = null;
		while(true) {
			System.out.println("Enter the processing equipment that will be used (none/mill/keg/preserves jar/oil maker)");
			switch (getInput().toLowerCase().trim()) {
				case "none":
					processor = new NoProcessor();
					break;
				case "mill":
					processor = new Mill();
					break;
				case "keg":
					processor = new Keg();
					break;
				case "preserves jar":
					processor = new PreservesJar();
					break;
				case "oil maker":
					processor = new OilMaker();
					break;
				default:
					System.err.println("Unrecognised equipment name.");
					continue;
			}
			if (crop != null) {
				try {
					processor.getRecipe(crop);
				} catch (NoRecipeException e) {
					System.err.println(e.getMessage());
					continue;
				}
			}
			break;
		}
		return new SubPlan(days, crop, speedIncrease, wateredDayOne, seedMaker, processor);
	}

	private Plan getYearPlan() {
		Plan plan = new Plan();
		for (int i = 0; i < 3; i++) {
			addCropToPlan(plan, i);
		}
		return plan;
	}

	private String getCropName(String[] cropsArray, JsonObject idsJson) {
		return idsJson
				.get(cropsArray[3]) // Use the harvested crop index (key is the seed index)
				.getAsString()
				.split("/")
				[0];
	}

	private int getGrowthTime(String[] cropsArray) {
		// Get times as strings
		String[] growthTimesStrings = cropsArray[0].split(" ");
		// Convert to ints and sum
		int growthTime = 0;
		for (int i = 0; i < growthTimesStrings.length; i++) {
			growthTime += Integer.parseInt(growthTimesStrings[i]);
		}
		// Return total growth time
		return growthTime;
	}

	private Boolean[] getSeasonBools(String[] cropsArray) {
		Boolean[] seasonBools = {false, false, false};
		String[] seasons = cropsArray[1].split(" ");
		for (String season: seasons) {
			switch(season) {
				case "spring":
					seasonBools[0] = true;
					break;
				case "summer":
					seasonBools[1] = true;
					break;
				case "fall":
					seasonBools[2] = true;
					break;
			}
		}
		return seasonBools;
	}

	private int getRegrowthTime(String[] cropsArray) {
		return Integer.parseInt(cropsArray[4]); // No regrowth indicated by a value of -1
	}

	private double getAverageYield(String[] cropsArray) {
		String[] yieldData = cropsArray[6].split(" ");
		if (yieldData.length == 1) return 1d;
		return Integer.parseInt(yieldData[1]) + Double.parseDouble(yieldData[4]); // Base yield + chance for one extra crop.
	}

	private int getSeedCost(String seedKey, JsonObject idsJson) {
		switch (seedKey) {
		case "433": // Coffee beans
			return 0; // Not sold
		case "431": // Sunflower seeds
			return 200; // 200 at Pierre's, 100 at JojaMart
		case "745": // Strawberry seeds
			return 100; // 100 at the Egg Festival
		case "499": // Ancient seeds
			return 0; // Not sold
		case "347": // Rare seeds
			return 0; // Not viable to buy them from the Traveling Cart
		default:
			return getPrice(seedKey, idsJson) * 2; // Seeds are normally sold for 2 times their base value
		}
	}

	private int getPrice(String key, JsonObject idsJson) {
		return Integer.parseInt(idsJson
				.get(key)
				.getAsString()
				.split("/")
				[1]);
	}

	private int getCategory(String cropKey, JsonObject idsJson) {
		return Integer.parseInt(idsJson
				.get(cropKey)
				.getAsString()
				.split("/")
				[3]
				.split(" ")
				[1]);
	}

	private Crop getCrop(String seedKey, JsonObject cropsJson, JsonObject idsJson) {
		String[] cropsArray = cropsJson
				.get(seedKey)
				.getAsString()
				.split("/");
		String cropKey = cropsArray[3];
		return new Crop(
				Integer.parseInt(cropKey),
				getCropName(cropsArray, idsJson),
				getSeasonBools(cropsArray),
				getSeedCost(seedKey, idsJson),
				getGrowthTime(cropsArray),
				getRegrowthTime(cropsArray),
				getAverageYield(cropsArray),
				getPrice(cropKey, idsJson),
				getCategory(cropKey, idsJson));
	}

	private List<Crop> getFilteredCrops(List<Crop> unfilteredCrops) {
		// Remove crops that can't be effectively farmed
		return unfilteredCrops.stream()
			.filter(crop ->
				!crop.getName().equals("Wild Horseradish") &&
				!crop.getName().equals("Spice Berry") &&
				!crop.getName().equals("Common Mushroom") &&
				!crop.getName().equals("Winter Root") &&
				!crop.getName().equals("Cactus Fruit"))
			.collect(Collectors.toList());
	}

	private List<Crop> getCrops() {
		// Access Stardew Valley data files
		JsonObject cropsJson = getJson("Crops");
		JsonObject idsJson = getJson("ObjectInformation");
		// Return a list of Crop objects
		return getFilteredCrops(cropsJson.keySet()
				.stream()
				.map(key -> getCrop(key, cropsJson, idsJson))
				.collect(Collectors.toList()));
	}

	public static void main(String[] args) throws IOException {
		Interface i = new Interface();
	}
}
