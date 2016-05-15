package cz.tomascejka.learn.jta.playing;

/**
 * High level interface for processing JTA transaction
 * 
 * @author tomas.cejka
 *
 */
public interface JtaTransactionManager {
	
	/**
	 * Allows to setup template process between begin/commit (border jta operations) and invoke its code
	 * 
	 * @param template keeps code which can be invoked between begin/commit
	 * @throws Exception if something failed during template invocation
	 */
	void doInTransaction(JtaTransactionTemplate template) throws Exception;
}
