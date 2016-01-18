package com.cb.oneclipboard.server;

import com.cb.oneclipboard.lib.ApplicationProperties;
import com.cb.oneclipboard.lib.DefaultPropertyLoader;
import com.cb.oneclipboard.lib.security.KeyStoreBuilder;
import com.cb.oneclipboard.lib.security.KeyStoreManager;
import com.cb.oneclipboard.server.admin.AdminServer;
import com.cb.oneclipboard.server.logging.LevelBasedFileHandler;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.SocketException;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {
    private final static Logger LOGGER = Logger.getLogger(Server.class.getName());

    public static final String[] PROP_LIST = {"config.properties"};
    private static int serverPort;
    private static ServerSocket serverSocket = null;
    private static String serverPrivateKeyStorePass;
    private static String serverPrivateKeyStorePath = "/server.private";
    private static String clientPublicKeyStorePass;
    private static String clientPublicKeyStorePath = "/client.public";
    private static KeyStoreManager keyStoreManager;

    private static SecureRandom secureRandom;

    static {
        // TODO this might slow down the app startup
        secureRandom = new SecureRandom();
        secureRandom.nextInt();
    }

    public static void main(String[] args) throws Exception {
        init(args);
        start();
    }

    public static void start() {
        Thread startupThread = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    SSLContext sslContext = getSSLContext();
                    SSLServerSocketFactory sf = sslContext.getServerSocketFactory();
                    serverSocket = sf.createServerSocket( serverPort );
                    ((SSLServerSocket)serverSocket).setNeedClientAuth(true);

                    LOGGER.info("Server started on port " + serverPort);
                } catch (Exception e) {
                    LOGGER.log(Level.SEVERE, "Could not start server", e);
                    System.exit(-1);
                }

                while (true) {
                    ServerThread serverThread = null;
                    try {
                        serverThread = new ServerThread(serverSocket.accept());
                    } catch (SocketException e) {
                        break; // exit loop
                    } catch (Exception e) {
                        try {
                            serverThread.close();
                        } catch (Exception ex) {
                            LOGGER.log(Level.WARNING, "Unable to close server thread properly " + ex.getMessage());
                        }
                        LOGGER.log(Level.WARNING, "Connection lost: " + e.getMessage());
                    }
                }

            }
        }, "Startup Thread");
        startupThread.start();
    }

    public static boolean restart() {
        try {
            LOGGER.info("Restarting server...");
            serverSocket.close();
            start();
            return true;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Restart failed!", e);
            return false;
        }
    }

    private static void init(String[] args) throws SecurityException, IOException {
        // set up loggers
        Logger.getLogger("").addHandler(new LevelBasedFileHandler("oneclipboardserver.log", Arrays.asList(Level.INFO, Level.WARNING)));
        Logger.getLogger("").addHandler(new LevelBasedFileHandler("oneclipboardserver.err", Arrays.asList(Level.SEVERE)));

        // Load properties
        ApplicationProperties.loadProperties(PROP_LIST, new DefaultPropertyLoader());
        serverPort = ApplicationProperties.getIntProperty("server_port");
        serverPrivateKeyStorePass = ApplicationProperties.getStringProperty("serverPrivateKeyStorePass");
        clientPublicKeyStorePass = ApplicationProperties.getStringProperty("clientPublicKeyStorePass");

        InputStream privateKeyStoreIns = null;
        InputStream publicKeyStoreIns = null;

        try {
            privateKeyStoreIns = Server.class.getResourceAsStream(serverPrivateKeyStorePath);
            publicKeyStoreIns = Server.class.getResourceAsStream(clientPublicKeyStorePath);

            Security.addProvider(new BouncyCastleProvider());

            keyStoreManager = new KeyStoreBuilder()
                    .privateKeyStorePass(serverPrivateKeyStorePass)
                    .privateKeyStoreInputStream(privateKeyStoreIns)
                    .publicKeyStorePass(clientPublicKeyStorePass)
                    .publicKeyStoreInputStream(publicKeyStoreIns)
                    .build();

            // Start admin server
            AdminServer.start(args);
            ServerThreadCleaner.start(args);

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error starting admin server", e);
        } finally {
            closeStream(privateKeyStoreIns);
            closeStream(publicKeyStoreIns);
        }
    }

    private static SSLContext getSSLContext() throws Exception {
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(keyStoreManager.getKeyManagerFactory().getKeyManagers(),
                keyStoreManager.getTrustManagerFactory().getTrustManagers(),
                secureRandom);

        return sslContext;
    }

    private static void closeStream(InputStream inputStream) {
        try {
            inputStream.close();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Unable to close stream.", e);
        }
    }

}