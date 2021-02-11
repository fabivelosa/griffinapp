package testCode;

import static org.junit.Assert.*;
import org.junit.Test;
import pack1.TrialRun;

public class testTrialRun {

	@Test
	public void testProgram() {
		
		TrialRun t = new TrialRun();
		int ans = t.testSum();
		assertEquals(4, ans);
	}

}
