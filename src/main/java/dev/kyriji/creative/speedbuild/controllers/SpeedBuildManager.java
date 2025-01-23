package dev.kyriji.creative.speedbuild.controllers;

import dev.kyriji.creative.misc.Out;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import java.time.Duration;

public class SpeedBuildManager implements Listener {
	public static boolean isEnabled;
	public static long buildTimeStart;
	public static long buildTimeEnd;

	public SpeedBuildManager() {}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		if (!isSpeedBuildingOver() || player.hasPermission("tritonmc.admin")) return;

		event.setCancelled(true);
		Out.send(player, "&c&lERROR!&7 Building time is over!");
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		if (!isSpeedBuildingOver() || player.hasPermission("tritonmc.admin")) return;

		event.setCancelled(true);
		Out.send(player, "&c&lERROR!&7 Building time is over!");
	}

	public static boolean isSpeedBuildOngoing() {
		return isEnabled && System.currentTimeMillis() < buildTimeEnd;
	}

	public static boolean isSpeedBuildingOver() {
		return isEnabled && System.currentTimeMillis() >= buildTimeEnd;
	}

	public static void speedBuildFor(Duration duration) {
		isEnabled = true;
		buildTimeStart = System.currentTimeMillis();
		buildTimeEnd = buildTimeStart + duration.toMillis();
	}
}
