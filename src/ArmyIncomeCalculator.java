import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

// NOTE: Final version, accuracy is fairly high.
// References:
// [1]"Home : Pay and Conditions : Department of Defence", Defence.gov.au, 2020. [Online]. Available: https://www.defence.gov.au/payandconditions/. [Accessed: 18- Apr- 2020].
// [2]"Income tax: Australian tax brackets and rates (2019/2020 and 2018/2019)", SuperGuide, 2020. [Online]. Available: https://www.superguide.com.au/boost-your-superannuation/income-tax-rates. [Accessed: 18- Apr- 2020].
// [3]"Factsheets and publications", Csc.gov.au, 2020. [Online]. Available: https://www.csc.gov.au/Members/Advice-and-resources/Factsheets-and-publications/. [Accessed: 18- Apr- 2020].
// [4]"Special forces of Australia", En.wikipedia.org, 2020. [Online]. Available: https://en.wikipedia.org/wiki/Special_forces_of_Australia#/media/File:Australian_SOTG_patrol_Oct_2009.jpg. [Accessed: 03- Jun- 2020].

public class ArmyIncomeCalculator {
	private GTerm gt;

	// Salary Variables
	private double salaryRate[];

	// Allowance Variables
	private double taxedAllowances[];
	private double nonTaxedAllowances[];
	private int nonTaxedAllowanceCount;
	private int taxedAllowanceCount;
	private int currentElementsInListCount;
	private int maxAllowances;
	private double currentRentAllowance;

	// Contributions
	private double rentAllowanceContribution;

	// Rank Variables
	private String stringRank[];
	private int sig;
	private int ptep;
	private int lcpl;
	private int cpl;
	private int cpl1;
	private int cpl2;
	private int sgt;
	private int sgt1;

	// Trade/Career Variables (ECN = Employee Categorization Number)
	private int ECN[];

	// Calculation Variables
	private double hoursWorkedPerYear;
	private double hoursWorkedPerFortnight;
	private double fortnightsInAYear;

	// Current Variables (Assigned when a button is pressed)
	private double currentTaxedAllowances[];
	private double currentNonTaxedAllowances[];
	private String currentElementsInList[];
	private int rank;
	private int ecn;
	private double currentLiveInContributions;
	private double currentLiveOffContributions;
	private double currentSuperRate;
	private double currentEmployeeSuperContribution;
	private double currentTaxRate;
	private double currentBeforeTaxDeductions;
	private double currentTotalTaxableSalary;
	private double currentYearTax;

	// Employee's
	private Employee[] employees;
	private int maxEmployees;
	private int currentNumEmployees;
	private Employee currEmployee;

	// The constructor is full of method initiators to help build the UI for the
	// user. all the methods starting with the prefix of build is related to the UI.
	// This helps to avoid crowding in the constructor and makes code edits a lot
	// eaasier for anyone working on the program as they can easily identify UI
	// issues by the methods prefix.
	public ArmyIncomeCalculator() {
		this.gt = new GTerm(1500, 985);
		this.gt.setFontSize(20);
		this.gt.setTitle("Army (Signal Corps) Income Calculator V.3.0.1");
		// As there are many allowances all the key ones
		// have been selected and sorted into two main groups
		// Taxed and Non Taxed to aid with calculations.
		this.maxAllowances = 5;
		// Maximum amount of ranks this program is capable of
		// Catering for. Expanded from initial 5 in A2.
		int maxRanks = 8;
		// Variables used for calculations when the calculation
		// button is pressed or when another method requires them. 
		// Frequent use.
		this.hoursWorkedPerYear = 2919.9968;
		this.hoursWorkedPerFortnight = 112;
		this.fortnightsInAYear = 26.0714;
		// Creates the array size as per amount of ranks in the program
		this.salaryRate = new double[maxRanks];
		// ECN is Employee Categorization Number, as there are multiple
		// trades within the army only 3 have been used within this program.
		// All specific to the Signals Corps.
		this.ECN = new int[3];
		// This is used to populate text into text fields when a button
		// is selected or pressed.
		this.stringRank = new String[maxRanks];
		// Text area 0
		this.buildRank();
		this.gt.println("");
		// Text area 1
		this.buildTradeECN();
		this.gt.println("");
		// Text area 2
		this.buildSfCapacity();
		this.gt.println("");
		// Text area 3
		this.buildAllowances();
		this.gt.println("");
		// Text area 4
		this.buildLivingSituation();
		this.gt.println("");
		// Text area 5
		this.buildEmployeeSuper();
		this.gt.println("");
		// Text area 6
		this.buildBeforeTaxDeductions();
		// Places text fields next to each question so the user knows what they
		// have selected.
		this.buildTextAreas();
		// Creates separate areas for an allowance list and fortnight and yearly
		// summaries
		this.buildAllowanceList();
		this.buildSummaryAreas();
		// Creates the employee list area
		this.buildEmployeeArea();
		// Sets the background for the program
		this.buildBackground();
	}

	public void buildBackground() {
		this.gt.setXY(0, 0);
		this.gt.addImageIcon(".\\Background1.PNG");
	}

	public void buildEmployeeArea() {
		this.maxEmployees = 5;
		this.employees = new Employee[this.maxEmployees];
		this.currentNumEmployees = 0;
		this.gt.setXY(0, 650);
		this.gt.println("Personnel:");
		this.gt.addList(750, 250, this, null);
		this.gt.addButton("Inspect", this, "inspect");
		this.gt.println("");
		this.gt.addButton("Save to File", this, "saveToFile");
		this.gt.addButton("Load from File", this, "loadFromFile");
	}

	public void buildRank() {

		this.gt.println("Please select your rank:");

		this.gt.println("");

		this.gt.addButton("SIG", this, "sig");
		this.gt.addButton("PTE(P)", this, "ptep");
		this.gt.addButton("LCPL", this, "lcpl");
		this.gt.addButton("CPL", this, "cpl");
		this.gt.addButton("CPL[1]", this, "cpl1");
		this.gt.addButton("CPL[2]", this, "cpl2");
		this.gt.addButton("SGT", this, "sgt");
		this.gt.addButton("SGT[1]", this, "sgt1");

		this.sig = 0;
		this.ptep = 1;
		this.lcpl = 2;
		this.cpl = 3;
		this.cpl1 = 4;
		this.cpl2 = 5;
		this.sgt = 6;
		this.sgt1 = 7;

	}

