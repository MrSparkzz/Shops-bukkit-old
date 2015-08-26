package net.sparkzz.shops.command;

import net.milkbowl.vault.item.ItemInfo;
import net.milkbowl.vault.item.Items;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

/**
 * Created by Brendon on 8/20/2015.
 */
public class Buy extends Command {

	public Buy() {
		super("Buy from the current shop you're in", "shops.buy", "/buy <id> <amount>");
	}

	@Override
	public boolean process() {
		if (!(sender instanceof Player)) {
			sender.sendMessage("Silly console, you can't buy things!");
			return true;
		}

		if (args.length < 2 || args.length > 2) return false;

		Player player = (Player) sender;

		int amount = 0;
		ItemInfo item;
		short damage;
		String rawDamage = "0", rawId = null;

		if (args[0].contains(":")) {
			rawId = args[0].split(":")[0];
			rawDamage = args[0].split(":")[1];
		} else {
			rawId = args[0];
		}

		if (isInt(rawDamage))
			damage = Short.parseShort(rawDamage);
		else {
			player.sendMessage(ChatColor.RED + "Damage value of " + ChatColor.GOLD + rawDamage + ChatColor.RED + " is invalid!");
			return false;
		}

		if (isInt(rawId))
			item = Items.itemById(Integer.parseInt(rawId), damage);
		else if (rawId != null);
			item = Items.itemByType(Items.itemByString(rawId).material, damage);

		if (item == null) {
			player.sendMessage(ChatColor.RED + "Item " + ChatColor.GOLD + rawId + ChatColor.RED + " could not be found!");
			return false;
		}

		if (isInt(args[1]))
			amount = Integer.parseInt(args[1]);

		if (amount < 1) {
			player.sendMessage(ChatColor.RED + "Invalid quantity!");
			return false;
		}

		buy(item, amount);
		return true;
	}

	private void buy(ItemInfo item, int amount) {
		Player player = (Player) sender;

		boolean unlimitedStock = false, unlimitedMoney = false;
		double price = getShop("Spawn Shop").getBuyPrice(item);
		int stock = 100;
		OfflinePlayer owner = null;

		if (amount > stock && !unlimitedStock) {
			amount = stock;
			player.sendMessage(ChatColor.RED + "The shop only has " + ChatColor.GOLD + stock + " " + item.name);
		}

		int freeSpace = getFreeSpace(player.getInventory(), item);

		if (freeSpace == 0) {
			player.sendMessage(ChatColor.RED + "You don't have anymore space in your inventory!");
			return;
		}

		if (amount > freeSpace) {
			amount = freeSpace;
			player.sendMessage(ChatColor.RED + "You only have room for " + ChatColor.GOLD + amount);
		}

		double total = amount * price;

		if (!unlimitedMoney || owner != null) {
			// TODO: pay owner
		}

		if (econ.withdrawPlayer(player, total).transactionSuccess()) {
			stock -= amount;
			giveItems(item, amount);
			player.sendMessage(ChatColor.GREEN + "You have purchased " + ChatColor.GOLD + amount + " "  + item.name + ChatColor.GREEN + " for " + ChatColor.GOLD + econ.format(total) + ChatColor.GREEN + "!");
			return;
		}

		player.sendMessage(ChatColor.RED + "Insufficient funds!");
	}
}