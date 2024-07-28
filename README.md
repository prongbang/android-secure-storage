# android-secure-storage

Android Secure Storage provides an API to securely store sensitive data.

[![](https://jitpack.io/v/prongbang/android-secure-storage.svg)](https://jitpack.io/#prongbang/android-secure-storage)

## Setup

- `build.gradle`

```groovy
buildscript {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}
```

- `settings.gradle`

```groovy
dependencyResolutionManagement {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}
```

- `app/build.gradle`

```groovy
implementation 'com.github.prongbang:android-secure-storage:1.0.2'
```

## How to use

```kotlin
val androidSecureStorage = AndroidPreferencesSecureStorage.newInstance(context)
```

- Write

```kotlin
val message = "Android Secure Storage"
androidSecureStorage.write("key", message)
```

- Contains Key

```kotlin
val found = androidSecureStorage.containsKey("key")
```

- Read

```kotlin
val value = androidSecureStorage.read("key")
```

- Read All

```kotlin
val values = androidSecureStorage.readAll()
```

- Delete

```kotlin
androidSecureStorage.delete("key")
```

- Delete All

```kotlin
androidSecureStorage.deleteAll()
```
