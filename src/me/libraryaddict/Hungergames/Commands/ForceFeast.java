package me.libraryaddict.Hungergames.Commands;

import me.libraryaddict.Hungergames.Hungergames;
import me.libraryaddict.Hungergames.Managers.TranslationManager;
import me.libraryaddict.Hungergames.Managers.ConfigManager;
import me.libraryaddict.Hungergames.Types.HungergamesApi;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ForceFeast implements CommandExecutor {
    public String[] aliases = new String[] { "ffeast" };
    private TranslationManager cm = HungergamesApi.getTranslationManager();
    private ConfigManager config = HungergamesApi.getConfigManager();
    public String description = "Force a feast to generate at your feet";
    private Hungergames hg = HungergamesApi.getHungergames();

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (sender.hasPermission("hungergames.forcefeast")) {
            int radius = config.getFeastSize();
            int chestLayers = config.getChestLayers();
            if (args.length > 0) {
                if (hg.isNumeric(args[0])) {
                    radius = Integer.parseInt(args[0]);
                    if (radius <= 0) {
                        sender.sendMessage(cm.getCommandForceFeastStupidInput());
                        return true;
                    }
                } else {
                    sender.sendMessage(String.format(cm.getCommandForceFeastNotANumber(), args[0]));
                    return true;
                }
                if (args.length > 1) {
                    if (hg.isNumeric(args[1])) {
                        chestLayers = Integer.parseInt(args[1]);
                        if (radius <= 0) {
                            sender.sendMessage(cm.getCommandForceFeastStupidInput());
                            return true;
                        }
                    } else {
                        sender.sendMessage(String.format(cm.getCommandForceFeastNotANumber(), args[1]));
                        return true;
                    }
                }
            }
            Location loc = ((Player) sender).getLocation().clone();
            int height = HungergamesApi.getFeastManager().getSpawnHeight(loc, radius);
            loc.setY(height);
            HungergamesApi.getFeastManager().generatePlatform(loc, height, radius);
            HungergamesApi.getFeastManager().generateChests(loc, chestLayers);
            Bukkit.broadcastMessage(String.format(cm.getCommandForceFeastGenerated(), loc.getBlockX(), loc.getBlockY(),
                    loc.getBlockZ()));
        } else
            sender.sendMessage(cm.getCommandForceFeastNoPermission());
        return true;
    }
}
