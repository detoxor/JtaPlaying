package cz.tomascejka.learn.jta.playing.manager.impl;
import static org.bitbucket.dollar.Dollar.*;

import org.bitbucket.dollar.Ranges;
import org.junit.Test;

public class RandomStrings {
	// "0123456789" + "ABCDE...Z"
	private String validCharacters = $('0', '9').join() + $('A', 'Z').join()+ $('a', 'z').join();
	
	String randomString(int length) {
	    return $(validCharacters).shuffle().slice(length).toString();
	}
	
	@Test
	public void testString() {
		for (int i : Ranges.upto(5)) {
	        System.out.println(randomString(8));
	    }
	}
}
