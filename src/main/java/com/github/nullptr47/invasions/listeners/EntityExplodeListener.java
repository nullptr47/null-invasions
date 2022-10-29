package com.github.nullptr47.invasions.listeners;

import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.ps.PS;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

public class EntityExplodeListener implements Listener {

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    void on(EntityExplodeEvent event) {

        Faction factionAt = BoardColl.get().getFactionAt(PS.valueOf(event.getEntity()));

        if (factionAt.isNone() || event.blockList().stream().noneMatch(block -> block.getType() == Material.MOB_SPAWNER))
            return;

        MPlayer mplayer = event.getEntity().getNearbyEntities(15, 4, 15)
                .stream()
                .filter(Player.class::isInstance)
                .map(MPlayer::get)
                .filter(context -> !context.getFaction().isNone())
                .filter(context -> !context.getFaction().getTag().equals(factionAt.getTag()))
                .findFirst().orElse(null);

        if (mplayer == null)
            return;

        Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage(StringUtils.replaceEach(
                "§eA facção §f[{factionAt}]§e foi invadida pela §f[{faction}]",
                new String[] { "{factionAt}", "{faction}" },
                new String[] { factionAt.getTag(), mplayer.getFaction().getTag() }
        ));
        Bukkit.broadcastMessage("");

    }

}