	public void sig() {

		this.rank = this.sig;
		this.stringRank[this.rank] = "SIG";
		this.gt.setTextInEntry(0, "  " + this.stringRank[this.rank] + " Selected");

		// Runs only if Employee super rate has been selected (recalculates)
		if (this.currentEmployeeSuperContribution > 0) {
			this.calculateEmployeeSuper();

		}
	}

	public void ptep() {
		this.rank = this.ptep;
		this.stringRank[this.rank] = "PTE(P)";
		this.gt.setTextInEntry(0, this.stringRank[this.rank] + " Selected");

		// Runs only if Employee super rate has been selected (recalculates)
		if (this.currentEmployeeSuperContribution > 0) {
			this.calculateEmployeeSuper();

		}
	}

	public void lcpl() {
		this.rank = this.lcpl;
		this.stringRank[this.rank] = "LCPL";
		this.gt.setTextInEntry(0, " " + this.stringRank[this.rank] + " Selected");

		// Runs only if Employee super rate has been selected (recalculates)
		if (this.currentEmployeeSuperContribution > 0) {
			this.calculateEmployeeSuper();

		}
	}

	public void cpl() {
		this.rank = this.cpl;
		this.stringRank[this.rank] = "CPL";
		this.gt.setTextInEntry(0, "  " + this.stringRank[this.rank] + " Selected");

		// Runs only if Employee super rate has been selected (recalculates)
		if (this.currentEmployeeSuperContribution > 0) {
			this.calculateEmployeeSuper();

		}
	}

	public void cpl1() {
		this.rank = this.cpl1;
		this.stringRank[this.rank] = "CPL[1]";
		this.gt.setTextInEntry(0, "  " + this.stringRank[this.rank] + "Select");

		// Runs only if Employee super rate has been selected (recalculates)
		if (this.currentEmployeeSuperContribution > 0) {
			this.calculateEmployeeSuper();

		}
	}

	public void cpl2() {
		this.rank = this.cpl2;
		this.stringRank[this.rank] = "CPL[2]";
		this.gt.setTextInEntry(0, "  " + this.stringRank[this.rank] + "Select");

		// Runs only if Employee super rate has been selected (recalculates)
		if (this.currentEmployeeSuperContribution > 0) {
			this.calculateEmployeeSuper();

		}
	}

	public void sgt() {
		this.rank = this.sgt;
		this.stringRank[this.rank] = "SGT";
		this.gt.setTextInEntry(0, "  " + this.stringRank[this.rank] + " Selected");

		// Runs only if Employee super rate has been selected (recalculates)
		if (this.currentEmployeeSuperContribution > 0) {
			this.calculateEmployeeSuper();

		}
	}

	public void sgt1() {
		this.rank = this.sgt1;
		this.stringRank[this.rank] = "SGT[1]";
		this.gt.setTextInEntry(0, "  " + this.stringRank[this.rank] + " Select");

		// Runs only if Employee super rate has been selected (recalculates)
		if (this.currentEmployeeSuperContribution > 0) {
			this.calculateEmployeeSuper();

		}
	}

	public void buildTradeECN() {
		this.gt.println("Please select your ECN Code:");

		this.gt.println("");

		this.gt.addButton("661", this, "InfoSysTech661");
		this.gt.addButton("662", this, "RadioOp662");
		this.gt.addButton("665", this, "TeleTech665");
	}

	public void InfoSysTech661() {
		this.ECN[0] = 661;
		this.gt.setTextInEntry(1, "    ECN " + this.ECN[0]);
		this.ecn = 661;
	}

	public void RadioOp662() {
		this.ECN[1] = 662;
		this.gt.setTextInEntry(1, "    ECN " + this.ECN[1]);
		this.ecn = 662;
	}

	public void TeleTech665() {
		this.ECN[2] = 665;
		this.gt.setTextInEntry(1, "    ECN " + this.ECN[2]);
		this.ecn = 665;
	}

	public void buildSfCapacity() {
		// builds variables in all ranks to allow user to switch between ranks without
		// establishing new variables.
		this.gt.println("J or K ? (If neither apply, select 'None')");
		this.gt.println("");
		this.gt.addButton("None", this, "noneButton");
		this.gt.addButton("J", this, "jPay");
		this.gt.addButton("K", this, "kPay");

	}

	public void noneButton() {
		if (this.ecn == 0) {
			this.gt.showMessageDialog("Please select an  Rank & ECN before selecting SF Capacity");
			this.gt.setTextInEntry(2, "  No Selection");
		} else if (this.ecn == 661) {
			this.buildSalaryRate661();
			this.gt.setTextInEntry(2, "  Non - SOCOMD");
		} else if (this.ecn == 662) {
			this.buildSalaryRate662();
			this.gt.setTextInEntry(2, "  Non - SOCOMD");
		} else if (this.ecn == 665) {
			this.buildSalaryRate665();
			this.gt.setTextInEntry(2, "  Non - SOCOMD");
		}

	}

	public void jPay() {
		if (this.ecn == 0) {
			this.gt.showMessageDialog("Please select an  Rank & ECN before selecting SF Capacity");
			this.gt.setTextInEntry(2, "  No Selection");
		} else if (this.ecn == 661) {
			this.buildSalaryRate661J();
			this.gt.setTextInEntry(2, " J Pay Selected");
		} else if (this.ecn == 662) {
			this.buildSalaryRate662J();
			this.gt.setTextInEntry(2, " J Pay Selected");
		} else if (this.ecn == 665) {
			this.buildSalaryRate665J();
			this.gt.setTextInEntry(2, " J Pay Selected");
		}

	}

	public void kPay() {
		if (this.ecn == 0) {
			this.gt.showMessageDialog("Please select an  Rank & ECN before selecting SF Capacity");
			this.gt.setTextInEntry(2, "  No Selection");
		} else if (this.ecn == 661) {
			this.buildSalaryRate661K();
			this.gt.setTextInEntry(2, " K Pay Selected");
		} else if (this.ecn == 662) {
			this.buildSalaryRate662K();
			this.gt.setTextInEntry(2, " K Pay Selected");
		} else if (this.ecn == 665) {
			this.buildSalaryRate665K();
			this.gt.setTextInEntry(2, " K Pay Selected");
		}
	}

