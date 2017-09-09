package osotnikov.demo.instantloan.dao;

import java.util.Calendar;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.hibernate.Criteria;

import osotnikov.demo.instantloan.constants.LoanReqStatus;
import osotnikov.demo.instantloan.dao.entity.LoanApplication;

@Component
public class LoanApplicationDaoImpl implements LoanApplicationDao {

	@Autowired
	private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
	
	@Override
	public List<LoanApplication> getLoanApplications() {
		Session session = this.sessionFactory.openSession();
		Transaction tx = session.beginTransaction(); // Default isolation level in oracle is READ_COMMITED.
		List<LoanApplication> loanApplicationsList = session.createQuery("from LoanApplication").list();
		tx.commit();
		session.close();
		return loanApplicationsList;
	}

	@Override
	public LoanApplication getLoanApplication(long id) {
		Session session = this.sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		LoanApplication loanApplication = (LoanApplication)session.get(LoanApplication.class, id);
		tx.commit();
		session.close();
		return loanApplication;
	}

	@Override
	public LoanApplication storeLoanApplication(LoanApplication loanApplication) {
		Session session = this.sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		loanApplication.setId(null); // Hibernate will think this entity is detached if you have set the id previously.
		session.persist(loanApplication);
		tx.commit();
		tx = session.beginTransaction(); 
		session.refresh(loanApplication); // Make sure the generated id value is read from the database.
		tx.commit();
		session.close();
		return loanApplication;
	}

	/** Supposed to update an already existing entity with the provided id.
	 *  Creating a new entity with a custom id is not supported. */
	@Override
	public LoanApplication updateExistingLoanApplication(LoanApplication loanApplication) {
		LoanApplication oldLoanApplication = getLoanApplication(loanApplication.getId());
		if(oldLoanApplication == null){
			return null;
		}else{
			Session session = this.sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			oldLoanApplication.setAmount(loanApplication.getAmount());
			oldLoanApplication.setFullName(loanApplication.getFullName());
			oldLoanApplication.setIp(loanApplication.getIp());
			oldLoanApplication.setStatus(loanApplication.getStatus());
			oldLoanApplication.setSubmissionTimestamp(loanApplication.getSubmissionTimestamp());
			oldLoanApplication.setTerm(loanApplication.getTerm());
			oldLoanApplication.setRejectionReason(loanApplication.getRejectionReason());
			session.update(oldLoanApplication);
			tx.commit();
			session.close();
			return oldLoanApplication;
		}
	}

	@Override
	public LoanApplication deleteLoanApplication(long id) {
		Session session = this.sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		LoanApplication loanApplication = (LoanApplication)session.load(LoanApplication.class,id);
		session.delete(loanApplication);
		tx.commit();
		return loanApplication;
	}
	
	@Override
	public List<LoanApplication> getAcceptedLoanApplicationsByIpWithinLastHours(String ip, int hours){
		
		Session session = this.sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		
		Calendar c = Calendar.getInstance();
		c.add(Calendar.HOUR_OF_DAY, -hours); // last number of hours
		Criteria criteria = session.createCriteria(LoanApplication.class);
		criteria.add(Restrictions.ge("submissionTimestamp", c.getTime()));
		criteria.add(Restrictions.eq("ip", ip));
		criteria.add(Restrictions.eq("status", LoanReqStatus.ACCEPTED));
		List<LoanApplication> results = (List<LoanApplication>)criteria.list();
		
		tx.commit();
		session.close();
		
		return results;
	}

}
