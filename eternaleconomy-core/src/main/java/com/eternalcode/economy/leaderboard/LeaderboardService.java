package com.eternalcode.economy.leaderboard;

import com.eternalcode.economy.account.Account;

public interface LeaderboardService {

    LeaderboardEntry getLeaderboardPosition(Account target);

    LeaderboardPage getLeaderboardPage(int page, int pageSize);

}
