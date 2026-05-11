package com.eternalcode.economy.leaderboard.menu;

import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import java.util.List;
import org.bukkit.Material;

public class LeaderboardConfig extends OkaeriConfig {

    @Comment("GUI title (supports MiniMessage)")
    public String title =
        "<b><gradient:#00FFA2:#4EFFBE:#00FFA2>ʙᴀʟᴛᴏᴘ</gradient></b> <dark_gray>-</dark_gray> <white>Page {CURRENT_PAGE}/{TOTAL_PAGES}</white>";

    @Comment("Number of GUI rows (1-6)")
    public int rows = 6;

    @Comment("Player items configuration")
    public PlayerItems playerItems = new PlayerItems();

    @Comment("Pagination items")
    public PaginationItems paginationItems = new PaginationItems();

    @Comment("Fill settings")
    public FillSettings fillSettings = new FillSettings();

    public static class PlayerItems extends OkaeriConfig {

        @Comment({
            "Player item template",
            "Placeholders: {POSITION}, {PLAYER}, {BALANCE}, {FORMATTED_BALANCE}"
        })
        public LeaderboardItemRepresenter template = LeaderboardItemRepresenter.of(
            Material.PLAYER_HEAD,
            "<white>#{POSITION} <dark_gray>-</dark_gray> <gradient:#00FFA2:#4EFFBE:#00FFA2>{PLAYER}</gradient>",
            List.of(
                "",
                "<dark_gray>➤</dark_gray> <white>Balance:</white> <gray>{FORMATTED_BALANCE}</gray>",
                ""
            ),
            false,
            "{PLAYER_HEAD}",
            0
        );

        @Comment("Slots for player items (0-53)")
        public List<Integer> slots = List.of(
            10, 11, 12, 13, 14, 15, 16,
            19, 20, 21, 22, 23, 24, 25,
            28, 29, 30, 31, 32, 33, 34,
            37, 38, 39, 40, 41, 42, 43);
    }

    public static class LeaderboardItem extends OkaeriConfig {
        public Material material;
        public boolean glow;
        public String texture;

        public LeaderboardItem() {
        }

        public LeaderboardItem(Material material, boolean glow, String texture) {
            this.material = material;
            this.glow = glow;
            this.texture = texture;
        }
    }

    public static class PaginationItems extends OkaeriConfig {

        @Comment("Previous page button")
        public LeaderboardItemRepresenter previousPage = LeaderboardItemRepresenter.of(
            Material.ARROW,
            "<gradient:#00FFA2:#34AE00>← Previous page</gradient>",
            List.of(
                "",
                "<dark_gray>➤</dark_gray> <white>Current page: <gradient:#00FFA2:#34AE00>{CURRENT_PAGE}</gradient></white>",
                "",
                "<dark_gray>➤</dark_gray> <gray>Click to go back</gray>"),
            false,
            "none",
            48);

        @Comment("Previous page button (disabled)")
        public LeaderboardItemRepresenter previousPageDisabled = LeaderboardItemRepresenter.of(
            Material.GRAY_DYE,
            "<gray>← Previous page</gray>",
            List.of(
                "",
                "<dark_gray>➤</dark_gray> <red>You are on the first page</red>"),
            false,
            "none",
            48);

        @Comment("Next page button")
        public LeaderboardItemRepresenter nextPage = LeaderboardItemRepresenter.of(
            Material.ARROW,
            "<gradient:#00FFA2:#34AE00>Next page →</gradient>",
            List.of(
                "",
                "<dark_gray>➤</dark_gray> <white>Current page: <gradient:#00FFA2:#34AE00>{CURRENT_PAGE}</gradient></white>",
                "",
                "<dark_gray>➤</dark_gray> <gray>Click to go forward</gray>"),
            false,
            "none",
            50);

        @Comment("Next page button (disabled)")
        public LeaderboardItemRepresenter nextPageDisabled = LeaderboardItemRepresenter.of(
            Material.GRAY_DYE,
            "<gray>Next page →</gray>",
            List.of(
                "",
                "<dark_gray>➤</dark_gray> <red>No more pages</red>"),
            false,
            "none",
            50);

        @Comment("Page info item")
        public LeaderboardItemRepresenter pageInfo = LeaderboardItemRepresenter.of(
            Material.PAPER,
            "<white>Page <gradient:#00FFA2:#34AE00>{CURRENT_PAGE}</gradient><dark_gray>/</dark_gray><gradient:#00FFA2:#34AE00>{TOTAL_PAGES}</gradient></white>",
            List.of(
                "",
                "<dark_gray>➤</dark_gray> <white>Total players: <gradient:#00FFA2:#34AE00>{TOTAL_PLAYERS}</gradient></white>"),
            false,
            "none",
            49);

        @Comment("Close button")
        public LeaderboardItemRepresenter closeButton = LeaderboardItemRepresenter.of(
            Material.BARRIER,
            "<b><gradient:#ff5555:#aa0000>Close</gradient></b>",
            List.of(
                "",
                "<dark_gray>➤</dark_gray> <gray>Click to close</gray>"),
            false,
            "none",
            45);
    }

    public static class FillSettings extends OkaeriConfig {
        @Comment("Enable fill items")
        public boolean enableFillItems = true;

        @Comment("Materials for fill items")
        public List<Material> fillItems = List.of(
            Material.BLACK_STAINED_GLASS_PANE,
            Material.GRAY_STAINED_GLASS_PANE);
    }
}
