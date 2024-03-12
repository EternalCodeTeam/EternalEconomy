package com.eternalcode.eternaleconomy.config.implementation;

import com.eternalcode.multification.notice.Notice;

public interface PluginConfig {

    interface Argument {
        Notice playerNotFound();

        Notice usageMessage();
        Notice usageMessageHead();
        Notice usageMessageEntry();
    }

    interface Messages {

        Notice notEnoughMoneyMessage();

        Notice paySentMessage();

        Notice receivePayMessage();

        Notice checkBalanceMessage();

        Notice checkBalanceOtherMessage();

        Notice minimalPayAmountMessage();

        Notice addBalanceMessage();

        Notice removeBalanceMessage();

        Notice setBalanceMessage();

        Notice resetBalanceMessage();

        Notice incorrectEconomyUsageMessage();
    }
    Messages messages();

    Argument argument();
}
