package dev.kyriji.creative.speedbuild.controllers;

import dev.kyriji.creative.TritonCreative;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class BossBarManager implements Listener {
	public static BossBar bossBar;
	public static BukkitRunnable updateTask;

	public BossBarManager() {
		bossBar = Bukkit.createBossBar(
				"Speed Build",
				BarColor.GREEN,
				BarStyle.SOLID
		);

		startUpdateTask();
	}

	public void startUpdateTask() {
		updateTask = new BukkitRunnable() {
			@Override
			public void run() {
				if (!SpeedBuildManager.isEnabled) {
					bossBar.setVisible(false);
					return;
				}

				if (SpeedBuildManager.isSpeedBuildingOver()) {
					bossBar.setColor(BarColor.RED);
					bossBar.setTitle(ChatColor.GOLD + "Speed " + ChatColor.DARK_AQUA + "Build" + ChatColor.DARK_GRAY + " - " + ChatColor.WHITE + "Time Elapsed");
					bossBar.setProgress(1.0);
					bossBar.setVisible(true);
					return;
				}

				long timeLeft = SpeedBuildManager.buildTimeEnd - System.currentTimeMillis();
				long minutes = timeLeft / (60 * 1000);
				long seconds = (timeLeft % (60 * 1000)) / 1000;

				String timeString = minutes > 0 ?
						String.format("%dm %ds", minutes, seconds) :
						String.format("%ds", seconds);

				bossBar.setTitle(ChatColor.GOLD + "Speed " + ChatColor.DARK_AQUA + "Build" + ChatColor.DARK_GRAY + " - " + ChatColor.WHITE + timeString + " remaining");
				double progress = (double) timeLeft / (SpeedBuildManager.buildTimeEnd - SpeedBuildManager.buildTimeStart);
				bossBar.setProgress(Math.max(0.0, Math.min(1.0, progress)));
				bossBar.setVisible(true);
			}
		};
		updateTask.runTaskTimer(TritonCreative.INSTANCE, 0L, 1L);
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		bossBar.addPlayer(player);
	}

	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent event) {
		bossBar.removePlayer(event.getPlayer());
	}
}