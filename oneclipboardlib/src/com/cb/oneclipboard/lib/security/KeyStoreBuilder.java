package com.cb.oneclipboard.lib.security;

import java.io.InputStream;

/**
 * Created by krishnaraj on 24/12/15.
 */
public class KeyStoreBuilder {
    private InputStream publicKeyStoreInputStream;
    private InputStream privateKeyStoreInputStream;
    private String publicKeyStorePass;
    private String privateKeyStorePass;

    public KeyStoreBuilder publicKeyStoreInputStream(InputStream publicKeyStoreInputStream) {
        this.publicKeyStoreInputStream = publicKeyStoreInputStream;
        return this;
    }

    public KeyStoreBuilder privateKeyStoreInputStream(InputStream privateKeyStoreInputStream) {
        this.privateKeyStoreInputStream = privateKeyStoreInputStream;
        return this;
    }

    public KeyStoreBuilder publicKeyStorePass(String publicKeyStorePass) {
        this.publicKeyStorePass = publicKeyStorePass;
        return this;
    }

    public KeyStoreBuilder privateKeyStorePass(String privateKeyStorePass) {
        this.privateKeyStorePass = privateKeyStorePass;
        return this;
    }

    public KeyStoreManager build() throws Exception {
        KeyStoreManager keyStoreManager = new KeyStoreManager();
        keyStoreManager.setPrivateKeyStore(privateKeyStoreInputStream, privateKeyStorePass);
        keyStoreManager.setPublicKeyStore(publicKeyStoreInputStream, publicKeyStorePass);

        return keyStoreManager;
    }
}
