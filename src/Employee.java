
public class Employee {
	private String lastName;
	private double annualGrossIncome;
	private double annualTax;
	private double annualNetIncome;
	private double annualEmployeeContributions;
	private double fortnightGrossIncome;
	private double fortnightTax;
	private double fortnightNetIncome;
	private double fortnightEmployeeContribution;

	public Employee(String lastName) {

		this.lastName = lastName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public double getFortnightGrossIncome() {
		return fortnightGrossIncome;
	}

	public void setFortnightGrossIncome(double fortnightGrossIncome) {
		this.fortnightGrossIncome = fortnightGrossIncome;
	}

	public double getFortnightTax() {
		return fortnightTax;
	}

	public void setFortnightTax(double fortnightTax) {
		this.fortnightTax = fortnightTax;
	}

	public double getFortnightNetIncome() {
		return fortnightNetIncome;
	}

	public void setFortnightNetIncome(double fortnightNetIncome) {
		this.fortnightNetIncome = fortnightNetIncome;
	}

	public double getFortnightEmployeeContribution() {
		return fortnightEmployeeContribution;
	}

	public void setFortnightEmployeeContribution(double fortnightEmployeeContribution) {
		this.fortnightEmployeeContribution = fortnightEmployeeContribution;
	}

	public void setAnnualGrossIncome(double annualGrossIncome) {
		this.annualGrossIncome = annualGrossIncome;
	}

	public void setAnnualTax(double annualTax) {
		this.annualTax = annualTax;
	}

	public void setAnnualNetIncome(double annualNetIncome) {
		this.annualNetIncome = annualNetIncome;
	}

	public void setAnnualEmployeeContributions(double annualEmployeeContributions) {
		this.annualEmployeeContributions = annualEmployeeContributions;
	}

	public String toString() {
		String message = "";
		message += this.lastName + ",";
		message += this.annualGrossIncome + ",";
		message += this.annualTax + ",";
		message += this.annualNetIncome + ",";
		message += this.annualEmployeeContributions;
		return (message);
	}

	public double getAnnualGrossIncome() {
		return annualGrossIncome;
	}

	public double getAnnualTax() {
		return annualTax;
	}

	public double getAnnualNetIncome() {
		return annualNetIncome;
	}

	public double getAnnualEmployeeContributions() {
		return annualEmployeeContributions;
	}

}
