
public class SeedMaker {
	
	public static double getCropSeedRatio(Item crop) {
		switch (crop.getId()) {
			case 433:
				// Coffee Bean
				return 1;
			default:
				return 2;
		}
	}

}
