package com.cb.oneclipboard.lib.socket;

import com.cb.oneclipboard.lib.Message;
import com.cb.oneclipboard.lib.MessageType;
import com.cb.oneclipboard.lib.SocketListener;
import com.cb.oneclipboard.lib.security.KeyStoreManager;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.security.SecureRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClipboardConnector {
    private final static Logger LOGGER = Logger.getLogger(ClipboardConnector.class.getName());
    private static final int MAX_RECONNECT_ATTEMPTS = 3;
    private int reconnectCounter = 0;
    private static SecureRandom secureRandom;
    private volatile boolean connected = false;

    static {
        // TODO this might slow down the app startup
        secureRandom = new SecureRandom();
        secureRandom.nextInt();
    }

    private String server;
    private int port;
    private Socket clientSocket;
    private ObjectInputStream objInputStream;
    private ObjectOutputStream objOutputStream;
    private SocketListener socketListener;
    private KeyStoreManager keyStoreManager;

    public boolean isConnected() {
        return connected;
    }

    public ClipboardConnector server(String server) {
        this.server = server;
        return this;
    }

    public ClipboardConnector port(int port) {
        this.port = port;
        return this;
    }

    public ClipboardConnector socketListener(SocketListener socketListener) {
        this.socketListener = socketListener;
        return this;
    }

    public ClipboardConnector keyStoreManager(KeyStoreManager keyStoreManager) {
        this.keyStoreManager = keyStoreManager;
        return this;
    }

    public ClipboardConnector connect() {
        final boolean listening = true;
        Thread listenerThread = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    LOGGER.info("Connecting to " + server + "...");
                    clientSocket = createSocket();
                    /*
                     * The objOutputStream needs to be created and flushed
					 * before the objInputStream can be created
					 */
                    objOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
                    objOutputStream.flush();
                    objInputStream = new ObjectInputStream(clientSocket.getInputStream());
                } catch (Exception e) {
                    // try reconnecting
                    reconnectCounter++;
                    if (reconnectCounter <= MAX_RECONNECT_ATTEMPTS) {
                        LOGGER.info("Reconnect attempt " + reconnectCounter);
                        connect();
                        return;
                    }
                    LOGGER.log(Level.SEVERE, "Unable to connect", e);
                    connected = false;
                    return;
                }
                LOGGER.info("connected to " + server + ":" + port);
                connected = true;

                // resert counter
                reconnectCounter = 0;

                socketListener.onConnect();

                try {
                    while (listening) {
                        if (objInputStream != null) {
                            Message message = (Message) objInputStream.readObject();
                            processMessage(message, socketListener);
                        }
                    }

                } catch (SocketException e) {
                    LOGGER.severe(e.getMessage());
                } catch (Exception e) {
                    LOGGER.log(Level.SEVERE, "Exception when listening for message", e);
                } finally {
                    close();
                    connected = false;
                    socketListener.onDisconnect();
                }
            }

        }, "Incoming message listener thread");

        listenerThread.start();

        return this;
    }

    private Socket createSocket() throws Exception {
        return new Socket(server, port);
    }

    @SuppressWarnings("incomplete-switch")
    private void processMessage(Message message, SocketListener listener) {
        switch (message.getMessageType()) {
            case CLIPBOARD_TEXT:
                listener.onMessageReceived(message);
                break;
            case PING:
                // Server is checking if connection is alive, ping back to say yes.
                send(new Message("ping", MessageType.PING, message.getUser()));
                break;
        }
    }

    public void send(Message message) {
        try {
            if (isConnected() && objOutputStream != null) {
                objOutputStream.writeObject(message);
                objOutputStream.flush();
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Exception when sending message", e);
        }
    }

    public void close() {
        try {
            objInputStream.close();
            objOutputStream.close();
            clientSocket.close();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Exception when closing resources", e);
        }
    }

    public String getServerName() {
        try {
            return clientSocket.getInetAddress().getHostName();
        } catch (Exception e) {
            LOGGER.log(Level.FINE, "Exception when getting host name", e);
        }
        return "";
    }
}
