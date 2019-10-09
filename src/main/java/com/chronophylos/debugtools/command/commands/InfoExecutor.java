package com.chronophylos.debugtools.command.commands;

import net.ilexiconn.llibrary.server.command.ICommandExecutor;
import net.ilexiconn.llibrary.server.command.argument.CommandArguments;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.block.statemap.BlockStateMapper;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.event.RegistryEvent;

public class InfoExecutor implements ICommandExecutor {

    private ItemStack getItemInHand(Entity entity) {
        for (ItemStack itemStack : entity.getHeldEquipment()) {
            return itemStack;
        }

        return null;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, CommandArguments arguments) throws CommandException {
        if (!(sender instanceof Entity)) {
            throw new CommandException("invoker must be a entity");
        }

        Entity entity = (Entity) sender;
        ItemStack itemStack = getItemInHand(entity);
        Item item = itemStack.getItem();
        String tabLabel = item.getCreativeTab() != null ? item.getCreativeTab().getTabLabel() : "None";

        String stringBuilder = "Name: " + itemStack.getDisplayName() + "\n" +
                "ID: " + itemStack.getCount() + "x" + item.getRegistryName() + "@" + itemStack.getMetadata() + "\n" +
                "Type: " + item.getRegistryType().getName() + "\n" +
                "Enchantments: " + itemStack.getEnchantmentTagList().toString() + "\n" +
                "Tab: " + tabLabel + "\n" +
                "Unlocalized Name: " + itemStack.getUnlocalizedName() + "\n" +
                "NBT Data: " + itemStack.getTagCompound();
        sender.sendMessage(new TextComponentString(stringBuilder));

    }
}
