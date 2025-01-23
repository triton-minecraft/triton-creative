package dev.kyriji.creative;

import dev.kyriji.creative.commands.CTestCommand;
import org.bukkit.plugin.java.JavaPlugin;

public class TritonCreative extends JavaPlugin {

	@Override
	public void onEnable() {
		getCommand("ctest").setExecutor(new CTestCommand());
	}
}
