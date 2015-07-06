package com.yauritux.common.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Date;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.codec.Hex;

import com.yauritux.model.entity.User;

/**
 * @author yauritux@gmail.com
 */
public class AuthTokenUtils {

	private final static SecureRandom secureRandom = new SecureRandom();
	
    /**
     * Create token based on supplied userDetails.
     * @param userDetails Object that stores user information
     * @return String Valid authentication token
     */	
	public static String createToken(UserDetails userDetails) {
		// Build the token
		StringBuilder tokenBuilder = new StringBuilder();
		tokenBuilder.append(AuthTokenUtils.computeSignature(userDetails));
		
		return tokenBuilder.toString();
	}
	
    /**
     * Compute signature that will be used as a part of the token.
     * @param userDetails Object that stores user information
     * @return String Valid authentication token signature
     */	
	public static String computeSignature(UserDetails userDetails) {
        /** Build the signature */
        StringBuilder signatureBuilder = new StringBuilder();
        signatureBuilder.append(userDetails.getUsername());
        signatureBuilder.append(":");
        signatureBuilder.append(userDetails.getPassword());
        signatureBuilder.append(":");
        signatureBuilder.append(new BigInteger(130, secureRandom).toString(16));

        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-512");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("No SHA-512 algorithm available!");
        }

        return new String(Hex.encode(digest.digest(signatureBuilder.toString().getBytes())));		
	}
	
    /**
     * Validate supplied authToken with actual userDetails.
     * @param authToken Authentication token
     * @param user Object that stores user information
     * @return boolean true if supplied authToken is valid against actual userDetails
     */
    public static boolean validateToken(String authToken, User user) {
        if (user.getAuthTokenValidThru() != null) {
            if (new Date().after(user.getAuthTokenValidThru())) {
                return false;
            }
        }

        return authToken.equals(user.getAuthToken());
    }	
}
