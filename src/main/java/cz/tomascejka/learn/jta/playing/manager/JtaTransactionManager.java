package cz.tomascejka.learn.jta.playing.manager;

/**
 * High level interface for processing JTA transaction
 * 
 * @author tomas.cejka
 */
public interface JtaTransactionManager {
	
	/**
	 * Allows to setup template process between in order to be executed/invoked between begin/commit (border JTA operations)
	 * 
	 * @param template keeps code which can be invoked between begin/commit
	 * @throws Exception if something failed during template invocation
	 */
	void doInTransaction(JtaTransactionTemplate template) throws Exception;
}
