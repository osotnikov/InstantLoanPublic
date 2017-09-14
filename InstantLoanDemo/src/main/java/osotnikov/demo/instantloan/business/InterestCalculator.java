package osotnikov.demo.instantloan.business;

public interface InterestCalculator {
	
	public double calculateTotalInterest(double amount, double interest, int intervals);
	
}
