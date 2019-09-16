package com.chronophylos.debugtools.commands;

import org.apache.logging.log4j.Logger;

public class DumpCommand extends SimpleDumpCommand {
  public DumpCommand(Logger logger) {
    super(logger, "itemdump.txt", "dump", false);
  }
}
