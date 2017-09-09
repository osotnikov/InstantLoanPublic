package osotnikov.demo.instantloan.rest;
 
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import osotnikov.demo.instantloan.business.LoanApplicationBusiness;
import osotnikov.demo.instantloan.constants.LoanReqStatus;
import osotnikov.demo.instantloan.dao.LoanApplicationDao;
import osotnikov.demo.instantloan.dao.entity.LoanApplication;
 
@RestController
@RequestMapping(value = "/rest")
public class InstantLoanRestController {
	
	@Autowired
	LoanApplicationBusiness loanAppBusiness;
	
	private String adminPassword = "osotnikov";
	      
    @RequestMapping(value = "/loan_applications", method = RequestMethod.GET, 
		produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<LoanApplication>> getAllLoanApplications() {
       
    	System.out.println("InstantLoanRestController.getAllLoanApplications was called!");
    	
    	List<LoanApplication> loanApplications;
		try {
			loanApplications = loanAppBusiness.getLoanApplications();
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<List<LoanApplication>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
        if(loanApplications.isEmpty()){
            return new ResponseEntity<List<LoanApplication>>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<List<LoanApplication>>(loanApplications, HttpStatus.OK);
    }
 
    @RequestMapping(value = "/loan_applications/{id}", method = RequestMethod.GET, 
    	produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LoanApplication> getLoanApplication(@PathVariable("id") long id) {
    	
    	LoanApplication loanApplication;
		try {
			loanApplication = loanAppBusiness.getLoanApplication(id);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<LoanApplication>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
        if (loanApplication == null) {
            System.out.println("loanApplication with id " + id + " not found");
            return new ResponseEntity<LoanApplication>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<LoanApplication>(loanApplication, HttpStatus.OK);
    }
 
    @RequestMapping(value = "/loan_applications", method = RequestMethod.POST, 
    	produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LoanApplication> postLoanApplication(@RequestBody LoanApplication loanApplication,
    	UriComponentsBuilder ucBuilder, HttpServletRequest request) {
    	
    	loanApplication.setSubmissionTimestamp(new Date());
    	loanApplication.setIp(request.getRemoteAddr());
        System.out.println("Processing LoanApplication " + loanApplication.toString());
        try {
			loanAppBusiness.validateAndStoreLoanApplication(loanApplication);
		} catch (Exception e) {
			System.out.println("Loan application failed because of an internal server error!");
			loanApplication.setStatus(LoanReqStatus.FAILED);
			loanApplication.setRejectionReason("Loan application failed because of an internal server error!");
			e.printStackTrace();
			return new ResponseEntity<LoanApplication>(loanApplication, HttpStatus.INTERNAL_SERVER_ERROR);
		}
        LoanReqStatus loanReqStatus = loanApplication.getStatus();
        
        if ( ! LoanReqStatus.ACCEPTED.equals(loanReqStatus)) {
            System.out.println("The LoanApplication " + loanApplication.toString() + " was not accepted because: " + 
            	loanApplication.getRejectionReason());
            return new ResponseEntity<LoanApplication>(loanApplication, HttpStatus.CONFLICT);
        }else{
        	HttpHeaders headers = new HttpHeaders();
            headers.setLocation(ucBuilder.path(
            	"/loan_applications/{id}").buildAndExpand(loanApplication.getId()).toUri());
            return new ResponseEntity<LoanApplication>(loanApplication, headers, HttpStatus.CREATED);
        }
 
    }
    
    @RequestMapping(value = "/loan_applications/{id}", method = RequestMethod.PUT,
    		consumes = MediaType.APPLICATION_JSON_VALUE,
    		produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LoanApplication> updateLoanApplication(
    		@PathVariable("id") long id, 
    		@RequestBody LoanApplication loanApplication,
    		@RequestHeader("adminPasswordHeader") String adminPasswordHeader) {
    	
    	if( ! adminPassword.equals(adminPasswordHeader)){
    		System.out.println("PUT is a protected operation, unauthorized access detected.");
    		loanApplication.setStatus(LoanReqStatus.REJECTED);
    		loanApplication.setRejectionReason("PUT is a protected operation, unauthorized access detected.");
    		return new ResponseEntity<LoanApplication>(loanApplication, HttpStatus.UNAUTHORIZED);
    	}
    	
    	System.out.println("Updating LoanApplication with id " + id + ".");
    	
    	// A LOAN APPLICATION WITH THE PROVIDED ID MUST ALREADY EXIST (technically this should not be the case
    	// for a PUT request)
    	
    	LoanApplication oldLoanApplication;
    	
        try {
			oldLoanApplication = loanAppBusiness.getLoanApplication(id);
		} catch (Exception e) {
			System.out.println("Loan application update failed because of an internal server error!");
			loanApplication.setStatus(LoanReqStatus.FAILED);
			loanApplication.setRejectionReason("Loan application update failed because of an internal server error!");
			e.printStackTrace();
			return new ResponseEntity<LoanApplication>(loanApplication, HttpStatus.INTERNAL_SERVER_ERROR);
		}
         
        if ( oldLoanApplication == null) {
            System.out.println("LoanApplication with id " + id + " was not found, normally PUT should create "
            	+ "a new reousrce but this is not supported for simplicity reasons!");
            loanApplication.setStatus(LoanReqStatus.FAILED);
            loanApplication.setRejectionReason("LoanApplication with id " + id + " was not found, "
                + "normally PUT should create a new resource but this is not supported for simplicity reasons!");
            
            return new ResponseEntity<LoanApplication>(oldLoanApplication, HttpStatus.NOT_IMPLEMENTED);
        }else{
        	try {
        		loanApplication.setId(id);
        		loanAppBusiness.updateExistingLoanApplication(loanApplication);
			} catch (Exception e) {
				System.out.println("Loan application update failed because of an internal server error!");
				loanApplication.setStatus(LoanReqStatus.FAILED);
				loanApplication.setRejectionReason("Loan application update failed because of an internal server error!");
				e.printStackTrace();
				return new ResponseEntity<LoanApplication>(loanApplication, HttpStatus.INTERNAL_SERVER_ERROR);
			}
        	
        	// UPDATE SUCCESSFUL
            return new ResponseEntity<LoanApplication>(loanApplication, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/loan_applications/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<LoanApplication> deleteLoanApplication(@PathVariable("id") long id,
    		 @RequestHeader("adminPasswordHeader") String adminPasswordHeader) {
    	
    	if( ! this.adminPassword.equals(adminPasswordHeader)){
    		System.out.println("DELETE is a protected operation, unauthorized access detected.");
    		return new ResponseEntity<LoanApplication>(HttpStatus.UNAUTHORIZED);
    	}
    	
    	System.out.println("Deleting LoanApplication with " + id + ".");
    	
    	LoanApplication loanApplication = null;
    	
    	try {
			loanApplication = loanAppBusiness.getLoanApplication(id);
			if(loanApplication != null){
				loanAppBusiness.deleteLoanApplication(id);
				loanApplication.setStatus(LoanReqStatus.DELETED);
		        return new ResponseEntity<LoanApplication>(loanApplication, HttpStatus.OK);
			}else{
				return new ResponseEntity<LoanApplication>(loanApplication, HttpStatus.NOT_FOUND);
			}
			
		} catch (Exception e) {
			System.out.println("Loan application deletion failed because of an internal server error!");
			e.printStackTrace();
			if(loanApplication != null){
				loanApplication.setStatus(LoanReqStatus.FAILED);
				loanApplication.setRejectionReason("Loan application deletion failed because of an internal server error!");	
			}
			
			return new ResponseEntity<LoanApplication>(loanApplication, HttpStatus.INTERNAL_SERVER_ERROR);
		}
    	
    }
 
}