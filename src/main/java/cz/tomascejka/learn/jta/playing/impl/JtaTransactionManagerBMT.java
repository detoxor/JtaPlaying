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
 * @see http://www.kumaranuj.com/2013/05/jpa-2-understanding-relationships.html
 * @see http://www.javatpoint.com/hibernate-transaction-management-example
 * @see https://docs.oracle.com/cd/E19798-01/821-1841/bncih/index.html
 * 
 * Examples
 * @see http://what-when-how.com/enterprise-javabeans-3/bean-managed-transactions-ejb-3/ << DIRECT
 * @see https://en.wikibooks.org/wiki/Java_Persistence/Transactions#Example_JTA_transaction << JNDI
 * @see https://geertschuring.wordpress.com/2008/10/07/how-to-use-bean-managed-transactions-with-ejb3-jpa-and-jta/
 * @see http://www.developerscrappad.com/435/java/java-ee/ejb-3-x-jpa-bean-managed-transaction-with-javax-ejb-usertransaction/ << EJB_CONTEXT
 * 
 * Study
 * @see https://www.subbu.org/articles/jts/JTS.html << rules
 * @see http://www.mastercorp.free.fr/Ing1/Cours/Java/java_lesson2/lightGamelan/misc/Transaction%20Services%20with%20JTA%20and%20JTS-1.htm << rules with images
 * @see http://java.boot.by/ibm-287/ch05s02.html
 * @see http://java.boot.by/scbcd5-guide/ch09s05.html
 * @see http://www.muskingum.edu/~reichard/J2EE/j2eetutorial/doc/Transaction4.html << MAYBE OBSOLETE
 * @see http://www2.sys-con.com/itsg/virtualcd/java/archives/0504/tyagi/index.html << MAYBE OBSOLETE
 * @see http://docstore.mik.ua/orelly/java-ent/ebeans/ch08_05.htm << MAYBE OBSOLETE
 * @see http://www.slideserve.com/sana/java-transaction-api
 * @see http://www.di.unipi.it/~ghelli/didattica/bdldoc/A97329_03/web.902/a95879/jta.htm
 * @see https://www.progress.com/jdbc/resources/tutorials/understanding-jta/distributed-transactions-and-the-transaction-manager
 * 
 * Q/A
 * @see http://stackoverflow.com/questions/5985038/jta-transactions-committing-too-early-fails-when-using-constraints
 * @see http://stackoverflow.com/questions/13370443/use-jta-transaction-with-ejb-and-jpa
 * @see http://www.mastertheboss.com/jboss-server/jboss-datasource/demystifying-datasource-jta-and-xa-settings-on-jboss-wildfly
 * 
 * Testing
 * @see https://testablejava.wordpress.com/2011/12/30/standalone-jta-integration-testing-using-atomikos/
 * @see http://tomee.apache.org/testing-transactions-example.html
 * @see http://www.oracle.com/technetwork/articles/java/integrationtesting-487452.html
 */
@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
public class JtaTransactionManagerBMT implements JtaTransactionManager {
	
	private static final Logger LOG = LoggerFactory.getLogger(JtaTransactionManagerBMT.class);
	
	@Resource
	private EJBContext context;

	@Override
	public void doInTransaction(JtaTransactionTemplate template) throws Exception {
		UserTransaction utx = getUserTransaction();
		try {
			LOG.info("[{}] BEGIN JTA transaction", template);
			utx.begin();
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
	
	/**
	 * @return JTA user transaction for manually processing begin/commit methods
	 */
	protected UserTransaction getUserTransaction(){
		return context.getUserTransaction();
	}
}
