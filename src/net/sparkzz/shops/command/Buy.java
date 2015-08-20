package net.sparkzz.shops.command;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Brendon on 8/20/2015.
 */
public class Buy extends Command {

	public Buy() {
		super("Buy from the current shop you're in", "shop.buy", "/buy <id> <amount>");
	}

	@Override
	public boolean process() {
		if (!(sender instanceof Player)) {
			sender.sendMessage("Silly console, you can't buy things!");
			return true;
		}

		if (args.length < 2 || args.length > 2) return false;

		Player player = (Player) sender;

		int id = 0, amount = 0;

		try {
			id = Integer.parseInt(args[0]);
			amount = Integer.parseInt(args[1]);
		} catch (NumberFormatException exception) {
			sender.sendMessage(ChatColor.RED + "One of your numbers was not a number!");
			return false;
		}

		if (econ.withdrawPlayer(player, 20 * amount).transactionSuccess()) {
			player.getInventory().addItem(new ItemStack(Material.getMaterial(id), amount));
			player.sendMessage(ChatColor.GREEN + "You have purchased: " + ChatColor.GOLD + amount + ChatColor.GREEN + " of " + ChatColor.GOLD + id + ChatColor.GREEN + "!");
			return true;
		}

		player.sendMessage(ChatColor.RED + "Insufficient funds!");
		return true;
	}
}