package net.sparkzz.shops.storage;

import net.milkbowl.vault.item.ItemInfo;
import net.milkbowl.vault.item.Items;
import net.sparkzz.shops.util.Utility;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.UUID;

/**
 * Created by Brendon on 8/26/2015.
 */
public class IOManager extends Utility {

	private static FileConfiguration shopConfig;

	public static void load(Plugin plugin) {
		File dir = new File(plugin.getDataFolder() + "/shops/");

		if (dir.exists() && dir.list().length > 0) {
			for (File file : dir.listFiles()) {
				shopConfig = YamlConfiguration.loadConfiguration(file);

				cacheShopData(shopConfig, Long.parseLong(file.getName().replace(".yml", "")));
			}
		} else {
			log.info("No shops to be loaded!");
			return;
		}

		log.info("All shops have been loaded!");
	}

	public static void save(Plugin plugin) {
		if (getShops() != null) {
			for (Shop shop : getShops()) {
				File shopFile = new File(plugin.getDataFolder() + "/shops/" + shop.getId() + ".yml");

				shopConfig = YamlConfiguration.loadConfiguration(shopFile);

				shopConfig.set("name", shop.getName());
				shopConfig.set("owner", shop.getOwner().getUniqueId().toString());
				shopConfig.set("created", shop.getFormattedDate());
				shopConfig.set("inventory", shop.getItemsAsFormattedList());
				shopConfig.set("unlimited.balance", shop.isLimitlessBalance());
				shopConfig.set("unlimited.inventory", shop.isLimitlessStock());

				try {
					shopConfig.save(shopFile);
				} catch (IOException e) {
					log.severe("Unable to save shop: " + shop.getName() + " (" + shop.getId() + ".yml)");
					continue;
				}
			}
			log.info("Shops saved!");
		}
	}

	private static void cacheShopData(FileConfiguration config, long id) {
		Shop shop = new Shop(config.getString("name"));

		shop.setId(id);
		shop.setOwner(Bukkit.getOfflinePlayer(UUID.fromString(config.getString("owner"))));
		try {
			shop.setDateFromFile(config.getString("created"));
		} catch (ParseException exception) {
			exception.printStackTrace();
		}

		for (String inventory : config.getStringList("inventory")) {
			String[] values = inventory.split(",");

			int itemId = 0, inventoryAmount = 0, maxInventory = -1;
			double buyPrice = -1, sellPrice = -1;
			short damage = 0;

			if (values[0].contains(":")) {
				if (isInt(values[0].split(":")[0]) && isInt(values[0].split(":")[1])) {
					itemId = Integer.parseInt(values[0].split(":")[0]);

					damage = Short.parseShort(values[0].split(":")[1]);
				}
			} else if (isInt(values[0]))
				itemId = Integer.parseInt(values[0]);

			ItemInfo item = Items.itemById(itemId, damage);

			if (values.length >= 1)
				if (isDouble(values[1]))
					buyPrice = Double.parseDouble(values[1]);

			if (values.length >= 2)
				if (isDouble(values[2]))
					sellPrice = Double.parseDouble(values[2]);

			if (values.length >= 3)
				if (isInt(values[3]))
					inventoryAmount = Integer.parseInt(values[3]);

			if (values.length >= 4)
				if (isInt(values[4]))
					maxInventory = Integer.parseInt(values[4]);

			shop.add(item.getType(), damage, buyPrice, sellPrice, inventoryAmount, maxInventory);
		}

		addShop(shop);
	}
}