package com.eternalcode.economy.command.context;

import com.eternalcode.economy.account.Account;
import com.eternalcode.economy.account.AccountManager;
import com.eternalcode.economy.config.implementation.MessageConfig;
import dev.rollczi.litecommands.context.ContextProvider;
import dev.rollczi.litecommands.context.ContextResult;
import dev.rollczi.litecommands.invocation.Invocation;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AccountContext implements ContextProvider<CommandSender, Account> {

    private final AccountManager accountManager;
    private final MessageConfig messageConfig;

    public AccountContext(AccountManager accountManager, MessageConfig messageConfig) {
        this.accountManager = accountManager;
        this.messageConfig = messageConfig;
    }

    @Override
    public ContextResult<Account> provide(Invocation<CommandSender> invocation) {
        if (invocation.sender() instanceof Player player) {
            return ContextResult.ok(() -> {
                Account account = this.accountManager.getAccount(player.getUniqueId());

                if (account == null) {
                    throw new IllegalStateException("account " + player.getName() + " not found");
                }

                return account;
            });
        }

        return ContextResult.error(this.messageConfig.invalidPlayer);
    }
}
