package com.eternalcode.economy;

import com.eternalcode.commons.adventure.AdventureLegacyColorPostProcessor;
import com.eternalcode.commons.adventure.AdventureLegacyColorPreProcessor;
import com.eternalcode.commons.adventure.AdventureUrlPostProcessor;
import net.kyori.adventure.text.minimessage.MiniMessage;

public interface MiniMessageHolder {

    MiniMessage MINI_MESSAGE = MiniMessage.builder()
        .postProcessor(new AdventureUrlPostProcessor())
        .postProcessor(new AdventureLegacyColorPostProcessor())
        .preProcessor(new AdventureLegacyColorPreProcessor())
        .build();
}
