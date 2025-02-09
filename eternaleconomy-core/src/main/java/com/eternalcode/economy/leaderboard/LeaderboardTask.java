package com.eternalcode.economy.leaderboard;

public class LeaderboardTask implements Runnable {

    private final LeaderboardService leaderboardService;

    public LeaderboardTask(LeaderboardService leaderboardService) {
        this.leaderboardService = leaderboardService;
    }

    @Override
    public void run() {
        this.leaderboardService.updateLeaderboard();
    }
}
