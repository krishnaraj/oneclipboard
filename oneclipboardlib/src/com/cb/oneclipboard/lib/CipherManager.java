package com.cb.oneclipboard.lib;

import org.jasypt.util.text.BasicTextEncryptor;

/**
 * Created by krishnaraj on 15/12/15.
 */
public class CipherManager {
    private BasicTextEncryptor encryptor = new BasicTextEncryptor();
    private String encryptionPassword;

    public CipherManager(String userName, String password) {
        encryptionPassword = userName + password;
        encryptor.setPassword(encryptionPassword);
    }

    public String encrypt(String text) {
        return encryptor.encrypt(text);
    }

    public String decrypt(String text) {
        return encryptor.decrypt(text);
    }

    public String getEncryptionPassword() {
        return encryptionPassword;
    }
}
