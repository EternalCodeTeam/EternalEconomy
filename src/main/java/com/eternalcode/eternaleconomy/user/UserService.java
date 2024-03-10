package com.eternalcode.eternaleconomy.user;

import com.eternalcode.eternaleconomy.configuration.implementation.PluginConfiguration;

import java.math.BigDecimal;
import java.util.*;

public class UserService {

    private final PluginConfiguration configuration;


    private final Map<UUID, User> usersByUniqueId = new HashMap<>();
    private final Map<String, User> usersByName = new HashMap<>();

    private final UserRepository userRepository;

    public UserService(PluginConfiguration configuration, UserRepository userRepository) {
        this.configuration = configuration;
        this.userRepository = userRepository;
    }

    public Optional<User> findUser(UUID uuid) {
        return Optional.ofNullable(this.usersByUniqueId.get(uuid));
    }

    public Optional<User> findUser(String name) {
        return Optional.ofNullable(this.usersByName.get(name));
    }

    public void create(UUID uuid, String name) {
        this.usersByUniqueId.put(uuid, new User(uuid, name, configuration.starting_balance));
        this.usersByName.put(name, this.usersByUniqueId.get(uuid));
    }

    public void addUser(User user) {
        this.usersByUniqueId.put(user.getUniqueId(), user);
        this.usersByName.put(user.getName(), user);
    }

    public void removeUser(UUID uuid) {
        this.usersByUniqueId.remove(uuid);
        this.usersByName.remove(this.usersByUniqueId.get(uuid).getName());
    }

    public void saveUser(User user) {
        this.userRepository.saveUser(user);
    }

    public Collection<User> users() {
        return this.usersByUniqueId.values();
    }

}
