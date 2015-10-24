package net.sparkzz.shops.command;

import net.milkbowl.vault.item.ItemInfo;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Brendon on 8/28/2015.
 */
public class Browse extends Command {

	public Browse() {
		super("Browse the shop you're in", "shops.browse", "/browse <item>");
	}

	@Override
	public boolean process() {
		if (!(sender instanceof Player)) {
			sender.sendMessage("Silly console, you can't browse this shop!");
			return true;
		}

		if (args.length > 2) return false;

		if (args.length == 2) {
			// search for item and list a description
			// /browse item coal --> item: name (id:damage), stock amount, buy price, sell price, your quantity
			return false;
		}

		Player player = (Player) sender;

		int currentPage = 1;

		net.sparkzz.shops.storage.Shop shop = getShop("Example Shop"); // TODO: choose shop based on location

		List<ItemInfo> items = shop.getItems(); // TODO: permanently store items in store inv in some way

		if (items.isEmpty()) {
			player.sendMessage(ChatColor.RED + "This shop is currently empty!");
			return true;
		}

		Collections.sort(items, new Comparator<ItemInfo>() {
			public int compare(ItemInfo item1, ItemInfo item2) {
				int result = String.CASE_INSENSITIVE_ORDER.compare(item1.getName(), item2.getName());
				return (result != 0) ? result : item1.getName().compareTo(item2.getName());
			}
		});

		// custom pagination test
		int page = 1, resultsPerPage = 5, pages = (int) Math.ceil(items.size() / resultsPerPage);

		if (args.length == 1)
			if (isInt(args[0]))
				page = Integer.parseInt(args[0]);
			else {
				sender.sendMessage(ChatColor.RED + "Invalid page number!");
				return true;
			}

		if (page > pages) {
			sender.sendMessage(ChatColor.RED + "Invalid page number!");
			return true;
		}

		List<ItemInfo> itemsOnPage = new ArrayList<ItemInfo>();

		int i = (page * resultsPerPage) - 1;

		while (i < (page * resultsPerPage) + resultsPerPage - 1 && i < items.size()) {

			sender.sendMessage("items on page = " + itemsOnPage.size());

			itemsOnPage.add(items.get(i));
			i++;
		}

		sender.sendMessage(ChatColor.DARK_GREEN + "===[" + ChatColor.GOLD + shop.getName() + ChatColor.DARK_GREEN + "]===");
		sender.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + items.size() + " items in this shop");
		i = 0;

		while (itemsOnPage.size() > i) {
			ItemInfo item = itemsOnPage.get(i);

			String formatted = ChatColor.DARK_AQUA + item.getName();

			if (item.getSubTypeId() > 0) formatted += ChatColor.DARK_GREEN + ":" + ChatColor.DARK_AQUA + item.getSubTypeId();
			if (shop.getStock(item) > 0) formatted += ChatColor.DARK_GREEN + ", stock: " + ChatColor.GOLD + shop.getStock(item);
			else formatted += ChatColor.DARK_GREEN + ", " + ChatColor.RED + "OUT OF STOCK";
			if (shop.getBuyPrice(item) != -1 && shop.getStock(item) != 0) formatted += ChatColor.DARK_GREEN + ", buy for " + ChatColor.GOLD + econ.format(shop.getBuyPrice(item));
			if (shop.getSellPrice(item) != -1) formatted += ChatColor.DARK_GREEN + ", sell for " + ChatColor.GOLD + econ.format(shop.getSellPrice(item));

			sender.sendMessage(formatted);
			i++;
		}
		if (pages > 1) sender.sendMessage(ChatColor.DARK_GREEN + "------ page " + ChatColor.GOLD + page + ChatColor.DARK_GREEN + " of " + ChatColor.GOLD + pages + ChatColor.DARK_GREEN + " ------");

		return true;
	}
}