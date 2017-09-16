package osotnikov.demo.instantloan.test.business;

import org.junit.*;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;

import osotnikov.demo.instantloan.business.InterestCalculator;
import osotnikov.demo.instantloan.business.InterestCalculatorImpl;

public class InterestCalculatorTest {
	
	InterestCalculator interestCalculator = new InterestCalculatorImpl();
	
	@BeforeClass
	public static void beforeTests(){
		System.out.println("About to start tests for InterestCalculatorTest.");
	}
	
	@Before
	public void beforeTest(){
		System.out.println("About to start a test.");
	}
	
	@Test
	public void testTotalCompoundInterestCalculation(){
		// expected, actual, delta (i.e. error margin)
		assertEquals(610.51d, interestCalculator.calculateTotalCompoundInterest(1000, 0.1, 5), 0.01); 
		assertEquals(184.96d, interestCalculator.calculateTotalCompoundInterest(2564, 0.01, 7), 0.01); 
		assertEquals(184.96d, interestCalculator.calculateTotalCompoundInterest(2564, 0.01, 7), 0.01); 
		assertEquals(0d, interestCalculator.calculateTotalCompoundInterest(0, 0.00, 7), 0.01);
	}
	
	@Test
	public void testTotalSimpleInterestCalculation(){
		// expected, actual, delta (i.e. error margin)
		assertEquals(500.0d, interestCalculator.calculateTotalSimpleInterest(1000, 0.1, 5), 0.01); 
		assertEquals(179.48d, interestCalculator.calculateTotalSimpleInterest(2564, 0.01, 7), 0.01);  
		assertEquals(0d, interestCalculator.calculateTotalSimpleInterest(0, 0.00, 7), 0.01);
	}
	
}
