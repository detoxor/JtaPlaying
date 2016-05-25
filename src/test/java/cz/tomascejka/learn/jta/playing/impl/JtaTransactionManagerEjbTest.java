package cz.tomascejka.learn.jta.playing.impl;

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import org.junit.Before;
import org.junit.Test;

import cz.tomascejka.learn.jta.playing.JtaTransactionTemplate;

public class JtaTransactionManagerEjbTest {

	private JtaTransactionManagerBMT tested;
	private UserTransaction utx = new UserTransaction() {
		@Override
		public void setTransactionTimeout(int seconds) throws SystemException {
			// do nothing ...
		}
		@Override
		public void setRollbackOnly() throws IllegalStateException, SystemException {
			throw new IllegalStateException("We use bean managed jta transaction processing ... ");
		}
		
		@Override
		public void rollback() throws IllegalStateException, SecurityException, SystemException {
			System.out.println("UTX ROLLBACK");
		}
		
		@Override
		public int getStatus() throws SystemException {
			return 0;
		}
		
		@Override
		public void commit() throws RollbackException, HeuristicMixedException, HeuristicRollbackException,
				SecurityException, IllegalStateException, SystemException {
			System.out.println("UTX COMMIT");
		}
		
		@Override
		public void begin() throws NotSupportedException, SystemException {
			System.out.println("UTX BEGIN");
		}
	};
	
	@Before
	public void setUp(){
		tested = new JtaTransactionManagerBMT(){
			@Override
			protected UserTransaction getUserTransaction() {
				return utx;
			}
		};
	}
	
	@Test
	public void shouldTest() throws Exception{
		tested.doInTransaction(new JtaTransactionTemplate() {
			@Override
			public void invoke() {
				System.out.println("INVOKE some process...");
			}
		});
	}
}
