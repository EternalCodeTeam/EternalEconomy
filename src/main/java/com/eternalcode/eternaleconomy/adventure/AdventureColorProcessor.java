package com.eternalcode.eternaleconomy.adventure;

import net.kyori.adventure.text.Component;

import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

import static net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.legacyAmpersand;

public class AdventureColorProcessor implements UnaryOperator<Component> {

    @Override
    public Component apply(Component component) {
        return component.replaceText(builder -> builder.match(Pattern.compile(".*"))
            .replacement((matchResult, builder1) -> legacyAmpersand().deserialize(matchResult.group())));
    }
}