	public void buildAllowances() {

		// Builds the buttons and text for Allowances from constructor
		this.setNonTaxedAllowances();
		this.setTaxedAllowances();
		this.taxedAllowanceCount = 0;
		this.nonTaxedAllowanceCount = 0;
		this.gt.println("Please press the allowances you recieve.");
		this.gt.println("");
		this.gt.addButton("Service", this, "pressServiceAllowance");
		this.gt.addButton("Uniform", this, "pressUniformAllowance");
		this.gt.addButton("MWD Seperation", this, "pressSeparationAllowance");
		this.gt.addButton("SF Tier 1", this, "sfTierSupport1C");
		this.gt.addButton("SF Tier 2", this, "sfTierSupport2");
	}

	// Could have made int variable names to match as done with rank
	// but avoided this due to confusion it may have caused and just
	// left the array annotated in the method to make my life easier.
	// As there are both taxed and non taxed allowance there has been methods and
	// arrays assigned to help separate these types of allowances.
	public void setTaxedAllowances() {

		// Items set to hourly rate for accuracy
		this.currentElementsInList = new String[this.maxAllowances];
		this.taxedAllowances = new double[this.maxAllowances];
		this.currentTaxedAllowances = new double[this.maxAllowances];

		this.taxedAllowances[0] = 4.984931; // service Allowance (Taxed)
		this.taxedAllowances[1] = 3.471579; // Special Forces Support Tier 1 (Taxed) - Clarify this value
		this.taxedAllowances[2] = 8.906164; // Special Forces Support Tier 2 (Taxed)

	}

	public void setNonTaxedAllowances() {

		this.nonTaxedAllowances = new double[this.maxAllowances];
		this.currentNonTaxedAllowances = new double[this.maxAllowances];
		this.nonTaxedAllowances[0] = 0.143493; // uniform Allowance (Not Taxed)
		this.nonTaxedAllowances[1] = 0.263698; // separation Allowance (Not Taxed)
		this.nonTaxedAllowances[2] = 0;

	}

	public void buildLivingSituation() {
		this.gt.println("What is you current living situation?");
		this.gt.println("");
		this.gt.addButton("Living On", this, "livingInContributions");
		this.gt.addButton("Living Off (DHA)", this, "livingOffContributions");
		this.gt.addButton("Living Off (RA)", this, "rentalAllowance");
	}

	public void livingInContributions() {

		double liveInContributions;

		// per Fortnight
		if (this.rank > this.cpl) {
			liveInContributions = 224.76;
		} else {
			liveInContributions = 214.22;
		}
		this.gt.setTextInEntry(4, "Live On Selected");
		this.currentLiveInContributions = liveInContributions;
		// All other living variables set to 0 to avoid calculation errors
		this.currentLiveOffContributions = 0;
		this.currentRentAllowance = 0;
		this.rentAllowanceContribution = 0;
		// Adds utilities onto liveInContributions
		this.utilities();
	}

	public void livingOffContributions() {

		double liveOffContributions;

		// per Fortnight
		liveOffContributions = 504.91;
		this.gt.setTextInEntry(4, " DHA Selected");
		this.currentLiveOffContributions = liveOffContributions;
		// Set all other living variables to 0 to avoid calculation errors
		this.currentLiveInContributions = 0;
		this.currentRentAllowance = 0;
		this.rentAllowanceContribution = 0;
		// Adds utilities onto liveOffContributions
		this.utilities();

	}

	public void utilities() {
		// Calculated in fortnight value
		if (this.currentLiveInContributions > 0) {
			double liveInUtilities;
			liveInUtilities = 48.87;
			this.currentLiveInContributions = this.currentLiveInContributions + liveInUtilities;
			// Converted to yearly for calculate method
			this.currentLiveInContributions = this.currentLiveInContributions * this.fortnightsInAYear;
		}
		// Calculated with 1 service member living at residence
		if (this.currentLiveOffContributions > 0) {
			double liveOffUtilities;
			liveOffUtilities = 22.40;
			this.currentLiveOffContributions = this.currentLiveOffContributions + liveOffUtilities;
			// Converted to yearly for calculate method
			this.currentLiveOffContributions = this.currentLiveOffContributions * this.fortnightsInAYear;
		}
	}

	public void rentalAllowance() {

		this.gt.setTextInEntry(4, "  RA Selected");

		// Multiple Locations - 4 main locations chosen to keep it simple.
		int locations = 4;
		int syd = 0;
		int mel = 1;
		int bri = 2;
		int liv = 3;
		double[] rentCeiling = new double[locations];

		// per Fortnight
		rentCeiling[syd] = 1267;
		rentCeiling[mel] = 832;
		rentCeiling[bri] = 961;
		rentCeiling[liv] = 1066;

		String currentLocation = this.gt
				.getInputString("Where are you posted? (Sydney, Brisbane, Melbourne or Liverpool)");

		double currentRentCeiling = 0;
		int i = 0;
		while (i < locations) {
			if (currentLocation.equalsIgnoreCase("syd")) {
				currentRentCeiling = rentCeiling[syd];
			} else if (currentLocation.equalsIgnoreCase("mel")) {
				currentRentCeiling = rentCeiling[mel];
			} else if (currentLocation.equalsIgnoreCase("bri")) {
				currentRentCeiling = rentCeiling[bri];
			} else if (currentLocation.equalsIgnoreCase("liv"))
				currentRentCeiling = rentCeiling[liv];

			i += 1;
		}

		// Weekly
		double currentRent = Double.parseDouble(this.gt.getInputString("What is the current rent per week?"));
		// Converted to Fortnight for ease of calculating
		currentRent = currentRent * 2;
		double currentRentDifference;

		// Fortnight
		if (currentRent > currentRentCeiling) {
			currentRentDifference = currentRent - currentRentCeiling;
		} else
			currentRentDifference = 0;

		double rentAllowance = currentRent - currentRentDifference;

		this.rentAllowanceContribution = 454.42;

		this.currentRentAllowance = rentAllowance;

		this.currentLiveOffContributions = 0;
		this.currentLiveInContributions = 0;

	}

	// build method for the UI - Avoid crowding in the construction method.
	public void buildEmployeeSuper() {
		this.gt.println("How much super do you contribute through PMKEYS?");
		this.gt.println("");
		this.gt.addButton("5%", this, "setEmployeeSuper5");
		this.gt.addButton("6%", this, "setEmployeeSuper6");
		this.gt.addButton("7%", this, "setEmployeeSuper7");
		this.gt.addButton("8%", this, "setEmployeeSuper8");
		this.gt.addButton("9%", this, "setEmployeeSuper9");
		this.gt.addButton("10%", this, "setEmployeeSuper10");
	}

