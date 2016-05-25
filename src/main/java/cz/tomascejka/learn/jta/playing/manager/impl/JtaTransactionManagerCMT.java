package cz.tomascejka.learn.jta.playing.manager.impl;

import javax.annotation.Resource;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
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
	private EJBContext context;
	@Resource
	private TransactionSynchronizationRegistry txReg;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@Override
	public void doInTransaction(JtaTransactionTemplate template) throws Exception {
		LOG.info("[TX={}, TXS={}] Start JTA", txReg.getTransactionKey(), txReg.getTransactionStatus());
		try {
			template.invoke();
		} catch (Exception e) {
			LOG.error("[TX="+txReg.getTransactionKey()+", TXS="+txReg.getTransactionStatus()+"] Problem during processing JTA transaction", e);
			throw e;
		} finally {
			LOG.info("[TX={}, TXS={}] End JTA", txReg.getTransactionKey(), txReg.getTransactionStatus());
		}
	}
}
