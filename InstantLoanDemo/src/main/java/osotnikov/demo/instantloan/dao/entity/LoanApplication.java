package osotnikov.demo.instantloan.dao.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Transient;

import osotnikov.demo.instantloan.constants.LoanReqStatus;

@Entity
@Table(name="LOAN_APPLICATION")
public class LoanApplication {
	
	private Long id;
	private String fullName;
	private String ip;
	private int term; // in months
	private long amount;
	private Date submissionTimestamp;
	private LoanReqStatus status;
	private String rejectionReason;
	
	public LoanApplication() {}
	
	public LoanApplication(String fullName, String ip, int term, long amount, Date submissionTimestamp,
			LoanReqStatus status, String rejectionReason) {
		super();
		this.fullName = fullName;
		this.ip = ip;
		this.term = term;
		this.amount = amount;
		this.submissionTimestamp = submissionTimestamp;
		this.status = status;
		this.rejectionReason = rejectionReason;
	}
	
	@Id
	@Column(name="ID")
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	@Column(name="FULL_NAME",  length=1024)
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	
	@Column(name="IP",  length=128)
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	
	@Column(name="TERM")
	public int getTerm() {
		return term;
	}
	public void setTerm(int term) {
		this.term = term;
	}
	
	@Column(name="AMOUNT")
	public long getAmount() {
		return amount;
	}
	public void setAmount(long amount) {
		this.amount = amount;
	}
	
	@Column(name="SUBMISSION_TIMESTAMP")
	@Temporal(javax.persistence.TemporalType.TIMESTAMP)
	public Date getSubmissionTimestamp() {
		return submissionTimestamp;
	}
	public void setSubmissionTimestamp(Date submissionTimestamp) {
		this.submissionTimestamp = submissionTimestamp;
	}
	
	@Enumerated(EnumType.STRING)
	@Column(name="STATUS",  length=64)
	public LoanReqStatus getStatus() {
		return status;
	}
	public void setStatus(LoanReqStatus status) {
		this.status = status;
	}

	@Column(name="REJECTION_REASON", length=256)
	public String getRejectionReason() {
		return rejectionReason;
	}
	public void setRejectionReason(String rejectionReason) {
		this.rejectionReason = rejectionReason;
	}

	@Override
	public String toString() {
		return "LoanApplication [id=" + id + ", fullName=" + fullName + ", ip=" + ip + ", term=" + term + ", amount="
				+ amount + ", submissionTimestamp=" + submissionTimestamp + ", status=" + status + ", rejectionReason="
				+ rejectionReason + "]";
	}
	
}
