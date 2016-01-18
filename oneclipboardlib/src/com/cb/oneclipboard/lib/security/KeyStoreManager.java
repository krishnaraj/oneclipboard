package com.cb.oneclipboard.lib.security;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManagerFactory;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyStore;

/**
 * Created by krishnaraj on 24/12/15.
 */
public class KeyStoreManager {
    KeyStore publicKeyStore;
    KeyStore privateKeyStore;

    private TrustManagerFactory tmf;
    private KeyManagerFactory kmf;

    protected KeyStoreManager() {

    }

    protected void setPublicKeyStore(InputStream keyStoreInputStream, String passphrase)
            throws GeneralSecurityException, IOException {
        publicKeyStore = setupKeystore(keyStoreInputStream, passphrase);

        tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(publicKeyStore);
    }

    protected void setPrivateKeyStore(InputStream keyStoreInputStream, String passphrase)
            throws GeneralSecurityException, IOException {
        privateKeyStore = setupKeystore(keyStoreInputStream, passphrase);

        kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(privateKeyStore, passphrase.toCharArray());
    }

    private KeyStore setupKeystore(InputStream keyStoreInputStream, String passphrase)
            throws GeneralSecurityException, IOException {
        KeyStore keyStore = KeyStore.getInstance("BKS");
        keyStore.load(keyStoreInputStream, passphrase.toCharArray());

        return keyStore;
    }

    public TrustManagerFactory getTrustManagerFactory() {
        return tmf;
    }

    public KeyManagerFactory getKeyManagerFactory() {
        return kmf;
    }

}
