package com.eternalcode.economy.leaderboard;

import com.eternalcode.economy.account.Account;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

record LeaderboardSnapshot(List<Account> accounts, long totalCount, Map<UUID, Integer> positionIndex) {

    LeaderboardSnapshot(List<Account> accounts, long totalCount) {
        this(List.copyOf(accounts), totalCount, buildPositionIndex(accounts));
    }

    private static Map<UUID, Integer> buildPositionIndex(List<Account> accounts) {
        Map<UUID, Integer> index = new ConcurrentHashMap<>(accounts.size());
        for (int i = 0; i < accounts.size(); i++) {
            index.put(accounts.get(i).uuid(), i + 1);
        }
        return index;
    }

    Integer getPosition(UUID uuid) {
        return this.positionIndex.get(uuid);
    }

    int size() {
        return this.accounts.size();
    }
}