	// The next 5 methods created for buttons of the above method to help set values
	// to the employeeSuperrate and ultimately calculate the employee super
	// contributions. This could have been achieved by putting the values into the
	// brackets. But chosen this way for readability.
	public void setEmployeeSuper5() {
		this.currentSuperRate = 0.05;
		this.gt.setTextInEntry(5, "  " + this.currentSuperRate * 100 + "% Selected");
		this.calculateEmployeeSuper();
	}

	public void setEmployeeSuper6() {
		this.currentSuperRate = 0.06;
		this.gt.setTextInEntry(5, "  " + this.currentSuperRate * 100 + "% Selected");
		this.calculateEmployeeSuper();
	}

	public void setEmployeeSuper7() {
		this.currentSuperRate = 0.07;
		// Unsure why but it messes up the value and displayed 7.00000000000000000%
		// Set as 7.0 for now to blend in with the others.
		this.gt.setTextInEntry(5, "  7.0% Selected");
		this.calculateEmployeeSuper();
	}

	public void setEmployeeSuper8() {
		this.currentSuperRate = 0.08;
		this.gt.setTextInEntry(5, "  " + this.currentSuperRate * 100 + "% Selected");
		this.calculateEmployeeSuper();
	}

	public void setEmployeeSuper9() {
		this.currentSuperRate = 0.09;
		this.gt.setTextInEntry(5, "  " + this.currentSuperRate * 100 + "% Selected");
		this.calculateEmployeeSuper();
	}

	public void setEmployeeSuper10() {
		this.currentSuperRate = 0.10;
		this.gt.setTextInEntry(5, " " + this.currentSuperRate * 100 + "% Selected");
		this.calculateEmployeeSuper();
	}

	// Created this method to avoid using the same code in 6 other methods.
	// If GTerm was able to pass a value through a button this method would be
	// unessissary in the program.
	public void calculateEmployeeSuper() {
		// Will not calculate without rank or SF capacity being selected
		if (this.salaryRate[this.rank] == 0) {
			this.gt.showMessageDialog("Please select your rank and SF Capacity before Employee Super..");
			this.gt.setTextInEntry(5, "  No Selection");
		} else
			// Calculated yearly employee super contribution.
			// Employee Super is calculated using only salary and service allowances.
			this.currentEmployeeSuperContribution = (this.salaryRate[this.rank]
					+ (this.taxedAllowances[0] * this.hoursWorkedPerYear));

		// Calculated Fortnightly employee super contribution.
		this.currentEmployeeSuperContribution = this.currentEmployeeSuperContribution * this.currentSuperRate
				/ this.fortnightsInAYear;
	}

	// Included to help achieve higher accuracy within the program.
	public void buildBeforeTaxDeductions() {
		this.gt.println("Do you have before tax deductions? (Salary Sacrifice Super) ");
		this.gt.println("");
		this.gt.addButton("Yes", this, "setBeforeTaxDeductions");
		this.gt.addButton("No", this, "noBeforeTaxDeductions");
	}

	public void setBeforeTaxDeductions() {
		this.gt.setTextInEntry(6, "      " + "Yes");
		// Asks user for fortnightly before tax deductions
		this.currentBeforeTaxDeductions = Double
				.parseDouble(this.gt.getInputString("Enter a value for your Fortnighly before tax deductions:"));
		// Converted to yearly format in calculateTax() method
	}

	public void noBeforeTaxDeductions() {
		this.gt.setTextInEntry(6, "      " + "No");
		this.currentBeforeTaxDeductions = 0;
	}

	// Text areas created through a while loop to avoid coding it 1 by 1.
	// A little bit of automation for the UI. Spacing was a pain to work out.
	// Could have used a bit of math to make my life easier here, but was already
	// toast from building the calculation methods.
	public void buildTextAreas() {
		// Creates text areas next to the buttons through a while looped
		// method and spaces them accordingly.
		this.gt.setXY(740, 50);
		int i = 0;
		int sety = 142;

		// Set number of text areas here.
		while (i < 7) {
			this.gt.addTextArea("  No Selection", 200, 40);
			this.gt.setXY(740, sety);
			sety = sety + 92;
			i += 1;
		}
	}

	// Builds the UI list for allowances, Done in a method to avoiding crowding in
	// the constructor.
	public void buildAllowanceList() {
		// list created for the tracking of all allowances as there are
		// many different allowances.
		this.gt.setXY(950, 5);
		this.gt.println("Allowances Added: ");
		this.gt.setXY(950, 50);
		this.gt.addList(500, 200, this, null);
		this.gt.println("");
		this.gt.addButton("Clear Allowances", this, "clearAllowanceList");
	}

	// Used to clear ALL allowances selected, done this way "fresh start" purposes,
	// however could have also down a remove item 1 by 1 method.
	public void clearAllowanceList() {
		// Removes all allowances in the list
		int i = 0;
		while (i < this.currentElementsInListCount) {
			this.gt.removeElementFromList(0, 0);
			this.currentElementsInListCount -= 1;
		}

		// Resets allowances (Taxed and Non Taxed to 0
		i = 0;
		while (i < this.maxAllowances) {
			this.currentTaxedAllowances[i] = 0;
			this.currentNonTaxedAllowances[i] = 0;
			i += 1;
		}

		// Resets counts to 0 and updates text field
		this.nonTaxedAllowanceCount = 0;
		this.taxedAllowanceCount = 0;
		this.gt.setTextInEntry(3, "0 of 5 Selected");

	}

	// Creates the UI for user - Broke this down into build* methods to make it easy
	// for edits. Could have done this all in the constructor method but would have
	// led to extreme amounts of UI code creating confusion for the next coder.
	public void buildSummaryAreas() {
		this.gt.setXY(950, 300);

		// Font size changed to be slightly smaller to fit text in text areas
		this.gt.setFontSize(18);
		// Yearly Calc / text area 8
		this.gt.println("Yearly Summary:");
		this.gt.addTextArea("", 500, 250);
		this.gt.println("");

		// Fortnight Calc / Text area 9 Calc
		this.gt.println("");
		this.gt.println("Fortnight Summary:");
		this.gt.addTextArea("", 500, 250);
		this.gt.println("");

		this.gt.addButton("Calculate", this, "calculate");
		this.gt.addButton("Clear", this, "clearCalculations");

	}

