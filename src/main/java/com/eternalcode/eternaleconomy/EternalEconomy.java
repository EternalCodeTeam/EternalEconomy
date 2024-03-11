package com.eternalcode.eternaleconomy;

import com.eternalcode.commons.adventure.AdventureLegacyColorPostProcessor;
import com.eternalcode.commons.adventure.AdventureLegacyColorPreProcessor;
import com.eternalcode.commons.bukkit.scheduler.BukkitSchedulerImpl;
import com.eternalcode.commons.scheduler.Scheduler;
import com.eternalcode.eternaleconomy.command.EconomyCommand;
import com.eternalcode.eternaleconomy.command.MoneyCommand;
import com.eternalcode.eternaleconomy.command.PayCommand;
import com.eternalcode.eternaleconomy.configuration.ConfigInterface;
import com.eternalcode.eternaleconomy.configuration.ConfigurationService;
import com.eternalcode.eternaleconomy.configuration.implementation.PluginConfiguration;
import com.eternalcode.eternaleconomy.database.DatabaseService;
import com.eternalcode.eternaleconomy.notification.MessageProvider;
import com.eternalcode.eternaleconomy.notification.NoticeService;
import com.eternalcode.eternaleconomy.user.User;
import com.eternalcode.eternaleconomy.user.UserRepositoryImpl;
import com.eternalcode.eternaleconomy.user.UserService;
import com.zaxxer.hikari.HikariDataSource;
import dev.rollczi.litecommands.LiteCommands;
import dev.rollczi.litecommands.bukkit.LiteCommandsBukkit;
import net.kyori.adventure.platform.AudienceProvider;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class EternalEconomy extends JavaPlugin {

    private AudienceProvider audiences;
    private LiteCommands<CommandSender> liteCommands;
    private UserService userService;
    private User user;
    private NoticeService noticeService;
    private Scheduler scheduler;
    private MiniMessage miniMessage;



    @Override
    public void onEnable() {
        Server server = this.getServer();

        File dataFolder = this.getDataFolder();
        ConfigurationService configurationService = new ConfigurationService();
        PluginConfiguration config = configurationService.create(PluginConfiguration.class, new File(dataFolder, "config.yml"));
        ConfigInterface configInterface = config.getInterface();
        MessageProvider messageProvider = new MessageProvider(configInterface);

        DatabaseService databaseService = new DatabaseService(config);
        HikariDataSource connect = databaseService.connect(dataFolder);
        UserRepositoryImpl userRepository = new UserRepositoryImpl(connect);


        this.scheduler = new BukkitSchedulerImpl(this);
        this.audiences = BukkitAudiences.create(this);


        this.miniMessage = MiniMessage.builder()
            .preProcessor(new AdventureLegacyColorPreProcessor())
            .postProcessor(new AdventureLegacyColorPostProcessor())
            .build();


        userService = new UserService(config, userRepository);
        this.noticeService = new NoticeService(audiences, userService, server, messageProvider, this.miniMessage);



        this.liteCommands = LiteCommandsBukkit.builder("EternalEconomy")
            .commands(new EconomyCommand(this, userService, config, noticeService),
                new MoneyCommand(this, userService, config, noticeService),
                new PayCommand(this, userService, config, noticeService))
            .build();


    }

    @Override
    public void onDisable() {
        if (this.audiences != null) {
            this.audiences.close();
        }
    }

}

