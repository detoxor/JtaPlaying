package cz.tomascejka.learn.jta.playing;

public interface JtaTransactionManager {
	
	void doInTransaction(JtaTransactionTemplate template) throws Exception;
}
