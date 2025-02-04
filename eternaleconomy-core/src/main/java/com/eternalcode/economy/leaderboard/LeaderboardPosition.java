package com.eternalcode.economy.leaderboard;

import com.eternalcode.economy.account.Account;

public record LeaderboardPosition(Account account, int position) {
}
