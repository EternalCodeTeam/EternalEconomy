package com.eternalcode.eternaleconomy;

import com.eternalcode.eternaleconomy.adventure.AdventureColorProcessor;
import com.eternalcode.eternaleconomy.configuration.ConfigurationService;
import com.eternalcode.eternaleconomy.configuration.implementation.PluginConfiguration;
import com.eternalcode.eternaleconomy.database.DatabaseService;
import com.eternalcode.eternaleconomy.notification.NotificationSender;
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
        databaseService.connect(dataFolder);

        this.audiences = BukkitAudiences.create(this);
        MiniMessage miniMessage = MiniMessage.builder()
            .postProcessor(new AdventureColorProcessor())
            .build();

        NotificationSender notificationSender = new NotificationSender(this.audiences, miniMessage);
    }

    @Override
    public void onDisable() {
        if (this.audiences != null) {
            this.audiences.close();
        }
    }

}

