package com.eternalcode.economy.account;

import com.eternalcode.commons.scheduler.Scheduler;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;

public class AccountResolver {

    private static final String OFFLINE_UUID_PREFIX = "OfflinePlayer:";

    private final AccountManager accountManager;
    private final Server server;
    private final Scheduler scheduler;
    private final Logger logger;

    public AccountResolver(AccountManager accountManager, Server server, Scheduler scheduler, Logger logger) {
        this.accountManager = accountManager;
        this.server = server;
        this.scheduler = scheduler;
        this.logger = logger;
    }

    public CompletableFuture<Optional<Account>> resolve(AccountTarget target) {
        return switch (target) {
            case AccountTarget.ByUuid byUuid -> this.resolveByUuid(byUuid.uuid());
            case AccountTarget.ByName byName -> this.resolveByName(byName.name());
        };
    }

    private CompletableFuture<Optional<Account>> resolveByUuid(UUID uuid) {
        Account existing = this.accountManager.getAccount(uuid);
        if (existing != null) {
            return CompletableFuture.completedFuture(Optional.of(existing));
        }

        return this.lookupAsync(() -> Optional.of(this.createFromUuid(uuid)));
    }

    private CompletableFuture<Optional<Account>> resolveByName(String name) {
        Account existing = this.accountManager.getAccount(name);
        if (existing != null) {
            return CompletableFuture.completedFuture(Optional.of(existing));
        }

        return this.lookupAsync(() -> this.createFromName(name));
    }

    private Account createFromUuid(UUID uuid) {
        String knownName = this.server.getOfflinePlayer(uuid).getName();

        boolean nameAvailable = knownName != null && this.accountManager.getAccount(knownName) == null;
        String name = nameAvailable ? knownName : uuid.toString();

        return this.accountManager.getOrCreate(uuid, name);
    }

    private Optional<Account> createFromName(String name) {
        OfflinePlayer offlinePlayer = this.server.getOfflinePlayer(name);
        UUID uuid = offlinePlayer.getUniqueId();

        // An online-mode server falls back to the offline UUID when Mojang does not know the name
        if (this.server.getOnlineMode() && uuid.equals(offlineUuid(name))) {
            return Optional.empty();
        }

        // The player changed their name and the account still has the old one until the next join
        Account renamed = this.accountManager.getAccount(uuid);
        if (renamed != null) {
            return Optional.of(renamed);
        }

        String resolvedName = offlinePlayer.getName();
        return Optional.of(this.accountManager.getOrCreate(uuid, resolvedName != null ? resolvedName : name));
    }

    private CompletableFuture<Optional<Account>> lookupAsync(Supplier<Optional<Account>> lookup) {
        CompletableFuture<Optional<Account>> future = new CompletableFuture<>();

        this.scheduler.runAsync(() -> {
            Optional<Account> account = this.lookupSafely(lookup);
            this.scheduler.run(() -> future.complete(account));
        });

        return future;
    }

    private Optional<Account> lookupSafely(Supplier<Optional<Account>> lookup) {
        try {
            return lookup.get();
        }
        catch (RuntimeException exception) {
            this.logger.log(Level.WARNING, "Failed to look up player account", exception);
            return Optional.empty();
        }
    }

    private static UUID offlineUuid(String name) {
        return UUID.nameUUIDFromBytes((OFFLINE_UUID_PREFIX + name).getBytes(StandardCharsets.UTF_8));
    }
}
