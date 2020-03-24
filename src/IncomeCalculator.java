import java.text.DecimalFormat;

public class IncomeCalculator {

	public static void main(String[] args) {
		GTerm gt = new GTerm(700, 1000);
		gt.setFontSize(35);
		gt.setXY(130, 0);
		gt.println("Army Pay Calculator\n");
		DecimalFormat df = new DecimalFormat("0.00");
		gt.setXY(30, 50);
		gt.setFontSize(20);

		// EDIT THESE TO UPDATE //

		double serviceAllowanceUnit = 4.984931;
		double uniformAllowanceUnit = 0.143493;
		double SepAllowanceUnit = 0.263698;

		// DO NOT TOUCH //

		// ALL ANNUAL RATES //

		double salary = Double.parseDouble(gt.getInputString("Annual base salary : "));

		while (salary <= 1) {
			salary = Double.parseDouble(gt.getInputString("Error! Enter a valid salary. \n\nAnnual base salary : "));
		}

		double serviceAllowance = serviceAllowanceUnit * 2919.9968;
		double uniformAllowance = uniformAllowanceUnit * 2919.9968;
		double SepAllowance = SepAllowanceUnit * 2919.9968;

		// double salaryRate = (salary / 26.0714) / 112;

		// DO NOT TOUCH //

		int ynSepAllowance = Integer
				.parseInt(gt.getInputString("Do you recieve Seperation Allowance? \n\n1 = YES\n2 = NO"));

		while (ynSepAllowance <= 0 || ynSepAllowance >= 3) {
			ynSepAllowance = Integer.parseInt(gt.getInputString(
					"Error! Incorrect value entered.\nDo you recieve Seperation Allowance? \n\n1 = YES\n2 = NO"));
		}

		if (ynSepAllowance == 1) {

			SepAllowance = SepAllowanceUnit * 2919.9968;
		}

		if (ynSepAllowance == 2) {

			SepAllowance = 0;
		}

		double ServResCon = 0;
		double rentAssist = 0;
		double rentCost = 0;
		double OnBaseRent = 0;

		int ServRes = Integer
				.parseInt(gt.getInputString("Do you live in Service Residence/On-base? \n\n1 = YES\n2 = NO"));

		while (ServRes <= 0 || ServRes >= 3) {
			ServRes = Integer.parseInt(gt.getInputString(
					"Error! Incorrect value entered.\nDo you live in Service Residence/On-base? \n\n1 = YES\n2 = NO"));
		}

		if (ServRes == 1) {
			int ynOnBase = Integer.parseInt(gt.getInputString("Do you live On-base ?\n\n1 = YES\n2 = NO"));

			while (ynOnBase <= 0 || ynOnBase >= 3) {
				ynOnBase = Integer.parseInt(
						gt.getInputString("Error! Incorrect value entered.\nDo you live On-base ?\n\n1 = YES\n2 = NO"));
			}

			// Lives On-base

			if (ynOnBase == 1) {

				// UPDATE IF CHANGED // Holsworthy average

				OnBaseRent = 263.09 * 26.0714;
			}

			// Lives Off-base in Service residence (DHA)

			if (ynOnBase == 2) {

				// UPDATE IF CHANGED // Combination of rent and water figure.

				ServResCon = 527.31 * 26.0714;
			}
		}

		// Lives Off-base getting paid rental assistance

		if (ServRes == 2) {
			ServResCon = 0;
			rentAssist = Double
					.parseDouble(gt.getInputString("How much rental assistance do you recieve per pay (fornight)?"));
			rentAssist = rentAssist * 26.0714;
			rentCost = Double.parseDouble(gt.getInputString("How much does your rent cost (fornight)?"));
			rentCost = rentCost * 26.0714;
		}

		double totalTaxAllowances = serviceAllowance;
		double SFSupportTier = 0;

		// SF Support tier value - Edit if changes

		SFSupportTier = Double.parseDouble(gt.getInputString("Which SF tier do you recieve? (0 (none) or 1 or 2)"));

		while (SFSupportTier <= -1 || SFSupportTier >= 3) {
			SFSupportTier = Double.parseDouble(gt.getInputString(
					"Error! Incorrect value entered.\nWhich SF tier do you recieve? (0 (none) or 1 or 2) \n"));
		}

		if (SFSupportTier == 0) {
			SFSupportTier = 0;
		}

		if (SFSupportTier == 1) {
			SFSupportTier = 9000.00;
		}

		if (SFSupportTier == 2) {
			SFSupportTier = 997.490368 * 26.0714;
		}

		// Members contribution rates (based off minimum 5% and maximum 10%)

		double superRate = Double
				.parseDouble(gt.getInputString("Enter the % of Super you contribute:\n5 = 5%\n7=7%\netc.."));

		while (superRate <= 4 || superRate >= 11) {
			superRate = Integer.parseInt(gt.getInputString(
					"Error! Incorrect value entered.\nEnter the % of Super you contribute:\n5 = 5%\n7=7%\netc.."));
		}

		if (superRate == 5) {
			superRate = 0.05;
		}

		if (superRate == 6) {
			superRate = 0.06;
		}

		if (superRate == 7) {
			superRate = 0.07;
		}

		if (superRate == 8) {
			superRate = 0.08;
		}

		if (superRate == 9) {
			superRate = 0.09;
		}

		if (superRate == 10) {
			superRate = 0.1;
		}

		double BeforeTaxDeductions = 0;
		int ynBeforeTaxDeductions = Integer
				.parseInt(gt.getInputString("Do you have any before tax deductions?\n\n1 = YES\n2 = NO"));

		while (ynBeforeTaxDeductions <= 0 || ynBeforeTaxDeductions >= 3) {
			ynBeforeTaxDeductions = Integer.parseInt(gt.getInputString(
					"Error! Incorrect value entered.\nDo you have any before tax deductions?\n\n1 = YES\n2 = NO"));
		}

		if (ynBeforeTaxDeductions == 1) {
			BeforeTaxDeductions = Double
					.parseDouble(gt.getInputString("What is the total before tax deductions listed on your payslip?"));
			BeforeTaxDeductions = BeforeTaxDeductions * 26.0714;
		}

		// Summary and combinations of Taxable and Non Taxable Allowances.

		double totalNonTaxAllowances = uniformAllowance + SepAllowance;
		double totalTaxableSalary = salary + totalTaxAllowances + SFSupportTier - BeforeTaxDeductions;
		double totalSalary = ((salary + totalNonTaxAllowances + totalTaxAllowances) + SFSupportTier);

		// Tax rate based off income of most military members.

		double taxrate = 0;

		if (totalTaxableSalary >= 90001.00) {
			taxrate = 0.37;
		}

		if (totalTaxableSalary <= 90000.00) {
			taxrate = 0.325;
		}

		// Tax on salary calculations - Edit if changes

		double taxTotal = 0;

		if (taxrate == 0.37) {
			taxTotal = ((totalTaxableSalary - 90000) * taxrate) + 20797;
		}

		if (taxrate == 0.325) {
			taxTotal = ((totalTaxableSalary - 37000) * taxrate) + 3572;
		}

		// Round up tax to nearest dollar value as done in Pay Slips.

		double superCon = (salary + serviceAllowance) * (superRate / 26.0714);
		double afterTaxDeduct = (ServResCon + OnBaseRent + superCon * 26.0714);

		// ANNUAL SUM

		gt.println("\n\nANNUAL SUMMARY");

		// 1
		gt.println("\n1. Gross Income + Allowances (Before Tax) : \n$" + df.format(totalSalary + rentAssist));

		// 2
		if (ServResCon >= 1 || OnBaseRent >= 1) {
			gt.println("\n2. Gross Income – DHA/Live-on :  \n$" + df.format(totalSalary - ServResCon - OnBaseRent));
		}
		if (rentAssist >= 1) {
			gt.println("\n2. Gross Income – RA – Rent :\n$" + df.format((totalSalary + rentAssist) - rentCost));
		}

		// 3
		gt.println("\n3. Tax : \n$" + df.format(taxTotal));

		// 4
		gt.println("\n4. Net Income (After Tax) : \n$"
				+ df.format((totalSalary - taxTotal) - afterTaxDeduct - BeforeTaxDeductions));

		// FORTNIGHT SUM

		gt.println("\nFORTNIGHT SUMMARY");

		// 1
		gt.println(
				"\n1. Gross Income + Allowances (Before Tax) : \n$" + df.format((totalSalary + rentAssist) / 26.0714));

		// 2
		if (ServResCon >= 1 || OnBaseRent >= 1) {
			gt.println("\n2. Gross Income – DHA/Live-on :  \n$"
					+ df.format((totalSalary - ServResCon - OnBaseRent) / 26.0714));
		}
		if (rentAssist >= 1) {
			gt.println(
					"\n2. Gross Income – RA – Rent :\n$" + df.format((totalSalary + rentAssist - rentCost) / 26.0714));
		}

		// 3
		gt.println("\n3. Tax : \n$" + df.format((taxTotal) / 26.0714));

		// 4
		gt.println("\n4. Net Income (After Tax) : \n$"
				+ df.format(((totalSalary - taxTotal) - afterTaxDeduct - BeforeTaxDeductions) / 26.0714));

		// 4.a.
		if (rentAssist >= 1) {
			gt.println("\n4.a. Net Income (After Tax & Rent) : \n$"
					+ df.format((((totalSalary - taxTotal) - afterTaxDeduct) - rentCost) / 26.0714));
		}

	}

}