	// Instead of clicking on the item in the list and automatically display
	// the values in the summary zone of the program, I have opted to use a button
	// called "Inspect" to display as the user wants, I think this is a nice touch
	// and really it is aimed to do this for usability of the program to avoid any
	// confusion if the user is mid way through adding values for a new Salary.
	public void inspect() {
		if (this.gt.getSelectedElementFromList(1) != null) {
			Employee employee = (Employee) this.gt.getSelectedElementFromList(1);
			String annualMessage = "";
			annualMessage += "Gross Income:\n$" + employee.getAnnualGrossIncome();
			annualMessage += "\nTax:\n$" + employee.getAnnualTax();
			annualMessage += "\nNet Income:\n$" + employee.getAnnualNetIncome();
			annualMessage += "\nEmployee Super Contribution:\n$" + employee.getAnnualEmployeeContributions();

			String fortnightMessage = "";
			fortnightMessage += "Gross Income:\n$" + employee.getFortnightGrossIncome();
			fortnightMessage += "\nTax:\n$" + employee.getFortnightTax();
			fortnightMessage += "\nNet Income:\n$" + employee.getFortnightNetIncome();
			fortnightMessage += "\nEmployee Super Contribution:\n$" + employee.getFortnightEmployeeContribution();

			this.gt.setTextInEntry(7, annualMessage);
			this.gt.setTextInEntry(8, fortnightMessage);
		} else
			this.gt.showMessageDialog("Please select an item in the list");
	}

	// The most complex method and really the brains of the program is contained
	// within this method, I did initially have it split for A2 into 2 separate
	// methods, one for yearly and one for fortnight calculations. This worked quite
	// well until the introduction of another class and the storing of object
	// values. It has been converted into 1 method now and is quite complex. The
	// code can be shortened quite a bit with smaller code/ less comments. However I
	// think because of the complexity of the method it needs to be written this way
	// to easily identify any issues and make it readable to know exactly what is
	// happening.
	public void calculate() {

		double totalTaxedAllowances = 0;
		double totalNonTaxedAllowances = 0;

		// Collects all the currently selected allowances in the array
		// adds them all together in the while loop, This could have been done
		// in another method, but to try and keep everything contained and
		// not to increase the amount of methods I left it in here.
		int i = 0;
		while (i < this.maxAllowances) {
			totalTaxedAllowances = this.currentTaxedAllowances[i] + totalTaxedAllowances;
			totalNonTaxedAllowances = this.currentNonTaxedAllowances[i] + totalNonTaxedAllowances;
			i += 1;
		}

		// Adds all of the allowances together and multiples by hours worked
		// in the fortnight to get fortnight result
		double totalAllowances = (totalTaxedAllowances + totalNonTaxedAllowances) * this.hoursWorkedPerFortnight;

		// Creates Fortnight variables
		double fortnightGrossIncome = this.salaryRate[this.rank] / this.fortnightsInAYear;

		// Adds all standard allowances
		fortnightGrossIncome = fortnightGrossIncome + totalAllowances;

		// Adds Rent allowance
		fortnightGrossIncome = fortnightGrossIncome + this.currentRentAllowance;

		// Calculates tax rate then converts to a fortnight value
		this.setTaxRate();
		double fortnightTax = this.currentYearTax / this.fortnightsInAYear;

		// Broken into smaller pieces to help understand the math
		double fortnightNetIncome = fortnightGrossIncome - fortnightTax;

		// Deducts Employee super contribution
		fortnightNetIncome = fortnightNetIncome - this.currentEmployeeSuperContribution;

		// Deducts any before tax deductions
		fortnightNetIncome = fortnightNetIncome - this.currentBeforeTaxDeductions;

		// Converts this.currentLiveX and rentAllowance in to fortnight figures
		double fortnightCurrentLiveInContributions = this.currentLiveInContributions / this.fortnightsInAYear;
		double fortnightCurrentLiveOffContributions = this.currentLiveOffContributions / this.fortnightsInAYear;

		// Matches the current living situation that the user has selected and
		// does the math accordingly.
		if (this.currentLiveInContributions > 0) {
			fortnightNetIncome = fortnightNetIncome - fortnightCurrentLiveInContributions;
		} else if (this.currentLiveOffContributions > 0) {
			fortnightNetIncome = fortnightNetIncome - fortnightCurrentLiveOffContributions;
		} else if (this.currentRentAllowance > 0) {
			fortnightNetIncome = fortnightNetIncome - this.rentAllowanceContribution;
		} else
			this.gt.showMessageDialog("Please select a living situation");

		// Rounds up the tax then adds difference to Net Income
		double roundUpFornightTax = fortnightTax;
		fortnightTax = Math.ceil(fortnightTax);
		roundUpFornightTax = roundUpFornightTax - fortnightTax;
		fortnightNetIncome = fortnightNetIncome + roundUpFornightTax;

		// Round all values to 2 decimal places
		fortnightGrossIncome = this.roundToTwoDecimals(fortnightGrossIncome);
		fortnightTax = this.roundToTwoDecimals(fortnightTax);
		fortnightNetIncome = this.roundToTwoDecimals(fortnightNetIncome);
		double fortnightEmployeeContribution = this.roundToTwoDecimals(this.currentEmployeeSuperContribution);

		// Asks user for last name to create the object later on. This could have been
		// done later however it looks cleaner when done before anything is displayed in
		// the text areas.
		String lastname = this.gt.getInputString("Please Enter your Lastname");

		// Displays the summary for the fortnight in the text area
		this.gt.setTextInEntry(8,
				"Gross Income:\n$" + fortnightGrossIncome + "\nTax:\n$" + fortnightTax + "\nNet Income:\n$"
						+ fortnightNetIncome + "\nEmployee Super Contribution:\n$" + fortnightEmployeeContribution);

		// resets the variables just in case the calculate has been run, member
		// variables remain constant.
		totalTaxedAllowances = 0;
		totalNonTaxedAllowances = 0;
		double annualNetIncome = 0;
		double annualTax = 0;

		// Collects all the currently selected allowances in the array
		// adds them all together.
		i = 0;
		while (i < this.maxAllowances) {
			totalTaxedAllowances = this.currentTaxedAllowances[i] + totalTaxedAllowances;
			totalNonTaxedAllowances = this.currentNonTaxedAllowances[i] + totalNonTaxedAllowances;
			i += 1;
		}

		// Adds all of the allowances together and multiples by hours worked
		// in the year to get annual result
		totalAllowances = (totalTaxedAllowances + totalNonTaxedAllowances) * this.hoursWorkedPerYear;

		// Creates yearly variables
		double annualGrossIncome = this.salaryRate[this.rank];

		// Adds all standard allowances
		annualGrossIncome = annualGrossIncome + totalAllowances;

		// Adds Rent allowance
		annualGrossIncome = annualGrossIncome + (this.currentRentAllowance * this.fortnightsInAYear);

		// Calculates tax rate then converts to an annual value
		this.setTaxRate();
		annualTax = this.currentYearTax;

		// Broken into smaller pieces to help understand the math
		annualNetIncome = annualGrossIncome - annualTax;

		// Deducts Employee super contribution (Converts to yearly)
		annualNetIncome = annualNetIncome - (this.currentEmployeeSuperContribution * this.fortnightsInAYear);

		// Deducts any before tax deductions (Converts to yearly)
		annualNetIncome = annualNetIncome - (this.currentBeforeTaxDeductions * this.fortnightsInAYear);

		// Converts this.currentLiveX and rentAllowance in to annual figures
		double annualCurrentLiveInContributions = this.currentLiveInContributions;
		double annualCurrentLiveOffContributions = this.currentLiveOffContributions;

		// Finds out the current living situation that the user has selected and
		// does the math accordingly.
		if (this.currentLiveInContributions > 0) {
			annualNetIncome = annualNetIncome - annualCurrentLiveInContributions;
		} else if (this.currentLiveOffContributions > 0) {
			annualNetIncome = annualNetIncome - annualCurrentLiveOffContributions;
		} else if (this.currentRentAllowance > 0) {
			annualNetIncome = annualNetIncome - (this.rentAllowanceContribution * this.fortnightsInAYear);
		} else
			this.gt.showMessageDialog("Please select a living situation");

		// Rounds up the tax then adds difference to Net Income
		// Converted to fortnight for accuracy and then converted back to annual
		double roundUpAnnualTax = this.currentYearTax / this.fortnightsInAYear;
		annualTax = this.currentYearTax / this.fortnightsInAYear;
		annualTax = Math.ceil(annualTax);
		roundUpAnnualTax = (roundUpAnnualTax - annualTax) * this.fortnightsInAYear;
		annualNetIncome = annualNetIncome + roundUpAnnualTax;
		annualTax = annualTax * this.fortnightsInAYear;

		// Rounds all values to 2 decimals places using method
		annualGrossIncome = this.roundToTwoDecimals(annualGrossIncome);
		annualTax = this.roundToTwoDecimals(annualTax);
		annualNetIncome = this.roundToTwoDecimals(annualNetIncome);
		double annualEmployeeContributions = this
				.roundToTwoDecimals(this.currentEmployeeSuperContribution * this.fortnightsInAYear);

		// Displays the summary for the annual in the text area
		this.gt.setTextInEntry(7, "Gross Income:\n$" + annualGrossIncome + "\nTax:\n$" + annualTax + "\nNet Income:\n$"
				+ annualNetIncome + "\nEmployee Super Contribution:\n$" + annualEmployeeContributions);

		// Add the variables to the object, calls array expander if needed. I could have
		// written the array expanded into the if statement but because this code
		// is used multiple times I have placed it in its own method. Finally,
		// The list is then populated with the object contents.
		if (this.currentNumEmployees >= this.maxEmployees) {
			extendArray();
		}
		this.employees[this.currentNumEmployees] = new Employee(lastname);
		this.currEmployee = (Employee) this.employees[this.currentNumEmployees];

		this.currEmployee.setFortnightGrossIncome(fortnightGrossIncome);
		this.currEmployee.setFortnightTax(fortnightTax);
		this.currEmployee.setFortnightNetIncome(fortnightNetIncome);
		this.currEmployee.setFortnightEmployeeContribution(fortnightEmployeeContribution);
		this.currEmployee.setAnnualGrossIncome(annualGrossIncome);
		this.currEmployee.setAnnualTax(annualTax);
		this.currEmployee.setAnnualNetIncome(annualNetIncome);
		this.currEmployee.setAnnualEmployeeContributions(annualEmployeeContributions);

		this.gt.addElementToList(1, (Employee) this.employees[this.currentNumEmployees]);
		this.employees[this.currentNumEmployees] = this.currEmployee;
		this.currentNumEmployees += 1;
	}

