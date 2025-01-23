package dev.kyriji.creative.misc;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Out {
	public static void send(Player player, String message) {
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
	}

	public static void broadcast(String message) {
		for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
			send(onlinePlayer, message);
		}
	}
}
