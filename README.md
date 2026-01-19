<div align="center">

![readme-banner.png](https://github.com/EternalCodeTeam/EternalEconomy/blob/master/assets/readme-banner.png?raw=true)

# EternalEconomy

[![Available on SpigotMC](https://raw.githubusercontent.com/vLuckyyy/badges/main/available-on-spigotmc.svg)](https://www.spigotmc.org/resources/eternalcore-%E2%99%BE%EF%B8%8F-all-the-most-important-server-functions-in-one.112264/)
[![Available on Modrinth](https://raw.githubusercontent.com/vLuckyyy/badges/main/avaiable-on-modrinth.svg)](https://modrinth.com/plugin/eternalcore)
[![Available on Hangar](https://raw.githubusercontent.com/vLuckyyy/badges/main/avaiable-on-hangar.svg)](https://hangar.papermc.io/EternalCodeTeam/EternalCore)

[![Chat on Discord](https://raw.githubusercontent.com/vLuckyyy/badges/main//chat-with-us-on-discord.svg)](https://discord.com/invite/FQ7jmGBd6c)
[![Read the Docs](https://raw.githubusercontent.com/vLuckyyy/badges/main/read-the-documentation.svg)](https://docs.eternalcode.pl/eternalcore/introduction)
[![Available on BStats](https://raw.githubusercontent.com/vLuckyyy/badges/main/available-on-bstats.svg)](https://bstats.org/plugin/bukkit/EternalEconomy2/28941)

**The most modern and advanced economy plugin for your Minecraft server.**

[Report Bug](https://github.com/EternalCodeTeam/EternalEconomy/issues) • [Request Feature](https://github.com/EternalCodeTeam/EternalEconomy/issues) • [Join Discord](https://discord.gg/eternalcode)

<br>

<a href="https://ko-fi.com/eternalcodeteam">
  <img src="https://github.com/intergrav/devins-badges/blob/v3/assets/cozy/donate/kofi-plural-alt_64h.png?raw=true" height="64" alt="Support us on Ko-fi">
</a>

</div>

---

## 🔥 Features

| Feature | Description |
| :--- | :--- |
| 🚀 **High Performance** | Optimized for large servers with asynchronous database operations. |
| 💾 **Multi-Database** | Support for H2, SQLite, MySQL, MariaDB, and PostgreSQL. |
| 🌍 **Modern Design** | Slick default messages with MiniMessage support. |
| 📊 **Leaderboards** | Built-in high-performance balance top system. |
| ⚙️ **Configurable** | Extensive `config.yml` and `messages.yml` customization. |

---

## 📷 Gallery

<div align="center">

| Feature Preview | GIF / Image |
| :--- | :--- |
| **Balance Check** | *Place your GIF here* |
| **Pay System** | *Place your GIF here* |
| **Admin Management** | *Place your GIF here* |

</div>

---

## 💻 Commands & Permissions

### Player Commands
| Command | Usage | Permission | Description |
| :--- | :--- | :--- | :--- |
| `/balance` | `/balance [player]` | `eternaleconomy.player.balance` | Check your own account balance. |
| `/balance` | `/balance <player>` | `eternaleconomy.player.balance.other` | Check another player's balance. |
| `/pay` | `/pay <player> <amount>` | `eternaleconomy.player.pay` | Transfer money to another player. |
| `/withdraw` | `/withdraw <amount>` | `eternaleconomy.player.withdraw` | Withdraw money from your account. |
| `/balancetop` | `/balancetop [page]` | `eternaleconomy.player.balance.top` | View the richest players on the server. |

### Admin Commands
| Command | Usage | Permission | Description |
| :--- | :--- | :--- | :--- |
| `/economy set` | `/eco set <player> <amount>` | `eternaleconomy.admin.set` | Set a player's balance to a specific amount. |
| `/economy add` | `/eco add <player> <amount>` | `eternaleconomy.admin.add` | Add a specific amount to a player's balance. |
| `/economy remove` | `/eco remove <player> <amount>` | `eternaleconomy.admin.remove` | Remove a specific amount from a player's balance. |
| `/economy reset` | `/eco reset <player>` | `eternaleconomy.admin.reset` | Reset a player's balance to the default. |
| `/economy reload` | `/eco reload` | `eternaleconomy.admin.reload` | Reload the plugin configuration. |
| `/economy check` | `/eco check <player>` | `eternaleconomy.admin.balance` | Check a player's balance (Admin alias). |

---

## 🧩 Placeholders

These placeholders are used within the `messages.yml` file to dynamic content.

| Placeholder | Context | Description |
| :--- | :--- | :--- |
| `{PLAYER}` | Global | The name of the player involved in the transaction. |
| `{BALANCE}` | Balance Check | The formatted balance of the player. |
| `{AMOUNT}` | Transaction | The amount of money being transferred, added, or removed. |
| `{POSITION}` | Leaderboard | The rank of the player in the leaderboard. |
| `{PAGE}` | Leaderboard | The current page number. |
| `{TOTAL_PAGES}` | Leaderboard | The total number of pages. |

---

## 📥 Installation

1. Download the latest release from [GitHub](https://github.com/EternalCodeTeam/EternalEconomy/releases) or [Modrinth](https://modrinth.com/plugin/eternaleconomy).
2. Place the `.jar` file into your server's `plugins` folder.
3. Restart your server.
4. Configure `config.yml` and database settings if necessary.

---

<div align="center">
Made with ❤️ by the <a href="https://github.com/EternalCodeTeam">EternalCodeTeam</a>
</div>
