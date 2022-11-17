package com.github.nullptr47.invasions.listeners;

import com.github.nullptr47.invasions.InvasionsPlugin;
import com.google.common.collect.Maps;
import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.ps.PS;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

import java.util.Map;
import java.util.concurrent.TimeUnit;

public class EntityExplodeListener implements Listener {

    private final Map<PS, Long> lastBroadcastMillis = Maps.newHashMap();

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    void on(EntityExplodeEvent event) {

        PS ps = PS.valueOf(event.getEntity());
        Faction factionAt = BoardColl.get().getFactionAt(ps);

        if (factionAt.isNone() || event.blockList().stream().noneMatch(block -> block.getType() == Material.MOB_SPAWNER))
            return;

        MPlayer mplayer = event.getEntity().getNearbyEntities(8, 4, 8)
                .stream()
                .filter(Player.class::isInstance)
                .map(MPlayer::get)
                .filter(context -> !context.getFaction().isNone())
                .filter(context -> !context.getFaction().getTag().equals(factionAt.getTag()))
                .findFirst().orElse(null);

        if (mplayer == null || System.currentTimeMillis() - lastBroadcastMillis.get(ps) < TimeUnit.MINUTES.toMillis(5))
            return;

        InvasionsPlugin.getInstance().getConfig()
                .getStringList("messages.invasion")
                .stream()
                .map(string -> StringUtils.replaceEach(
                        string, new String[] { "&", "{factionAt}", "{faction}" },
                        ArrayUtils.toArray(factionAt.getTag(), mplayer.getFaction().getTag()))
                )
                .forEach(Bukkit::broadcastMessage);

        lastBroadcastMillis.put(ps, System.currentTimeMillis());

    }

}
