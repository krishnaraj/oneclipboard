package com.cb.oneclipboard.desktop;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintStream;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import com.cb.oneclipboard.desktop.ApplicationConstants.Property;
import com.cb.oneclipboard.desktop.gui.ApplicationUI;
import com.cb.oneclipboard.desktop.gui.Tray;
import com.cb.oneclipboard.lib.*;
import com.cb.oneclipboard.lib.socket.ClipboardConnector;
import com.jezhumble.javasysmon.JavaSysMon;
import com.jezhumble.javasysmon.OsProcess;

public class Client implements PropertyChangeListener {
    private static Client client = null;
    private static ApplicationUI ui = new ApplicationUI();
    private static String serverAddress = null;
    private static int serverPort;
    private static ScheduledExecutorService scheduler = null;
    private static User user = null;
    private static CipherManager cipherManager = null;
    public static ApplicationPropertyChangeSupport propertyChangeSupport = new ApplicationPropertyChangeSupport();

    public static final String[] PROP_LIST = {"config.properties"};

    public static void main(String[] args) {
        // Set logging file location
        String logFileLocation = System.getProperty("user.home") + File.separator + "onecliboarddesktop.log";
        System.setProperty("java.util.logging.config.file", logFileLocation);

        client = new Client();
        client.init(args);
    }

    public void init(String args[]) {
        //pipeSysoutToFile();

        try {
            lock();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Initialize properties
        ApplicationProperties.loadProperties(PROP_LIST, new DefaultPropertyLoader());
        serverAddress = ApplicationProperties.getStringProperty("server");
        serverPort = ApplicationProperties.getIntProperty("server_port");

        if (args.length > 0) {
            try {
                serverPort = Integer.parseInt(args[0]);
            } catch (Exception e) {
            }
        }

        propertyChangeSupport.addPropertyChangeListener(this);

        ui.showLogin();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String propertyName = evt.getPropertyName();
        Property property = Property.valueOf(propertyName);

        switch (property) {
            case LOGIN:
                user = (User) evt.getNewValue();
                cipherManager = new CipherManager(user);
                client.start();
                ui.createAndShowTray();
                break;
            case START:
                client.start();
                break;
            case STOP:
                client.stop();
                break;
        }
    }

    public void start() {
        final TextTransfer textTransfer = new TextTransfer();
        scheduler = Executors.newScheduledThreadPool(1);

        // Poll the clipboard for changes
        final ClipboardPollTask clipboardPollTask = new ClipboardPollTask(textTransfer, new Callback() {

            @Override
            public void execute(Object object) {
                String clipboardText = (String) object;
                ClipboardConnector.send(new Message(cipherManager.encrypt(clipboardText), user));
            }
        });

        // Listen for clipboard content from other clients
        ClipboardConnector.connect(serverAddress, serverPort, user, new SocketListener() {

            @Override
            public void onMessageReceived(Message message) {
                String clipboardText = cipherManager.decrypt(message.getText());
                clipboardPollTask.updateClipboardContent(clipboardText);
                textTransfer.setClipboardContents(clipboardText);
            }

            @Override
            public void onConnect() {
                ClipboardConnector.send(new Message("register", MessageType.REGISTER, user));
            }

            @Override
            public void onDisconnect() {
                // TODO Auto-generated method stub

            }

        });

        // Run the poll task every 2 seconds
        final ScheduledFuture<?> pollHandle = scheduler.scheduleAtFixedRate(clipboardPollTask, 1, 2, TimeUnit.SECONDS);

    }

    public void stop() {
        scheduler.shutdownNow();
        ClipboardConnector.close();
    }

    static String lockFileName = System.getProperty("user.home") + File.separator + "oneclipboard.lock";
    static JavaSysMon monitor = new JavaSysMon();

    public void lock() throws Exception {
        int currentPid = monitor.currentPid();
        if (isRunning()) {
            System.err.println("Application already running");
            System.exit(0);
        } else {
            // Write current PID to file.
            File lockFile = new File(lockFileName);
            lockFile.deleteOnExit();
            FileOutputStream fos = new FileOutputStream(lockFile);
            fos.write(String.valueOf(currentPid).getBytes());
            fos.flush();
            fos.close();
        }

    }

    public boolean isRunning() {
        File lockFile = new File(lockFileName);
        boolean isRunning = false;
        if (lockFile.exists()) {
            int pid = getPID(lockFile);
            if (pid > 0) {
                OsProcess process = monitor.processTree().find(getPID(lockFile));
                if (process != null && process.processInfo().getPid() == pid) {
                    isRunning = true;
                }
            }
        }
        return isRunning;
    }

    private int getPID(File lockFile) {
        int pid = -1;
        // Read PID
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(lockFile));
            pid = Integer.parseInt(reader.readLine().trim());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return pid;
    }

    private void pipeSysoutToFile() {
        try {
            String logFileName = System.getProperty("user.home") + File.separator + "oneclipboard.log";
            File file = new File(logFileName);
            PrintStream printStream = new PrintStream(new FileOutputStream(file));
            System.setOut(printStream);
            System.setErr(printStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
