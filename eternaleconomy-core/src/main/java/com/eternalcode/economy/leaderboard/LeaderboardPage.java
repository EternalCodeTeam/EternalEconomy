package com.eternalcode.economy.leaderboard;

import java.util.List;

public record LeaderboardPage(List<LeaderboardEntry> entries, int currentPage, int maxPages, int nextPage) {}
