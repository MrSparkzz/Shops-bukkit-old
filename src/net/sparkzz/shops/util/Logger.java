package net.sparkzz.shops.util;

import java.util.logging.Level;

/**
 * Created by Brendon on 8/20/2015.
 */
public class Logger {
	
	private java.util.logging.Logger logger = java.util.logging.Logger.getLogger("Minecraft");

	public void info(String message) {
		logger.log(Level.INFO, "[Shops] " + message);
	}

	public void severe(String message) {
		logger.log(Level.SEVERE, "[Shops] " + message);
	}

	public void warn(String message) {
		logger.log(Level.WARNING, "[Shops] " + message);
	}
}