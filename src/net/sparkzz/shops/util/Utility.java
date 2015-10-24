package net.sparkzz.shops.util;

import net.milkbowl.vault.economy.Economy;
import net.sparkzz.shops.storage.Shop;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Brendon on 8/20/2015.
 */
public class Utility {

	private static Map<String, Shop> shops = Collections.synchronizedMap(new HashMap<String, Shop>());

	public static Logger log;
	public static Economy econ;

	public static Shop getShop(String name) {
		if (shops.containsKey(name))
			return shops.get(name);
		return null;
	}

	public static Collection<Shop> getShops() {
		if (!shops.isEmpty())
			return shops.values();
		return null;
	}

	public static void addShop(Shop shop) {
		shops.put(shop.getName(), shop);
	}

	public static boolean isInt(String string) {
		if (string == null) return false;

		int length = string.length();

		if (length == 0) return false;

		int i = 0;

		if (string.charAt(0) == '-') {
			if (length == 1) return false;

			i = 1;
		}

		for (; i < length; i++) {
			char c = string.charAt(i);

			if (c <= '/' || c >= ':') return false;
		}
		return true;
	}

	public static boolean isDouble(String str) {
		if (str == null) {
			return false;
		}

		int length = str.length();

		if (length == 1 ) {
			return false;
		}

		int i = 0;

		if (str.charAt(0) == '-') {
			if (length < 3) {
				return false;
			}
			i = 1;
		}

		int numOfDot = 0;

		for (; i < length; i++) {
			char c = str.charAt(i);
			if (c == '.')
				numOfDot++;
			else if (c == '/')
				return false;
			else if (c < '.' || c > '9') {
				return false;
			}
		}

		if (numOfDot != 1 )
			return false;
		return true;
	}
}