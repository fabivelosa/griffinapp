package com.callfailures.services.impl;

import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.ejb.Stateless;
import javax.inject.Inject;

import com.callfailures.dao.UsersDAO;
import com.callfailures.entity.User;
import com.callfailures.services.UserService;

@Stateless
public class UserServiceImpl implements UserService {
	
	private User user;
	

	@Inject
	UsersDAO userDao;

	
	@Override
	public User getUserById(final String UserId) {
		return userDao.getUserByUserId(UserId);
	}
	
	@Override
	public User getUserByName(final String username) {
		return userDao.getUserByName(username);
	}

	@Override
	public void addUser(final User user) {
		userDao.addUser(user);
	}

	@Override
	public void updateUser(final User user) {
		userDao.updateUser(user);
	}
	
	public String passEncrypt(String input) throws Exception {
		//input = user.getUserPassword();
		String ALGORITHM = "AES" ;
		
		KeyGenerator keygen = KeyGenerator.getInstance(ALGORITHM);
		SecretKey key = keygen.generateKey();
		Cipher eCipher = Cipher.getInstance(ALGORITHM);
		eCipher.init(Cipher.ENCRYPT_MODE, key);
		
		byte[] cleartext = input.getBytes();
		byte[] ciphertext = eCipher.doFinal(cleartext);
		String encryptedPassword = Base64.getEncoder().encodeToString(ciphertext);

		return encryptedPassword;
	}
	
	public String passDecrypt() throws Exception {
		String encrypted = user.getUserPassword();
		byte[] decode = encrypted.getBytes();
		String ALGORITHM = "AES" ;
		KeyGenerator keygen = KeyGenerator.getInstance(ALGORITHM);

		SecretKey key = keygen.generateKey();

		//decryption
		Cipher dCipher = Cipher.getInstance(ALGORITHM);
		dCipher.init(Cipher.DECRYPT_MODE, key);

		// Decrypt the ciphertext
		byte[] clearText = dCipher.doFinal(decode);

		String password = new String(clearText);
		return password;
	}
	

}
