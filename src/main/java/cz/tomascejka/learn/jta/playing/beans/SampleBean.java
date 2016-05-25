package cz.tomascejka.learn.jta.playing.beans;

import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import cz.tomascejka.learn.jta.playing.manager.JtaTransactionManager;
import cz.tomascejka.learn.jta.playing.manager.JtaTransactionTemplate;

@Singleton
@Startup
public class SampleBean {

	@EJB(beanName="JtaTransactionManagerCMT")
	private JtaTransactionManager txm;
	
	@Schedule(second="*/3", minute="*", hour="*")
	public void doThatRuntime() throws Exception {
		txm.doInTransaction(new JtaTransactionTemplate() {
			
			@Override
			public void invoke() {
				throw new RuntimeException("runtime exception ...");
			}
		});
	}
	
	//@Schedule(second="*/3", minute="*", hour="*")
	public void doThat() throws Exception {
		txm.doInTransaction(new JtaTransactionTemplate() {
			
			@Override
			public void invoke() {  
				System.out.println("do someting ..");
			}
		});
	}
}
