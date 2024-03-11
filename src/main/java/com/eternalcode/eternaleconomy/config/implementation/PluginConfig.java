package com.eternalcode.eternaleconomy.config.implementation;

import com.eternalcode.multification.notice.Notice;

public interface PluginConfig {

    EconomyConfiguration economy();

    interface EconomyConfiguration {

        Notice notEnoughMoneyMessage();

        Notice paySentMessage();

        Notice receivePayMessage();

        Notice checkBalanceMessage();

        Notice checkBalanceOtherMessage();

        Notice minimalPayAmmountMessage();

        Notice addBalanceMessage();

        Notice removeBalanceMessage();

        Notice setBalanceMessage();

        Notice resetBalanceMessage();

        Notice incorrectEconomyUsageMessage();

    }
}
