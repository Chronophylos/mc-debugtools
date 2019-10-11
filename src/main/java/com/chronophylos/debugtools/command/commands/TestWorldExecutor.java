package com.chronophylos.debugtools.command.commands;

import net.ilexiconn.llibrary.server.command.ICommandExecutor;
import net.ilexiconn.llibrary.server.command.argument.CommandArguments;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

public class TestWorldExecutor implements ICommandExecutor {
  private void setGameRule(MinecraftServer server, ICommandSender sender, String key, String value) {
    server.getEntityWorld().getGameRules().setOrCreateGameRule(key, value);
    if (sender.sendCommandFeedback()) {
      sender.sendMessage(new TextComponentString("Game rule " + key + " has been updated to " + value));
    }
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, CommandArguments arguments) throws CommandException {
    boolean verbose = sender.sendCommandFeedback();

    setGameRule(server, sender,"doDaylightCycle", "false");
    setGameRule(server, sender,"doWeatherCycle", "false");

    server.getEntityWorld().setWorldTime(1000);
    if (verbose) sender.sendMessage(new TextComponentString("Set the time to 1000"));

    setGameRule(server, sender, "doMobSpawning", "false");
    setGameRule(server, sender, "reducedDebugInfo", "true");

    server.setAllowFlight(true);
    if (verbose) sender.sendMessage(new TextComponentString("Enabled flight on the server"));

    sender.sendMessage(new TextComponentString("Sucessfully made this world a test world"));
  }
}
