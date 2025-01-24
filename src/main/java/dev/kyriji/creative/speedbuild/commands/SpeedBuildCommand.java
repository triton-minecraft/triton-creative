package dev.kyriji.creative.speedbuild.commands;

import dev.kyriji.creative.misc.Out;
import dev.kyriji.creative.speedbuild.controllers.SpeedBuildManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;

public class SpeedBuildCommand implements CommandExecutor {
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
		if (!(sender instanceof Player player)) return false;

		if (!player.hasPermission("tritonmc.admin")) {
			Out.send(player, "&c&lERROR!&7 You do not have permission to use this command!");
			return false;
		}

		if (args.length == 0) {
			Out.send(player, "&8&m               &8< &3&lSPEED BUILD&8 >&m               &r");
			Out.send(player, "&8Usage: &3/" + label + " enable &7[time]");
			Out.send(player, "&8Usage: &3/" + label + " disable");
			Out.send(player, "&8&m                                                     &r");

			return false;
		}

		if (args[0].equalsIgnoreCase("enable")) {
			if (SpeedBuildManager.isSpeedBuildOngoing()) {
				Out.send(player, "&c&lERROR!&7 Speed building is already in progress!");
				return false;
			}

			if (args.length == 1) {
				Out.send(player, "&c&lERROR!&7 Please specify a time (format: #m#s, #m, or #s)!");
				return false;
			}

			try {
				long durationMillis = parseTime(args[1]);
				if (durationMillis <= 0) {
					Out.send(player, "&c&lERROR!&7 Time must be positive!");
					return false;
				}

				SpeedBuildManager.speedBuildFor(Duration.ofMillis(durationMillis));

				long minutes = durationMillis / (60 * 1000);
				long seconds = (durationMillis % (60 * 1000)) / 1000;
				if (minutes > 0) {
					if (seconds > 0) {
						Out.broadcast(String.format("&3&lSPEED BUILD!&7 Speed build enabled for %d minutes and %d seconds!", minutes, seconds));
					} else {
						Out.broadcast(String.format("&3&lSPEED BUILD!&7 Speed build enabled for %d minutes!", minutes));
					}
				} else {
					Out.broadcast(String.format("&3&lSPEED BUILD!&7 Speed build enabled for %d seconds!", seconds));
				}
			} catch (IllegalArgumentException e) {
				Out.send(player, "&c&lERROR!&7 Invalid time format!");
				return false;
			}
		} else if (args[0].equalsIgnoreCase("disable")) {
			SpeedBuildManager.isEnabled = false;
			Out.send(player, "&3&lSPEED BUILD!&7 Speed build disabled!");
		} else {
			Out.send(player, "&c&lERROR!&7 Invalid subcommand argument!");
		}
		return true;
	}

	/**
	 * Parses a time string in the format: #m#s, #m, or #s
	 * Examples: "5m30s", "2m", "30s"
	 *
	 * @param time The time string to parse
	 * @return The total number of milliseconds
	 * @throws IllegalArgumentException if the format is invalid
	 */
	public static long parseTime(String time) {
		if (time == null || time.isEmpty()) throw new IllegalArgumentException("Time cannot be empty");

		long totalMillis = 0;
		StringBuilder currentNumber = new StringBuilder();

		for (int i = 0; i < time.length(); i++) {
			char c = time.charAt(i);

			if (Character.isDigit(c)) {
				currentNumber.append(c);
			} else if (c == 'm' || c == 's') {
				if (currentNumber.isEmpty()) throw new IllegalArgumentException("Invalid time format");

				try {
					long value = Long.parseLong(currentNumber.toString());
					if (c == 'm') {
						totalMillis += value * 60 * 1000;
					} else {
						totalMillis += value * 1000;
					}
					currentNumber = new StringBuilder();
				} catch (NumberFormatException e) {
					throw new IllegalArgumentException("Invalid number in time format");
				}
			} else {
				throw new IllegalArgumentException("Invalid character in time format");
			}
		}

		if (!currentNumber.isEmpty()) throw new IllegalArgumentException("Invalid time format");
		return totalMillis;
	}
}