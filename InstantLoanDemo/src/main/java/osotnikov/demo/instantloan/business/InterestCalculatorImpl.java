package osotnikov.demo.instantloan.business;

public class InterestCalculatorImpl implements InterestCalculator {

	@Override
	public double calculateTotalInterest(double amount, double interest, int intervals) {
		
		return amount*Math.pow(1 + interest, intervals) - amount;
	}

}
