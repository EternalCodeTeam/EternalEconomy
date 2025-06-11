package com.eternalcode.economy.leaderboard;

import com.eternalcode.economy.account.Account;

public record LeaderboardEntry(Account account, int position) {
}
