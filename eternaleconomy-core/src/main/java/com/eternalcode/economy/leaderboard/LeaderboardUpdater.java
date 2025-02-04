package com.eternalcode.economy.leaderboard;

public class LeaderboardUpdater implements Runnable {

    private final LeaderboardService leaderboardService;

    public LeaderboardUpdater(LeaderboardService leaderboardService) {
        this.leaderboardService = leaderboardService;
    }

    @Override
    public void run() {
        this.leaderboardService.updateLeaderboard();
    }

}
