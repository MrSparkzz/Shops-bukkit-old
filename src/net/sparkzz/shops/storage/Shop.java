package net.sparkzz.shops.storage;

import net.milkbowl.vault.item.ItemInfo;
import net.milkbowl.vault.item.Items;
import org.bukkit.Material;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Brendon on 8/25/2015.
 */
public class Shop {

	private Map<ItemInfo, Double[]> price = Collections.synchronizedMap(new HashMap<>());
	private String name;

	public Shop(String name) {
		this.name = name;
	}

	public double getBuyPrice(ItemInfo item) {
		if (price.containsKey(item))
			return price.get(item)[0];
		return -1;
	}

	public double getSellPrice(ItemInfo item) {
		if (price.containsKey(item))
			return price.get(item)[1];
		return -1;
	}

	public String getName() {
		return name;
	}

	private void add(Material material, double buy, double sell) {
		add(material, 0, buy, sell);
	}

	private void add(Material material, int damage, double buy, double sell) {
		Double[] required = {buy, sell};

		price.put(Items.itemByType(material, (short) damage), required);
	}
}