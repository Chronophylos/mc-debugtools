package com.chronophylos.debugtools;

import com.chronophylos.debugtools.command.DCommand;
import com.chronophylos.debugtools.command.ModCommand;
import com.chronophylos.debugtools.command.commands.DumpExecuter;
import net.ilexiconn.llibrary.server.command.Command;
import net.ilexiconn.llibrary.server.command.CommandHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

@Mod(modid = DebugTools.MOD_ID, name = DebugTools.MOD_NAME, version = DebugTools.VERSION)
public class DebugTools {
  public static final String MOD_ID = "debugtools";
  public static final String MOD_NAME = "Debug Tools";
  public static final String VERSION = "@VERSION@";

  public static final Logger LOGGER = LogManager.getLogger("DebugTools");

  @Mod.Instance(DebugTools.MOD_ID)
  public static DebugTools instance;

  @Mod.EventHandler
  public void preInit(FMLPreInitializationEvent event) {}

  @Mod.EventHandler
  public void init(FMLInitializationEvent event) {
  }

  @Mod.EventHandler
  public void onServerStart(FMLServerStartingEvent event) {
    ModCommand cmd = new ModCommand("debugtools").addAlias("dt");

    DCommand dump = DCommand.create("dump")
            .addAlias("d")
            .addRequiredArgument("which", String.class)
            .addOptionalArgument("modid", String.class)
            .addOptionalArgument("showNBT", Boolean.class)
            .setExecutor(new DumpExecuter());
    cmd.addSubcommand(dump);

    event.registerServerCommand(cmd);
  }
}
