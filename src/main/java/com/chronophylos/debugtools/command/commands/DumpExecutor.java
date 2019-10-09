package com.chronophylos.debugtools.command.commands;

import com.chronophylos.debugtools.DebugTools;
import net.ilexiconn.llibrary.server.command.ICommandExecutor;
import net.ilexiconn.llibrary.server.command.argument.CommandArguments;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;

import java.io.PrintWriter;
import java.util.SortedSet;
import java.util.TreeSet;

public class DumpExecutor implements ICommandExecutor {
    private boolean showNBT = false;
    private String modid = null;

    private static final String filename = "itemdump.txt";


    private void dumpRegistry(ICommandSender sender) {
        SortedSet<String> items = new TreeSet<>();
        String filename = (modid != null ? modid + "_" : "") + DumpExecutor.filename;

        for (Item item : Item.REGISTRY) {
            ResourceLocation registryName = item.getRegistryName();
            if (registryName == null) {
                DebugTools.LOGGER.warn(item.getUnlocalizedName() + " has a null registry name");
                continue;
            }

            if (modid != null && !registryName.getResourceDomain().equalsIgnoreCase(modid)) {
                continue;
            }

            items.addAll(dumpItem(item));
        }

        writeItemDump(sender, items, filename);
    }

    private static void writeItemDump(ICommandSender sender, SortedSet<String> items, String filename) {
        try {
            PrintWriter writer = new PrintWriter(filename, "UTF-8");
            items.forEach(writer::println);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        sender.sendMessage(new TextComponentString("Dumped " + items.size() + " items to " + filename));
    }

    private SortedSet<String> dumpItem(Item item) {
        SortedSet<String> items = new TreeSet<>();

        if (item.getHasSubtypes()) {
            NonNullList<ItemStack> itemStacks = NonNullList.create();

            CreativeTabs creativeTab = item.getCreativeTab();
            if (creativeTab == null) creativeTab = CreativeTabs.SEARCH;

            item.getSubItems(creativeTab, itemStacks);
            for (ItemStack itemStack : itemStacks) {
                items.add(itemToString(itemStack));
            }
        } else {
            items.add(itemToString(item));
        }

        return items;
    }

    private SortedSet<String> dumpItemStack(ItemStack itemStack) {
        SortedSet<String> items = new TreeSet<>();

        items.add(itemToString(itemStack));

        return items;
    }

    private void dumpHand(Entity sender) {
        SortedSet<String> items = new TreeSet<>();
        String filename = "hand_" + DumpExecutor.filename;

        for (ItemStack itemStack : sender.getHeldEquipment()) {
            if (itemStack.getItem() == Items.AIR) continue;
            items.addAll(dumpItemStack(itemStack));
        }

        writeItemDump(sender, items, filename);
    }

    private void dumpInventory(Entity sender) {
        SortedSet<String> items = new TreeSet<>();
        String filename = "inv_" + DumpExecutor.filename;

        for (ItemStack itemStack : sender.getArmorInventoryList()) {
            if (itemStack.getItem() == Items.AIR) continue;
            items.addAll(dumpItemStack(itemStack));
        }

        writeItemDump(sender, items, filename);
    }

    private String itemToString(Item item) {
        return String.valueOf(item.getRegistryName());
    }

    private String itemToString(ItemStack itemStack) {
        // Get NBT data if the item has any
        String nbt = "";
        if (itemStack.hasTagCompound() && showNBT) {
            nbt = itemStack.getTagCompound().toString();
        }
        return itemToString(itemStack.getItem()) + "#" + itemStack.getItemDamage() + nbt;
    }

    private static void ensureIsEntity(ICommandSender sender) throws CommandException {
        if (!(sender instanceof Entity)) {
            throw new WrongUsageException("Invoker must be a entity");
        }
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, CommandArguments arguments) throws CommandException {
        if (arguments.hasArgument("modid")) {
            modid = arguments.getArgument("modid");
            switch (modid) {
                case "*":
                case "a":
                case "all":
                    modid = null;
            }
        }

        if (arguments.hasArgument("showNBT")) {
            showNBT = arguments.getArgument("showNBT");
        }

        String which = arguments.getArgument("which");

        switch (which) {
            case "h":
            case "hand":
                ensureIsEntity(sender);
                dumpHand((Entity) sender);
                break;
            case "i":
            case "inv":
            case "inventory":
                ensureIsEntity(sender);
                dumpInventory((Entity) sender);
                break;
            case "b":
            case "hotbar":
                ensureIsEntity(sender);
                sender.sendMessage(new TextComponentString("not yet implemented"));
                break;
            case "a":
            case "all":
            case "registry":
                dumpRegistry(sender);
                break;
            default:
                throw new WrongUsageException("I don't know what do dump.");
        }
    }
}
