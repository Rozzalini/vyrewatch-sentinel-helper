package com.example;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("vyrewatchhelper")
public interface ExampleConfig extends Config
{
	@ConfigItem(
			keyName = "lowPrayerAlert",
			name = "Low Prayer Alert",
			description = "Notify when prayer drops below a threshold",
			position = 1
	)
	default boolean lowPrayerAlert() { return true; }

	@ConfigItem(
			keyName = "prayerThreshold",
			name = "Prayer Threshold",
			description = "The prayer level to trigger the alert",
			position = 2
	)
	default int prayerThreshold() { return 10; }

	@ConfigItem(
			keyName = "bloodShardAlert",
			name = "Blood Shard Notification",
			description = "Send a desktop notification when a Blood Shard drops",
			position = 3
	)
	default boolean bloodShardAlert() { return true; }
}
