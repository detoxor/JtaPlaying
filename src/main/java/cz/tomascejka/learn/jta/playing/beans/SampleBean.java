package cz.tomascejka.learn.jta.playing.beans;

import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.tomascejka.learn.jta.playing.manager.JtaTransactionManager;
import cz.tomascejka.learn.jta.playing.manager.JtaTransactionTemplate;

@Singleton
@Startup
public class SampleBean {

	private static final Logger LOG = LoggerFactory.getLogger(SampleBean.class);

	@EJB(beanName = "JtaTransactionManagerCMT")
	private JtaTransactionManager txm;
	
	private int counter = 0;

	@Schedule(second = "*/30", minute = "*", hour = "*")
	public void doThatRuntime() throws Exception {
		
		try {
			txm.doInTransaction(new JtaTransactionTemplate() {
				@Override
				public void invoke() {
	//				TransactionManagerDelegate tx = (com.arjuna.ats.jbossatx.jta.TransactionManagerDelegate) com.arjuna.ats.jta.TransactionManager
	//						.transactionManager();
	//				try {
	//					Transaction arjunaTM = (com.arjuna.ats.jta.transaction.Transaction) tx.getTransaction();
	//					//LOG.info("Transaction UID" + arjunaTM.get_uid());
	//				} catch (SystemException e) {
	//					// TODO Auto-generated catch block
	//					e.printStackTrace();
	//				}
					if(counter < 1){
						LOG.error("Runtime exception");
						throw new RuntimeException("runtime exception ... counter="+counter++);
					}
				}
			});
		} catch (Exception e) {
			LOG.error("Transaction fail catched...", e);
		}
	}

	// @Schedule(second="*/3", minute="*", hour="*")
	public void doThat() throws Exception {
		txm.doInTransaction(new JtaTransactionTemplate() {

			@Override
			public void invoke() {
				System.out.println("do someting ..");
			}
		});
	}
}
