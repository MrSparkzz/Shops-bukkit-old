package net.sparkzz.shops.command;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Brendon on 8/20/2015.
 */
public class Sell extends Command {

	public Sell() {
		super("Sell to the shop you're in.", "shops.sell", "/sell <id:amount> <amount>");
	}

	@Override
	public boolean process() {
		if (!(sender instanceof Player)) {
			sender.sendMessage("Silly console, you can't sell things!");
			return true;
		}

		if (args.length > 2) return false;

		Player player = (Player) sender;

		int id = 0, amount = 0;

		if (args.length == 0) {
				id = player.getInventory().getItemInHand().getTypeId();
				amount = player.getInventory().getItemInHand().getAmount();
		} else if (args.length == 1) {
			id = player.getInventory().getItemInHand().getTypeId();

			try {
				amount = Integer.parseInt(args[0]);
			} catch (NumberFormatException exception) {
				sender.sendMessage(ChatColor.RED + "Your amount was an invalid number!");
				return false;
			}
		} else {
			try {
				id = Integer.parseInt(args[0]);
				amount = Integer.parseInt(args[1]);
			} catch (NumberFormatException exception) {
				sender.sendMessage(ChatColor.RED + "One of your numbers was invalid!");
			}
		}

		if (player.getInventory().contains(Material.getMaterial(id), amount)) {
			if (econ.depositPlayer(player, 20 * amount).transactionSuccess()) {
				player.getInventory().remove(new ItemStack(Material.getMaterial(id), amount));
				player.sendMessage(ChatColor.GREEN + "You have sold: " + ChatColor.GOLD + amount + ChatColor.GREEN + " of " + ChatColor.GOLD + id + ChatColor.GREEN + "!");
				return true;
			}

			player.sendMessage(ChatColor.RED + "Insufficient Funds");
			return true;
		}

		player.sendMessage(ChatColor.RED + "You do not have enough of this item!");
		return true;
	}
}
