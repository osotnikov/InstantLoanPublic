package osotnikov.demo.instantloan.business;

public interface InterestCalculator {
	
	public double calculateTotalCompoundInterest(double amount, double interest, int intervals);
	public double calculateTotalSimpleInterest(double amount, double interest, int intervals);
}
