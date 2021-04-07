package com.callfailures.entity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import org.mockito.runners.MockitoJUnitRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(MockitoJUnitRunner.class)
public class UserFunctionsTest {
	
	private User user;
	
	private String password = "Password1";
	private String encrypted = "UGFzc3dvcmQx";
	private String token = "f217c2b6-95fe-41ab-9a3a-8e3ef33fa44b";
	
	@Before
	public void setup() throws Exception{
		user = new User();
				
	}
	
	@Test
	public void testEncryptionSuccess() {	
		assertEquals((user.passEncrypt(password)), encrypted);
	}
	
	@Test
	public void testDecryptionSuccess() {
		assertEquals((user.passDecrypt(encrypted)), password);
	}
	
	@Test
	public void testEncryptFailure() {
		assertNotEquals((user.passEncrypt("fail")), encrypted);
	}
	@Test
	public void testDecryptionFailure() {
		assertNotEquals((user.passDecrypt("fail")), password);
	}
	
	@Test
	public void testTokenGetterSetter() {
		user.setToken(token);
		assertEquals(user.getToken(), token);
	}
	

}
