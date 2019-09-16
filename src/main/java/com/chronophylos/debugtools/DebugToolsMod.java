package com.chronophylos.debugtools;

import com.chronophylos.debugtools.commands.DumpCommand;
import com.chronophylos.debugtools.commands.NBTDumpCommand;
import com.chronophylos.debugtools.commands.SimpleDumpCommand;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.List;

@Mod(modid = DebugToolsMod.MODID, name = DebugToolsMod.NAME, version = DebugToolsMod.VERSION)
public class DebugToolsMod {
  public static final String MODID = "debugtools";
  public static final String NAME = "Debug Tools";
  public static final String VERSION = "0.1.0";

  private static Logger logger;

  private List<SimpleDumpCommand> commands;

  @Mod.EventHandler
  public void preInit(FMLPreInitializationEvent event) {
    logger = event.getModLog();

    commands = Arrays.asList(new DumpCommand(logger), new NBTDumpCommand(logger));
  }

  @Mod.EventHandler
  @SideOnly(Side.CLIENT)
  public void init(FMLInitializationEvent event) {
    commands.forEach(SimpleDumpCommand::register);
  }
}