	public void clearCalculations() {
		// Clears all previous data in fortnight summary
		this.gt.setTextInEntry(8, "");
		// Clears all previous data in yearly summary
		this.gt.setTextInEntry(7, "");
	}

	// This method is used a lot throughout the program to ensure that the values
	// displayed do not exceed 2 decimal places. This can be done multiple ways from
	// what I have gathered, however for this program I have used the math function
	// as taught in lessons throughout the semester.
	public double roundToTwoDecimals(double d) {
		d = Math.round(d * 100.0) / 100.0;
		return d;
	}

	// There are 5 allowances in play for this program (the below 5 methods),
	// They correspond to the buttons as they are pressed. As the buttons are
	// pressed the method also conducts a boolean check to see if the allowance has
	// already been pressed or not, This creates a form of error correction and
	// avoids adding multiples of the same allowances and is displayed in a list.
	public void pressUniformAllowance() {

		boolean trueOrFalse = false;
		int i = 0;
		while (i < this.currentElementsInListCount) {
			if (this.currentElementsInList[i].toLowerCase().contains("uniform")) {
				trueOrFalse = true;
			}
			i += 1;
		}

		if (trueOrFalse == false) {
			this.currentNonTaxedAllowances[this.nonTaxedAllowanceCount] = this.nonTaxedAllowances[0];
			this.currentElementsInList[this.currentElementsInListCount] = "Uniform Allowance";
			this.gt.addElementToList(0, this.currentElementsInList[this.currentElementsInListCount]);
			this.gt.setTextInEntry(3, (this.currentElementsInListCount + 1) + " of 5 Selected");
			this.currentElementsInListCount += 1;
			this.nonTaxedAllowanceCount += 1;

		}

	}

	public void pressSeparationAllowance() {
		boolean trueOrFalse = false;
		int i = 0;
		while (i < this.currentElementsInListCount) {
			if (this.currentElementsInList[i].toLowerCase().contains("separation")) {
				trueOrFalse = true;
			}
			i += 1;
		}

		if (trueOrFalse == false) {
			this.currentNonTaxedAllowances[this.nonTaxedAllowanceCount] = this.nonTaxedAllowances[1];
			this.currentElementsInList[this.currentElementsInListCount] = "Separation Allowance";
			this.gt.addElementToList(0, this.currentElementsInList[this.currentElementsInListCount]);
			this.gt.setTextInEntry(3, (this.currentElementsInListCount + 1) + " of 5 Selected");
			this.currentElementsInListCount += 1;
			this.nonTaxedAllowanceCount += 1;
		}
	}

