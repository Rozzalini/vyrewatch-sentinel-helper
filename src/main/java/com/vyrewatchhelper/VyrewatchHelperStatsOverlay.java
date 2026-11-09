package com.vyrewatchhelper;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.time.Duration;
import java.time.Instant;
import javax.inject.Inject;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;

public class VyrewatchHelperStatsOverlay extends OverlayPanel
{
    private final VyrewatchHelperPlugin plugin;

    // Optimization: Cache static color objects to prevent recreating them mid-render
    private static final Color TITLE_COLOR = Color.GREEN;
    private static final Color SHARD_DROP_COLOR = Color.RED;
    private static final Color DEFAULT_TEXT_COLOR = Color.WHITE;

    @Inject
    private VyrewatchHelperStatsOverlay(VyrewatchHelperPlugin plugin)
    {
        this.plugin = plugin;
        setPosition(OverlayPosition.TOP_LEFT);
    }

    @Override
    public Dimension render(Graphics2D graphics)
    {
        if (!plugin.isTrackingStarted() || plugin.getStartTime() == null)
        {
            return null;
        }

        // CRITICAL FIX: Clear last frame's components to prevent an infinite memory leak accumulation
        panelComponent.getChildren().clear();

        // 1. Add Title
        panelComponent.getChildren().add(TitleComponent.builder()
                .text("Vyre Tracker")
                .color(TITLE_COLOR)
                .build());

        // 2. Add Kill Count
        panelComponent.getChildren().add(LineComponent.builder()
                .left("Kills:")
                .right(String.valueOf(plugin.getKillCount()))
                .build());

        // 3. Add Shards Collected
        int shardCount = plugin.getShardCount();
        panelComponent.getChildren().add(LineComponent.builder()
                .left("Shards:")
                .right(String.valueOf(shardCount))
                .rightColor(shardCount > 0 ? SHARD_DROP_COLOR : DEFAULT_TEXT_COLOR)
                .build());

        // 4. Calculate and Format Time
        Duration elapsed = Duration.between(plugin.getStartTime(), Instant.now());
        long s = elapsed.getSeconds();
        String formattedTime = String.format("%02d:%02d:%02d", s / 3600, (s % 3600) / 60, s % 60);

        panelComponent.getChildren().add(LineComponent.builder()
                .left("Time:")
                .right(formattedTime)
                .build());

        return super.render(graphics);
    }
}
