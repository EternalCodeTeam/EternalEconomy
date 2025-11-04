package com.eternalcode.economy.command.argument;

import com.eternalcode.economy.config.implementation.PluginConfig;
import com.eternalcode.economy.config.implementation.messages.MessageConfig;
import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.argument.resolver.ArgumentResolver;
import dev.rollczi.litecommands.invocation.Invocation;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.bukkit.command.CommandSender;

public class MoneyFormatArgument extends ArgumentResolver<CommandSender, BigDecimal> {

    public static final String KEY = "price";
    private static final BigDecimal MINIMUM_VALUE = new BigDecimal("0.10");
    private static final int DECIMAL_SCALE = 2;

    private final Pattern pricePattern;
    private final PluginConfig config;
    private final MessageConfig messages;
    private final Map<Character, BigDecimal> multipliers;

    public MoneyFormatArgument(PluginConfig config, MessageConfig messages) {
        this.config = config;
        this.messages = messages;
        this.multipliers = new HashMap<>();

        this.pricePattern = this.buildPricePattern();
    }

    @Override
    protected ParseResult<BigDecimal> parse(
        Invocation<CommandSender> invocation,
        Argument<BigDecimal> argument,
        String raw
    ) {
        Matcher matcher = this.pricePattern.matcher(raw.toLowerCase());

        if (!matcher.matches()) {
            return ParseResult.failure(this.messages.invalidMoney);
        }

        String numberPart = matcher.group(1).replace(',', '.');
        String suffix = matcher.group(2);

        try {
            BigDecimal value = new BigDecimal(numberPart);

            if (suffix != null) {
                BigDecimal multiplier = this.multipliers.get(Character.toLowerCase(suffix.charAt(0)));

                if (multiplier == null) {
                    return ParseResult.failure(this.messages.invalidMoney);
                }

                value = value.multiply(multiplier);
            }

            if (value.compareTo(MINIMUM_VALUE) < 0) {
                return ParseResult.failure(this.messages.incorrectMoneyArgument);
            }

            return ParseResult.success(value.setScale(DECIMAL_SCALE, RoundingMode.DOWN));
        }
        catch (NumberFormatException exception) {
            return ParseResult.failure(this.messages.invalidMoney);
        }
    }

    private Pattern buildPricePattern() {
        StringBuilder suffixes = new StringBuilder();

        this.config.units.format.forEach(unit -> {
            char suffix = unit.getSuffix();
            this.multipliers.put(suffix, BigDecimal.valueOf(unit.getFactor()));
            suffixes.append(suffix);
        });

        String patternString = "^(\\d+(?:[.,]\\d+)?)([" + suffixes + "])?$";
        return Pattern.compile(patternString, Pattern.CASE_INSENSITIVE);
    }
}
