package com.eternalcode.eternaleconomy.notification;

import com.eternalcode.eternaleconomy.configuration.ConfigInterface;
import com.eternalcode.multification.translation.TranslationProvider;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public class MessageProvider implements TranslationProvider<ConfigInterface> {


    private final ConfigInterface configInterface;

    public MessageProvider(ConfigInterface configInterface) {
        this.configInterface = configInterface;
    }


    public ConfigInterface getMessages() {
        return this.configInterface;
    }


    @Override
    public @NotNull ConfigInterface provide(Locale locale) {
        return this.getMessages();
    }
}
