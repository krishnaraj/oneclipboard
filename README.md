oneclipboard
============

An Android + Desktop app to sync clipboard automatically

###Requirements
1. Java 1.7+
2. Gradle 1.12
3. Android Tools 19.0
4. Android SDK 16

###Build Instructions
```shell
gradle :oneclipboardlib:build
gradle :oneclipboardandroid:build
gradle :oneclipboarddesktop:build
gradle :oneclipboardserver:build
```

####Generate Eclipse Project Files
```shell
gradle :oneclipboardlib:eclipse
gradle :oneclipboardandroid:eclipse
gradle :oneclipboarddesktop:eclipse
gradle :oneclipboardserver:eclipse
```

###Usage
1. Run oneclipboardserver ( on your desktop )
2. Run oneclipboarddesktop ( on desktop of course :) )
3. Run oneclipboardandroid ( on your android device )
4. Copy some text on your desktop/android, paste it on your android/desktop.

Note: You can use any username/password combination but make sure its the same on all the devices.
