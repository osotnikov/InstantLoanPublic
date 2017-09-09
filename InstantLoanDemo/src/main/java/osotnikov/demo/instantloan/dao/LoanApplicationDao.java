package osotnikov.demo.instantloan.dao;

import java.util.List;

import osotnikov.demo.instantloan.dao.entity.LoanApplication;

public interface LoanApplicationDao {
	
	public List<LoanApplication> getLoanApplications();
	public LoanApplication getLoanApplication(long id);
	public LoanApplication storeLoanApplication(LoanApplication loanApplication);
	public LoanApplication updateExistingLoanApplication(LoanApplication loanApplication);
	public LoanApplication deleteLoanApplication(long id);
	public List<LoanApplication> getAcceptedLoanApplicationsByIpWithinLastHours(String ip, int hours);
}
