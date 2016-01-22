package com.cb.oneclipboard.lib;

import java.security.MessageDigest;

/**
 * Created by krishnaraj on 22/1/16.
 */
public class Util {

    public static String getSha256Hash(String text) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");

        md.update(text.getBytes("UTF-8")); // Change this to "UTF-16" if needed
        byte[] digest = md.digest();

        return new String(digest);
    }
}
