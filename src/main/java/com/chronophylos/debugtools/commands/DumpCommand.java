package com.chronophylos.debugtools.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import org.apache.logging.log4j.Logger;

import java.io.PrintWriter;
import java.util.SortedSet;
import java.util.TreeSet;

public class DumpCommand extends CommandBase {
    private Logger logger;
    public DumpCommand(Logger logger) {
        this.logger = logger;
    }

    @Override
    public String getName() {
        return "dump";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return null;
    }

    private void dump(ICommandSender sender) {
        dump(sender, null);
    }

    private void dump(ICommandSender sender,String modid) {
        SortedSet<String> items = new TreeSet<>();
        String filename = (modid != null ? modid + "_" : "") + "itemdump.txt";

        for (Item item : Item.REGISTRY) {
            ResourceLocation registryName = item.getRegistryName();
            if (registryName == null) {
                logger.warn(item.getUnlocalizedName() + " has a null registry name");
                continue;
            }

            if (modid != null && !registryName.getResourceDomain().equalsIgnoreCase(modid)) {
                continue;
            }

            if (item.getHasSubtypes()) {
                NonNullList<ItemStack> itemStacks = NonNullList.create();

                CreativeTabs creativeTab = item.getCreativeTab();
                if (creativeTab == null) creativeTab = CreativeTabs.SEARCH;

                item.getSubItems(creativeTab, itemStacks);
                for (ItemStack itemStack : itemStacks) {
                    items.add(itemStack.getItem().getRegistryName() + "#" + itemStack.getItemDamage());
                }
            } else {
                items.add(String.valueOf(item.getRegistryName()));
            }
        }

        try {
            PrintWriter writer = new PrintWriter(filename, "UTF-8");
            items.forEach(writer::println);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        sender.sendMessage(new TextComponentString("Dumped " + items.size() + " items to " + filename));
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length > 0) {
            dump(sender, args[0]);
        } else {
            dump(sender);
        }
    }
}
