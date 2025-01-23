package dev.kyriji.creative;

import dev.kyriji.creative.speedbuild.commands.SpeedBuildCommand;
import dev.kyriji.creative.speedbuild.controllers.BossBarManager;
import dev.kyriji.creative.speedbuild.controllers.SpeedBuildManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class TritonCreative extends JavaPlugin {
	public static TritonCreative INSTANCE;

	@Override
	public void onEnable() {
		INSTANCE = this;

		Bukkit.getPluginManager().registerEvents(new BossBarManager(), this);
		Bukkit.getPluginManager().registerEvents(new SpeedBuildManager(), this);

		getCommand("speedbuild").setExecutor(new SpeedBuildCommand());

		for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
			BossBarManager.bossBar.addPlayer(onlinePlayer);
		}
	}
}
