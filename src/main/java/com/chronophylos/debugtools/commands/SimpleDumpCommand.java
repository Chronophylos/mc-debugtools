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
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import org.apache.logging.log4j.Logger;

import java.io.PrintWriter;
import java.util.SortedSet;
import java.util.TreeSet;

public abstract class SimpleDumpCommand extends CommandBase {
  private Logger logger;
  private String filename;
  private String name;
  private boolean showNBT;

  public SimpleDumpCommand(Logger logger, String filename, String name, boolean showNBT) {
    this.logger = logger;
    this.filename = filename;
    this.name = name;
    this.showNBT = showNBT;
  }

  public void register() {
    try {
      ClientCommandHandler.instance.registerCommand(this);

      MinecraftForge.EVENT_BUS.register(this);
      logger.info("Registered " + name + " command");
    } catch (Throwable e) {
      logger.error("Could not register " + name + " command");
      e.printStackTrace();
    }
  }

  private void dump(ICommandSender sender) {
    dump(sender, null);
  }

  private void dump(ICommandSender sender, String modid) {
    SortedSet<String> items = new TreeSet<>();
    String filename = (modid != null ? modid + "_" : "") + this.filename;

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
          items.add(itemToString(itemStack));
        }
      } else {
        items.add(itemToString(item));
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

  private String itemToString(Item item) {
    return String.valueOf(item.getRegistryName());
  }

  private String itemToString(ItemStack itemStack) {
    // Get NBT data if the item has any
    String nbt =
        itemStack.hasTagCompound() && showNBT ? itemStack.getTagCompound().toString() : "";
    return itemToString(itemStack.getItem()) + "#" + itemStack.getItemDamage() + nbt;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getUsage(ICommandSender sender) {
    return "/" + name + " [modid]";
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args)
      throws CommandException {
    if (args.length > 0) {
      dump(sender, args[0]);
    } else {
      dump(sender);
    }
  }
}
