# Android Liquid Glass Demo

A focused Jetpack Compose demo for the CMP Backdrop library. It showcases a glass bottom bar over a main host, an interactive bottom bar, a glass bottom sheet, a glass slider, and a tinted glass icon button.

## Install CMP Backdrop

```kotlin
dependencies {
    implementation("io.github.kyant0:backdrop:<version>")
}
```

This sample uses `io.github.kyant0:backdrop:1.0.6`, which is compatible with the Android API 36 toolchain currently available on GitHub-hosted runners.

## Runtime compatibility

Android 11 runs a Compose-only liquid-glass-styled fallback for stability. Android 12+ devices can use live CMP Backdrop blur, lens, vibrancy, and tint effects. Waydroid Android 13 starts in the stable fallback because its graphics stack can terminate apps during live backdrop initialization, but the app includes a button to try the live CMP Backdrop demo from inside the fallback screen.
