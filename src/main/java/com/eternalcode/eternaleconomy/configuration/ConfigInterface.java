package com.eternalcode.eternaleconomy.configuration;

import com.eternalcode.multification.notice.Notice;

public interface ConfigInterface {

    EconomyConfiguration economy();
    interface EconomyConfiguration {

        Notice not_enough_money_message();

        Notice pay_sent_message();

        Notice pay_received_message();

        Notice checking_balance_message();

        Notice checking_balance_other_message();

        Notice minimal_pay_ammount_message();

        Notice adding_balance_message();

        Notice removing_balance_message();

        Notice set_balance_message();

        Notice reset_balance_message();

        Notice incorrect_economy_usage();

    }
}
