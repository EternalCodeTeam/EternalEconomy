package com.eternalcode.eternaleconomy.user;

import java.util.List;

public interface UserRepository {

    List<User> loadUsers();

    void saveUser();

    void removeUser();

}
