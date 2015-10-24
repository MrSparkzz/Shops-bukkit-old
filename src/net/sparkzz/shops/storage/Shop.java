package net.sparkzz.shops.storage;

import net.milkbowl.vault.item.ItemInfo;
import net.milkbowl.vault.item.Items;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Brendon on 8/25/2015.
 */
public class Shop {

	private boolean limitlessBalance = false, limitlessStock = false;
	private Date created;
	private Map<ItemInfo, Map<String, Object>> inv = Collections.synchronizedMap(new HashMap<ItemInfo, Map<String, Object>>());
	private OfflinePlayer owner;
	private String name;
	private long id;

	public Shop(String name) {
		this.name = name;
	}

	public boolean isLimitlessBalance() {
		return limitlessBalance;
	}

	public boolean isLimitlessStock() {
		return limitlessStock;
	}

	public double getBuyPrice(ItemInfo item) {
		if (inv.containsKey(item))
			return (Double) inv.get(item).get("buyPrice");
		return -1;
	}

	public double getSellPrice(ItemInfo item) {
		if (inv.containsKey(item))
			return (Double) inv.get(item).get("sellPrice");
		return -1;
	}

	public int getStock(ItemInfo item) {
		if (inv.containsKey(item))
			return (Integer) inv.get(item).get("stock");
		return 0;
	}

	public int getMaxStock(ItemInfo item) {
		if (inv.containsKey(item))
			return (Integer) inv.get(item).get("maxStock");
		return -1;
	}

	public List<ItemInfo> getItems() {
		return new ArrayList<ItemInfo>(inv.keySet());
	}

	public List<String> getItemsByName() {
		List<String> names = new ArrayList<String>();

		for (ItemInfo item : inv.keySet()) {
			names.add(item.getName());
		}

		return names;
	}

	public List<String> getItemsAsFormattedList() {
		List<String> list = new ArrayList<String>();

		for (ItemInfo item : inv.keySet()) {
			String formatted;

			if (item.getSubTypeId() > 0)
				formatted = item.getId() + ":" + item.getSubTypeId() + ",";
			else formatted = item.getId() + ",";

			formatted += inv.get(item).get("buyPrice") + ",";
			formatted += inv.get(item).get("sellPrice") + ",";
			formatted += inv.get(item).get("stock") + ",";
			formatted += inv.get(item).get("maxStock");

			list.add(formatted);
		}

		return list;
	}

	public long getId() {
		return id;
	}

	public OfflinePlayer getOwner() {
		return owner;
	}

	public String getFormattedDate() {
		SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy;HH:mm:ss");
		return format.format(created);
	}

	public String getName() {
		return this.name;
	}

	public void add(ItemInfo item, double buy, double sell, int inventory, int maxInventory) {
		add(item.getType(), item.getSubTypeId(), buy, sell, inventory, maxInventory);
	}

	public void add(Material material, int damage, double buy, double sell, int inventory, int maxInventory) {
		Map<String, Object> invData = new HashMap<String, Object>();

		invData.put("buyPrice", buy);
		invData.put("sellPrice", sell);
		invData.put("stock", inventory);
		invData.put("maxStock", maxInventory);

		this.inv.put(Items.itemByType(material, (short) damage), invData);
	}

	public void addStock(ItemInfo item, int amount) {
		inv.get(item).put("stock", getStock(item) + amount);
	}

	public void removeStock(ItemInfo item, int amount) {
		inv.get(item).put("stock", getStock(item) - amount);
	}

	public void remove(Material material) {
		if (inv.containsKey(Items.itemByType(material)))
			inv.remove(Items.itemByType(material));
	}

	public void setDateCreated() {
		this.created = new Date();
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setOwner(OfflinePlayer player) {
		this.owner = player;
	}

	protected void setDateFromFile(String date) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("mm/dd/yyyy;hh:mm:ss");
		this.created = format.parse(date);
	}

	public void setLimitlessBalance(boolean value) {
		this.limitlessBalance = value;
	}

	public void setLimitlessStock(boolean value) {
		this.limitlessStock = value;
	}
}