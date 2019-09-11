package com.chronophylos.debugtools;

import com.chronophylos.debugtools.commands.DumpCommand;
import net.minecraft.command.ICommand;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.Logger;

@Mod(modid=DebugToolsMod.MODID, name = DebugToolsMod.NAME, version=DebugToolsMod.VERSION)
public class DebugToolsMod {
    public static final String MODID = "debugtools";
    public static final String NAME = "Debug Tools";
    public static final String VERSION = "0.1.0";

    private static Logger logger;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();
    }

    @Mod.EventHandler
    @SideOnly(Side.CLIENT)
    public void init(FMLInitializationEvent event)
    {
        try {
            ICommand dumpCommand = new DumpCommand(logger);
            ClientCommandHandler.instance.registerCommand(dumpCommand);
            MinecraftForge.EVENT_BUS.register(dumpCommand);
            logger.info("Registered dump command");
        } catch (Throwable e) {
            logger.error("Could not register dump command");
            e.printStackTrace();
        }
    }
}
