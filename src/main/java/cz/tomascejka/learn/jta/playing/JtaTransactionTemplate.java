package cz.tomascejka.learn.jta.playing;

/**
 * Block of code is executed between start/commit of JTA transaction
 * 
 * @author tomas.cejka
 */
public interface JtaTransactionTemplate {

	/**
	 * Invoke code inside of JTA transaction (between begin/commit commands)
	 */
	void invoke();

}
