package com.eternalcode.economy;

import com.eternalcode.commons.adventure.AdventureLegacyColorPostProcessor;
import com.eternalcode.commons.adventure.AdventureLegacyColorPreProcessor;
import com.eternalcode.commons.adventure.AdventureUrlPostProcessor;
import com.eternalcode.commons.scheduler.Scheduler;
import com.eternalcode.economy.account.Account;
import com.eternalcode.economy.account.AccountController;
import com.eternalcode.economy.account.AccountManager;
import com.eternalcode.economy.account.AccountPaymentService;
import com.eternalcode.economy.account.database.AccountRepository;
import com.eternalcode.economy.account.database.AccountRepositoryImpl;
import com.eternalcode.economy.bridge.BridgeManager;
import com.eternalcode.economy.command.admin.AdminAddCommand;
import com.eternalcode.economy.command.admin.AdminBalanceCommand;
import com.eternalcode.economy.command.admin.AdminRemoveCommand;
import com.eternalcode.economy.command.admin.AdminResetCommand;
import com.eternalcode.economy.command.admin.AdminSetCommand;
import com.eternalcode.economy.command.argument.AccountArgument;
import com.eternalcode.economy.command.context.AccountContext;
import com.eternalcode.economy.command.cooldown.CommandCooldownEditor;
import com.eternalcode.economy.command.cooldown.CommandCooldownMessage;
import com.eternalcode.economy.command.handler.InvalidUsageHandlerImpl;
import com.eternalcode.economy.command.handler.MissingPermissionHandlerImpl;
import com.eternalcode.economy.command.message.InvalidBigDecimalMessage;
import com.eternalcode.economy.command.player.MoneyBalanceCommand;
import com.eternalcode.economy.command.player.MoneyTransferCommand;
import com.eternalcode.economy.leaderboard.LeaderboardCommand;
import com.eternalcode.economy.command.validator.notsender.NotSender;
import com.eternalcode.economy.command.validator.notsender.NotSenderValidator;
import com.eternalcode.economy.config.ConfigService;
import com.eternalcode.economy.config.implementation.CommandsConfig;
import com.eternalcode.economy.config.implementation.PluginConfig;
import com.eternalcode.economy.config.implementation.messages.MessageConfig;
import com.eternalcode.economy.database.DatabaseManager;
import com.eternalcode.economy.format.DecimalFormatter;
import com.eternalcode.economy.format.DecimalFormatterImpl;
import com.eternalcode.economy.leaderboard.LeaderboardService;
import com.eternalcode.economy.multification.NoticeBroadcastHandler;
import com.eternalcode.economy.multification.NoticeHandler;
import com.eternalcode.economy.multification.NoticeService;
import com.eternalcode.economy.vault.VaultEconomyProvider;
import com.eternalcode.multification.notice.Notice;
import com.eternalcode.multification.notice.NoticeBroadcast;
import com.google.common.base.Stopwatch;
import dev.rollczi.litecommands.LiteCommands;
import dev.rollczi.litecommands.bukkit.LiteBukkitFactory;
import dev.rollczi.litecommands.jakarta.LiteJakartaExtension;
import dev.rollczi.litecommands.message.LiteMessages;
import jakarta.validation.constraints.Positive;
import java.io.File;
import java.math.BigDecimal;
import java.time.Duration;
import net.kyori.adventure.platform.AudienceProvider;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("unused")
public class EconomyBukkitPlugin extends JavaPlugin {

    private static final String PLUGIN_STARTED = "EternalEconomy has been enabled in %dms.";

    private AudienceProvider audienceProvider;
    private DatabaseManager databaseManager;

    private LiteCommands<CommandSender> liteCommands;

