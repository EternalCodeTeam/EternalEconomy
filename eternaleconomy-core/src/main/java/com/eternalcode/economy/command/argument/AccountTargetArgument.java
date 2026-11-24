package com.eternalcode.economy.command.argument;

import com.eternalcode.economy.account.Account;
import com.eternalcode.economy.account.AccountManager;
import com.eternalcode.economy.account.AccountTarget;
import com.eternalcode.economy.format.DecimalFormatter;
import com.eternalcode.economy.multification.NoticeService;
import com.eternalcode.multification.notice.NoticeBroadcast;
import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.argument.resolver.ArgumentResolver;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.suggestion.Suggestion;
import dev.rollczi.litecommands.suggestion.SuggestionContext;
import dev.rollczi.litecommands.suggestion.SuggestionResult;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.regex.Pattern;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AccountTargetArgument extends ArgumentResolver<CommandSender, AccountTarget> {

    private static final int MINIMUM_ACCOUNT_SEARCH_LENGTH = 3;
    private static final int SUGGESTION_LIMIT = 50;
    private static final Pattern UUID_PREFIX_PATTERN = Pattern.compile("^[0-9a-fA-F-]{4,36}$");

    private final AccountManager accountManager;
    private final NoticeService noticeService;
    private final DecimalFormatter decimalFormatter;
    private final Server server;

    public AccountTargetArgument(
        AccountManager accountManager,
        NoticeService noticeService,
        DecimalFormatter decimalFormatter,
        Server server) {
        this.accountManager = accountManager;
        this.noticeService = noticeService;
        this.decimalFormatter = decimalFormatter;
        this.server = server;
    }

    @Override
    protected ParseResult<AccountTarget> parse(
        Invocation<CommandSender> invocation,
        Argument<AccountTarget> argument,
        String string) {
        Optional<AccountTarget> target = AccountTarget.parse(string);

        if (target.isEmpty() || this.isHiddenFromSender(invocation.sender(), this.findAccount(target.get()))) {
            NoticeBroadcast invalidPlayerNotice = this.noticeService.create()
                .notice(messageConfig -> messageConfig.invalidPlayer)
                .viewer(invocation.sender());

            return ParseResult.failure(invalidPlayerNotice);
        }

        return ParseResult.success(target.get());
    }

    @Override
    public SuggestionResult suggest(
        Invocation<CommandSender> invocation,
        Argument<AccountTarget> argument,
        SuggestionContext context
    ) {
        CommandSender sender = invocation.sender();
        String input = context.getCurrent().multilevel();

        if (input.length() < MINIMUM_ACCOUNT_SEARCH_LENGTH) {
            return SuggestionResult.from(this.suggestOnlinePlayers(sender));
        }

        List<Suggestion> suggestions = new ArrayList<>();
        this.addAccountSuggestions(sender, this.accountManager.getAccountStartingWith(input), suggestions);

        if (UUID_PREFIX_PATTERN.matcher(input).matches()) {
            this.addUuidSuggestions(sender, input.toLowerCase(Locale.ROOT), suggestions);
        }

        return SuggestionResult.from(suggestions);
    }

    private Account findAccount(AccountTarget target) {
        return switch (target) {
            case AccountTarget.ByUuid byUuid -> this.accountManager.getAccount(byUuid.uuid());
            case AccountTarget.ByName byName -> this.accountManager.getAccount(byName.name());
        };
    }

    private List<Suggestion> suggestOnlinePlayers(CommandSender sender) {
        List<Suggestion> suggestions = new ArrayList<>();

        for (Player player : this.server.getOnlinePlayers()) {
            if (this.canSee(sender, player)) {
                suggestions.add(this.toNameSuggestion(player.getName(), this.accountManager.getAccount(player.getUniqueId())));
            }
        }

        return suggestions;
    }

    private void addAccountSuggestions(CommandSender sender, Collection<Account> accounts, List<Suggestion> suggestions) {
        for (Account account : accounts) {
            if (suggestions.size() >= SUGGESTION_LIMIT) {
                return;
            }

            if (!this.isHiddenFromSender(sender, account)) {
                suggestions.add(this.toNameSuggestion(account.name(), account));
            }
        }
    }

    private void addUuidSuggestions(CommandSender sender, String uuidPrefix, List<Suggestion> suggestions) {
        for (Account account : this.accountManager.getAccounts()) {
            if (suggestions.size() >= SUGGESTION_LIMIT) {
                return;
            }

            String uuid = account.uuid().toString();
            if (uuid.startsWith(uuidPrefix) && !this.isHiddenFromSender(sender, account)) {
                suggestions.add(Suggestion.of(uuid, this.describe(account)));
            }
        }
    }

    private Suggestion toNameSuggestion(String name, Account account) {
        if (account == null) {
            return Suggestion.of(name);
        }

        return Suggestion.of(name, this.describe(account));
    }

    private String describe(Account account) {
        return account.name() + " - " + this.decimalFormatter.format(account.balance());
    }

    private boolean isHiddenFromSender(CommandSender sender, Account account) {
        if (account == null) {
            return false;
        }

        Player onlineTarget = this.server.getPlayer(account.uuid());
        return onlineTarget != null && !this.canSee(sender, onlineTarget);
    }

    private boolean canSee(CommandSender sender, Player target) {
        if (!(sender instanceof Player player)) {
            return true;
        }

        return player.canSee(target);
    }
}
