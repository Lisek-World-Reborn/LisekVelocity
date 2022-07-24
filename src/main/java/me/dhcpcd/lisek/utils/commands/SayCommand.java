package me.dhcpcd.lisek.utils.commands;


import me.dhcpcd.lisek.utils.commands.base.CommandBase;
import me.dhcpcd.lisek.utils.commands.base.CommandInfo;
import me.dhcpcd.lisek.utils.commands.base.FallbackMethod;
import me.dhcpcd.lisek.utils.commands.base.Sender;
import me.dhcpcd.lisek.utils.commands.base.SubCommand;
import net.kyori.adventure.text.Component;

@CommandInfo(name = "say", description = "Say hello to server", aliases = {})
public class SayCommand extends CommandBase {

    @FallbackMethod
    public void fallback(Sender sender, Invocation invocation) {
        sender.getSource().sendMessage(Component.text("This command is not implemented yet."));
    }

    @SubCommand(name = "hello", acceptsConsole = true, acceptsPlayer = false)
    public void helloSubCommand(Sender sender, Invocation invocation) {
        sender.getSource().sendMessage(Component.text("Hello, world!"));
    }
    
}
