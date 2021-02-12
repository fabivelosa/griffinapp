package prodigiesApp;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class AppTest {
	@Test
	public void test() {
		AppMain myTest = new AppMain();
		assertEquals("Test Passed!", myTest.test());
	}
	
	@Test
	public void failingTest() {
		fail();
	}
}
