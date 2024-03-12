package com.eternalcode.eternaleconomy.notification;

import com.eternalcode.eternaleconomy.config.implementation.PluginConfig;
import com.eternalcode.multification.translation.TranslationProvider;
import java.util.Locale;
import org.jetbrains.annotations.NotNull;

public class MessageProvider implements TranslationProvider<PluginConfig> {

    private final PluginConfig pluginConfig;

    public MessageProvider(PluginConfig pluginConfig) {
        this.pluginConfig = pluginConfig;
    }

    public PluginConfig getMessages() {
        return this.pluginConfig;
    }

    @Override
    public @NotNull PluginConfig provide(Locale locale) {
        return this.getMessages();
    }
}
