package com.eternalcode.eternaleconomy.user;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class UserService {

    private final Map<UUID, User> usersByUniqueId = new HashMap<>();

    public Optional<User> getUser(UUID uuid) {
        return Optional.ofNullable(this.usersByUniqueId.get(uuid));
    }

    public User create(UUID uuid, String name) {
        if (this.usersByUniqueId.containsKey(uuid)) {
            throw new IllegalArgumentException("User already exists");
        }

        User user = new User(uuid, name, BigDecimal.ZERO);
        this.usersByUniqueId.put(uuid, user);

        return user;
    }

    public void addUser(User user) {
        this.usersByUniqueId.put(user.uniqueId, user);
    }

    public void remove(UUID uuid) {
        this.usersByUniqueId.remove(uuid);
    }

    // save

    public Collection<User> getUsers() {
        return Collections.unmodifiableCollection(this.usersByUniqueId.values());
    }
}
