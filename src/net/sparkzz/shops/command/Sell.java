package net.sparkzz.shops.command;

import net.milkbowl.vault.item.ItemInfo;
import net.milkbowl.vault.item.Items;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

/**
 * Created by Brendon on 8/20/2015.
 */
public class Sell extends Command {

	public Sell() {
		super("Sell to the shop you're in.", "shops.sell", "/sell <id|amount> <amount>");
	}

	@Override
	public boolean process() {
		if (!(sender instanceof Player)) {
			sender.sendMessage("Silly console, you can't sell things!");
			return true;
		}

		if (args.length > 2) return false;

		Player player = (Player) sender;

		int amount = -1;
		ItemInfo item = null;
		short damage;
		String rawDamage = "0", rawId = null;

		if (args.length == 0) {
			if (player.getItemInHand() == null || player.getItemInHand().getType() == Material.AIR) {
				player.sendMessage(ChatColor.RED + "You aren't holding any items!");
				return false;
			}

			item = Items.itemByStack(player.getItemInHand());
			amount = player.getItemInHand().getAmount();
		} else if (args.length == 1) {
			if (player.getItemInHand() == null || player.getItemInHand().getType() == Material.AIR) {
				player.sendMessage(ChatColor.RED + "You aren't holding any items!");
				return false;
			}

			item = Items.itemByStack(player.getItemInHand());

			if (isInt(args[0]))
				amount = Integer.parseInt(args[0]);
			else {
				player.sendMessage(ChatColor.RED + "Invalid quantity!");
				return false;
			}
		} else {
			if (args[0].contains(":")) {
				rawId = args[0].split(":")[0];
				rawDamage = args[0].split(":")[1];
			} else {
				rawId = args[0];
			}

			if (isInt(args[1]))
				amount = Integer.parseInt(args[1]);
			else {
				player.sendMessage(ChatColor.RED + "Invalid quantity!");
				return true;
			}
		}

		if (isInt(rawDamage))
			damage = Short.parseShort(rawDamage);
		else {
			player.sendMessage(ChatColor.RED + "Damage value of " + ChatColor.GOLD + rawDamage + ChatColor.RED + " is invalid!");
			return false;
		}

		if (isInt(rawId))
			item = Items.itemById(Integer.parseInt(rawId), damage);
		else if (rawId != null)
			item = Items.itemByType(Items.itemByString(rawId).material, damage);

		if (item == null) {
			player.sendMessage(ChatColor.RED + "Item " + ChatColor.GOLD + rawId + ChatColor.RED + " could not be found!");
			return false;
		}

		if (amount < 0) {
			player.sendMessage(ChatColor.RED + "Invalid quantity!");
			return true;
		}

		sell(item, amount);
		return true;
	}

	private void sell(ItemInfo item, int amount) {
		Player player = (Player) sender;

		boolean unlimitedStock = false, unlimitedMoney = false;
		double price = getShop("Spawn Shop").getSellPrice(item);
		OfflinePlayer owner = null;

		if (price == -1) {
			player.sendMessage(ChatColor.RED + "This shop is not buying any " + ChatColor.GOLD + item.name + ChatColor.RED + " at this time!");
			return;
		}

		int inventory = countItems(player.getInventory(), item);

		if (inventory == 0) {
			player.sendMessage(ChatColor.RED + "You don't have any " + ChatColor.GOLD + item.name + "s" + ChatColor.RED + "!");
			return;
		}

		if (amount > inventory) {
			player.sendMessage(ChatColor.RED + "You only have " + ChatColor.GOLD + inventory + ChatColor.RED + " in your inventory that can be sold!");
			return;
		}

		double total = price * amount;

		if (!unlimitedMoney && owner != null) {
			// TODO: take from owner
		}

		if (econ.depositPlayer(player, total).transactionSuccess()) {
			removeItems(player.getInventory(), item, amount);
			player.sendMessage(ChatColor.GREEN + "You have sold " + ChatColor.GOLD + amount + " " + item.name + ChatColor.GREEN + " for " + ChatColor.GOLD + econ.format(total) + ChatColor.GREEN + "!");
			return;
		}

		player.sendMessage(ChatColor.RED + "Insufficient funds!");
	}
}