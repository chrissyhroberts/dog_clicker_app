# Dog Clicker

Minimal native Android dog-training clicker.

- Tap anywhere on the main button for an immediate, short click.
- Add any of the three 1×1 home-screen widgets: **1**, **2**, or **3** clicks. They send a broadcast directly to the widget provider and play the sound without opening the app UI.
- Launch the app once and allow notifications to pin an ongoing **Dog Clicker** notification with a **CLICK** action; the action is available from the lock screen when the device's lock-screen notification settings allow it.
- The lock-screen notification exposes three controls: **1**, **2**, and **3**.
- No ads, analytics, accounts, or network access. Android 13+ requires the notification permission for the lock-screen control.

The supplied `Click.m4a` is trimmed to the sound itself, normalized to a 0 dB peak, converted to mono 44.1 kHz PCM, and preloaded into a static `AudioTrack` with unity playback gain, avoiding asset decoding on each tap. Android still honors the device's volume, mute, and Do Not Disturb settings; the app does not change global system volume.

## Build

```sh
./gradlew assembleDebug
```

Install the debug APK on a connected device with `adb install -r app/build/outputs/apk/debug/app-debug.apk`.
