package net.sparkzz.shops.command;

import net.milkbowl.vault.item.ItemInfo;
import net.sparkzz.shops.util.Utility;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

/**
 * Created by Brendon on 8/20/2015.
 */
public abstract class Command extends Utility implements CommandExecutor {

	protected CommandSender sender = null;
	protected String command = null, description = null, permission = null, usage = null;
	protected String[] args;

	public Command(String description, String permission, String usage) {
		this.description = description;
		this.permission = permission;
		this.usage = usage;
	}

	public abstract boolean process();

	protected boolean permitted(CommandSender sender, String permission) {
		return sender.hasPermission(permission);
	}

	@Override
	public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String commandLabel, String[] args) {
		if (!(sender.hasPermission(permission))) {
			sender.sendMessage(ChatColor.RED + "You are not permitted to perform this command!");
			return true;
		}

		this.sender = sender;
		this.command = command.toString();
		this.args = args;

		if (!process()) sender.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + usage);
		return true;
	}

	protected int countItems(PlayerInventory inventory, ItemInfo item) {
		int amount = 0;

		for (ItemStack current : inventory.all(item.material).values()) {
			if (item.isDurable()) {
				int damage = percentOfDamage(current);
			} else {
				if (current.getDurability() != item.subTypeId) continue;
			}
			amount += current.getAmount();
		}
		return amount;
	}

	protected int getFreeSpace(PlayerInventory inventory, ItemInfo item) {
		int count = 0;

		for (ItemStack slot : inventory.getContents()) {
			if (slot == null || slot.getType() == Material.AIR) {
				count += item.getStackSize();
				continue;
			}
			if (slot.getType() == item.getType() && slot.getDurability() == item.subTypeId) {
				count += Math.max(item.getStackSize() - slot.getAmount(), 0);
			}
		}
		return count;
	}

	protected int percentOfDamage(ItemStack item) {
		return (short)((double) item.getDurability() / (double) item.getType().getMaxDurability() * 100);
	}

	protected int removeItems(PlayerInventory inventory, ItemInfo item, int amount) {
		for (int i : inventory.all(item.material).keySet()) {
			if (amount == 0) continue;

			ItemStack current = inventory.getItem(i);

			if (item.isDurable()) {
				int damage = percentOfDamage(current);

				if (current.getDurability() != item.subTypeId) continue;
			}

			int found = current.getAmount();

			if (amount >= found) {
				amount -= found;
				inventory.setItem(i, null);
			} else {
				current.setAmount(found - amount);
				inventory.setItem(i, current);
				amount = 0;
			}
		}
		return amount;
	}

	protected void giveItems(ItemInfo item, int amount) {
		Player player = (Player) sender;
		int stackSize = item.getStackSize();

		// fills stacks of items that aren't currently filled
		for (int i : player.getInventory().all(item.material).keySet()) {
			if (amount == 0) break;

			ItemStack current = player.getInventory().getItem(i);

			if (current.getType().equals(item.material) && current.getDurability() == item.subTypeId) {
				if (current.getAmount() < stackSize) {
					int remainder = stackSize - current.getAmount();

					if (remainder <= amount) {
						amount -= remainder;
						current.setAmount(stackSize);
					} else {
						current.setAmount(stackSize - remainder + amount);
						amount = 0;
					}
				}
			}
		}

		// fill free slots
		for (int i = 0 ; i < 36 ; i++) {
			if (amount == 0) break;

			ItemStack current = player.getInventory().getItem(i);

			if (current == null || current.getType() == Material.AIR) {
				if (amount == 0) continue;

				if (amount >= stackSize) {
					player.getInventory().setItem(i, new ItemStack(item.material, stackSize, item.subTypeId));
					amount -= stackSize;
				} else {
					player.getInventory().setItem(i, new ItemStack(item.material, amount, item.subTypeId));
					amount = 0;
				}
			}
		}

		do {
			if (amount == 0) break;

			ItemStack stack = null;

			if (amount >= stackSize) {
				stack = new ItemStack(item.material, stackSize, item.subTypeId);
				amount -= stackSize;
			} else {
				stack = new ItemStack(item.material, amount, item.subTypeId);
				amount = 0;
			}
			player.getWorld().dropItemNaturally(player.getLocation(), stack);
		} while (amount > 0);
	}
}