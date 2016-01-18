package com.cb.oneclipboard;

import android.app.Application;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import com.cb.oneclipboard.lib.*;
import com.cb.oneclipboard.lib.security.KeyStoreBuilder;
import com.cb.oneclipboard.lib.security.KeyStoreManager;
import com.cb.oneclipboard.lib.socket.ClipboardConnector;
import com.cb.oneclipboard.util.IntentUtil;
import com.cb.oneclipboard.util.Utility;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ClipboardApplication extends Application {
    public static final int NOTIFICATION_ID = 1;

    private static final String TAG = ClipboardApplication.class.getName();
    public static final String CLIPBOARD_UPDATED = "clipboard_updated";

    private static final String[] PROP_LIST = {"app.properties"};
    private ClipboardListener clipboardListener = null;
    private ClipboardManager clipBoard = null;

    private CipherManager cipherManager = null;

    private User user = null;
    private NotificationCompat.Builder notificationBuilder = null;
    private LocalBroadcastManager broadcaster = null;
    public Preferences pref = null;

    private static String serverAddress = null;
    private static int serverPort;
    private static String serverPublicKeyStorePass;
    private static String clientPrivateKeyStorePass;

    @Override
    public void onCreate() {
        super.onCreate();
        pref = new Preferences(this);
        broadcaster = LocalBroadcastManager.getInstance(this);
    }

    public void loadProperties() {
        loadProperties(PROP_LIST);
        serverAddress = getString(R.string.serverHostName);
        serverPort = getResources().getInteger(R.integer.serverPort);
        serverPublicKeyStorePass = getString(R.string.serverPublicKeyStorePass);
        clientPrivateKeyStorePass = getString(R.string.clientPrivateKeyStorePass);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public CipherManager getCipherManager() {
        return cipherManager;
    }

    public void setCipherManager(CipherManager cipherManager) {
        this.cipherManager = cipherManager;
    }

    public void initializeClipboardListener() {
        clipBoard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        clipboardListener = new ClipboardListener(clipBoard, new Callback() {

            @Override
            public void execute(Object object) {
                String clipboardText = (String) object;
                ClipboardConnector.send(new Message(cipherManager.encrypt(clipboardText), user));
            }
        });
        clipBoard.addPrimaryClipChangedListener(clipboardListener);
    }

    public void establishConnection() {
        Log.d(TAG, "Establishing connection to server...");

        InputStream clientPrivateKeyStoreIns = null;
        InputStream serverPublicKeyStoreIns = null;

        try {
            clientPrivateKeyStoreIns = getResources().openRawResource(R.raw.client);
            serverPublicKeyStoreIns = getResources().openRawResource(R.raw.server);

            KeyStoreManager keyStoreManager = new KeyStoreBuilder()
                    .privateKeyStorePass(clientPrivateKeyStorePass)
                    .privateKeyStoreInputStream(clientPrivateKeyStoreIns)
                    .publicKeyStorePass(serverPublicKeyStorePass)
                    .publicKeyStoreInputStream(serverPublicKeyStoreIns)
                    .build();

            // Listen for clipboard content from other clients
            ClipboardConnector.connect(serverAddress, serverPort, user, new SocketListener() {

                @Override
                public void onMessageReceived(Message message) {
                    String clipboardText = cipherManager.decrypt(message.getText());
                    clipboardListener.updateClipboardContent(clipboardText);
                    clipBoard.setText(clipboardText);

                    // broadcast the message so that activities can update
                    Intent intent = new Intent(ClipboardApplication.CLIPBOARD_UPDATED);
                    intent.putExtra("message", clipboardText);
                    broadcaster.sendBroadcast(intent);
                }

                @Override
                public void onConnect() {
                    ClipboardConnector.send(new Message("register", MessageType.REGISTER, user));
                    updateNotification();
                }

                @Override
                public void onDisconnect() {
                    Log.d(TAG, "disconnected!");
                    updateNotification();
                }

            }, keyStoreManager);
        } catch (Exception e) {
            Log.e(TAG, "Unable to establish connection", e);
        } finally {
            closeStream(clientPrivateKeyStoreIns);
            closeStream(serverPublicKeyStoreIns);
        }
    }

    private void closeStream(InputStream inputStream) {
        try {
            inputStream.close();
        } catch (IOException e) {
            Log.e(TAG, "Unable to close stream.", e);
        }
    }

    public NotificationCompat.Builder getNotificationBuilder(Context context) {
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, IntentUtil.getHomePageIntent(context), PendingIntent.FLAG_CANCEL_CURRENT);

        notificationBuilder = new NotificationCompat.Builder(context)
                .setContentTitle("Oneclipboard")
                .setSmallIcon(R.drawable.logo)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        return notificationBuilder;
    }

    public void updateNotification() {
        Log.d(TAG, "notificationBuilder: " + notificationBuilder);
        if (notificationBuilder != null) {
            notificationBuilder.setContentText(Utility.getConnectionStatus());
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(ClipboardApplication.NOTIFICATION_ID, notificationBuilder.build());
        }
    }

    private void loadProperties(String[] fileList) {
        PropertyLoader loader = new PropertyLoader() {

            @Override
            public void load(Properties properties, String fileName) {
                try {
                    InputStream fileStream = getAssets().open(fileName);
                    properties.load(fileStream);
                    fileStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d(TAG, e.getMessage());
                }
            }
        };
        ApplicationProperties.loadProperties(fileList, loader);
    }
}
