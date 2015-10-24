package net.sparkzz.shops.command;

/**
 * Created by Brendon on 8/28/2015.
 */
public class Shop extends Command {

    public Shop() {
        super("Shop commands", "shops.info", "/shop help");
    }

    // /shop create/destroy/info/add/remove/modify

    @Override
    public boolean process() {

        return false;
    }
}