package net.sparkzz.shops.util;

import net.milkbowl.vault.economy.Economy;
import net.sparkzz.shops.storage.Shop;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Brendon on 8/20/2015.
 */
public class Utility {

	private static Map<String, Shop> shops = Collections.synchronizedMap(new HashMap<>());

	public static Logger log;
	public static Economy econ;

	public static Shop getShop(String name) {
		if (shops.containsKey(name))
			return shops.get(name);
		return null;
	}

	public static void addShop(Shop shop) {
		shops.put(shop.getName(), shop);
	}
}