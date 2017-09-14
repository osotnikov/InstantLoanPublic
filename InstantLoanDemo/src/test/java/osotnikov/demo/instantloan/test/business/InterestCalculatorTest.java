package osotnikov.demo.instantloan.test.business;

import org.junit.*;
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
	public void test(){
		// expected, actual, delta (i.e. error margin)
		assertEquals(610.51d, interestCalculator.calculateTotalInterest(1000, 0.1, 5), 0.01); 
		assertEquals(184.96d, interestCalculator.calculateTotalInterest(2564, 0.01, 7), 0.01); 
		assertEquals(184.96d, interestCalculator.calculateTotalInterest(2564, 0.01, 7), 0.01); 
		assertEquals(0d, interestCalculator.calculateTotalInterest(0, 0.00, 7), 0.01);
	}
	
}
