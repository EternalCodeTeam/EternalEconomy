package com.eternalcode.economy.config.implementation.messages;

import com.eternalcode.multification.notice.Notice;
import eu.okaeri.configs.OkaeriConfig;

public class MessagePaycheckSubSection extends OkaeriConfig {
    public Notice noItem = Notice.chat(
        "<red>You must hold item to create paycheck!"
    );

    public Notice setItem = Notice.chat(
        "<green>You have set paycheck item to <white>{ITEM}<green>!"
    );

    public Notice withdraw = Notice.chat(
        "<green>You have withdrawn your paycheck of <white>{VALUE}<green>!"
    );
}
