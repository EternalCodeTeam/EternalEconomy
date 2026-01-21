package com.eternalcode.economy.command.impl.admin;

import com.eternalcode.commons.scheduler.Scheduler;
import com.eternalcode.economy.EconomyPermissionConstant;
import com.eternalcode.economy.account.Account;
import com.eternalcode.economy.account.AccountManager;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import jakarta.validation.constraints.Min;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;

@Command(name = "economy generate", aliases = "eco generate")
@Permission(EconomyPermissionConstant.ADMIN_SET_PERMISSION)
public class AdminGenerateCommand {

    private static final String[] FIRST_NAMES = {
        "Alex", "Jordan", "Taylor", "Morgan", "Casey", "Riley", "Avery", "Quinn",
        "Blake", "Cameron", "Dakota", "Drew", "Hayden", "Jesse", "Kendall", "Logan",
        "Parker", "Reese", "Skyler", "Tyler", "Rowan", "Sage", "River", "Phoenix",
        "Charlie", "Finley", "Emerson", "Harper", "Kai", "Lennon", "Marley", "Oakley",
        "Peyton", "Rory", "Sawyer", "Spencer", "Tatum", "Winter", "Aspen", "Blair",
        "Ellis", "Ezra", "Gray", "Haven", "Jude", "Kit", "Lane", "Nova",
        "Onyx", "Quinn", "Rain", "Salem", "True", "Vale", "Wren", "Zion",
        "Azure", "Bay", "Cedar", "Echo", "Frost", "Harbor", "Indigo", "Jade"
    };

    private static final String[] SUFFIXES = {
        "MC", "YT", "TV", "TTV", "HD", "Pro", "Gaming", "Plays",
        "Live", "Stream", "King", "Lord", "Master", "Shadow", "Dark", "Red",
        "Blue", "Gold", "Silver", "Epic", "Mega", "Ultra", "Super", "Hyper",
        "Alpha", "Beta", "Omega", "Prime", "Elite", "Ace", "Champion", "Legend",
        "Hero", "Warrior", "Knight", "Dragon", "Wolf", "Tiger", "Lion", "Bear",
        "Fox", "Hawk", "Eagle", "Raven", "Storm", "Blaze", "Flame", "Frost",
        "Night", "Star", "Moon", "Sun", "Sky", "Ocean", "Fire", "Ice",
        "Thunder", "Lightning", "Nova", "Void", "Nexus", "Apex"
    };

    private final AccountManager accountManager;
    private final Scheduler scheduler;

    public AdminGenerateCommand(AccountManager accountManager, Scheduler scheduler) {
        this.accountManager = accountManager;
        this.scheduler = scheduler;
    }

    @Execute
    void execute(@Context CommandSender sender, @Arg @Min(1) int count) {
        if (count > 100000) {
            sender.sendMessage(Component.text("Maximum count is 100,000", NamedTextColor.RED));
            return;
        }

        sender.sendMessage(Component.text("Generating " + count + " accounts...", NamedTextColor.YELLOW));

        AtomicInteger generated = new AtomicInteger(0);
        long startTime = System.currentTimeMillis();

        scheduler.runAsync(() -> {
            List<Account> batch = new ArrayList<>(Math.min(1000, count));
            Random random = ThreadLocalRandom.current();

            for (int i = 0; i < count; i++) {
                String name = generateRealisticName(random);
                UUID uuid = UUID.randomUUID();
                BigDecimal balance = generateRandomBalance(random);

                Account account = new Account(uuid, name, balance);
                batch.add(account);

                if (batch.size() >= 1000) {
                    saveBatch(batch, generated);
                    batch.clear();

                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            }

            if (!batch.isEmpty()) {
                saveBatch(batch, generated);
            }

            long elapsed = System.currentTimeMillis() - startTime;
            scheduler.run(() -> {
                sender.sendMessage(Component.text(
                    "Generated " + generated.get() + " accounts in " + elapsed + "ms",
                    NamedTextColor.GREEN
                ));
            });
        });
    }

    private void saveBatch(List<Account> batch, AtomicInteger counter) {
        for (Account account : batch) {
            accountManager.save(account);
            counter.incrementAndGet();
        }
    }

    private String generateRealisticName(Random random) {
        String firstName = FIRST_NAMES[random.nextInt(FIRST_NAMES.length)];

        int style = random.nextInt(100);

        if (style < 30) {
            return firstName + random.nextInt(10000);
        } else if (style < 60) {
            String suffix = SUFFIXES[random.nextInt(SUFFIXES.length)];
            return firstName + suffix;
        } else if (style < 80) {
            String suffix = SUFFIXES[random.nextInt(SUFFIXES.length)];
            return firstName + suffix + random.nextInt(1000);
        } else if (style < 90) {
            return firstName + "_" + SUFFIXES[random.nextInt(SUFFIXES.length)];
        } else {
            String second = FIRST_NAMES[random.nextInt(FIRST_NAMES.length)];
            return firstName + second;
        }
    }

    private BigDecimal generateRandomBalance(Random random) {
        int distribution = random.nextInt(100);

        double rawBalance;

        if (distribution < 40) {
            rawBalance = 1 + random.nextDouble() * 100;
        } else if (distribution < 70) {
            rawBalance = 100 + random.nextDouble() * 1000;
        } else if (distribution < 85) {
            rawBalance = 1000 + random.nextDouble() * 10000;
        } else if (distribution < 95) {
            rawBalance = 10000 + random.nextDouble() * 100000;
        } else {
            rawBalance = 100000 + random.nextDouble() * 900000;
        }

        return BigDecimal.valueOf(rawBalance).setScale(2, RoundingMode.HALF_UP);
    }
}
