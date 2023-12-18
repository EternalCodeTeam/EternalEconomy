package com.eternalcode.eternaleconomy;

import com.eternalcode.eternaleconomy.eco.Economy;
import com.eternalcode.eternaleconomy.eco.VaultImpl;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

public class EternalEconomy extends JavaPlugin {


        private static EternalEconomy instance;
        private static VaultImpl vaultImpl;
        public static Economy eco;

        public void onEnable(){
                instance = this;
                vaultImpl = new VaultImpl();
                if (!this.setupEconomy()) {
                        this.getLogger().warning("Economy couldn't be registed, Vault plugin is missing!");
                } else {
                        this.getLogger().info("Vault found, Economy has been registered.");
                }

        }
        public static EternalEconomy getInstance() {
                return instance;
        }
        private boolean setupEconomy() {
                if (this.getServer().getPluginManager().getPlugin("Vault") == null) {
                        return false;
                } else {
                        this.getServer().getServicesManager().register(net.milkbowl.vault.economy.Economy.class, vaultImpl, this, ServicePriority.Highest);
                        return true;
                }
        }
        public static Economy getEconomy() {
                return eco;
        }
}

