package osotnikov.demo.instantloan.business;

import java.util.List;

import osotnikov.demo.instantloan.dao.entity.LoanApplication;

public interface LoanApplicationBusiness {

	public LoanApplication validateLoanApplication(LoanApplication loanApplication);
	public LoanApplication validateAndStoreLoanApplication(LoanApplication loanApplication)throws Exception;
	public List<LoanApplication> getLoanApplications();
	public LoanApplication getLoanApplication(long id);
	public LoanApplication updateExistingLoanApplication(LoanApplication loanApplication);
	public LoanApplication deleteLoanApplication(long id);
	
}