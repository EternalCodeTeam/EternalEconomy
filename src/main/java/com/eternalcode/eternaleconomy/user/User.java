package com.eternalcode.eternaleconomy.user;

import java.util.UUID;

public class User {

    public UUID uniqueId;
    public String name;
    public double balance;

    public User(UUID uniqueId, String name, double balance) {
        this.uniqueId = uniqueId;
        this.name = name;
        this.balance = balance;
    }

    public UUID getUniqueId() {
        return uniqueId;
    }

    public String getName() {
        return name;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void addBalance(double balance) {
        this.balance += balance;
    }

    public void removeBalance(double balance) {
        this.balance -= balance;
    }

    public void setName(String name) {
        this.name = name;
    }
}
