package com.eternalcode.eternaleconomy.user;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface UserRepository {

    List<User> loadUsers();

    CompletableFuture<Void> saveUser(User user);

    CompletableFuture<Void> removeUser(User user);
}
