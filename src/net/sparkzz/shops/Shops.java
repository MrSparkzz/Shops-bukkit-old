package net.sparkzz.shops;

import net.milkbowl.vault.economy.Economy;
import net.sparkzz.shops.command.Buy;
import net.sparkzz.shops.command.Sell;
import net.sparkzz.shops.util.Logger;
import net.sparkzz.shops.util.Utility;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by Brendon on 8/20/2015.
 */
public class Shops extends JavaPlugin {

	private Economy economy;
	private Logger log;

	@Override
	public void onDisable() {
		log.info("Shops has been disabled!");
	}

	@Override
	public void onEnable() {
		log = new Logger();

		log.info("Attempting Vault link...");
		if (!setupEconomy()) {
			log.severe("Disabling due to no Vault dependency found!");
			getServer().getPluginManager().disablePlugin(this);
			return;
		}
		log.info("Successfully linked to Vault!");

		Utility.log = this.log;
		Utility.econ = this.economy;

		setupCommands();

		log.info("Shops has been enabled!");
	}

	private boolean setupEconomy() {
		RegisteredServiceProvider<Economy> provider = getServer().getServicesManager().getRegistration(Economy.class);

		if (provider != null) this.economy = provider.getProvider();

		return (economy != null);
	}

	private void setupCommands() {
		getCommand("buy").setExecutor(new Buy());
		getCommand("sell").setExecutor(new Sell());
	}
}