// NOTE : This is a conceptual design and values are not as accurate as final design.
// References:
// [1]"Home : Pay and Conditions : Department of Defence", Defence.gov.au, 2020. [Online]. Available: https://www.defence.gov.au/payandconditions/. [Accessed: 18- Apr- 2020].
// [2]"Income tax: Australian tax brackets and rates (2019/2020 and 2018/2019)", SuperGuide, 2020. [Online]. Available: https://www.superguide.com.au/boost-your-superannuation/income-tax-rates. [Accessed: 18- Apr- 2020].
// [3]"Factsheets and publications", Csc.gov.au, 2020. [Online]. Available: https://www.csc.gov.au/Members/Advice-and-resources/Factsheets-and-publications/. [Accessed: 18- Apr- 2020].

public class IncomeCalcMVP {
	private GTerm gt;

	private double salary;
	private double taxedAllowances;
	private double nonTaxedAllowances;
	private double liveInContribution;
	private double liveOffContributionDHA;
	private double rentalAllowance;
	private double superContribution;
	private double yearTax;

	public IncomeCalcMVP() {

		this.gt = new GTerm(500, 700);
		this.gt.setFontSize(15);

		// This can be achieved by turning into methods to avoid clutter
		// in the constructor however this is just the MVP version and
		// I have chosen to just put the "build" of the interface
		// directly into the constructor to avoid too many method calls.
		this.gt.println("Salary Calculator (MVP)");
		this.gt.println("");
		this.gt.addTextArea("", 150, 30);
		this.gt.addButton("Add Base Salary", this, "salary");
		this.gt.println("");
		this.gt.println("Allowances");
		this.gt.addButton("Service", this, "pressServiceAllowance");
		this.gt.addButton("Uniform", this, "pressUniformAllowance");
		this.gt.addButton("MWD Seperation", this, "pressSeparationAllowance");
		this.gt.println("");
		this.gt.addButton("SF Tier 1", this, "sfTierSupport1C");
		this.gt.addButton("SF Tier 2", this, "sfTierSupport2");
		this.gt.println("");

		this.gt.println("What is your current living situation");
		this.gt.addButton("Live On", this, "liveIn");
		this.gt.addButton("Live Off (DHA)", this, "liveOffDHA");
		this.gt.addButton("Live Off (RA)", this, "liveOffRA");
		this.gt.println("");

		this.gt.println("Enter your current employee superannuation rate");
		this.gt.println("example: 5 = 5%");
		this.gt.addTextArea("", 150, 30);
		this.gt.addButton("Add Super Rate", this, "superRate");
		this.gt.println("");

		this.gt.println("Annual Summary");
		this.gt.addTextArea("", 300, 200);
		this.gt.addButton("Calculate", this, "calculate");
		this.gt.println("");
		this.gt.addList(300, 100, null, null);
	}

	public void salary() {
		// Retrieves salary from text field and populates list
		// to track the values selected or entered.
		this.salary = Double.parseDouble(this.gt.getTextFromEntry(0));
		this.gt.setTextInEntry(0, "");
		this.gt.addElementToList(0, "Salary set to: " + this.salary);
	}

	public void liveIn() {
		// Live In contributions + utilities (standard rate)
		this.liveInContribution = 224.76 + 48.87;
		// Reset other values to avoid messing up calculations
		this.liveOffContributionDHA = 0;
		this.rentalAllowance = 0;
		this.gt.addElementToList(0, "Live In selected");
	}

	public void liveOffDHA() {
		// Sets to average rate for Holsworthy area
		this.liveOffContributionDHA = 504.91 + 22.40;
		this.liveInContribution = 0;
		this.rentalAllowance = 0;
		this.gt.addElementToList(0, "Live Off DHA selected");
	}

	public void liveOffRA() {
		this.liveInContribution = 0;
		this.liveOffContributionDHA = 0;
		// To avoid complication of code, ceiling rate preset for Liverpool area.
		this.gt.showMessageDialog("Rental contribution has been set to the rate of Liverpool area at $1066");
		double rentCeiling = 1066;
		double rentPerFortnight = Double.parseDouble(this.gt.getInputString("Please enter rent per fortnight:"));
		double rentDifference = 0;
		if (rentPerFortnight > rentCeiling) {
			rentDifference = rentPerFortnight - rentCeiling;
		} else
			rentDifference = 0;

		this.rentalAllowance = rentPerFortnight - rentDifference - 454.42;
		this.gt.addElementToList(0, "RA selected" + this.rentalAllowance);
	}

	public void superRate() {
		double superRate = Integer.parseInt(this.gt.getTextFromEntry(1));
		this.gt.setTextInEntry(1, "");
		superRate = superRate / 100;
		this.superContribution = (this.salary + 14555.9825) * superRate;
		this.gt.addElementToList(0, "Super rate set: " + superRate);
	}

	public void tax() {
		double taxedSalary = this.salary + this.taxedAllowances;
		double taxRate = 0;

		if (taxedSalary < 90000) {
			taxRate = 0.325;

		} else if (taxedSalary > 90000) {
			taxRate = 0.37;
		}

		if (taxRate == 0.325) {
			this.yearTax = ((taxedSalary - 37000) * taxRate) + 3572;
		} else if (taxRate == 0.37) {
			this.yearTax = ((taxedSalary - 90000) * taxRate) + 20797;
		}

	}

	public void calculate() {
		double gross = this.salary + this.taxedAllowances + this.nonTaxedAllowances + (this.rentalAllowance * 26.0714);
		this.tax();
		double tax = this.yearTax;
		double superannuation = this.superContribution;
		double net = gross - tax - superannuation;
		if (this.liveInContribution > 0) {
			net = net - (this.liveInContribution * 26.0714);
		} else if (this.liveOffContributionDHA > 0) {
			net = net - (this.liveOffContributionDHA * 26.0714);
		} else if (this.rentalAllowance > 0) {
		} else
			this.gt.showMessageDialog("Please select a living allowance");

		String message = "Gross Income:\n" + gross + "\nTax:\n" + tax + "\nNet Income:\n" + net
				+ "\nSuper Contribution:\n" + superannuation;
		this.gt.setTextInEntry(2, message);
	}

	// All allowances have been set to yearly values.
	// Adds entry to list to see which allowances have been added
	// to the calculation.
	public void pressUniformAllowance() {
		this.nonTaxedAllowances += 418.99;
		this.gt.addElementToList(0, "Recieving Uniform Allowance");
	}

	public void pressSeparationAllowance() {
		this.nonTaxedAllowances += 769.9973;
		this.gt.addElementToList(0, "Recieving Seperation Allowance");
	}

	public void pressServiceAllowance() {
		this.taxedAllowances += 14555.9825;
		this.gt.addElementToList(0, "Recieving Service Allowance");
	}

	public void sfTierSupport1C() {
		this.taxedAllowances += 10136.99;
		this.gt.addElementToList(0, "Recieving SF 1C Allowance");
	}

	public void sfTierSupport2() {
		this.taxedAllowances += 26005.9703;
		this.gt.addElementToList(0, "Recieving SF 2 Allowance");
	}

	public static void main(String[] args) {
		IncomeCalcMVP ai = new IncomeCalcMVP();

	}

}
