package me.dhcpcd.lisek.utils.commands.base;

import java.lang.reflect.Method;
import java.sql.Struct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.velocitypowered.api.command.RawCommand;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.TabCompleteEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;

import net.kyori.adventure.text.Component;

public class CommandBase implements RawCommand {

    @Inject
    private ProxyServer server;

    @Inject
    private Logger logger;

    public CommandBase() {
        
    }
    
    @Override
    public void execute(Invocation invocation) {
        int arguments = invocation.arguments().split(" ").length;
        if (arguments == 0) {
            runFallbackMethod(invocation);
            return;
        }
       
        String[] args = invocation.arguments().split(" ");
        String subCommandName = args[0];
        Optional<Method> subCommandJavaMethodOptional = Arrays.stream(getClass().getMethods()).filter(m -> m.isAnnotationPresent(SubCommand.class)).filter(m -> m.getAnnotation(SubCommand.class).name().equalsIgnoreCase(subCommandName)).findFirst();



        if (!subCommandJavaMethodOptional.isPresent()) {
            //Running fallback method if no subcommand is found.
            runFallbackMethod(invocation);
            return;
        }

        try {
            Method subCommandJavaMethod = subCommandJavaMethodOptional.get();

            Sender sender = new Sender(null, invocation.source());
            SubCommand subCommand = subCommandJavaMethod.getAnnotation(SubCommand.class);
            if (!subCommand.acceptsConsole()) {
                if (!(invocation.source() instanceof Player)) {
                    invocation.source().sendMessage(Component.text("You are not allowed to use that."));
                    return;
                }else{
                    sender.setPlayer((Player) invocation.source());
                }       
            }
            
            subCommandJavaMethod.invoke(this, sender, invocation);
        } catch (Exception e) {
            invocation.source().sendMessage(Component.text("An error occurred while executing the sub command."));
            e.printStackTrace();
        }
    }

    public void runFallbackMethod(Invocation invocation) {
        Method fallbackJavaMethod = Arrays.stream(getClass().getMethods()).filter(m -> m.isAnnotationPresent(FallbackMethod.class)).findFirst().get();

        if (fallbackJavaMethod == null) {
            invocation.source().sendMessage(Component.text("No fallback method found."));
            return;
        }


        try {
            Sender sender = new Sender(null, invocation.source());
            FallbackMethod fallbackMethod = fallbackJavaMethod.getAnnotation(FallbackMethod.class);
            if (!fallbackMethod.consoleAllowed()) {
                if (!(invocation.source() instanceof Player)) {
                    invocation.source().sendMessage(Component.text("You are not allowed to use that."));
                    return;
                }else{
                    sender.setPlayer((Player) invocation.source());
                }       
            }
            
        
            fallbackJavaMethod.invoke(this, sender, invocation);
        } catch (Exception e) {
            invocation.source().sendMessage(Component.text("An error occurred while executing the fallback method."));
            e.printStackTrace();
        }
    }

    @Override
    public CompletableFuture<List<String>> suggestAsync(Invocation invocation) {
        int arguments = invocation.arguments().length();

        if (arguments == 0) {
            FallbackMethod fallbackMethod = Arrays.stream(getClass().getMethods()).filter(m -> m.isAnnotationPresent(FallbackMethod.class)).findFirst().get().getAnnotation(FallbackMethod.class);


            List<String> suggestions = new ArrayList<>();

            if (!fallbackMethod.autoComplete().isEmpty()) {
                try {
                    List<String> suggests = (List<String>) getClass().getMethod(fallbackMethod.autoComplete(), Invocation.class).invoke(this, invocation);
                    suggestions.addAll(suggests);
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (fallbackMethod.acceptsPlayer()) {
                for (Player player : server.getAllPlayers()) {
                    suggestions.add(player.getUsername());
                }
            }
            List<String> subcommands = Arrays.stream(getClass().getMethods()).filter(m -> m.isAnnotationPresent(SubCommand.class)).map(m -> m.getAnnotation(SubCommand.class).name()).collect(java.util.stream.Collectors.toList());
            suggestions.addAll(subcommands);
            return CompletableFuture.completedFuture(suggestions);
        }
        return RawCommand.super.suggestAsync(invocation);
    }


    public CommandInfo getInfo() {
        if (getClass().isAnnotationPresent(CommandInfo.class)) {
            return getClass().getAnnotation(CommandInfo.class);
        }
        throw new IllegalStateException("Command class " + getClass().getName() + " is missing @CommandInfo annotation.");
    }

    

    
}
