package com.eternalcode.eternaleconomy.notification.serializer;

import com.eternalcode.commons.time.DurationParser;
import com.eternalcode.commons.time.TemporalAmountParser;
import com.eternalcode.eternaleconomy.util.DurationUtil;
import com.eternalcode.multification.notice.NoticeContent.Times;
import eu.okaeri.configs.schema.GenericsPair;
import eu.okaeri.configs.serdes.BidirectionalTransformer;
import eu.okaeri.configs.serdes.SerdesContext;
import java.time.Duration;
import org.jetbrains.annotations.ApiStatus.Experimental;

@Experimental
public class MultificationTimesTransformer extends BidirectionalTransformer<String, Times> {

    private static final TemporalAmountParser<Duration> DURATION_PARSER = DurationParser.TIME_UNITS;

    @Override
    public GenericsPair<String, Times> getPair() {
        return this.genericsPair(String.class, Times.class);
    }

    @Override
    public Times leftToRight(String data, SerdesContext serdesContext) {
        String[] split = data.split(" ");

        if (split.length != 3) {
            throw new IllegalArgumentException(
                "Error parsing time format, expects format '<fadeIn> <stay> <fadeOut>', example: '1s 1s 1s', received: "
                    + data);
        }

        Duration fadeIn = DURATION_PARSER.parse(split[0]);
        Duration stay = DURATION_PARSER.parse(split[1]);
        Duration fadeOut = DURATION_PARSER.parse(split[2]);

        return new Times(fadeIn, stay, fadeOut);
    }

    @Override
    public String rightToLeft(Times data, SerdesContext serdesContext) {
        return DurationUtil.format(data.fadeIn()) + " " + DurationUtil.format(data.stay()) + " "
            + DurationUtil.format(data.fadeOut());
    }
}
