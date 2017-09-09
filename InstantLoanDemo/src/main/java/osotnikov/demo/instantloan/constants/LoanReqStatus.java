package osotnikov.demo.instantloan.constants;

public enum LoanReqStatus {
	
	FAILED("FAILED"),
	REJECTED("REJECTED"),
	ACCEPTED("ACCEPTED"),
	DELETED("DELETED");
	
	private String reqStatus;
	
	private LoanReqStatus(String reqStatus){
		this.reqStatus = reqStatus;
	}
}
