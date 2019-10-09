package com.chronophylos.debugtools.command;

import net.minecraft.command.ICommandSender;
import net.minecraftforge.server.command.CommandTreeBase;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ModCommand extends CommandTreeBase {
    private String name;
    private List<String> aliases = new ArrayList<>();

    public ModCommand(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return getSubCommands()
                .stream()
                .map(cmd -> cmd.getUsage(sender))
                .map(usage -> "/" + name + usage)
                .collect(Collectors.joining("\n"));
    }

    @Override
    public List<String> getAliases() {
        return aliases;
    }

    public ModCommand addAlias(String name) {
        aliases.add(name);
        return this;
    }

    public DCommand createCommand(String name) {
        DCommand cmd = DCommand.create(name);
        addSubcommand(cmd);
        return cmd;
    }
}
