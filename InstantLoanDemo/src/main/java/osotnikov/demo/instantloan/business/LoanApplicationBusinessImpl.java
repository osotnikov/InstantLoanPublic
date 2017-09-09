package osotnikov.demo.instantloan.business;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import osotnikov.demo.instantloan.constants.LoanReqStatus;
import osotnikov.demo.instantloan.dao.LoanApplicationDao;
import osotnikov.demo.instantloan.dao.entity.LoanApplication;

@Component
@PropertySource("classpath:config.properties")
public class LoanApplicationBusinessImpl implements LoanApplicationBusiness {
	
	// Configuration parameters
	
	@Value("#{new Integer('${suspiciousHourPeriodBegin}')}")
	private Integer suspiciousHourPeriodBegin; // hour in 24 hour format
	@Value("#{new Integer('${suspiciousHourPeriodEnd}')}")
	private Integer suspiciousHourPeriodEnd;
	
	@Value("#{new Integer('${minAmount}')}")
	private Integer minAmount; // in euros
	@Value("#{new Integer('${maxAmount}')}")
	private Integer maxAmount;
	
	@Value("#{new Integer('${minTerm}')}")
	private Integer minTerm; // in months
	@Value("#{new Integer('${maxTerm}')}")
	private Integer maxTerm; 
	
	@Value("#{new Integer('${maxAppsAllowedInPeriod}')}")
	private Integer maxAppsAllowedInPeriod;
	@Value("#{new Integer('${appCountResetPeriod}')}")
	private Integer appCountResetPeriod; // in hours
	
	@Autowired
	LoanApplicationDao loanApplicationDao;
	
	/* (non-Javadoc)
	 * @see osotnikov.demo.instantloan.business.LoanApplicationBusiness#validateLoanApplication(osotnikov.demo.instantloan.dao.entity.LoanApplication)
	 */
	@Override
	public LoanApplication validateLoanApplication(LoanApplication loanApplication){
		
		if(StringUtils.isEmpty(loanApplication.getIp()) ||
		   StringUtils.isEmpty(loanApplication.getFullName()) ||
		   loanApplication.getSubmissionTimestamp() == null){
			// MISSING INFORMATION RULE
		   
			loanApplication.setStatus(LoanReqStatus.REJECTED);
			loanApplication.setRejectionReason("Empty fields are not allowed!");
			return loanApplication;
		}else{
			
			if(loanApplication.getTerm() < minTerm ||
			   loanApplication.getTerm() > maxTerm){
				// TERM RANGE RULE
				
				loanApplication.setStatus(LoanReqStatus.REJECTED);
				loanApplication.setRejectionReason("Term must be between these values: " + minTerm + " and "
					+ maxTerm);
				return loanApplication;
			}else if( loanApplication.getAmount() < minAmount ||
				loanApplication.getAmount() > maxAmount){
				// AMOUNT RANGE RULE
				
				loanApplication.setStatus(LoanReqStatus.REJECTED);
				loanApplication.setRejectionReason("Amount must be between these values: " + minAmount + " and "
					+ maxAmount);
				return loanApplication;
			}
			
			// HOUR OF THE DAY RANGE RULE (applied only in case of max requested amount)
			
			if( loanApplication.getAmount() == maxAmount){
				
				Date subTime = loanApplication.getSubmissionTimestamp();
				Calendar subTimeC = Calendar.getInstance();
				subTimeC.setTime(subTime);
				int subTimeHour = subTimeC.get(Calendar.HOUR_OF_DAY);
				
				if(suspiciousHourPeriodBegin <= subTimeHour &&
				   subTimeHour < suspiciousHourPeriodEnd){
					
					loanApplication.setStatus(LoanReqStatus.REJECTED);
					loanApplication.setRejectionReason("It is not allowed to submit for a loan " + 
						"with the maximum amount between the " + suspiciousHourPeriodBegin + " and " + 
						suspiciousHourPeriodEnd + " hour of the day.");
					return loanApplication;
				}
			}
			
			// MAXIMUM NUMBER OF LOAN APPLICATIONS ALLOWED PER DAY RULE
			List<LoanApplication> loanApplications = 
				loanApplicationDao.getAcceptedLoanApplicationsByIpWithinLastHours(
					loanApplication.getIp(), appCountResetPeriod);
			if(loanApplications.size() >= maxAppsAllowedInPeriod){
				loanApplication.setStatus(LoanReqStatus.REJECTED);
				loanApplication.setRejectionReason("You are allowed to make " + maxAppsAllowedInPeriod + 
					" applications within a period of " + appCountResetPeriod + " hours.");
				return loanApplication;
			}
			
			// ALL CHECKS PASSED
			loanApplication.setStatus(LoanReqStatus.ACCEPTED);
			loanApplication.setRejectionReason(null);
			return loanApplication;
			
		}
	}
	
	@Override
	@Transactional
	public LoanApplication validateAndStoreLoanApplication(LoanApplication loanApplication) throws Exception{
		
		try {
			// VALIDATE
			validateLoanApplication(loanApplication);
		} catch (Exception e) {
			loanApplication.setStatus(LoanReqStatus.FAILED);
			loanApplication.setRejectionReason("Due to an internal server error the application was not processed!");
			e.printStackTrace();
		}
		
		// Generate dummy exception to test transactional behavior.
		if("Exception Generator".equals(loanApplication.getFullName())){
			throw new Exception("Dummy Exception for Transactional Behavior Test");
		}
		
		// STORE
		loanApplicationDao.storeLoanApplication(loanApplication);
		
		return loanApplication;
	}

	@Override
	@Transactional
	public List<LoanApplication> getLoanApplications() {
		
		return loanApplicationDao.getLoanApplications();
		
	}

	@Override
	@Transactional
	public LoanApplication getLoanApplication(long id) {
		
		return loanApplicationDao.getLoanApplication(id);
	}

	@Override
	@Transactional
	public LoanApplication updateExistingLoanApplication(LoanApplication loanApplication) {

		return loanApplicationDao.updateExistingLoanApplication(loanApplication);
	}

	@Override
	@Transactional
	public LoanApplication deleteLoanApplication(long id) {

		return loanApplicationDao.deleteLoanApplication(id);
	}
	
}
