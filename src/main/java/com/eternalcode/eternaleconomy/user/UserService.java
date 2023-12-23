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

    public User create(UUID uuid, String name, BigDecimal balance) {
        if (this.usersByUniqueId.containsKey(uuid)) {
            throw new IllegalArgumentException("User already exists");
        }

        User user = new User(uuid, name, 0.0);
        this.usersByUniqueId.put(uuid, user);

        return user;
    }

    public Collection<User> getUsers() {
        return Collections.unmodifiableCollection(this.usersByUniqueId.values());
    }
}
