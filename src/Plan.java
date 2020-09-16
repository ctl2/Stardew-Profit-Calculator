
public class Plan {
	
	SubPlan[] subPlans = new SubPlan[3]; // May contain 1, 2 or 3 elements
	int curPlanIndex = -1;
	int seasonIndex = 0;
	
	public Plan() {
		
	}

	public void addCrop(SubPlan subPlan) {
		subPlans[++curPlanIndex] = subPlan;
		seasonIndex++;
	}
	
	public void extendCrop() {
		if (curPlanIndex != -1) {
			if (subPlans[curPlanIndex].getCrop().canGrow(seasonIndex)) subPlans[curPlanIndex].addSeason();
		}
		seasonIndex++;
	}
	
	public void printReport() throws NoRecipeException {
		int profit = 0;
		for (SubPlan subPlan: this.subPlans) {
			if (subPlan == null) break;
			Report cropReport = subPlan.getReport();
			profit += cropReport.getProfit();
			System.out.println(cropReport.toString());
			System.out.println();
		}
		System.out.println("Year Total Profit: " + profit);
	}
	

}
