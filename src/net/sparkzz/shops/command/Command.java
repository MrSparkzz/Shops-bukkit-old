package net.sparkzz.shops.command;

import net.md_5.bungee.api.ChatColor;
import net.sparkzz.shops.util.Utility;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

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
			sender.sendMessage("You are not permitted to perform this command!");
			return true;
		}

		this.sender = sender;
		this.command = command.toString();
		this.args = args;

		if (!process()) sender.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + usage);
		return true;
	}
}