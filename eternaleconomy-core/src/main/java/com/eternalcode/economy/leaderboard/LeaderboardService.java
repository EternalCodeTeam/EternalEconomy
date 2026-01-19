package com.eternalcode.economy.leaderboard;

import com.eternalcode.economy.account.Account;
import java.util.concurrent.CompletableFuture;

public interface LeaderboardService {

    CompletableFuture<LeaderboardEntry> getLeaderboardPosition(Account target);

    CompletableFuture<LeaderboardPage> getLeaderboardPage(int page, int pageSize);

}
