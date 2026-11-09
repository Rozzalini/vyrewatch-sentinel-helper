# Vyrewatch Sentinel Helper

A lightweight, performance-optimized RuneLite plugin designed to enhance your AFK experience while camping Vyrewatch Sentinels in Darkmeyer for Blood Shards.

## Features

- **Blood Shard Drop Alerts:** Sends a desktop notification and plays a distinct audio cue immediately when a Blood Shard drops nearby.
- **Proximity Filtering:** Intelligently ignores Blood Shard drops from distant players to ensure your statistics and notifications stay accurate.
- **Smart Low Prayer Alert:** Warns you when your prayer points drop below your preferred threshold, using built-in anti-spam debouncing to protect your frame rate.
- **Session Tracking Overlay:** A clean, minimal on-screen overlay that keeps track of your kill count, blood shards obtained, and elapsed time.

## Configuration

You can customize the plugin behaviors within the standard RuneLite Configuration panel under **Vyrewatch Sentinel Helper**:

| Setting Name | Type | Default | Description |
| :--- | :--- | :--- | :--- |
| **Low Prayer Alert** | Boolean | `true` | Toggles whether the client will notify you when your prayer points get low. |
| **Prayer Threshold** | Integer | `10` | The exact boosted prayer level that triggers the low prayer alert. |
| **Blood Shard Notification** | Boolean | `true` | Toggles OS-level desktop notifications and audio alerts for shard drops. |

## Performance & Optimization Notes

This plugin has been thoroughly audited to ensure an entirely lag-free experience during long camping sessions:
- **Zero Memory Leaks:** UI components are explicitly recycled and cleared every frame to guarantee flat memory usage.
- **Anti-Spam Debouncing:** Periodic checks (like low prayer alerts) run with throttled execution structures so they never flood your notification client thread or degrade your FPS.

## Installation & Testing

### Local Development Setup
To build and run this plugin locally inside a RuneLite sandbox environment:

1. Clone this repository to your local machine.
2. Open the project inside IntelliJ IDEA as a Gradle project.
3. Open `src/test/java/com/vyrewatchhelper/ExamplePluginTest.java`.
4. Right-click the `main` method and select **Run 'ExamplePluginTest.main()'**.

### Building the Project
To compile the plugin metadata and verify everything builds flawlessly against the standard RuneLite API wrappers, run:
```bash
./gradlew clean build
```

## License

This project is licensed under the BSD 2-Clause License - see the LICENSE file for details.
