package com.eternalcode.economy.config.implementation.messages;

import com.eternalcode.multification.notice.Notice;
import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;

public class MessagesPlayerSubSection extends OkaeriConfig {

    public Notice added = Notice.chat("<b><gradient:#00FFA2:#34AE00>ECONOMY</gradient></b> <dark_gray>➤</dark_gray> "
        + "<white>Added <gradient:#00FFA2:#34AE00>{AMOUNT}</gradient> to your account.</white>");
    public Notice removed = Notice.chat("<b><gradient:#00FFA2:#34AE00>ECONOMY</gradient></b> <dark_gray>➤</dark_gray>"
        + " <white>Removed <gradient:#00FFA2:#34AE00>{AMOUNT}</gradient> from your account.</white>");
    public Notice set = Notice.chat("<b><gradient:#00FFA2:#34AE00>ECONOMY</gradient></b> <dark_gray>➤</dark_gray> "
        + "<white>Set your balance to <gradient:#00FFA2:#34AE00>{AMOUNT}</gradient>.</white>");
    public Notice reset = Notice.chat("<b><gradient:#00FFA2:#34AE00>ECONOMY</gradient></b> <dark_gray>➤</dark_gray> "
        + "<white>Resetted your balance.</white>");
    public Notice balance = Notice.chat("<b><gradient:#00FFA2:#34AE00>ECONOMY</gradient></b> <dark_gray>➤</dark_gray>"
        + " <white>Your balance is <gradient:#00FFA2:#34AE00>{BALANCE}</gradient>.</white>");
    public Notice balanceOther =
        Notice.chat("<b><gradient:#00FFA2:#34AE00>ECONOMY</gradient></b> <dark_gray>➤</dark_gray>"
            + " <white><gradient:#00FFA2:#34AE00>{PLAYER}</gradient>'s balance is <gradient:#00FFA2:#34AE00>{BALANCE}</gradient>.</white>");
    public Notice insufficientBalance = Notice.chat("<b><gradient:#00FFA2:#34AE00>ECONOMY</gradient></b> "
        + "<dark_gray>➤</dark_gray> <white>Insufficient funds,"
        + " you are missing <gradient:#00FFA2:#34AE00> {MISSING_BALANCE}</gradient>.</white>");
    public Notice transferSuccess = Notice.chat("<b><gradient:#00FFA2:#34AE00>ECONOMY</gradient></b> <dark_gray"
        + ">➤</dark_gray> <white>Successfully transferred <gradient:#00FFA2:#34AE00>{AMOUNT}</gradient> to "
        + "<gradient:#00FFA2:#34AE00>{PLAYER}</gradient>.</white>");
    public Notice transferReceived = Notice.chat("<b><gradient:#00FFA2:#34AE00>ECONOMY</gradient></b> "
        + "<dark_gray>➤</dark_gray> <white>Received <gradient:#00FFA2:#34AE00>{AMOUNT}</gradient> from "
        + "<gradient:#00FFA2:#34AE00>{PLAYER}</gradient>.</white>");
    public Notice transferLimit = Notice.chat("<b><gradient:#00FFA2:#34AE00>ECONOMY</gradient></b> <dark_gray>➤</dark_gray>"
        + " <white>Transaction limit is <gradient:#00FFA2:#34AE00>{LIMIT}</gradient>.</white>");

    @Comment({
        "Use {PAGE} placeholder to show the current page number",
        "Use {TOTAL_PAGES} placeholder to show the total number of pages"
    })
    public Notice leaderboardHeader = Notice.chat("<newline> <white><b>Balance leaderboard</b> <gray>(Page "
        + "{PAGE}/{TOTAL_PAGES}):</gray> <newline>");

    @Comment({
        "Leaderboard entry notice, only displayed if showLeaderboardPosition is set to true in the config.yml",
        "Use {POSITION} placeholder to show the player's position in the leaderboard",
        "Use {PLAYER} placeholder to show the player's name",
        "Use {BALANCE} placeholder to show the player's formatted balance",
        "Use {BALANCE_RAW} placeholder to show the player's raw balance"
    })
    public Notice leaderboardEntry = Notice.chat("  <white>#{POSITION} <gradient:#00FFA2:#34AE00>{PLAYER}</gradient> -"
        + " <gradient:#00FFA2:#34AE00>{BALANCE}</gradient></white>");

    @Comment({
        "Leaderboard position notice, only displayed if showLeaderboardPosition is set to true in the config.yml",
        "Use {POSITION} placeholder to show the player's position in the leaderboard"
    })
    public Notice leaderboardPosition = Notice.chat("<newline>  <gray><u>Your position: #{POSITION} <newline>");

    @Comment({
        "Leaderboard footer notice, only displayed if there are more pages to show",
        "Use {NEXT_PAGE} placeholder to show the next page number",
        "Use {TOTAL_PAGES} placeholder to show the total number of pages",
        "Use {PAGE} placeholder to show the current page number"
    })
    public Notice leaderboardFooter = Notice.builder()
        .chat("<newline>  <white>Click <gradient:#00FFA2:#34AE00><hover:show_text:'<white>Go to page {NEXT_PAGE}</white>'><click:run_command:'/baltop {NEXT_PAGE}'>/baltop {NEXT_PAGE}</click></hover></gradient> to go to the next page.</white>")
        .build();

    @Comment("Leaderboard is empty notice")
    public Notice leaderboardEmpty = Notice.chat("<b><gradient:#00FFA2:#34AE00>ECONOMY</gradient></b> "
        + "<dark_gray>➤</dark_gray> <white>Leaderboard is empty :(</white>");
}
