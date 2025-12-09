# OneClipboard

OneClipboard is a cross-platform clipboard synchronization tool.

## Modules

*   **oneclipboardandroid**: Android client.
*   **oneclipboarddesktop**: Desktop client (Windows/Linux/Mac).
*   **oneclipboardserver**: Relay server.
*   **oneclipboardlib**: Shared library.

## Requirements

*   **Java**: JDK 21 or later is required for building the server and desktop clients.
    *   Server and Desktop modules target Java 21.
    *   Shared library and Android module target Java 17 for Android compatibility.
*   **Android SDK**: Android SDK Platform 34 (UpsideDownCake) is required.

## Building

The project uses Gradle.

### Command Line

```bash
# Build all modules
./gradlew build

# Build specific module
./gradlew oneclipboardserver:build
./gradlew oneclipboarddesktop:build
./gradlew oneclipboardandroid:build
```

### Configuration

Create `gradle-local.properties` in the root directory to override default properties (e.g., keys, hostnames) without committing them.

Example `gradle-local.properties`:

```properties
serverHostName=my.server.com
serverPort=4545
```

## Running

### Server

```bash
./gradlew oneclipboardserver:run
```

### Desktop Client

```bash
./gradlew oneclipboarddesktop:run
```

### Android Client

Deploy the APK generated in `oneclipboardandroid/build/outputs/apk/`.

## License

[License Information]
