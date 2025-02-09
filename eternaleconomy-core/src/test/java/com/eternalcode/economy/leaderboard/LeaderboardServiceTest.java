package com.eternalcode.economy.leaderboard;

import com.eternalcode.economy.account.Account;
import com.eternalcode.economy.account.database.AccountRepositoryInMemory;
import com.eternalcode.economy.config.implementation.PluginConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;

class LeaderboardServiceTest {

    private LeaderboardService leaderboardService;
    private AccountRepositoryInMemory accountRepository;

    @BeforeEach
    void setUp() {
        this.accountRepository = new AccountRepositoryInMemory();
        PluginConfig pluginConfig = new PluginConfig();
        pluginConfig.leaderboardSize = 3;
        this.leaderboardService = new LeaderboardService(this.accountRepository, pluginConfig);
    }

    @Test
    void testGetLeaderboard() {
        Account account1 = new Account(UUID.randomUUID(), "Player1", BigDecimal.valueOf(100));
        Account account2 = new Account(UUID.randomUUID(), "Player2", BigDecimal.valueOf(200));
        Account account3 = new Account(UUID.randomUUID(), "Player3", BigDecimal.valueOf(300));
        Account account4 = new Account(UUID.randomUUID(), "Player4", BigDecimal.valueOf(50));

        this.accountRepository.save(account1);
        this.accountRepository.save(account2);
        this.accountRepository.save(account3);
        this.accountRepository.save(account4);

        this.leaderboardService.updateLeaderboard();
        Collection<Account> leaderboard = this.leaderboardService.getLeaderboard();

        assertEquals(3, leaderboard.size(), "Leaderboard should contain top 3 accounts");
        List<Account> leaderboardList = List.copyOf(leaderboard);
        assertEquals(account3, leaderboardList.get(0), "Top account should be the one with the highest balance");
        assertEquals(account2, leaderboardList.get(1), "Second account should be the one with the second-highest balance");
        assertEquals(account1, leaderboardList.get(2), "Third account should be the one with the third-highest balance");
    }

    @Test
    void testGetLeaderboardPosition() {
        Account account1 = new Account(UUID.randomUUID(), "Player1", BigDecimal.valueOf(100));
        Account account2 = new Account(UUID.randomUUID(), "Player2", BigDecimal.valueOf(200));
        Account account3 = new Account(UUID.randomUUID(), "Player3", BigDecimal.valueOf(300));

        this.accountRepository.save(account1);
        this.accountRepository.save(account2);
        this.accountRepository.save(account3);

        this.leaderboardService.updateLeaderboard();

        CompletableFuture<LeaderboardPosition> position1Future = this.leaderboardService.getLeaderboardPosition(account1);
        CompletableFuture<LeaderboardPosition> position2Future = this.leaderboardService.getLeaderboardPosition(account2);
        CompletableFuture<LeaderboardPosition> position3Future = this.leaderboardService.getLeaderboardPosition(account3);

        LeaderboardPosition position1 = position1Future.join();
        LeaderboardPosition position2 = position2Future.join();
        LeaderboardPosition position3 = position3Future.join();

        assertEquals(3, position1.position(), "Player1 should be in position 3");
        assertEquals(2, position2.position(), "Player2 should be in position 2");
        assertEquals(1, position3.position(), "Player3 should be in position 1");
    }

    @Test
    void testLastUpdated() {
        Instant beforeUpdate = Instant.now();
        this.leaderboardService.updateLeaderboard();
        Instant afterUpdate = Instant.now();

        Instant lastUpdated = this.leaderboardService.getLastUpdated();

        assertTrue(lastUpdated.isAfter(beforeUpdate) || lastUpdated.equals(beforeUpdate),
            "Last updated time should be after or equal to the time before update");
        assertTrue(lastUpdated.isBefore(afterUpdate) || lastUpdated.equals(afterUpdate),
            "Last updated time should be before or equal to the time after update");
    }

    @Test
    void testLeaderboardWithSameBalances() {
        // Add test accounts with the same balance
        Account account1 = new Account(UUID.randomUUID(), "Player1", BigDecimal.valueOf(100));
        Account account2 = new Account(UUID.randomUUID(), "Player2", BigDecimal.valueOf(100));
        Account account3 = new Account(UUID.randomUUID(), "Player3", BigDecimal.valueOf(100));

        this.accountRepository.save(account1);
        this.accountRepository.save(account2);
        this.accountRepository.save(account3);

        // Update leaderboard
        this.leaderboardService.updateLeaderboard();

        // Test positions
        CompletableFuture<LeaderboardPosition> position1Future = this.leaderboardService.getLeaderboardPosition(account1);
        CompletableFuture<LeaderboardPosition> position2Future = this.leaderboardService.getLeaderboardPosition(account2);
        CompletableFuture<LeaderboardPosition> position3Future = this.leaderboardService.getLeaderboardPosition(account3);

        LeaderboardPosition position1 = position1Future.join();
        LeaderboardPosition position2 = position2Future.join();
        LeaderboardPosition position3 = position3Future.join();

        // All accounts have the same balance, so their positions should be 1
        assertEquals(1, position1.position(), "Player1 should be in position 1");
        assertEquals(1, position2.position(), "Player2 should be in position 1");
        assertEquals(1, position3.position(), "Player3 should be in position 1");
    }

    @Test
    void testLeaderboardWithNoAccounts() {
        // Update leaderboard with no accounts
        this.leaderboardService.updateLeaderboard();

        // Get leaderboard
        Collection<Account> leaderboard = this.leaderboardService.getLeaderboard();

        // Assert leaderboard is empty
        assertTrue(leaderboard.isEmpty(), "Leaderboard should be empty when no accounts exist");
    }
}
