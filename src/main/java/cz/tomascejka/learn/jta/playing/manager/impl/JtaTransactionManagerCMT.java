package cz.tomascejka.learn.jta.playing.manager.impl;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.transaction.Status;
import javax.transaction.TransactionSynchronizationRegistry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.tomascejka.learn.jta.playing.manager.JtaTransactionManager;
import cz.tomascejka.learn.jta.playing.manager.JtaTransactionTemplate;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class JtaTransactionManagerCMT implements JtaTransactionManager {

	private static final Logger LOG = LoggerFactory.getLogger(JtaTransactionManagerCMT.class);
	
	@Resource
	private TransactionSynchronizationRegistry txReg;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@Override
	public void doInTransaction(JtaTransactionTemplate template) throws Exception {
		LOG.info("[TX={}, TXS={}] Start JTA", txReg.getTransactionKey(), getStatusName(txReg.getTransactionStatus()));
		try {
			template.invoke();
		} catch (Exception e) {
			LOG.error("[TX="+txReg.getTransactionKey()+", TXS="+getStatusName(txReg.getTransactionStatus())+"] Problem during processing JTA transaction", e);
			throw new RuntimeException(e);
		} finally {
			LOG.info("[TX={}, TXS={}] End JTA", txReg.getTransactionKey(), getStatusName(txReg.getTransactionStatus()));
		}
	}
	
	private String getStatusName(int status) {
		if(Status.STATUS_ACTIVE==status) return "STATUS_ACTIVE";
		if(Status.STATUS_COMMITTED==status) return "STATUS_COMMITTED";
		if(Status.STATUS_COMMITTING==status) return "STATUS_COMMITTING";
		if(Status.STATUS_MARKED_ROLLBACK==status) return "STATUS_MARKED_ROLLBACK";
		if(Status.STATUS_NO_TRANSACTION==status) return "STATUS_NO_TRANSACTION";
		if(Status.STATUS_PREPARED==status) return "STATUS_PREPARED";
		if(Status.STATUS_PREPARING==status) return "STATUS_PREPARING";
		if(Status.STATUS_ROLLEDBACK==status) return "STATUS_ROLLEDBACK";
		if(Status.STATUS_ROLLING_BACK==status) return "STATUS_ROLLING_BACK";
		if(Status.STATUS_UNKNOWN==status) return "STATUS_UNKNOWN";
		return "N/A";
	}
}
