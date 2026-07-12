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

Android 11 runs a Compose-only liquid-glass-styled fallback for stability, using translucent gradients, bright rims, tint, and soft depth. Android 12+ devices, including Waydroid Android 13, use live CMP Backdrop blur, lens, vibrancy, and tint effects.
