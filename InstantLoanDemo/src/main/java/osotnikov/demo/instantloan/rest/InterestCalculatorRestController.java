package osotnikov.demo.instantloan.rest;
 
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import osotnikov.demo.instantloan.business.InterestCalculator;
import osotnikov.demo.instantloan.business.LoanApplicationBusiness;
import osotnikov.demo.instantloan.constants.LoanReqStatus;
import osotnikov.demo.instantloan.constants.TypeOfInterest;
import osotnikov.demo.instantloan.dao.LoanApplicationDao;
import osotnikov.demo.instantloan.dao.entity.LoanApplication;
 
@RestController
@RequestMapping(value = "/rest")
public class InterestCalculatorRestController {
	
	@Autowired
	InterestCalculator interestCalculator;
 
    @RequestMapping(value = "/tools/interest_calculator", method = RequestMethod.GET, 
    	produces = MediaType.APPLICATION_JSON_VALUE)
    public double getInterestTotalAmount(@RequestParam("typeOfInterest") TypeOfInterest typeOfInterest, 
    	@RequestParam("amount") double amount,
    	@RequestParam("interest") double interest, @RequestParam("intervals") int intervals) {

    	double totalInterest;
    	
    	if(TypeOfInterest.COMPOUND.equals(typeOfInterest)){
    		totalInterest = interestCalculator.calculateTotalCompoundInterest(amount, interest, intervals);
    	}else if(TypeOfInterest.SIMPLE.equals(typeOfInterest)){
    		totalInterest = interestCalculator.calculateTotalSimpleInterest(amount, interest, intervals);
    	}else{
    		totalInterest = -1;
    	}

        return totalInterest;
    }
 
}