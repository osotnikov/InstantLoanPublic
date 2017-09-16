package osotnikov.demo.instantloan.business;

import org.springframework.stereotype.Component;

@Component
public class InterestCalculatorImpl implements InterestCalculator {

	@Override
	public double calculateTotalCompoundInterest(double amount, double interest, int intervals) {
		
		return amount*Math.pow(1 + interest, intervals) - amount;
	}

	@Override
	public double calculateTotalSimpleInterest(double amount, double interest, int intervals) {
		// TODO Auto-generated method stub
		return amount*interest*intervals;
	}

}
