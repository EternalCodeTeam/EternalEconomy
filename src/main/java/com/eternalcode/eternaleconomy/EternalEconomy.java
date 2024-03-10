package com.eternalcode.eternaleconomy;

import com.eternalcode.commons.adventure.AdventureLegacyColorPostProcessor;
import com.eternalcode.commons.adventure.AdventureLegacyColorPreProcessor;
import com.eternalcode.eternaleconomy.configuration.ConfigurationService;
import com.eternalcode.eternaleconomy.configuration.implementation.PluginConfiguration;
import com.eternalcode.eternaleconomy.database.DatabaseService;
import com.eternalcode.eternaleconomy.listeners.PlayerJoinListener;
import com.eternalcode.eternaleconomy.notification.NotificationSender;
import com.eternalcode.eternaleconomy.user.UserRepositoryImpl;
import com.zaxxer.hikari.HikariDataSource;
import net.kyori.adventure.platform.AudienceProvider;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Server;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class EternalEconomy extends JavaPlugin {

    private AudienceProvider audiences;

    @Override
    public void onEnable() {
        Server server = this.getServer();

        File dataFolder = this.getDataFolder();
        ConfigurationService configurationService = new ConfigurationService();
        PluginConfiguration config = configurationService.create(PluginConfiguration.class, new File(dataFolder, "config.yml"));

        DatabaseService databaseService = new DatabaseService(config);
        HikariDataSource connect = databaseService.connect(dataFolder);
        UserRepositoryImpl userRepository = new UserRepositoryImpl(connect);

        this.audiences = BukkitAudiences.create(this);
        MiniMessage miniMessage = MiniMessage.builder()
            .preProcessor(new AdventureLegacyColorPreProcessor())
            .postProcessor(new AdventureLegacyColorPostProcessor())
            .build();

        NotificationSender notificationSender = new NotificationSender(this.audiences, miniMessage);
    }

    @Override
    public void onDisable() {
        if (this.audiences != null) {
            this.audiences.close();
        }
    }

    private void registerEvents(){
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
    }

}

