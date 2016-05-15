package cz.tomascejka.learn.jta.playing.impl;

import javax.annotation.Resource;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.transaction.UserTransaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.tomascejka.learn.jta.playing.JtaTransactionManager;
import cz.tomascejka.learn.jta.playing.JtaTransactionTemplate;

/**
 * 
 * @author tomas.cejka
 * 
 * @see http://what-when-how.com/enterprise-javabeans-3/bean-managed-transactions-ejb-3/
 * @see https://geertschuring.wordpress.com/2008/10/07/how-to-use-bean-managed-transactions-with-ejb3-jpa-and-jta/
 * @see http://www.developerscrappad.com/435/java/java-ee/ejb-3-x-jpa-bean-managed-transaction-with-javax-ejb-usertransaction/
 */
@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
public class JtaTransactionManagerEjb implements JtaTransactionManager {
	
	private static final Logger LOG = LoggerFactory.getLogger(JtaTransactionManagerEjb.class);
	
	@Resource
	private EJBContext context;

	@Override
	public void doInTransaction(JtaTransactionTemplate template) throws Exception {
		UserTransaction utx = context.getUserTransaction();
		try {
			utx.begin();
			LOG.info("[{}] BEGIN JTA transaction", template);
			template.invoke();// ..invoke multiples of em.persists(), em.merge() or em.remove()
			utx.commit();
			LOG.info("[{}] COMMIT JTA transaction", template);
		} catch (Exception ex) {
			// When something is wrong, just rollback to the state before
			// calling<!--DVFMTSC--> latest utx.begin();
			utx.rollback();
			LOG.error("[{}] ROLLBACK JTA transaction", template);
			throw ex;
		} finally {
			LOG.info("[{}] END invocation", template);
		}
	}
}