	public void pressServiceAllowance() {
		boolean trueOrFalse = false;
		int i = 0;
		while (i < this.currentElementsInListCount) {
			if (this.currentElementsInList[i].toLowerCase().contains("service")) {
				trueOrFalse = true;
			}
			i += 1;
		}

		if (trueOrFalse == false) {
			this.currentTaxedAllowances[this.taxedAllowanceCount] = this.taxedAllowances[0];
			this.currentElementsInList[this.currentElementsInListCount] = "Service Allowance";
			this.gt.addElementToList(0, this.currentElementsInList[this.currentElementsInListCount]);
			this.gt.setTextInEntry(3, (this.currentElementsInListCount + 1) + " of 5 Selected");
			this.currentElementsInListCount += 1;
			this.taxedAllowanceCount += 1;
		}
	}

	public void sfTierSupport1C() {
		boolean trueOrFalse = false;
		int i = 0;
		while (i < this.currentElementsInListCount) {
			if (this.currentElementsInList[i].toLowerCase().contains("1")) {
				trueOrFalse = true;
			}
			i += 1;
		}

		if (trueOrFalse == false) {
			this.currentTaxedAllowances[this.taxedAllowanceCount] = this.taxedAllowances[1];
			this.currentElementsInList[this.currentElementsInListCount] = "SF Tier 1C";
			this.gt.addElementToList(0, this.currentElementsInList[this.currentElementsInListCount]);
			this.gt.setTextInEntry(3, (this.currentElementsInListCount + 1) + " of 5 Selected");
			this.currentElementsInListCount += 1;
			this.taxedAllowanceCount += 1;
		}
	}

	public void sfTierSupport2() {
		boolean trueOrFalse = false;
		int i = 0;
		while (i < this.currentElementsInListCount) {
			if (this.currentElementsInList[i].toLowerCase().contains("2")) {
				trueOrFalse = true;
			}
			i += 1;
		}

		if (trueOrFalse == false) {
			this.currentTaxedAllowances[this.taxedAllowanceCount] = this.taxedAllowances[2];
			this.currentElementsInList[this.currentElementsInListCount] = "SF Tier 2";
			this.gt.addElementToList(0, this.currentElementsInList[this.currentElementsInListCount]);
			this.gt.setTextInEntry(3, (this.currentElementsInListCount + 1) + " of 5 Selected");
			this.currentElementsInListCount += 1;
			this.taxedAllowanceCount += 1;
		}
	}

	// All the buildSalaryRate* methods are directly related to the payscale used
	// to calculate income of a signal corps soldier, I believe this could
	// have been done better with the use of a 2D array, However for this
	// course I have not been taught the use of this and have opted for a more
	// lengthy process of setting variables in the below listed methods.
	public void buildSalaryRate661() {

		this.salaryRate[this.sig] = 55442;
		this.salaryRate[this.ptep] = 56484;
		this.salaryRate[this.lcpl] = 61278;
		this.salaryRate[this.cpl] = 69783;
		this.salaryRate[this.cpl1] = 70983;
		this.salaryRate[this.cpl2] = 72204;
		this.salaryRate[this.sgt] = 78572;
		this.salaryRate[this.sgt1] = 79947;

	}

	public void buildSalaryRate662() {

		this.salaryRate[this.sig] = 51988;
		this.salaryRate[this.ptep] = 56484;
		this.salaryRate[this.lcpl] = 61278;
		this.salaryRate[this.cpl] = 69783;
		this.salaryRate[this.cpl1] = 70983;
		this.salaryRate[this.cpl2] = 72204;
		this.salaryRate[this.sgt] = 78572;
		this.salaryRate[this.sgt1] = 79947;

	}

	public void buildSalaryRate665() {

		this.salaryRate[this.sig] = 55442;
		this.salaryRate[this.ptep] = 60216;
		this.salaryRate[this.lcpl] = 61278;
		this.salaryRate[this.cpl] = 69783;
		this.salaryRate[this.cpl1] = 70983;
		this.salaryRate[this.cpl2] = 72204;
		this.salaryRate[this.sgt] = 78572;
		this.salaryRate[this.sgt1] = 79947;

	}

	public void buildSalaryRate661J() {

		this.salaryRate[this.sig] = 55442;
		this.salaryRate[this.ptep] = 64248;
		this.salaryRate[this.lcpl] = 69661;
		this.salaryRate[this.cpl] = 74139;
		this.salaryRate[this.cpl1] = 75334;
		this.salaryRate[this.cpl2] = 76554;
		this.salaryRate[this.sgt] = 82925;
		this.salaryRate[this.sgt1] = 84301;

	}

	public void buildSalaryRate662J() {

		this.salaryRate[this.sig] = 55442;
		this.salaryRate[this.ptep] = 64248;
		this.salaryRate[this.lcpl] = 69661;
		this.salaryRate[this.cpl] = 74139;
		this.salaryRate[this.cpl1] = 75334;
		this.salaryRate[this.cpl2] = 76554;
		this.salaryRate[this.sgt] = 82925;
		this.salaryRate[this.sgt1] = 84301;

	}

	public void buildSalaryRate665J() {

		this.salaryRate[this.sig] = 59175;
		this.salaryRate[this.ptep] = 64248;
		this.salaryRate[this.lcpl] = 69661;
		this.salaryRate[this.cpl] = 74139;
		this.salaryRate[this.cpl1] = 75334;
		this.salaryRate[this.cpl2] = 76554;
		this.salaryRate[this.sgt] = 82925;
		this.salaryRate[this.sgt1] = 84301;

	}

	public void buildSalaryRate661K() {

		this.salaryRate[this.sig] = 63207;
		this.salaryRate[this.ptep] = 64248;
		this.salaryRate[this.lcpl] = 69661;
		this.salaryRate[this.cpl] = 78841;
		this.salaryRate[this.cpl1] = 80037;
		this.salaryRate[this.cpl2] = 81254;
		this.salaryRate[this.sgt] = 87624;
		this.salaryRate[this.sgt1] = 88997;

	}

