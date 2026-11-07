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

    @Inject
    private VyrewatchHelperStatsOverlay(VyrewatchHelperPlugin plugin)
    {
        this.plugin = plugin;
        setPosition(OverlayPosition.TOP_LEFT);
    }

    @Override
    public Dimension render(Graphics2D graphics)
    {
        if (!plugin.isTrackingStarted())
        {
            return null;
        }

        panelComponent.getChildren().add(TitleComponent.builder()
                .text("Vyre Tracker")
                .color(Color.GREEN)
                .build());

        panelComponent.getChildren().add(LineComponent.builder()
                .left("Kills:")
                .right(String.valueOf(plugin.getKillCount()))
                .build());

        panelComponent.getChildren().add(LineComponent.builder()
                .left("Shards:")
                .right(String.valueOf(plugin.getShardCount()))
                .rightColor(plugin.getShardCount() > 0 ? Color.RED : Color.WHITE)
                .build());

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
