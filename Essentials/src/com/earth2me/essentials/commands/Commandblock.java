package com.earth2me.essentials.commands;

import com.earth2me.essentials.User;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

import static com.earth2me.essentials.I18n._;

public class Commandblock extends EssentialsCommand {
    protected Commandblock() {
        super("block");
    }

    @Override
    protected void run(final Server server, final User user, final String commandLabel, final String[] args) throws Exception {
        if (args.length == 0) {
            if (user.isAuthorized("essentials.block")) {
                Player player = user.getBase();
                boolean converted = false;
                Map<Integer, ItemStack> spareItems = new HashMap<Integer, ItemStack>();
                for (ItemStack itemStack : player.getInventory().getContents()) {
                    if (itemStack != null) {
                        Material itemType = itemStack.getType();
                        int blockAmount = (int) (itemStack.getAmount() / 9);
                        int leftOver = itemStack.getAmount() % 9;
                        short oldData = itemStack.getDurability();
                        Material newType = null;
                        if (itemType == Material.REDSTONE) {
                            newType = Material.REDSTONE_BLOCK;
                        } else if (itemType == Material.INK_SACK && oldData == 4) {
                            newType = Material.LAPIS_BLOCK;
                        } else if (itemType == Material.COAL) {
                            newType = Material.COAL_BLOCK;
                        } else if (itemType == Material.IRON_INGOT) {
                            newType = (Material.IRON_BLOCK);
                        } else if (itemType == Material.GOLD_INGOT) {
                            newType = (Material.GOLD_BLOCK);
                        } else if (itemType == Material.DIAMOND) {
                            newType = (Material.DIAMOND_BLOCK);
                        } else if (itemType == Material.EMERALD) {
                            newType = (Material.EMERALD_BLOCK);
                        } else {
                            continue;
                        }
                        if (blockAmount > 0) {
                            player.getInventory().remove(itemStack);
                            player.getInventory().addItem(new ItemStack(newType, blockAmount));
                            if (!converted) converted = true;
                            spareItems.putAll(player.getInventory().addItem(new ItemStack(itemType, leftOver, oldData)));
                        }
                    }
                }
                for (ItemStack itemStack : spareItems.values()) {
                    if (itemStack != null) {
                        player.getWorld().dropItem(player.getLocation(), itemStack);
                    }
                }
                if (!spareItems.isEmpty()) {
                    user.sendMessage(_("itemsInventoryFull"));
                }
                if (converted) {
                    user.sendMessage(_("itemsConverted"));
                } else {
                    user.sendMessage(_("itemsNotConverted"));
                }
				// Update user inventory if necessary?
            } else {
                throw new Exception(_("noPerm", "essentials.block"));
            }
        } else {
            throw new NotEnoughArgumentsException();
        }
    }
}
