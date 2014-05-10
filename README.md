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