	public void buildSalaryRate662K() {

		this.salaryRate[this.sig] = 63207;
		this.salaryRate[this.ptep] = 64248;
		this.salaryRate[this.lcpl] = 69661;
		this.salaryRate[this.cpl] = 78841;
		this.salaryRate[this.cpl1] = 80037;
		this.salaryRate[this.cpl2] = 81254;
		this.salaryRate[this.sgt] = 87624;
		this.salaryRate[this.sgt1] = 88997;

	}

	public void buildSalaryRate665K() {

		this.salaryRate[this.sig] = 67560;
		this.salaryRate[this.ptep] = 68603;
		this.salaryRate[this.lcpl] = 69661;
		this.salaryRate[this.cpl] = 78841;
		this.salaryRate[this.cpl1] = 80037;
		this.salaryRate[this.cpl2] = 81254;
		this.salaryRate[this.sgt] = 87624;
		this.salaryRate[this.sgt1] = 88997;

	}

	// Coded this way to easily fault find calculation errors.I Could have added the
	// calculations into the one variable allocation however for readability and
	// Function. I have chosen to do it the long way to help correct issues as they
	// occur. Comments used to track what is happening at each point to help adjust
	// if needed.
	public void setTaxRate() {

		// Resets variable incase calculate has already been run.
		this.currentTotalTaxableSalary = 0;

		// While loop to add all taxable allowances together.
		int i = 0;
		while (i < this.maxAllowances) {
			this.currentTotalTaxableSalary = this.currentTotalTaxableSalary + this.currentTaxedAllowances[i];
			i += 1;
		}

		// Converts Allowances into a yearly unit.
		this.currentTotalTaxableSalary = this.currentTotalTaxableSalary * this.hoursWorkedPerYear;

		// Adds both the allowances and current salary rate together.
		this.currentTotalTaxableSalary = this.currentTotalTaxableSalary + this.salaryRate[this.rank];

		// converts before tax deductions into same unit of measurement as
		// currentTotalTaxableSalary.
		double BeforeTaxDeductions = this.currentBeforeTaxDeductions * this.fortnightsInAYear;

		// Minuses before tax deductions
		this.currentTotalTaxableSalary = this.currentTotalTaxableSalary - BeforeTaxDeductions;

		// As the salaries start at 51988 there is no need for
		// lesser tax rate variables and because the salaries won't
		// exceed 180000 there is no need for any of the higher rates.
		// As the program advances it will need this adjusted to suite
		// higher salaries.

		if (this.currentTotalTaxableSalary < 90000) {
			this.currentTaxRate = 0.325;

		} else if (this.currentTotalTaxableSalary > 90000) {
			this.currentTaxRate = 0.37;
		}

		if (this.currentTaxRate == 0.325) {
			this.currentYearTax = ((this.currentTotalTaxableSalary - 37000) * this.currentTaxRate) + 3572;
		} else if (this.currentTaxRate == 0.37) {
			this.currentYearTax = ((this.currentTotalTaxableSalary - 90000) * this.currentTaxRate) + 20797;
		}
	}

	// conducts the save to file on button press, Cannot see any other way to do
	// this..
	public void saveToFile() {

		String filename = "employeeData.csv";
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(filename));
			int i = 0;
			while (i < this.currentNumEmployees) {
				String writtenText = "";
				writtenText += this.employees[i].getLastName() + ",";
				writtenText += this.employees[i].getAnnualGrossIncome() + ",";
				writtenText += this.employees[i].getAnnualTax() + ",";
				writtenText += this.employees[i].getAnnualNetIncome() + ",";
				writtenText += this.employees[i].getAnnualEmployeeContributions() + ",";
				writtenText += this.employees[i].getFortnightGrossIncome() + ",";
				writtenText += this.employees[i].getFortnightTax() + ",";
				writtenText += this.employees[i].getFortnightNetIncome() + ",";
				writtenText += this.employees[i].getFortnightEmployeeContribution() + "\n";
				bw.write(writtenText);
				i += 1;
			}
			bw.close();
		} catch (Exception e) {
			this.gt.showMessageDialog("Oops, Something went wrong");
		}

		this.gt.showMessageDialog("Successfully saved!");
	}

	// Conducts the load on button press, cannot see any other way it could have
	// been done..
	public void loadFromFile() {

		String filename = this.gt.getFilePath();
		try {
			BufferedReader br = new BufferedReader(new FileReader(filename));
			String valueThatWasRead = br.readLine();

			while (valueThatWasRead != null) {
				if (this.currentNumEmployees >= this.maxEmployees) {
					extendArray();
				}
				String[] splitValues = valueThatWasRead.split(",");
				Employee currEmployee = new Employee(splitValues[0]);
				currEmployee.setAnnualGrossIncome(Double.parseDouble(splitValues[1]));
				currEmployee.setAnnualTax(Double.parseDouble(splitValues[2]));
				currEmployee.setAnnualNetIncome(Double.parseDouble(splitValues[3]));
				currEmployee.setAnnualEmployeeContributions(Double.parseDouble(splitValues[4]));
				currEmployee.setFortnightGrossIncome(Double.parseDouble(splitValues[5]));
				currEmployee.setFortnightTax(Double.parseDouble(splitValues[6]));
				currEmployee.setFortnightNetIncome(Double.parseDouble(splitValues[7]));
				currEmployee.setFortnightEmployeeContribution(Double.parseDouble(splitValues[8]));

				this.gt.addElementToList(1, currEmployee);
				this.employees[this.currentNumEmployees] = currEmployee;
				this.currentNumEmployees += 1;

				valueThatWasRead = br.readLine();
			}
			br.close();
		} catch (FileNotFoundException e) {
			this.gt.showMessageDialog("Sorry, could not find " + filename);
		} catch (IOException e) {
			this.gt.showMessageDialog("Sorry, problem with " + filename);
		}

	}

	// Array Extender placed into a separate method because it is used multiple
	// times
	// throughout the code, I could have just placed it into the methods that use
	// it but chose to separate it in a method for readability and function.

	public void extendArray() {

		Employee[] employeesLarge = new Employee[this.maxEmployees * 2];
		int i = 0;
		while (i < this.currentNumEmployees) {
			employeesLarge[i] = this.employees[i];
			i += 1;
		}
		this.employees = employeesLarge;
		this.maxEmployees = this.employees.length;

	}

	public static void main(String[] args) {
		ArmyIncomeCalculator ai = new ArmyIncomeCalculator();

	}

}
