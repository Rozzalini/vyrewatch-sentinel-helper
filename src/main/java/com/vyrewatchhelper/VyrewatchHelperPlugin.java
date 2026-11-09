package com.vyrewatchhelper;

import javax.inject.Inject;
import com.google.inject.Provides;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.events.ActorDeath;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.ItemSpawned;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.Notifier;
import net.runelite.client.ui.overlay.OverlayManager;
import java.awt.TrayIcon;
import java.time.Instant;

@Slf4j
@PluginDescriptor(
        name = "Vyrewatch Sentinel Helper",
        description = "Helpers for camping Vyrewatch Sentinels for Blood Shards",
        tags = {"combat", "vyre", "afk", "blood shard"}
)
public class VyrewatchHelperPlugin extends Plugin
{
    @Inject
    private Client client;

    @Inject
    private VyrewatchHelperconfig config;

    @Inject
    private Notifier notifier;

    @Inject
    private OverlayManager overlayManager;

    @Inject
    private VyrewatchHelperStatsOverlay statsOverlay;

    @Getter private int killCount = 0;
    @Getter private int shardCount = 0;
    @Getter private Instant startTime = null;
    @Getter private boolean trackingStarted = false;

    // Cooldown timer to prevent notifications screaming every 0.6 seconds
    private Instant lastPrayerAlertTime = Instant.EPOCH;
    private static final int ALERT_COOLDOWN_SECONDS = 10;

    @Provides
    VyrewatchHelperconfig provideConfig(ConfigManager configManager)
    {
        return configManager.getConfig(VyrewatchHelperconfig.class);
    }

    @Override
    protected void startUp() throws Exception
    {
        overlayManager.add(statsOverlay);
        log.info("Vyrewatch Sentinel Helper started!");
    }

    @Override
    protected void shutDown() throws Exception
    {
        overlayManager.remove(statsOverlay);
        resetTracker();
        log.info("Vyrewatch Sentinel Helper stopped!");
    }

    private void resetTracker()
    {
        killCount = 0;
        shardCount = 0;
        startTime = null;
        trackingStarted = false;
    }

    @Subscribe
    public void onActorDeath(ActorDeath event)
    {
        Actor actor = event.getActor();

        if (actor instanceof NPC)
        {
            NPC npc = (NPC) actor;
            String name = npc.getName();

            if (name != null && name.contains("Vyrewatch Sentinel") && npc.getInteracting() == client.getLocalPlayer())
            {
                if (!trackingStarted)
                {
                    startTime = Instant.now();
                    trackingStarted = true;
                }
                killCount++;
            }
        }
    }

    @Subscribe
    public void onGameTick(GameTick event)
    {
        if (client.getGameState() != GameState.LOGGED_IN)
        {
            return;
        }

        if (config.lowPrayerAlert())
        {
            int currentPrayer = client.getBoostedSkillLevel(Skill.PRAYER);
            if (currentPrayer <= config.prayerThreshold())
            {
                // Only alert if 10 seconds have passed since the last alert
                if (Instant.now().isAfter(lastPrayerAlertTime.plusSeconds(ALERT_COOLDOWN_SECONDS)))
                {
                    notifier.notify("Your prayer is getting low! Use the altar!");
                    lastPrayerAlertTime = Instant.now();
                }
            }
        }
    }

    @Subscribe
    public void onItemSpawned(ItemSpawned event)
    {
        if (!config.bloodShardAlert())
        {
            return;
        }

        TileItem item = event.getItem();

        if (item.getId() == ItemID.BLOOD_SHARD)
        {
            // Ensure the shard dropped within 3 tiles of your player
            // to protect against counting other people's distant loot.
            Player localPlayer = client.getLocalPlayer();
            if (localPlayer != null)
            {
                LocalPoint playerLocation = localPlayer.getLocalLocation();
                LocalPoint itemLocation = event.getTile().getLocalLocation();
                if (playerLocation.distanceTo(itemLocation) > 3 * Perspective.LOCAL_TILE_SIZE)
                {
                    return;
                }
            }

            shardCount++;
            notifier.notify("🔴 BLOOD SHARD DROPPED! 🔴", TrayIcon.MessageType.WARNING);

            // Replaced missing symbol reference with raw game engine sound ID constant
            client.playSoundEffect(3892);
        }
    }
}
