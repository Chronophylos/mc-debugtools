package com.chronophylos.debugtools.commands;

import org.apache.logging.log4j.Logger;

public class NBTDumpCommand extends SimpleDumpCommand {
  public NBTDumpCommand(Logger logger) {
    super(logger, "nbtdump.txt", "nbtdump", true);
  }
}
