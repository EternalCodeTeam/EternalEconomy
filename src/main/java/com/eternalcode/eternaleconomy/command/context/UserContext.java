package com.eternalcode.eternaleconomy.command.context;

import com.eternalcode.eternaleconomy.config.implementation.PluginConfig;
import com.eternalcode.eternaleconomy.user.User;
import com.eternalcode.eternaleconomy.user.UserService;
import dev.rollczi.litecommands.context.ContextProvider;
import dev.rollczi.litecommands.context.ContextResult;
import dev.rollczi.litecommands.invocation.Invocation;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UserContext implements ContextProvider<CommandSender, User> {

    private final UserService userService;
    private final PluginConfig pluginConfig;

    public UserContext(UserService userService, PluginConfig pluginConfig) {
        this.userService = userService;
        this.pluginConfig = pluginConfig;
    }

    @Override
    public ContextResult<User> provide(Invocation<CommandSender> invocation) {
        if (invocation.sender() instanceof Player player) {
            return ContextResult.ok(() -> this.userService.getUser(player.getUniqueId())
                .orElseThrow(() -> new IllegalStateException("Player " + player.getName() + " is not registered!")));
        }

        return ContextResult.error(this.pluginConfig.argument().playerNotFound());
    }
}