    @Override
    public void onEnable() {
        Stopwatch started = Stopwatch.createStarted();
        Server server = this.getServer();

        this.audienceProvider = BukkitAudiences.create(this);
        MiniMessage miniMessage = MiniMessage.builder()
            .postProcessor(new AdventureUrlPostProcessor())
            .postProcessor(new AdventureLegacyColorPostProcessor())
            .preProcessor(new AdventureLegacyColorPreProcessor())
            .build();

        File dataFolder = this.getDataFolder();

        ConfigService configService = new ConfigService();
        MessageConfig messageConfig = configService.create(MessageConfig.class, new File(dataFolder, "messages.yml"));
        PluginConfig pluginConfig = configService.create(PluginConfig.class, new File(dataFolder, "config.yml"));
        CommandsConfig commandsConfig = configService.create(CommandsConfig.class, new File(dataFolder, "commands.yml"));

        NoticeService noticeService = new NoticeService(messageConfig, this.audienceProvider, miniMessage);

        Scheduler scheduler = EconomySchedulerAdapter.getAdaptiveScheduler(this);

        this.databaseManager = new DatabaseManager(this.getLogger(), dataFolder, pluginConfig.database);
        this.databaseManager.connect();

        AccountRepository accountRepository = new AccountRepositoryImpl(this.databaseManager, scheduler);
        AccountManager accountManager = AccountManager.create(accountRepository);

        LeaderboardService leaderboardService = new LeaderboardService(accountRepository);

        DecimalFormatter decimalFormatter = new DecimalFormatterImpl(pluginConfig);
        AccountPaymentService accountPaymentService = new AccountPaymentService(accountManager, pluginConfig);

        VaultEconomyProvider vaultEconomyProvider =
            new VaultEconomyProvider(this, decimalFormatter, accountPaymentService, accountManager);
        server.getServicesManager().register(Economy.class, vaultEconomyProvider, this, ServicePriority.Highest);

        this.liteCommands = LiteBukkitFactory.builder("eternaleconomy", this, server)
            .extension(new LiteJakartaExtension<>(), settings -> settings
                .violationMessage(Positive.class, BigDecimal.class, new InvalidBigDecimalMessage<>(noticeService))
            )

            .annotations(extension -> extension.validator(
                Account.class,
                NotSender.class,
                new NotSenderValidator(messageConfig)))

            .missingPermission(new MissingPermissionHandlerImpl(noticeService))
            .invalidUsage(new InvalidUsageHandlerImpl(noticeService))

            .message(LiteMessages.COMMAND_COOLDOWN, new CommandCooldownMessage(noticeService, commandsConfig))
            .editorGlobal(new CommandCooldownEditor(commandsConfig))

            .commands(
                new AdminAddCommand(accountPaymentService, decimalFormatter, noticeService),
                new AdminRemoveCommand(accountPaymentService, decimalFormatter, noticeService),
                new AdminSetCommand(accountPaymentService, decimalFormatter, noticeService),
                new AdminResetCommand(accountPaymentService, noticeService),
                new AdminBalanceCommand(noticeService, decimalFormatter),
                new MoneyBalanceCommand(noticeService, decimalFormatter),
                new MoneyTransferCommand(accountPaymentService, decimalFormatter, noticeService, pluginConfig),
                new EconomyReloadCommand(configService, noticeService),
                new LeaderboardCommand(noticeService, decimalFormatter, leaderboardService, pluginConfig)
            )

            .context(Account.class, new AccountContext(accountManager, messageConfig))
            .argument(Account.class, new AccountArgument(accountManager, noticeService, server))

            .result(Notice.class, new NoticeHandler(noticeService))
            .result(NoticeBroadcast.class, new NoticeBroadcastHandler())

            .build();

        server.getPluginManager().registerEvents(new AccountController(accountManager), this);

        BridgeManager bridgeManager = new BridgeManager(
            this.getDescription(),
            accountManager,
            decimalFormatter,
            server,
            this,
            this.getLogger()
        );
        bridgeManager.init();

        Duration elapsed = started.elapsed();
        server.getLogger().info(String.format(PLUGIN_STARTED, elapsed.toMillis()));
    }

    @Override
    public void onDisable() {
        if (this.audienceProvider != null) {
            this.audienceProvider.close();
        }

        if (this.liteCommands != null) {
            this.liteCommands.unregister();
        }

        if (this.databaseManager != null) {
            this.databaseManager.close();
        }
    }
}
