package com.sport.common.encode;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Hex;

/**
 * MD5实现类
 * 
 * @author chenguanhua
 *
 */
public class Md5PwdImpl implements Md5Pwd {

	/**
	 * md5加密
	 * 
	 * @param password
	 */
	public String encode(String password) {
		//算法
		String algorithm = "MD5";
		MessageDigest messageDigset = null;
		try {
			messageDigset = MessageDigest.getInstance(algorithm);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		// 加密
		byte[] digest = messageDigset.digest(password.getBytes());
		// 16进制加密
		char[] encodeHex = Hex.encodeHex(digest);

		return new String(encodeHex);
	}
}
