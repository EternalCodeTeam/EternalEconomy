package com.eternalcode.economy.command.argument;

import com.eternalcode.economy.account.Account;
import com.eternalcode.economy.account.AccountManager;
import com.eternalcode.economy.multification.NoticeService;
import com.eternalcode.multification.notice.NoticeBroadcast;
import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.argument.resolver.ArgumentResolver;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.suggestion.SuggestionContext;
import dev.rollczi.litecommands.suggestion.SuggestionResult;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AccountArgument extends ArgumentResolver<CommandSender, Account> {

    private final AccountManager accountManager;
    private final NoticeService noticeService;
    private final Server server;

    public AccountArgument(AccountManager accountManager, NoticeService noticeService, Server server) {
        this.accountManager = accountManager;
        this.noticeService = noticeService;
        this.server = server;
    }

    @Override
    protected ParseResult<Account> parse(
            Invocation<CommandSender> invocation,
            Argument<Account> argument,
            String string) {
        Player player = this.server.getPlayer(string);

        if (player == null) {
            NoticeBroadcast invalidPlayerNotice = this.noticeService.create()
                    .notice(messageConfig -> messageConfig.invalidPlayer)
                    .viewer(invocation.sender());

            return ParseResult.failure(invalidPlayerNotice);
        }

        Account account = this.accountManager.getAccount(player.getUniqueId());
        return ParseResult.success(account);
    }

    @Override
    public SuggestionResult suggest(
            Invocation<CommandSender> invocation,
            Argument<Account> argument,
            SuggestionContext context
    ) {

        return this.server.getOnlinePlayers().stream()
                .map(Player::getName)
                .collect(SuggestionResult.collector());
    }
}